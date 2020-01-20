package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fwidjaja on 18/04/19.
 */
public class OrderLevel {
    @SerializedName("promo_code")
    @Expose
    private String promoCode;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("total_discount")
    @Expose
    private float totalDiscount;

    @SerializedName("total_benefit")
    @Expose
    private float totalBenefit;

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(float totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public float getTotalBenefit() {
        return totalBenefit;
    }

    public void setTotalBenefit(float totalBenefit) {
        this.totalBenefit = totalBenefit;
    }
}
