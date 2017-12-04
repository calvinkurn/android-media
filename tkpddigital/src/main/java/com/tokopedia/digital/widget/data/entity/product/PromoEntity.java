package com.tokopedia.digital.widget.data.entity.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class PromoEntity {

    @SerializedName("bonus_text")
    @Expose
    private String bonusText;
    @SerializedName("new_price")
    @Expose
    private String newPrice;
    @SerializedName("new_price_plain")
    @Expose
    private long newPricePlain;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("terms")
    @Expose
    private String terms;
    @SerializedName("value_text")
    @Expose
    private String valueText;

    public String getBonusText() {
        return bonusText;
    }

    public void setBonusText(String bonusText) {
        this.bonusText = bonusText;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public long getNewPricePlain() {
        return newPricePlain;
    }

    public void setNewPricePlain(long newPricePlain) {
        this.newPricePlain = newPricePlain;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getValueText() {
        return valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }
}
