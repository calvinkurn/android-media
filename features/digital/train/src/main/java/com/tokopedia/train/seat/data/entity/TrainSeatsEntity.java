package com.tokopedia.train.seat.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainSeatsEntity {
    @SerializedName("seatmaps")
    @Expose
    private List<TrainSeatMapEntity> seatMapEntities;

    public List<TrainSeatMapEntity> getSeatMapEntities() {
        return seatMapEntities;
    }
}
