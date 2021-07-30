package com.tokopedia.tokopedianow.searchcategory.utils

import androidx.recyclerview.widget.StaggeredGridLayoutManager

class CustomStaggeredGridLayoutManager: StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, VERTICAL) {

    companion object {
        private const val DEFAULT_SPAN_COUNT = 2
    }
    private var isScrollEnabled = true

    fun setScrollEnabled(flag: Boolean) {
        isScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
        return isScrollEnabled && super.canScrollVertically()
    }
}