package com.tokopedia.travelcalendar.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HolidayDetailEntity {
    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("label")
    @Expose
    private String label;

    public void setDate(String date) {
        this.date = date;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDate() {
        return this.date;
    }

    public String getLabel() {
        return this.label;
    }
}
