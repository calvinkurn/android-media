
package com.tokopedia.network.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PagingDetail {

    @SerializedName("uri_next")
    @Expose
    private String uriNext;
    @SerializedName("uri_previous")
    @Expose
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
