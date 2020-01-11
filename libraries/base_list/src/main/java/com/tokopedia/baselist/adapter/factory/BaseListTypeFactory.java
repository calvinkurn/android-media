package com.tokopedia.baselist.adapter.factory;

import android.view.View;

import com.tokopedia.baselist.adapter.Visitable;
import com.tokopedia.baselist.adapter.viewholders.AbstractViewHolder;

/**
 * Created by alvarisi on 12/8/17.
 */

public interface BaseListTypeFactory<T extends Visitable> {

    int type(T viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
