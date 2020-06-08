package com.tokopedia.entertainment.search.adapter.viewmodel

import android.content.res.Resources
import com.tokopedia.entertainment.search.adapter.SearchEventItem
import com.tokopedia.entertainment.search.adapter.factory.SearchTypeFactory
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventListViewHolder

class SearchEventViewModel(var listEvent: MutableList<SearchEventListViewHolder.KegiatanSuggestion>,
                           var resources: Resources) : SearchEventItem<SearchTypeFactory> {

    override fun type(typeFactory: SearchTypeFactory): Int {
        return typeFactory.type(this)
    }
}