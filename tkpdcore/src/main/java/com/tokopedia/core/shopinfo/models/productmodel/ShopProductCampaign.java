package com.tokopedia.core.shopinfo.models.productmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by brilliant.oka on 29/03/17.
 */

public class ShopProductCampaign {

    @SerializedName("product_id")
    @Expose
    private String productId;

    @SerializedName("percentage_amount")
    @Expose
    private int percentageAmount;

    @SerializedName("discounted_price")
    @Expose
    private String discountedPrice;

    @SerializedName("end_date")
    @Expose
    private String endDate;

    @SerializedName("original_price")
    @Expose
    private String originalPrice;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getPercentageAmount() {
        return percentageAmount;
    }

    public void setPercentageAmount(int percentageAmount) {
        this.percentageAmount = percentageAmount;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

}
