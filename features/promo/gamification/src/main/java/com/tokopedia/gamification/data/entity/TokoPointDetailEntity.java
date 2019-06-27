package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokoPointDetailEntity {

    @Expose
    @SerializedName("tokopoints")
    TokoPointEntity tokoPoints;

    public TokoPointEntity getTokoPoints() {
        return tokoPoints;
    }

    public void setTokoPoints(TokoPointEntity tokoPoints) {
        this.tokoPoints = tokoPoints;
    }

    @Override
    public String toString() {
        return "TokoPointDetailEntity{" +
                "tokoPoints=" + tokoPoints +
                '}';
    }
}
