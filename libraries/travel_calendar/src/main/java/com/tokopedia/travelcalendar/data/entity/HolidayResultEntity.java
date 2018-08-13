package com.tokopedia.travelcalendar.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HolidayResultEntity {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("attributes")
    @Expose
    private HolidayDetailEntity attributes;

    public void setId(String id) {
        this.id = id;
    }

    public void setAttributes(HolidayDetailEntity attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return this.id;
    }

    public HolidayDetailEntity getAttributes() {
        return this.attributes;
    }
}
