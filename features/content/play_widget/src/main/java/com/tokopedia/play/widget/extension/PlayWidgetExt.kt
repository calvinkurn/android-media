package com.tokopedia.play.widget.extension

import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay

/**
 * Created by jegul on 24/11/20
 */
suspend fun RecyclerView.stepScrollToPositionWithDelay(position: Int, delayMillis: Long) {
    var cursor = 0
    while (cursor <= position) {
        delay(delayMillis)
        smoothScrollToPosition(cursor++)
    }
}