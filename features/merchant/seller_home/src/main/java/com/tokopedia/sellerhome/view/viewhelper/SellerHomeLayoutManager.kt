package com.tokopedia.sellerhome.view.viewhelper

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created By @ilhamsuaib on 29/09/20
 */

class SellerHomeLayoutManager(
    context: Context?, spanCount: Int
) : GridLayoutManager(context, spanCount) {

    companion object {
        private const val DEFAULT_SCROLL_DISTANCE = 0
    }

    private var scrollVerticallyCallback: () -> Unit = {}

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        return try {
            scrollVerticallyCallback()
            super.scrollVerticallyBy(dy, recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            DEFAULT_SCROLL_DISTANCE
        }
    }

    override fun canScrollHorizontally(): Boolean {
        return false
    }

    fun setOnVerticalScrollListener(callback: () -> Unit) {
        this.scrollVerticallyCallback = callback
    }
}