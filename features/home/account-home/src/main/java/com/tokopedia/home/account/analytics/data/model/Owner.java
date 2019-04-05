
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("__typename")
    @Expose
    private String typename = "";
    @SerializedName("is_gold_merchant")
    @Expose
    private Boolean isGoldMerchant = false;
    @SerializedName("is_seller")
    @Expose
    private Boolean isSeller = false;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public Boolean getIsGoldMerchant() {
        return isGoldMerchant;
    }

    public void setIsGoldMerchant(Boolean isGoldMerchant) {
        this.isGoldMerchant = isGoldMerchant;
    }

    public Boolean getIsSeller() {
        return isSeller;
    }

    public void setIsSeller(Boolean isSeller) {
        this.isSeller = isSeller;
    }

}
