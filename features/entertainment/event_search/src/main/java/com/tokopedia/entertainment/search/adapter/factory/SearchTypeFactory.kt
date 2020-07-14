package com.tokopedia.entertainment.search.adapter.factory

import android.view.ViewGroup
import com.tokopedia.entertainment.search.adapter.SearchEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.*

interface SearchTypeFactory {
    fun type(viewModel: FirstTimeViewModel) : Int
    fun type(viewModel: HistoryViewModel) : Int
    fun type(viewModel: SearchLocationViewModel) : Int
    fun type(viewModel: SearchEventViewModel) : Int
    fun type(viewModel: SearchEmptyStateViewModel): Int
    fun createViewHolder(view: ViewGroup, type : Int) : SearchEventViewHolder<*>
}