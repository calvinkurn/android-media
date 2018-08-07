
package com.tokopedia.challenges.domain.model;

import java.util.HashMap;
import java.util.Map;

public class AuthProvider {

    private String network;
    private String id;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
