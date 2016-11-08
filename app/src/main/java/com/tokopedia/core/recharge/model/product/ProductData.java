package com.tokopedia.core.recharge.model.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ricoharisin on 7/14/16.
 */
public class ProductData {

    @SerializedName("data")
    @Expose
    private List<Product> data;

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }

}
