package com.tokopedia.broadcaster

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.broadcaster.data.BroadcasterConfig
import com.tokopedia.broadcaster.data.BroadcasterLogger
import com.tokopedia.broadcaster.state.BroadcasterState
import com.tokopedia.broadcaster.state.isPushing
import com.tokopedia.kotlin.extensions.view.hide

class DebugView : ScrollView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val tvStatus: AppCompatTextView
    private val tvPushInfo: AppCompatTextView
    private val tvPushUpdatedInfo: AppCompatTextView
    private val tvFullLog: AppCompatTextView

    init {
        val view = View.inflate(context, R.layout.view_debug, this)
        tvStatus = view.findViewById(R.id.tvt_status)
        tvPushInfo = view.findViewById(R.id.tv_debug_view_push_info)
        tvPushUpdatedInfo = view.findViewById(R.id.tv_debug_view_push_updated_info)
        tvFullLog = view.findViewById(R.id.tv_debug_view_full_log)
        val imgClose = view.findViewById<AppCompatImageView>(R.id.img_close)

        imgClose.setOnClickListener { hide() }
        tvFullLog.text = "\nSTATUS HISTORY"
    }

    fun setLiveInfo(url: String, config: BroadcasterConfig) {
        val info = StringBuilder()
        info.append("\n\n")
        info.append("Initial Configuration\n")
        info.append("URL: $url\n")
        info.append("Size: ${config.videoWidth}x${config.videoHeight}\n")
        info.append("FPS: ${config.fps}\n")
        info.append("Bitrate: ${config.videoBitrate}\n")
        tvPushInfo.text = info.toString()
    }

    fun updateStats(log: BroadcasterLogger) {
        val info = StringBuilder()
        info.append("\n\n")
        info.append("Current Bandwidth: ${log.getBandwidth()}\n")
        info.append("Current FPS: ${log.getFps()}\n")
        info.append("Network Usage: ${log.getTraffic()}\n")
        tvPushUpdatedInfo.text = info.toString()
    }

    fun updateState(state: BroadcasterState) {
        val status = when(state) {
            BroadcasterState.Connecting -> "CONNECTING"
            is BroadcasterState.Error -> "ERROR: \nreason:${state.reason}"
            BroadcasterState.Pause -> "PAUSED"
            BroadcasterState.Recovered -> "RECOVERED"
            is BroadcasterState.Resumed -> if (state.isPushing) "RESUMED" else "RESUME"
            BroadcasterState.Started -> "STARTED"
            is BroadcasterState.Stop -> "STOPPED"
            else -> "Unknown"
        }
        tvStatus.text = status
        tvFullLog.append("\n$status")
    }
}