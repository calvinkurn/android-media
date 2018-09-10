package com.tokopedia.train.seat.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainChangeSeatEntity {
    @SerializedName("bookCode")
    @Expose
    private String bookCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("wagonCode")
    @Expose
    private String wagonCode;
    @SerializedName("seat")
    @Expose
    private String seat;

    public TrainChangeSeatEntity() {
    }

    public String getBookCode() {
        return bookCode;
    }

    public String getName() {
        return name;
    }

    public String getWagonCode() {
        return wagonCode;
    }

    public String getSeat() {
        return seat;
    }
}
