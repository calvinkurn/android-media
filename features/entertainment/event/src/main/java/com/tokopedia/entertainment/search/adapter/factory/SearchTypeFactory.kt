package com.tokopedia.entertainment.search.adapter.factory

import android.view.ViewGroup
import com.tokopedia.entertainment.search.adapter.SearchEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.*

interface SearchTypeFactory {
    fun type(model: FirstTimeModel) : Int
    fun type(model: HistoryModel) : Int
    fun type(model: SearchLocationModel) : Int
    fun type(model: SearchEventModel) : Int
    fun type(model: SearchEmptyStateModel): Int
    fun createViewHolder(view: ViewGroup, type : Int) : SearchEventViewHolder<*>
}