package com.tokopedia.digital.newcart.data.entity.response.voucher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 3/7/17.
 */

public class Cart {
    @SerializedName("data")
    @Expose
    private RelationData data;

    public RelationData getData() {
        return data;
    }
}
