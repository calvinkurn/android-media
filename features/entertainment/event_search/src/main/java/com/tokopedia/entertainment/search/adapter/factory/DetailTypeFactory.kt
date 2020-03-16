package com.tokopedia.entertainment.search.adapter.factory

import android.view.ViewGroup
import com.tokopedia.entertainment.search.adapter.DetailEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.CategoryTextViewModel
import com.tokopedia.entertainment.search.adapter.viewmodel.ResetFilterViewModel
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchEventGridViewModel

interface DetailTypeFactory {
    fun type(viewModel: SearchEventGridViewModel) : Int
    fun type(viewModel: CategoryTextViewModel) : Int
    fun type(viewModel: ResetFilterViewModel) : Int
    fun createViewHolder(view: ViewGroup, type : Int) : DetailEventViewHolder<*>
}