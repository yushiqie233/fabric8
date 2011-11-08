/**
 * Copyright (C) 2011, FuseSource Corp.  All rights reserved.
 * http://fusesource.com
 *
 * The software in this package is published under the terms of the
 * CDDL license a copy of which has been included with this distribution
 * in the license.txt file.
 */
package org.fusesource.fabric.commands;

import java.util.Collections;
import java.util.List;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.fusesource.fabric.api.Agent;
import org.fusesource.fabric.api.Profile;

@Command(name = "create-agents", scope = "fabric", description = "Create a new agent")
public class CreateAgent extends FabricCommand {

    @Option(name = "--version")
    private String version = "base";

    @Option(name = "--profile", multiValued = true, required = false)
    private List<String> profiles;

    @Option(name = "--parent", multiValued = false, required = false)
    private String parent;

    @Option(name = "--enable-debuging", multiValued = false, required = false)
    private Boolean debugAgent = Boolean.FALSE;

    @Option(name = "--cluster-server", multiValued = false, required = false)
    private Boolean isClusterServer = Boolean.FALSE;

    @Option(name = "--url", multiValued = false, required = false)
    private String url;

    @Argument(index = 0, required = true, description = "The name of the agent to be created. When creating multiple agents it serves as a prefix")
    private String name;

    @Argument(index = 1, required = false, description = "The number of agents that should be created")
    private int number = 1;


    @Override
    protected Object doExecute() throws Exception {
        if (url == null && parent == null) {
            throw new Exception("Either an url or a parent must be specified");
        }
        if (url == null && parent != null) {
            url = "child:" + parent;
        }
        List<String> names = this.profiles;
        if (names == null || names.isEmpty()) {
            names = Collections.singletonList("default");
        }
        Agent[] children = fabricService.createAgents(url, name, isClusterServer, debugAgent, number);
        try {
            Profile[] profiles = getProfiles(version, names);
            for (Agent child : children) {
                child.setProfiles(profiles);
            }
        } catch (Exception ex) {

        }

        return null;
    }

}
