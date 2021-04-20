package com.tokopedia.sellerhome.view.viewhelper

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created By @ilhamsuaib on 29/09/20
 */

class SellerHomeLayoutManager(context: Context?, spanCount: Int) : GridLayoutManager(context, spanCount) {

    private var scrollVerticallyCallback: () -> Unit = {}

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        scrollVerticallyCallback()
        return super.scrollVerticallyBy(dy, recycler, state)
    }

    override fun canScrollHorizontally(): Boolean {
        return false
    }

    fun setOnVerticalScrollListener(callback: () -> Unit) {
        this.scrollVerticallyCallback = callback
    }
}