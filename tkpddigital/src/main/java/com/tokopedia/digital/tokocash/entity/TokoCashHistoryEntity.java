package com.tokopedia.digital.tokocash.entity;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class TokoCashHistoryEntity {

    private List<HeaderHistoryEntity> header;

    private List<ItemHistoryEntity> items;

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
