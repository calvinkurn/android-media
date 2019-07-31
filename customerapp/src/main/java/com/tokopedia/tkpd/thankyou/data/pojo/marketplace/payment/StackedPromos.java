package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fwidjaja on 18/04/19.
 */
public class StackedPromos {
    @SerializedName("promo_code_id")
    @Expose
    private long promoCodeId;

    @SerializedName("final_benefit_amount")
    @Expose
    private int finalBenefitAmount;

    @SerializedName("final_benefit_discount_amount")
    @Expose
    private int finalBenefitDiscountAmount;

    @SerializedName("benefits_by_orders")
    @Expose
    private List<BenefitByOrder> listBenefitByOrders;

    public long getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(long promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public int getFinalBenefitAmount() {
        return finalBenefitAmount;
    }

    public void setFinalBenefitAmount(int finalBenefitAmount) {
        this.finalBenefitAmount = finalBenefitAmount;
    }

    public int getFinalBenefitDiscountAmount() {
        return finalBenefitDiscountAmount;
    }

    public void setFinalBenefitDiscountAmount(int finalBenefitDiscountAmount) {
        this.finalBenefitDiscountAmount = finalBenefitDiscountAmount;
    }

    public List<BenefitByOrder> getListBenefitByOrders() {
        return listBenefitByOrders;
    }

    public void setListBenefitByOrders(List<BenefitByOrder> listBenefitByOrders) {
        this.listBenefitByOrders = listBenefitByOrders;
    }
}
