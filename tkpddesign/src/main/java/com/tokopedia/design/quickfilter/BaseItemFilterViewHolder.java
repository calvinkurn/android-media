package com.tokopedia.design.quickfilter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by nabillasabbaha on 1/9/18.
 */

public abstract class BaseItemFilterViewHolder extends RecyclerView.ViewHolder {
    protected QuickSingleFilterListener listener;

    public BaseItemFilterViewHolder(View itemView, QuickSingleFilterListener actionListener) {
        super(itemView);
        this.listener = actionListener;
    }
}
