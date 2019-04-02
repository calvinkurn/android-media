package com.tokopedia.digital.widget.data.entity.lastorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 9/20/17.
 */

public class AttributesEntity {

    @SerializedName("client_number")
    @Expose
    private String clientNumber;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("operator_id")
    @Expose
    private int operatorId;
    @SerializedName("category_id")
    @Expose
    private int categoryId;

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
