package com.tokopedia.digital.product.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 5/10/17.
 */
public class AttributesLastOrderData {

    @SerializedName("client_number")
    @Expose
    private String clientNumber;
    @SerializedName("operator_id")
    @Expose
    private int operatorId;
    @SerializedName("product_id")
    @Expose
    private int productId;

    public String getClientNumber() {
        return clientNumber;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public int getProductId() {
        return productId;
    }
}
