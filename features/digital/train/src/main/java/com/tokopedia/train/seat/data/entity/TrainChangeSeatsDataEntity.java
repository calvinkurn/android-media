package com.tokopedia.train.seat.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainChangeSeatsDataEntity {
    @SerializedName("kaiGantiKursi")
    @Expose
    private TrainChangeSeatsEntity seats;

    public TrainChangeSeatsEntity getSeats() {
        return seats;
    }
}
