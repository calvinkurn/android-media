package com.tokopedia.common_digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class Product {

    @SerializedName("data")
    @Expose
    private RelationData data;

    public void setData(RelationData data) {
        this.data = data;
    }

    public RelationData getData() {
        return data;
    }

}
