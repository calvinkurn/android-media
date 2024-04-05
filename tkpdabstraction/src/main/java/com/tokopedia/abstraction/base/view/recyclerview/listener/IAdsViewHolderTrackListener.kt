package com.tokopedia.abstraction.base.view.recyclerview.listener

import androidx.recyclerview.widget.RecyclerView

interface IAdsViewHolderTrackListener {
    fun onViewAttachedToWindow(recyclerView: RecyclerView?)

    fun onViewDetachedFromWindow(recyclerView: RecyclerView?)
    fun setVisiblePercentage(visiblePercentage: Int)

    val visiblePercentage: Int
}
