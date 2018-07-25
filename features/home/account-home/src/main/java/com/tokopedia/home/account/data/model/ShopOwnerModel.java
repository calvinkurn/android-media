package com.tokopedia.home.account.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class ShopOwnerModel {
    @SerializedName("is_gold_merchant")
    @Expose
    private Boolean isGoldMerchant;

    public Boolean getGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(Boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }
}
