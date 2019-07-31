package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PointHistoryList {
    @SerializedName("items")
    List<PointHistoryItem> items;

    @SerializedName("pageInfo")
    private TokopointPaging pageInfo;

    public List<PointHistoryItem> getItems() {
        return items;
    }

    public void setItems(List<PointHistoryItem> items) {
        this.items = items;
    }

    public TokopointPaging getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(TokopointPaging pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "PointHistoryList{" +
                "items=" + items +
                ", pageInfo=" + pageInfo +
                '}';
    }
}
