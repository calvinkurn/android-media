package com.tokopedia.digital.tokocash.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class TokoCashHistoryEntity {

    @SerializedName("header")
    @Expose
    private List<HeaderHistoryEntity> header;
    @SerializedName("items")
    @Expose
    private List<ItemHistoryEntity> items;
    @SerializedName("next_uri")
    @Expose
    private boolean next_uri;

    public List<HeaderHistoryEntity> getHeader() {
        return header;
    }

    public void setHeader(List<HeaderHistoryEntity> header) {
        this.header = header;
    }

    public List<ItemHistoryEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemHistoryEntity> items) {
        this.items = items;
    }

    public boolean isNext_uri() {
        return next_uri;
    }

    public void setNext_uri(boolean next_uri) {
        this.next_uri = next_uri;
    }
}
