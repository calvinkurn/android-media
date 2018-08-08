package com.tokopedia.events.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LayoutDetail {

    @SerializedName("rowId")
    @Expose
    private int rowId;
    @SerializedName("physicalRowId")
    @Expose
    private String physicalRowId;
    @SerializedName("seat")
    @Expose
    private List<Seat> seat = null;

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getPhysicalRowId() {
        return physicalRowId;
    }

    public void setPhysicalRowId(String physicalRowId) {
        this.physicalRowId = physicalRowId;
    }

    public List<Seat> getSeat() {
        return seat;
    }

    public void setSeat(List<Seat> seat) {
        this.seat = seat;
    }

}
