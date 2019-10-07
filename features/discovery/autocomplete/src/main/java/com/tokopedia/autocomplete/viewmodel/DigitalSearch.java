package com.tokopedia.autocomplete.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.autocomplete.adapter.SearchTypeFactory;

public class DigitalSearch extends BaseItemAutoCompleteSearch
        implements Visitable<SearchTypeFactory> {

    @Override
    public int type(SearchTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
