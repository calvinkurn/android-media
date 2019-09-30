package com.tokopedia.autocomplete.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.autocomplete.adapter.SearchAdapterTypeFactory;

public class AutoCompleteSearch extends BaseItemAutoCompleteSearch
        implements Visitable<SearchAdapterTypeFactory> {

    @Override
    public int type(SearchAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
