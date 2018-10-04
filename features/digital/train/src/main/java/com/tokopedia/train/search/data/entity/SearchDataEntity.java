package com.tokopedia.train.search.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rizky on 14/05/18.
 */
public class SearchDataEntity {

    @SerializedName("schedules")
    @Expose
    private List<ScheduleEntity> schedules;

    public List<ScheduleEntity> getSchedules() {
        return schedules;
    }
}
