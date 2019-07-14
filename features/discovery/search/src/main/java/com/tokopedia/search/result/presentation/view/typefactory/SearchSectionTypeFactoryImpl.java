package com.tokopedia.search.result.presentation.view.typefactory;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;

abstract class SearchSectionTypeFactoryImpl extends BaseAdapterTypeFactory implements SearchSectionTypeFactory {

    private int recyclerViewItem;

    @Override
    public int getRecyclerViewItem() {
        return recyclerViewItem;
    }

    @Override
    public void setRecyclerViewItem(int recyclerViewItem) {
        this.recyclerViewItem = recyclerViewItem;
    }
}
