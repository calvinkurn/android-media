package com.tokopedia.digital_deals.domain.model.dealdetailsdomailmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Catalog {

    @SerializedName("digital_category_id")
    @Expose
    private Integer digitalCategoryId;
    @SerializedName("digital_product_id")
    @Expose
    private Integer digitalProductId;
    @SerializedName("digital_product_code")
    @Expose
    private String digitalProductCode;

    public Integer getDigitalCategoryId() {
        return digitalCategoryId;
    }

    public void setDigitalCategoryId(Integer digitalCategoryId) {
        this.digitalCategoryId = digitalCategoryId;
    }

    public Integer getDigitalProductId() {
        return digitalProductId;
    }

    public void setDigitalProductId(Integer digitalProductId) {
        this.digitalProductId = digitalProductId;
    }

    public String getDigitalProductCode() {
        return digitalProductCode;
    }

    public void setDigitalProductCode(String digitalProductCode) {
        this.digitalProductCode = digitalProductCode;
    }

}