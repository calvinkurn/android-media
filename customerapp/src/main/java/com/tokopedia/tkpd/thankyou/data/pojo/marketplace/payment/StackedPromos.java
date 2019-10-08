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

//    @SerializedName("benefits_by_products")
//    @Expose
//    private List<BenefitByProduct> listBenefitByProducts;

    @SerializedName("final_benefit_amount")
    @Expose
    private float finalBenefitAmount;

//    @SerializedName("final_cashback_amount")
//    @Expose
//    private float finalCashbackAmount;
//
//    @SerializedName("final_discount_amount")
//    @Expose
//    private float finalDiscountAmount;
//
//    @SerializedName("message")
//    @Expose
//    private String message;

    @SerializedName("promo_code_id")
    @Expose
    private String promoCodeId;

//    @SerializedName("error")
//    @Expose
//    private String error;

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
