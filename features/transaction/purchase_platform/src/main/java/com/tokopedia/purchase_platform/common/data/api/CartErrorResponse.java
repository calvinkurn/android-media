package com.tokopedia.purchase_platform.common.data.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public class CartErrorResponse {
    @SerializedName("header")
    @Expose
    private CartHeaderResponse cartHeaderResponse;

    public CartHeaderResponse getCartHeaderResponse() {
        return cartHeaderResponse;
    }
}
