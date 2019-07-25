package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class PointHistoryBase {
    @SerializedName("pointHistory")
    private PointHistoryList pointHistory;

    public PointHistoryList getPointHistory() {
        return pointHistory;
    }

    public void setPointHistory(PointHistoryList pointHistory) {
        this.pointHistory = pointHistory;
    }

    @Override
    public String toString() {
        return "PointHistoryBase{" +
                "pointHistory=" + pointHistory +
                '}';
    }
}
