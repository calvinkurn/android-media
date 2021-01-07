package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FreeShipping {

    @SerializedName("is_free_shipping")
    @Expose
    private boolean isFreeShipping;
    @SerializedName("is_eligible_free_shipping")
    @Expose
    private boolean isEligibleFreeShipping;

    public boolean isFreeShipping() {
        return isFreeShipping;
    }

    public void setFreeShipping(boolean freeShipping) {
        isFreeShipping = freeShipping;
    }

    public boolean isEligibleFreeShipping() {
        return isEligibleFreeShipping;
    }

    public void setEligibleFreeShipping(boolean eligibleFreeShipping) {
        isEligibleFreeShipping = eligibleFreeShipping;
    }
}
