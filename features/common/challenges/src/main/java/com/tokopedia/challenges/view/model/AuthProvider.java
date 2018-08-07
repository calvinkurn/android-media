
package com.tokopedia.challenges.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthProvider {

    @SerializedName("Network")
    @Expose
    private String network;
    @SerializedName("Id")
    @Expose
    private String id;

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

}
