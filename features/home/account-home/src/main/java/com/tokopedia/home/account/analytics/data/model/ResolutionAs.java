
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResolutionAs {

    @SerializedName("buyer")
    @Expose
    private int resolutionAsBuyer = 0;
    @SerializedName("seller")
    @Expose
    private int resolutionAsSeller = 0;

    public int getResolutionAsBuyer() {
        return resolutionAsBuyer;
    }

    public void setResolutionAsBuyer(int resolutionAsBuyer) {
        this.resolutionAsBuyer = resolutionAsBuyer;
    }

    public int getResolutionAsSeller() {
        return resolutionAsSeller;
    }

    public void setResolutionAsSeller(int resolutionAsSeller) {
        this.resolutionAsSeller = resolutionAsSeller;
    }

}
