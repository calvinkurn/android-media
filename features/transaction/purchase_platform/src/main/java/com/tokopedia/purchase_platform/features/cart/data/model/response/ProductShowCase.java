package com.tokopedia.purchase_platform.features.cart.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class ProductShowCase {

    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("id")
    @Expose
    private int id;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
