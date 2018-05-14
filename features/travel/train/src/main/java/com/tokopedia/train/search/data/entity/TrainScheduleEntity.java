package com.tokopedia.train.search.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabilla on 3/9/18.
 */

public class TrainScheduleEntity {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("arrivalTimestamp")
    @Expose
    private String arrivalTimestamp;
    @SerializedName("departureTimestamp")
    @Expose
    private String departureTimestamp;
    @SerializedName("displayDuration")
    @Expose
    private String displayDuration;
    @SerializedName("duration")
    @Expose
    private int duration;
    @SerializedName("trainKey")
    @Expose
    private String trainKey;
    @SerializedName("trainName")
    @Expose
    private String trainName;
    @SerializedName("trainNo")
    @Expose
    private String trainNo;
    @SerializedName("fares")
    @Expose
    private List<FareEntity> fares;

    public String getId() {
        return id;
    }

    public String getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public String getDepartureTimestamp() {
        return departureTimestamp;
    }

    public String getDisplayDuration() {
        return displayDuration;
    }

    public int getDuration() {
        return duration;
    }

    public String getTrainKey() {
        return trainKey;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public List<FareEntity> getFares() {
        return fares;
    }

}


