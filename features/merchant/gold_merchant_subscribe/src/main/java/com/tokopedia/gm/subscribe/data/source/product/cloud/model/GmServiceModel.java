package com.tokopedia.gm.subscribe.data.source.product.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GmServiceModel {

    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("data")
    @Expose
    private Data data;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
