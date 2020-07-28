package com.tokopedia.entertainment.search.adapter.factory

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.entertainment.search.adapter.SearchEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.*
import com.tokopedia.entertainment.search.adapter.viewmodel.*
import kotlin.reflect.KFunction0

class SearchTypeFactoryImp(val onClicked: (() -> Unit) = {}) : SearchTypeFactory {

    override fun type(viewModel: FirstTimeViewModel): Int {
        return FirstTimeBackgroundItemViewHolder.LAYOUT
    }

    override fun type(viewModel: HistoryViewModel): Int {
        return HistoryBackgroundItemViewHolder.LAYOUT
    }

    override fun type(viewModel: SearchLocationViewModel): Int {
        return SearchLocationListViewHolder.LAYOUT
    }

    override fun type(viewModel: SearchEventViewModel): Int {
        return SearchEventListViewHolder.LAYOUT
    }

    override fun type(viewModel: SearchEmptyStateViewModel): Int {
        return SearchEmptyStateViewHolder.LAYOUT
    }

    override fun createViewHolder(view: ViewGroup, type: Int): SearchEventViewHolder<*> {
        val creatEventViewHolder: SearchEventViewHolder<*>
        creatEventViewHolder = if (type == FirstTimeBackgroundItemViewHolder.LAYOUT) {
            FirstTimeBackgroundItemViewHolder(view)
        } else if (type == HistoryBackgroundItemViewHolder.LAYOUT) {
            HistoryBackgroundItemViewHolder(view)
        }  else if (type == SearchLocationListViewHolder.LAYOUT) {
            SearchLocationListViewHolder(view, onClicked)
        } else if(type == SearchEventListViewHolder.LAYOUT){
            SearchEventListViewHolder(view)
        } else if(type == SearchEmptyStateViewHolder.LAYOUT){
            SearchEmptyStateViewHolder(view)
        } else{
            throw TypeNotSupportedException.create("Layout not supported")
        }
        return creatEventViewHolder
    }
}