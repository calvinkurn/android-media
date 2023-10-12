package com.tokopedia.chatbot.chatbot2.view.listener

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller

class SmoothScroller(context: Context?) : LinearSmoothScroller(context) {

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
