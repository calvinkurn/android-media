package com.tokopedia.transactiondata.entity.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 01/08/18.
 */
public class ProductTrackerData {

    @SerializedName("attribution")
    @Expose
    private String attribution;
    @SerializedName("product_list_name")
    @Expose
    private String productListName;

    public String getAttribution() {
        return attribution;
    }

    public String getProductListName() {
        return productListName;
    }
}
