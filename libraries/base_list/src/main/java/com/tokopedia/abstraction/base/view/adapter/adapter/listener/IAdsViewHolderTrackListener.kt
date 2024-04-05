package com.tokopedia.abstraction.base.view.adapter.adapter.listener


interface IAdsViewHolderTrackListener {
    fun onViewAttachedToWindow()

    fun onViewDetachedFromWindow()
    fun setVisiblePercentage(visiblePercentage: Int)

    val visiblePercentage: Int
}
