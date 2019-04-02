package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/26/18.
 */
public class NotificationResolutionModel {
    @SerializedName("buyer")
    @Expose
    private Integer buyer = 0;
    @SerializedName("seller")
    @Expose
    private Integer seller = 0;

    public Integer getBuyer() {
        return buyer;
    }

    public void setBuyer(Integer buyer) {
        this.buyer = buyer;
    }

    public Integer getSeller() {
        return seller;
    }

    public void setSeller(Integer seller) {
        this.seller = seller;
    }
}
