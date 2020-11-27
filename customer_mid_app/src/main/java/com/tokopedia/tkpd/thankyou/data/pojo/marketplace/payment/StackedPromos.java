package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fwidjaja on 18/04/19.
 */
public class StackedPromos {
    @SerializedName("benefits_by_orders")
    @Expose
    private List<BenefitByOrder> listBenefitByOrders;

    @SerializedName("final_benefit_amount")
    @Expose
    private float finalBenefitAmount;

    @SerializedName("promo_code_id")
    @Expose
    private String promoCodeId;

    public String getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(String promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public float getFinalBenefitAmount() {
        return finalBenefitAmount;
    }

    public void setFinalBenefitAmount(float finalBenefitAmount) {
        this.finalBenefitAmount = finalBenefitAmount;
    }

    public List<BenefitByOrder> getListBenefitByOrders() {
        return listBenefitByOrders;
    }

    public void setListBenefitByOrders(List<BenefitByOrder> listBenefitByOrders) {
        this.listBenefitByOrders = listBenefitByOrders;
    }
}
