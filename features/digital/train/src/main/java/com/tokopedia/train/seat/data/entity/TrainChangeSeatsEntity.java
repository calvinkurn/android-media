package com.tokopedia.train.seat.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainChangeSeatsEntity {
    @SerializedName("changeSeatmap")
    @Expose
    private List<TrainChangeSeatEntity> seats;

    public List<TrainChangeSeatEntity> getSeats() {
        return seats;
    }
}
