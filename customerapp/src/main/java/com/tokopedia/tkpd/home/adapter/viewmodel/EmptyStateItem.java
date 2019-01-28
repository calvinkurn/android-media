package com.tokopedia.tkpd.home.adapter.viewmodel;

import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

/**
 * Author errysuprayogi on 25,November,2018
 */
public class EmptyStateItem extends RecyclerViewItem {
    public EmptyStateItem() {
        setType(TkpdState.RecyclerView.VIEW_EMPTY_STATE);
    }
}
