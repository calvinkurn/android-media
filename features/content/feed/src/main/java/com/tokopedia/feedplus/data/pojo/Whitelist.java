package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 20/06/18.
 */
public class Whitelist {
    @SerializedName("content")
    private WhitelistContent content;

    @SerializedName("error")
    private String error;

    public WhitelistContent getContent() {
        return content;
    }

    public String getError() {
        return error;
    }
}
