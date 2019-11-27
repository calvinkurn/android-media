package com.tokopedia.purchase_platform.features.cart.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class NotifCounterCartDataResponse {
    @SerializedName("counter")
    @Expose
    private int counter;

    public int getCounter() {
        return counter;
    }
}
