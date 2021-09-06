package com.tokopedia.review.feature.imagepreview.presentation.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class ReviewImagePreviewLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean) : LinearLayoutManager(context, orientation, reverseLayout) {

    private var isScrollEnabled = true

    fun setScrollEnabled(flag: Boolean) {
        isScrollEnabled = flag
    }

    override fun canScrollHorizontally(): Boolean {
        return isScrollEnabled && super.canScrollHorizontally()
    }
}