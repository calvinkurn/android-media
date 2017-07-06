package com.tokopedia.core.drawer2.data.pojo.toppoints;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 5/5/17.
 */

public class TopPointsData {

    @SerializedName("active")
    @Expose
    private boolean isActive;
    @SerializedName("loyalty_point")
    @Expose
    private LoyaltyPoint loyaltyPoint;
    @SerializedName("uri")
    @Expose
    private String uri;

    public LoyaltyPoint getLoyaltyPoint() {
        return loyaltyPoint;
    }

    public void setLoyaltyPoint(LoyaltyPoint loyaltyPoint) {
        this.loyaltyPoint = loyaltyPoint;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
