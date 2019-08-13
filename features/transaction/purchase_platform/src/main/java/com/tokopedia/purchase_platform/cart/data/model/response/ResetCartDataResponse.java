package com.tokopedia.purchase_platform.cart.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class ResetCartDataResponse {
    @SerializedName("success")
    @Expose
    private int success;

    public int getSuccess() {
        return success;
    }
}
