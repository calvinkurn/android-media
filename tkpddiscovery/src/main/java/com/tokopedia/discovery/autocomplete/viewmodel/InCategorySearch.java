package com.tokopedia.discovery.autocomplete.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.discovery.search.view.adapter.factory.SearchTypeFactory;

public class InCategorySearch extends BaseItemAutoCompleteSearch
        implements Visitable<SearchTypeFactory> {

    @Override
    public int type(SearchTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
