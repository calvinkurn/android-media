package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 5/2/17.
 */

public class Promo {

    @SerializedName("bonus_text")
    @Expose
    private String bonusText;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("new_price")
    @Expose
    private String newPrice;
    @SerializedName("new_price_plain")
    @Expose
    private int newPricePlain;
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

    public String getId() {
        return id;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public int getNewPricePlain() {
        return newPricePlain;
    }

    public String getTag() {
        return tag;
    }

    public String getTerms() {
        return terms;
    }

    public String getValueText() {
        return valueText;
    }
}
