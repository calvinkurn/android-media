package com.tokopedia.core.shopinfo.models.productmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by brilliant.oka on 29/03/17.
 */

public class ProductCampaign {
    @SerializedName("percentage_amount")
    @Expose
    private int percentageAmount;
    @SerializedName("original_price")
    @Expose
    private String originalPrice;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("original_price_no_idr")
    @Expose
    private String originalPriceNoIdr;

    public int getPercentageAmount() {
        return percentageAmount;
    }

    public void setPercentageAmount(int percentageAmount) {
        this.percentageAmount = percentageAmount;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOriginalPriceNoIdr() {
        return originalPriceNoIdr;
    }

    public void setOriginalPriceNoIdr(String originalPriceNoIdr) {
        this.originalPriceNoIdr = originalPriceNoIdr;
    }
}
