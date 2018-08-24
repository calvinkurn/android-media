package com.tokopedia.core.referral.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ashwanityagi on 08/11/17.
 */

public class ReferralCodeEntity {

    @SerializedName("promo_content")
    @Expose
    private PromoContent promoContent;
    @SerializedName("promo_benefit")
    @Expose
    private PromoBenefit promoBenefit;
    @SerializedName("error")
    @Expose
    private String erorMessage;

    public PromoContent getPromoContent() {
        return promoContent;
    }

    public void setPromoContent(PromoContent promoContent) {
        this.promoContent = promoContent;
    }

    public String getErorMessage() {
        return erorMessage;
    }

    public void setErorMessage(String erorMessage) {
        this.erorMessage = erorMessage;
    }

    public PromoBenefit getPromoBenefit() {
        return promoBenefit;
    }

    public void setPromoBenefit(PromoBenefit promoBenefit) {
        this.promoBenefit = promoBenefit;
    }
}