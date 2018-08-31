package com.tokopedia.digital.cart.data.entity.response.voucher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class RelationData {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
