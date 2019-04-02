package com.tokopedia.train.search.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rizky on 14/05/18.
 */
public class ScheduleEntity {

    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("trains")
    @Expose
    private List<TrainScheduleEntity> trains;

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public List<TrainScheduleEntity> getTrains() {
        return trains;
    }
}
