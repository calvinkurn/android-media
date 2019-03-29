package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokoPointStatusEntity {

    @Expose
    @SerializedName("tier")
    TokoPointStatusTierEntity tier;

    @Expose
    @SerializedName("points")
    TokoPointStatusPointsEntity points;

    @SerializedName("fullName")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public TokoPointStatusTierEntity getTier() {
        return tier;
    }

    public void setTier(TokoPointStatusTierEntity tier) {
        this.tier = tier;
    }

    public TokoPointStatusPointsEntity getPoints() {
        return points;
    }

    public void setPoints(TokoPointStatusPointsEntity points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "TokoPointStatusEntity{" +
                "tier=" + tier +
                ", points=" + points +
                '}';
    }
}
