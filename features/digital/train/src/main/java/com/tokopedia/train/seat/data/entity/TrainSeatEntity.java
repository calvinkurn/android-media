package com.tokopedia.train.seat.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainSeatEntity {
    @SerializedName("column")
    @Expose
    private String column;
    @SerializedName("row")
    @Expose
    private int row;
    @SerializedName("status")
    @Expose
    private int status;

    public String getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getStatus() {
        return status;
    }
}
