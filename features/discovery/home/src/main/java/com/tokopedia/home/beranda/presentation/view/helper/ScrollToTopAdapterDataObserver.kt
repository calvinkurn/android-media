package com.tokopedia.home.beranda.presentation.view.helper

import androidx.recyclerview.widget.RecyclerView

class ScrollToTopAdapterDataObserver constructor(
    private val recyclerView: RecyclerView?
) : RecyclerView.AdapterDataObserver() {

    var forciblyFirstTimeScrollToTop = true

    private fun onceScrollToTop() {
        if (!forciblyFirstTimeScrollToTop) return

        recyclerView?.scrollToPosition(0)
    }

    override fun onChanged() = onceScrollToTop()
    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) = onceScrollToTop()
    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) = onceScrollToTop()
    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = onceScrollToTop()
    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) = onceScrollToTop()
    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = onceScrollToTop()
}
