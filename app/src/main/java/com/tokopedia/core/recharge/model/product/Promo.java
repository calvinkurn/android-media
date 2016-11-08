package com.tokopedia.core.recharge.model.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ricoharisin on 7/15/16.
 */
public class Promo {

    @SerializedName("bonus_text")
    @Expose
    private String bonusText;
    @SerializedName("new_price")
    @Expose
    private String newPrice;
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
