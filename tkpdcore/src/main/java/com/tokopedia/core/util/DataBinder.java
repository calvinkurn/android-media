package com.tokopedia.core.util;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Nisie on 2/26/16.
 */
@Deprecated
public abstract class DataBinder<T extends RecyclerView.ViewHolder> extends com.tokopedia.abstraction.base.view.adapter.binder.DataBinder<T> {

    public DataBinder(com.tokopedia.abstraction.base.view.adapter.binder.DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }
}