
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResolutionAs {

    @SerializedName("resolution_as_buyer")
    @Expose
    private int resolutionAsBuyer;
    @SerializedName("resolution_as_seller")
    @Expose
    private int resolutionAsSeller;

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
