package com.tokopedia.travelcalendar.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public class HolidayEntity {

    @SerializedName("flightHoliday")
    @Expose
    private List<HolidayResultEntity> holidayResultEntities;

    public List<HolidayResultEntity> getHolidayResultEntities() {
        return holidayResultEntities;
    }

    public void setHolidayResultEntities(List<HolidayResultEntity> holidayResultEntities) {
        this.holidayResultEntities = holidayResultEntities;
    }
}
