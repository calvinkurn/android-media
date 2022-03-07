package com.tokopedia.topchat.chatroom.view.adapter.util

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller

class CenterSmoothScroller(context: Context?) : LinearSmoothScroller(context) {

    /**
     * Calculate how much to scroll up or down to position view in the middle
     */
    override fun calculateDtToFit(
        viewStart: Int,
        viewEnd: Int,
        boxStart: Int,
        boxEnd: Int,
        snapPreference: Int
    ): Int {
        return (boxEnd + boxStart) / 2 - (viewStart + viewEnd) / 2
    }
}