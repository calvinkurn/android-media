package com.tokopedia.train.search.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabilla on 3/9/18.
 */

public class ScheduleAvailabilityEntity {

    @SerializedName("id")
    @Expose
    private String idSchedule;
    @SerializedName("available")
    @Expose
    private int availableSeat;

    public String getIdSchedule() {
        return idSchedule;
    }

    public int getAvailableSeat() {
        return availableSeat;
    }
}
