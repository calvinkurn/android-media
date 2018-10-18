package com.tokopedia.affiliate.feature.tracking.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BymeGetRealURL {

    @SerializedName("realURL")
    @Expose
    private String realURL;
    @SerializedName("error")
    @Expose
    private Error error;

    public String getRealURL() {
        return realURL;
    }

    public void setRealURL(String realURL) {
        this.realURL = realURL;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

}
