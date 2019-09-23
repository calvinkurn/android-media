package com.tokopedia.autocomplete.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.adapter.SearchAdapterTypeFactory
import com.tokopedia.autocomplete.viewmodel.BaseItemAutoCompleteSearch

class HotlistSearch : BaseItemAutoCompleteSearch(), Visitable<SearchAdapterTypeFactory> {
    override fun type(typeFactory: SearchAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}