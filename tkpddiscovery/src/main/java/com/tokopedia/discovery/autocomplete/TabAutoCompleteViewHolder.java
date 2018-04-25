package com.tokopedia.discovery.autocomplete;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;

public class TabAutoCompleteViewHolder extends AbstractViewHolder<TabAutoCompleteViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_suggestion_tabbing;

    public TabAutoCompleteViewHolder(View view, ItemClickListener clickListener) {
        super(view);

    }

    @Override
    public void bind(TabAutoCompleteViewModel element) {

    }
}
