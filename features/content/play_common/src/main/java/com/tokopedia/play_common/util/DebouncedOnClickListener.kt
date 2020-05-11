package com.tokopedia.play_common.util

import android.os.SystemClock
import android.view.View
import java.util.*

/**
 * Created by jegul on 10/03/20
 */
class DebouncedOnClickListener(private val debounceInterval: Long = 300L, private val onClicked: (view: View) -> Unit) : View.OnClickListener {

    private val lastClickMap: MutableMap<View, Long> = WeakHashMap()

    override fun onClick(clickedView: View) {
        val previousClickTimestamp = lastClickMap[clickedView]
        val currentTimestamp = SystemClock.uptimeMillis()

        if (previousClickTimestamp == null || currentTimestamp - previousClickTimestamp.toLong() > debounceInterval) {
            lastClickMap[clickedView] = currentTimestamp
            onClicked(clickedView)
        }
    }
}