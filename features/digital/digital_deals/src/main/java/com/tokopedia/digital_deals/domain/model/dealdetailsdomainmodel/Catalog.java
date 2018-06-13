package com.tokopedia.digital_deals.domain.model.dealdetailsdomainmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Catalog {

    @SerializedName("digital_category_id")
    @Expose
    private int digitalCategoryId;
    @SerializedName("digital_product_id")
    @Expose
    private int digitalProductId;
    @SerializedName("digital_product_code")
    @Expose
    private String digitalProductCode;

    public int getDigitalCategoryId() {
        return digitalCategoryId;
    }

    public void setDigitalCategoryId(int digitalCategoryId) {
        this.digitalCategoryId = digitalCategoryId;
    }

    public int getDigitalProductId() {
        return digitalProductId;
    }

    public void setDigitalProductId(int digitalProductId) {
        this.digitalProductId = digitalProductId;
    }

    public String getDigitalProductCode() {
        return digitalProductCode;
    }

    public void setDigitalProductCode(String digitalProductCode) {
        this.digitalProductCode = digitalProductCode;
    }

}