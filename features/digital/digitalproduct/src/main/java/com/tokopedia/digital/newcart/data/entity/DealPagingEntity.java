package com.tokopedia.digital.newcart.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealPagingEntity {
    @SerializedName("uri_next")
    @Expose
    private String uriNext;

    public String getUriNext() {
        return uriNext;
    }
}
