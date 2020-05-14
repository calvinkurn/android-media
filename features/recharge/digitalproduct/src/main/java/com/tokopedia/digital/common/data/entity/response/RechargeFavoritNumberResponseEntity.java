
package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RechargeFavoritNumberResponseEntity {

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
    @SerializedName("list")
    @Expose
    private java.util.List<RechargeFavoritNumber> list = null;

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public java.util.List<RechargeFavoritNumber> getList() {
        return list;
    }

    public void setList(java.util.List<RechargeFavoritNumber> list) {
        this.list = list;
    }

}
