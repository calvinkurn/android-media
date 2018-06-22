package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 20/06/18.
 */
public class Whitelist {

    @SerializedName("iswhitelist")
    private boolean isWhitelist;

    @SerializedName("url")
    private String url;

    @SerializedName("error")
    private String error;

    public boolean isWhitelist() {
        return isWhitelist;
    }

    public String getUrl() {
        return url;
    }

    public String getError() {
        return error;
    }
}
