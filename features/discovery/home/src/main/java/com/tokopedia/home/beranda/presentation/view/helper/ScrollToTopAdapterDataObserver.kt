package com.tokopedia.home.beranda.presentation.view.helper

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.beranda.helper.HomeFeedEndlessScrollListener

/**
 * Fixes a staggered layout manager issue.
 *
 * Saw the first-time load of for-you widget, the main recyclerView got unexpected behavior.
 * The start position to render got scrolled in the middle of position hence the first items
 * doesn't show properly.
 *
 * The second issue also, the staggered layout manager doesn't fit align on top, thus there are
 * a white space occurred for the 0 index position item.
 *
 * This is intermittent, if the for-you got a second recycled from parent home,
 * the issue is not happening. Additionally, the issue only happened in global version.
 */
class ScrollToTopAdapterDataObserver constructor(
    private val recyclerView: RecyclerView?
) : RecyclerView.AdapterDataObserver() {

    /**
     * A temporary flag.
     *
     * Since we faced a staggered layout manager issue, we have to ensure the first-time-load
     * should be get the right position and avoid [onceScrollToTop] re-call in infinite load state.
     *
     * The variable will be set to `false` if the user trigger load more from [HomeFeedEndlessScrollListener].
     */
    var forciblyFirstTimeScrollToTop = true

    private fun onceScrollToTop() {
        if (!forciblyFirstTimeScrollToTop) return

        recyclerView?.scrollToPosition(0)
    }

    override fun onChanged() = onceScrollToTop()
    override fun onItemRangeChanged(start: Int, count: Int) = onceScrollToTop()
    override fun onItemRangeChanged(start: Int, count: Int, payload: Any?) = onceScrollToTop()
    override fun onItemRangeInserted(start: Int, count: Int) = onceScrollToTop()
    override fun onItemRangeMoved(from: Int, to: Int, count: Int) = onceScrollToTop()
    override fun onItemRangeRemoved(start: Int, count: Int) = onceScrollToTop()
}
