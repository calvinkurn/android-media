package com.tokopedia.recentview.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PagingData {
    @SerializedName("uri_next")
    @Expose
    public String uriNext;
    @SerializedName("uri_previous")
    @Expose
    String uriPrevious;
    @SerializedName("current")
    @Expose
    public String uriCurrent;

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

    public String getUriCurrent() {
        return uriCurrent;
    }

    public void setUriCurrent(String uriCurrent) {
        this.uriCurrent = uriCurrent;
    }
}
