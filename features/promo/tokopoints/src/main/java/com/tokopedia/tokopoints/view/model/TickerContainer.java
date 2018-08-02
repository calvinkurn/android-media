package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TickerContainer {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("type")
    private String type;

    @Expose
    @SerializedName("metadata")
    private List<TickerMetadata> metadata;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TickerMetadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<TickerMetadata> metadata) {
        this.metadata = metadata;
    }
}
