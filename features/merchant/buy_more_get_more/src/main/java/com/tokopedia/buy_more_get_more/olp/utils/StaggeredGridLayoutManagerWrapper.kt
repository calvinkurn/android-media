package com.tokopedia.buy_more_get_more.olp.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class StaggeredGridLayoutManagerWrapper(spanCount: Int, orientation: Int) : StaggeredGridLayoutManager(spanCount, orientation) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {

        }
    }

    override fun onScrollStateChanged(state: Int) {
        try {
            super.onScrollStateChanged(state)
        } catch (e: Exception) {

        }
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        return try {
            return super.scrollVerticallyBy(dy, recycler, state)
        } catch (e: Exception) {
            0
        }
    }
}
