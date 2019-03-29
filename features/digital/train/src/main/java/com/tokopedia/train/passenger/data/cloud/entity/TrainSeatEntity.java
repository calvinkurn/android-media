package com.tokopedia.train.passenger.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainSeatEntity {
    /**
     * Example of response seat
     * <p>
     * "seat": {
     * "wagonCode": "EKS-1",
     * "row": "3",
     * "column": "E"
     * }
     */

    @SerializedName("wagonCode")
    @Expose
    private String wagonNo;
    @SerializedName("row")
    @Expose
    private String row;
    @SerializedName("column")
    @Expose
    private String column;

    public String getWagonNo() {
        return wagonNo;
    }

    public String getRow() {
        return row;
    }

    public String getColumn() {
        return column;
    }
}
