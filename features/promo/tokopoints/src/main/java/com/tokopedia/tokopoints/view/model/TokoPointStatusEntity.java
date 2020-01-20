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

    @SerializedName("emptyMessage")
    EmptyMessage emptyMessage;

    @SerializedName("cta")
    TokopointCta cta;

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

    public EmptyMessage getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(EmptyMessage emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public TokopointCta getCta() {
        return cta;
    }

    public void setCta(TokopointCta cta) {
        this.cta = cta;
    }

    @Override
    public String toString() {
        return "TokoPointStatusEntity{" +
                "tier=" + tier +
                ", points=" + points +
                ", userName='" + userName + '\'' +
                ", emptyMessage=" + emptyMessage +
                ", cta=" + cta +
                '}';
    }

}
