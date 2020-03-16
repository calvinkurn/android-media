package com.tokopedia.entertainment.search.adapter.factory

import android.view.ViewGroup
import com.tokopedia.entertainment.search.adapter.SearchEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.FirstTimeViewModel
import com.tokopedia.entertainment.search.adapter.viewmodel.HistoryViewModel
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchEventViewModel
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchLocationViewModel

interface SearchTypeFactory {
    fun type(viewModel: FirstTimeViewModel) : Int
    fun type(viewModel: HistoryViewModel) : Int
    fun type(viewModel: SearchLocationViewModel) : Int
    fun type(viewModel: SearchEventViewModel) : Int
    fun createViewHolder(view: ViewGroup, type : Int) : SearchEventViewHolder<*>
}