package com.tokopedia.shop.product.view.adapter.scrolllistener

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener

/**
 * Created by hendry on 16/07/18.
 */
abstract class DataEndlessScrollListener(layoutManager: RecyclerView.LayoutManager?,
                                         private val onDataEndlessScrollListener: OnDataEndlessScrollListener) : EndlessRecyclerViewScrollListener(layoutManager) {
    interface OnDataEndlessScrollListener {
        fun getEndlessDataSize(): Int
    }

    override fun isDataEmpty(): Boolean {
        return onDataEndlessScrollListener.getEndlessDataSize() == 0
    }
}