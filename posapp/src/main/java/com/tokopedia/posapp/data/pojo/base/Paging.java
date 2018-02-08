package com.tokopedia.posapp.data.pojo.base;

import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 9/4/17.
 */

public class Paging {
    @SerializedName("uri_next")
    private String uriNext;

    @SerializedName("uri_previous")
    private String uriPrevious;

    public String getUriNext() {
        return uriNext;
    }

    public void setUriNext(String uriNext) {
        this.uriNext = uriNext;
    }

    public String getUriPrevious() {
        return uriPrevious;
    }

    public void setUriPrevious(String uriPrevious) {
        this.uriPrevious = uriPrevious;
    }
}
