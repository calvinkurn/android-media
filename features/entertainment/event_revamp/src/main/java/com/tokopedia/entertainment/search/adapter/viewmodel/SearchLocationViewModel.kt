package com.tokopedia.entertainment.search.adapter.viewmodel

import com.tokopedia.entertainment.search.adapter.SearchEventItem
import com.tokopedia.entertainment.search.adapter.factory.SearchTypeFactory
import com.tokopedia.entertainment.search.adapter.viewholder.SearchLocationListViewHolder

class SearchLocationViewModel(var listLocation: MutableList<SearchLocationListViewHolder.LocationSuggestion>,
                              var query: String = "", var allLocation : Boolean = false) : SearchEventItem<SearchTypeFactory> {

    override fun type(typeFactory: SearchTypeFactory): Int {
        return typeFactory.type(this)
    }
}