package com.tokopedia.train.passenger.data.cloud.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainPaxPassengerEntity {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("paxType")
    @Expose
    private int paxType;
    @SerializedName("seat")
    @Expose
    private TrainSeatEntity seat;

    public String getName() {
        return name;
    }

    public int getPaxType() {
        return paxType;
    }

    public TrainSeatEntity getSeat() {
        return seat;
    }
}
