package com.tokopedia.entertainment.search.adapter.factory

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.entertainment.search.adapter.SearchEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.*
import com.tokopedia.entertainment.search.adapter.viewmodel.*

class SearchTypeFactoryImp(val onClicked: (() -> Unit) = {},
                           val searchEventListener: SearchEventListViewHolder.SearchEventListListener,
                           val searchLocationListener: SearchLocationListViewHolder.SearchLocationListener
) : SearchTypeFactory {

    override fun type(model: FirstTimeModel): Int {
        return FirstTimeBackgroundItemViewHolder.LAYOUT
    }

    override fun type(model: HistoryModel): Int {
        return HistoryBackgroundItemViewHolder.LAYOUT
    }

    override fun type(model: SearchLocationModel): Int {
        return SearchLocationListViewHolder.LAYOUT
    }

    override fun type(model: SearchEventModel): Int {
        return SearchEventListViewHolder.LAYOUT
    }

    override fun type(model: SearchEmptyStateModel): Int {
        return SearchEmptyStateViewHolder.LAYOUT
    }

    override fun createViewHolder(view: ViewGroup, type: Int): SearchEventViewHolder<*> {
        val creatEventViewHolder: SearchEventViewHolder<*>
        creatEventViewHolder = if (type == FirstTimeBackgroundItemViewHolder.LAYOUT) {
            FirstTimeBackgroundItemViewHolder(view)
        } else if (type == HistoryBackgroundItemViewHolder.LAYOUT) {
            HistoryBackgroundItemViewHolder(view)
        }  else if (type == SearchLocationListViewHolder.LAYOUT) {
            SearchLocationListViewHolder(view, onClicked, searchLocationListener)
        } else if(type == SearchEventListViewHolder.LAYOUT){
            SearchEventListViewHolder(view, searchEventListener)
        } else if(type == SearchEmptyStateViewHolder.LAYOUT){
            SearchEmptyStateViewHolder(view)
        } else{
            throw TypeNotSupportedException.create("Layout not supported")
        }
        return creatEventViewHolder
    }
}