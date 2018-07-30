package com.tokopedia.tokopoints.view.model;

import java.util.List;

public class TickerContainer {
    private int id;
    private String type;
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
