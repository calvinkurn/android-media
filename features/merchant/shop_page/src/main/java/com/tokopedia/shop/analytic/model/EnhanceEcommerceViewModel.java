package com.tokopedia.shop.analytic.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnhanceEcommerceViewModel {

    @SerializedName("currencyCode")
    @Expose
    private String currencyCode;
    @SerializedName("impressions")
    @Expose
    private List<Impression> impressions = null;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public List<Impression> getImpressions() {
        return impressions;
    }

    public void setImpressions(List<Impression> impressions) {
        this.impressions = impressions;
    }

}
