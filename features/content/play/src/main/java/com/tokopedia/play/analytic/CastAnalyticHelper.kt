package com.tokopedia.play.analytic

import android.util.Log
import com.tokopedia.play.view.type.PlayChannelType
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By : Jonathan Darwin on August 16, 2021
 */
class CastAnalyticHelper(
    private val analytic: PlayAnalytic
) {

    private var format = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault())
    private var startTime: Long = 0
    private var channelId: String = ""
    private var channelType: PlayChannelType = PlayChannelType.Unknown

    val isRecording: Boolean
        get() = startTime != 0L

    fun startRecording() {
        if(startTime != 0L) {
            stopRecording()
        }

        startTime = Calendar.getInstance().timeInMillis
        channelId = analytic.channelId
        channelType = analytic.channelType

        log(true)
    }

    fun stopRecording() {
        if(startTime != 0L) {
            val duration = Calendar.getInstance().timeInMillis - startTime
            startTime = 0
            analytic.recordCastDuration(channelId, channelType, duration)

            log(false)
        }
    }

    private fun log(isStart: Boolean) {
        if(isStart) {
            val date = Date(startTime)
            Log.d("<LOG>", "start: ${format.format(date)} channelId: $channelId channelType: ${channelType.value}")
        }
        else {
            val date = Date(Calendar.getInstance().timeInMillis)
            Log.d("<LOG>", "end : ${format.format(date)}")
        }
    }
}