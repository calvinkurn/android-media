package com.tokopedia.digital.tokocash.model;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class TokoCashHistoryData {

    private List<HeaderHistory> headerHistory;

    private List<ItemHistory> itemHistoryList;

    private boolean next_uri;

    public List<HeaderHistory> getHeaderHistory() {
        return headerHistory;
    }

    public void setHeaderHistory(List<HeaderHistory> headerHistory) {
        this.headerHistory = headerHistory;
    }

    public List<ItemHistory> getItemHistoryList() {
        return itemHistoryList;
    }

    public void setItemHistoryList(List<ItemHistory> itemHistoryList) {
        this.itemHistoryList = itemHistoryList;
    }

    public boolean isNext_uri() {
        return next_uri;
    }

    public void setNext_uri(boolean next_uri) {
        this.next_uri = next_uri;
    }
}
