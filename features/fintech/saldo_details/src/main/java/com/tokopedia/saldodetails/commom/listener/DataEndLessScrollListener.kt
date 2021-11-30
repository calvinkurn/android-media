package com.tokopedia.saldodetails.commom.listener

import androidx.recyclerview.widget.RecyclerView

import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener

abstract class DataEndLessScrollListener(layoutManager: RecyclerView.LayoutManager?,
                                         private val onDataEndlessScrollListener: OnDataEndlessScrollListener?) : EndlessRecyclerViewScrollListener(layoutManager) {

    interface OnDataEndlessScrollListener {
        fun endlessDataSize(): Int
    }

    override fun isDataEmpty(): Boolean {
        return onDataEndlessScrollListener?.endlessDataSize() == 0
    }

}
