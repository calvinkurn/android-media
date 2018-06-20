package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 20/06/18.
 */
public class WhitelistContent {
    @SerializedName("isWhitelist")
    private boolean isWhitelist;

    @SerializedName("url")
    private String url;

    public boolean isWhitelist() {
        return isWhitelist;
    }

    public String getUrl() {
        return url;
    }
}
