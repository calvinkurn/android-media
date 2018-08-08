package com.tokopedia.train.search.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rizky on 14/05/18.
 */
public class ScheduleAvailibilityDataEntity {

    @SerializedName("availabilities")
    @Expose
    private List<AvailabilityEntity> availabilities;

    public List<AvailabilityEntity> getAvailabilities() {
        return availabilities;
    }
}
