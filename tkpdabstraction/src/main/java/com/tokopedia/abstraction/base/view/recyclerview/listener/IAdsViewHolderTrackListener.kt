package com.tokopedia.abstraction.base.view.recyclerview.listener

import androidx.recyclerview.widget.RecyclerView

interface IAdsViewHolderTrackListener {

    fun onViewAttachedToWindow() {}

    fun onViewDetachedFromWindow(visiblePercentage: Int) {}

    fun setVisiblePercentage(visiblePercentage: Int)

    val visiblePercentage: Int
}
