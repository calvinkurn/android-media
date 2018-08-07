package com.tokopedia.train.seat.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainKaiSeatMapEntity {
    @SerializedName("kaiSeatmap")
    @Expose
    TrainSeatsEntity kaiSeatmap;

    public TrainSeatsEntity getKaiSeatMap() {
        return kaiSeatmap;
    }
}
