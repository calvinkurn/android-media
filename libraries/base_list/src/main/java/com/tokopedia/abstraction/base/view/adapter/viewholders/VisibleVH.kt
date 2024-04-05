package com.tokopedia.abstraction.base.view.adapter.viewholders

interface VisibleVH {

    fun setVisibilityExtent(visibilityExtent: Float)

    fun getVisiblePercentage(): Int

    fun onViewDetachedFromWindow(visiblePercentage: Int) {}

    fun onViewAttachedToWindow() {}
}
