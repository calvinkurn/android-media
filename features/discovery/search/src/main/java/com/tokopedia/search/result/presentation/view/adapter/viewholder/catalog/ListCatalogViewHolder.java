package com.tokopedia.search.result.presentation.view.adapter.viewholder.catalog;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.CatalogViewModel;
import com.tokopedia.search.result.presentation.view.listener.CatalogListener;

public class ListCatalogViewHolder extends GridCatalogViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_listview_browse_catalog;

    public ListCatalogViewHolder(View itemView, CatalogListener mCatalogListener) {
        super(itemView, mCatalogListener);
    }

    @Override
    public void bind(CatalogViewModel element) {
        super.bind(element);
    }
}
