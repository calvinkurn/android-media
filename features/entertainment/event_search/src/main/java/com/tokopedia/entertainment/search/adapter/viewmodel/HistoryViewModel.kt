package com.tokopedia.entertainment.search.adapter.viewmodel

import com.tokopedia.entertainment.search.adapter.SearchEventItem
import com.tokopedia.entertainment.search.adapter.factory.SearchTypeFactory
import com.tokopedia.entertainment.search.adapter.viewholder.HistoryBackgroundItemViewHolder

class HistoryViewModel(var list: MutableList<HistoryBackgroundItemViewHolder.EventModel>) : SearchEventItem<SearchTypeFactory> {

    override fun type(typeFactory: SearchTypeFactory): Int {
        return typeFactory.type(this)
    }
}