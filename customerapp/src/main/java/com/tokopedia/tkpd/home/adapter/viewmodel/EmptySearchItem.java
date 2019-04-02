package com.tokopedia.tkpd.home.adapter.viewmodel;

import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

/**
 * Author errysuprayogi on 25,November,2018
 */
public class EmptySearchItem extends RecyclerViewItem {
    private String query = "";
    public EmptySearchItem(String query) {
        this.query = query;
        setType(TkpdState.RecyclerView.VIEW_EMPTY_SEARCH);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
