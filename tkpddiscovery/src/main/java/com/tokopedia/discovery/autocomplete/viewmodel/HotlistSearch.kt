package com.tokopedia.discovery.autocomplete.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.search.view.adapter.factory.SearchAdapterTypeFactory

class HotlistSearch : BaseItemAutoCompleteSearch(), Visitable<SearchAdapterTypeFactory> {
    override fun type(typeFactory: SearchAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}