package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 02/03/18.
 */

public class FreeReturns {
    @SerializedName("free_returns_logo")
    @Expose
    public String freeReturnsLogo = "";

    public String getFreeReturnsLogo() {
        return freeReturnsLogo;
    }
}
