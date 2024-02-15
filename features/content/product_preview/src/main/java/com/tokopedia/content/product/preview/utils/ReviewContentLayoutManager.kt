package com.tokopedia.content.product.preview.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReviewContentLayoutManager(
    context: Context,
    private val listener: Listener
) : LinearLayoutManager(context, RecyclerView.VERTICAL, false) {

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val scrollRange = super.scrollVerticallyBy(dy, recycler, state)
        val overScroll = dy - scrollRange

        listener.onScrolling(overScroll != 0)
        return scrollRange
    }

    interface Listener {
        fun onScrolling(isOverScroll: Boolean)
    }
}
