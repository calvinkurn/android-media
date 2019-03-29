package com.tokopedia.discovery.autocomplete.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.discovery.search.view.adapter.factory.SearchAdapterTypeFactory;

public class AutoCompleteSearch extends BaseItemAutoCompleteSearch
        implements Visitable<SearchAdapterTypeFactory> {

    @Override
    public int type(SearchAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
