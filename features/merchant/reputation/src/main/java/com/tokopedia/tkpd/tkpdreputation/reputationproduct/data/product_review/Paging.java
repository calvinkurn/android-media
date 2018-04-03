
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Paging {

    @SerializedName("uri_next")
    @Expose
    private String uriNext;
    @SerializedName("uri_previous")
    @Expose
    private String uriPrevious;

    /**
     * 
     * @return
     *     The uriNext
     */
    public String getUriNext() {
        return uriNext;
    }

    /**
     * 
     * @param uriNext
     *     The uri_next
     */
    public void setUriNext(String uriNext) {
        this.uriNext = uriNext;
    }

    /**
     * 
     * @return
     *     The uriPrevious
     */
    public String getUriPrevious() {
        return uriPrevious;
    }

    /**
     * 
     * @param uriPrevious
     *     The uri_previous
     */
    public void setUriPrevious(String uriPrevious) {
        this.uriPrevious = uriPrevious;
    }

}
