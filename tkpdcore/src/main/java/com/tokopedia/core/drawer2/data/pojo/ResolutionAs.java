
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResolutionAs {

    @SerializedName("resolution_as_buyer")
    @Expose
    private Integer resolutionAsBuyer;
    @SerializedName("resolution_as_seller")
    @Expose
    private Integer resolutionAsSeller;

    public Integer getResolutionAsBuyer() {
        return resolutionAsBuyer;
    }

    public void setResolutionAsBuyer(Integer resolutionAsBuyer) {
        this.resolutionAsBuyer = resolutionAsBuyer;
    }

    public Integer getResolutionAsSeller() {
        return resolutionAsSeller;
    }

    public void setResolutionAsSeller(Integer resolutionAsSeller) {
        this.resolutionAsSeller = resolutionAsSeller;
    }

}
