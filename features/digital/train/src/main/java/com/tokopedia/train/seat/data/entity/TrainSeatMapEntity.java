package com.tokopedia.train.seat.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainSeatMapEntity {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("trainNo")
    @Expose
    private String trainNo;
    @SerializedName("departureTimestamp")
    @Expose
    private String departureTimestamp;
    @SerializedName("wagons")
    @Expose
    private List<TrainWagonEntity> wagons;

    public String getId() {
        return id;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public String getDepartureTimestamp() {
        return departureTimestamp;
    }

    public List<TrainWagonEntity> getWagons() {
        return wagons;
    }
}
