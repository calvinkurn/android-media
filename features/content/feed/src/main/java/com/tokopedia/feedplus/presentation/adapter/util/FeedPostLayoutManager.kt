package com.tokopedia.feedplus.presentation.adapter.util

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by kenny.hadisaputra on 05/07/23
 */
class FeedPostLayoutManager(
    context: Context?,
    private val listener: Listener
) : LinearLayoutManager(context, RecyclerView.VERTICAL, false) {

    private val helper = OrientationHelper.createOrientationHelper(this, RecyclerView.VERTICAL)

    override fun calculateExtraLayoutSpace(state: RecyclerView.State, extraLayoutSpace: IntArray) {
        extraLayoutSpace[0] = 2 * helper.totalSpace
        extraLayoutSpace[1] = 2 * helper.totalSpace
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val scrollRange = super.scrollVerticallyBy(dy, recycler, state)
        val overScroll = dy - scrollRange

        listener.onScrolling(this, overScroll != 0)
        return scrollRange
    }

    interface Listener {
        fun onScrolling(layoutManager: LinearLayoutManager, isOverScroll: Boolean)
    }
}
