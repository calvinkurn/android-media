package com.tokopedia.digital.common.data.entity.response;

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
    private String operatorId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;

    public String getClientNumber() {
        return clientNumber;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public String getProductId() {
        return productId;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
