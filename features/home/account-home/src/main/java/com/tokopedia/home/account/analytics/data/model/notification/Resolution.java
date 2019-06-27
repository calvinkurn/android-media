package com.tokopedia.home.account.analytics.data.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 31/01/18.
 */
public class Resolution {
    @SerializedName("resolution_as_buyer")
    @Expose
    private int resolutionBuyer = 0;
    @SerializedName("resolution_as_seller")
    @Expose
    private int resolutionSeller = 0;

    public int getResolutionBuyer() {
        return resolutionBuyer;
    }

    public void setResolutionBuyer(int resolutionBuyer) {
        this.resolutionBuyer = resolutionBuyer;
    }

    public int getResolutionSeller() {
        return resolutionSeller;
    }

    public void setResolutionSeller(int resolutionSeller) {
        this.resolutionSeller = resolutionSeller;
    }
}
