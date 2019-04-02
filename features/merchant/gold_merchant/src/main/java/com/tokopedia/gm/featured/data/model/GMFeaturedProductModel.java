package com.tokopedia.gm.featured.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class GMFeaturedProductModel {
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("data")
    @Expose
    private boolean data;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    public class Links {

        @SerializedName("self")
        @Expose
        private String self;

        public String getSelf() {
            return self;
        }

        public void setSelf(String self) {
            this.self = self;
        }

    }
}
