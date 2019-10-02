package com.tokopedia.autocomplete;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

public interface HostAutoCompleteTypeFactory {

    int type(DefaultAutoCompleteViewModel viewModel);

    int type(TabAutoCompleteViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
