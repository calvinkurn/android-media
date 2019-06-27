package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class PromoSuggestion {

    @SerializedName("cta")
    @Expose
    private String cta = "";
    @SerializedName("cta_color")
    @Expose
    private String ctaColor = "";
    @SerializedName("is_visible")
    @Expose
    private int isVisible;
    @SerializedName("promo_code")
    @Expose
    private String promoCode = "";
    @SerializedName("text")
    @Expose
    private String text = "";

    public String getCta() {
        return cta;
    }

    public String getCtaColor() {
        return ctaColor;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public String getText() {
        return text;
    }
}
