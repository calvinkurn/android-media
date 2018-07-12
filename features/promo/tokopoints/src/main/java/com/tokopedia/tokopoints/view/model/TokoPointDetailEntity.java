package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokoPointDetailEntity {

    @Expose
    @SerializedName("tokopoints")
    TokoPointEntity tokoPoints;

    @Expose
    @SerializedName("luckyegg")
    LuckyEggEntity luckyEgg;

    public LuckyEggEntity getLuckyEgg() {
        return luckyEgg;
    }

    public void setLuckyEgg(LuckyEggEntity luckyEgg) {
        this.luckyEgg = luckyEgg;
    }

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
                ", luckyEgg=" + luckyEgg +
                '}';
    }
}
