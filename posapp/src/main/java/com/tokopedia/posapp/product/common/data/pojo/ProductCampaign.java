package com.tokopedia.posapp.product.common.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 3/27/18.
 */
public class ProductCampaign {
    @SerializedName("product_id")
    @Expose
    private int productId;

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

    @SerializedName("original_price_idr")
    @Expose
    private String originalPriceIdr;

    @SerializedName("discounted_price_idr")
    @Expose
    private String discountedPriceIdr;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
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

    public String getOriginalPriceIdr() {
        return originalPriceIdr;
    }

    public void setOriginalPriceIdr(String originalPriceIdr) {
        this.originalPriceIdr = originalPriceIdr;
    }

    public String getDiscountedPriceIdr() {
        return discountedPriceIdr;
    }

    public void setDiscountedPriceIdr(String discountedPriceIdr) {
        this.discountedPriceIdr = discountedPriceIdr;
    }
}
