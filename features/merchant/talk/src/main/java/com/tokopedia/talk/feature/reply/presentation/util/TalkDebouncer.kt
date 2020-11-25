package com.tokopedia.talk.feature.reply.presentation.util

import android.os.SystemClock
import android.view.View
import java.util.*
import kotlin.math.abs

abstract class TalkDebouncer : View.OnClickListener {
    private val lastClickMap: MutableMap<View, Long>
    private val minimumIntervalMillis: Long = 1000L

    init {
        lastClickMap = WeakHashMap()
    }

    abstract fun onDebouncedClick(v: View?)
    override fun onClick(clickedView: View) {
        val previousClickTimestamp = lastClickMap[clickedView]
        val currentTimestamp: Long = SystemClock.uptimeMillis()
        lastClickMap[clickedView] = currentTimestamp
        if (previousClickTimestamp == null || abs(currentTimestamp - previousClickTimestamp) > minimumIntervalMillis) {
            onDebouncedClick(clickedView)
        }
    }

}