package com.tokopedia.affiliate.feature.tracking.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("bymeGetRealURL")
    @Expose
    private BymeGetRealURL bymeGetRealURL;

    public BymeGetRealURL getBymeGetRealURL() {
        return bymeGetRealURL;
    }

    public void setBymeGetRealURL(BymeGetRealURL bymeGetRealURL) {
        this.bymeGetRealURL = bymeGetRealURL;
    }

}
