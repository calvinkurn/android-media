package com.tokopedia.search.result.presentation.view.typefactory;

import com.tokopedia.search.result.presentation.model.EmptySearchViewModel;

public interface SearchSectionTypeFactory {
    int type(EmptySearchViewModel emptySearchViewModel);

    int getRecyclerViewItem();

    void setRecyclerViewItem(int recyclerViewItem);
}