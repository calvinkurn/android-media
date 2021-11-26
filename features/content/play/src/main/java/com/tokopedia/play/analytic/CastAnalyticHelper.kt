package com.tokopedia.play.analytic

import java.util.*

/**
 * Created By : Jonathan Darwin on August 16, 2021
 */
class CastAnalyticHelper(
    private val analytic: PlayAnalytic
) {
    private var startTime: Long = 0

    private val isRecording: Boolean
        get() = startTime != 0L

    fun startRecording() {
        if(isRecording) {
            stopRecording()
        }
        startTime = Calendar.getInstance().timeInMillis
    }

    fun stopRecording() {
        if(isRecording) {
            val duration = (Calendar.getInstance().timeInMillis - startTime) / SECOND_DIVIDER
            startTime = 0
            analytic.recordCastDuration(duration)
        }
    }

    private companion object {
        const val SECOND_DIVIDER = 1000L
    }
}