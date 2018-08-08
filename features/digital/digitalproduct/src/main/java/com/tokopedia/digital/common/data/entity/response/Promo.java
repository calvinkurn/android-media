
package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Vishal Gupta 7th May, 2018
 */
public class Promo {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("bonus_text")
    @Expose
    private String bonusText;
    @SerializedName("new_price")
    @Expose
    private String newPrice;
    @SerializedName("new_price_plain")
    @Expose
    private Integer newPricePlain;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("terms")
    @Expose
    private String terms;
    @SerializedName("value_text")
    @Expose
    private String valueText;
    @SerializedName("__typename")
    @Expose
    private String typename;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Integer getNewPricePlain() {
        return newPricePlain;
    }

    public void setNewPricePlain(Integer newPricePlain) {
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

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

}
