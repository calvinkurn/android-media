package com.tokopedia.tokopedianow.common.util

object TrackerUtil {
    private const val ADDITIONAL_POSITION = 1

    fun Int.getTrackerPosition(): Int {
        return this + ADDITIONAL_POSITION
    }
}
