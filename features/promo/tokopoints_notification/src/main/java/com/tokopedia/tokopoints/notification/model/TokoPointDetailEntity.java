package com.tokopedia.tokopoints.notification.model;

import com.google.gson.annotations.SerializedName;

public class TokoPointDetailEntity {

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
