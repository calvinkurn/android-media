package com.tokopedia.reviewseller.feature.reviewlist.view.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener

abstract class DataEndlessScrollListener(layoutManager: RecyclerView.LayoutManager?,
                                         private val onDataEndlessScrollListener: OnDataEndlessScrollListener) : EndlessRecyclerViewScrollListener(layoutManager) {

    interface OnDataEndlessScrollListener {
        val endlessDataSize: Int
    }

    override fun isDataEmpty(): Boolean {
        return onDataEndlessScrollListener.endlessDataSize == 0
    }

}