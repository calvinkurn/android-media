package com.tokopedia.train.seat.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainWagonEntity {
    @SerializedName("wagonCode")
    @Expose
    private String wagonCode;
    @SerializedName("seatDetail")
    @Expose
    private List<TrainSeatEntity> seatDetailEntities;

    public String getWagonCode() {
        return wagonCode;
    }

    public List<TrainSeatEntity> getSeatDetailEntities() {
        return seatDetailEntities;
    }
}
