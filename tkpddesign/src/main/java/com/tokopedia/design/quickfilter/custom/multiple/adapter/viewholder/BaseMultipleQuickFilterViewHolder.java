package com.tokopedia.design.quickfilter.custom.multiple.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.design.quickfilter.custom.multiple.listener.QuickMultipleItemListener;

/**
 * Created by yfsx on 30/01/18.
 */

public abstract class BaseMultipleQuickFilterViewHolder extends RecyclerView.ViewHolder {
    protected QuickMultipleItemListener listener;

    public BaseMultipleQuickFilterViewHolder(View itemView, QuickMultipleItemListener actionListener) {
        super(itemView);
        this.listener = actionListener;
    }
}
