package com.tokopedia.play.broadcaster.pusher.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherStatistic
import com.tokopedia.play.broadcaster.ui.model.pusher.PlayLiveInfoUiModel
import com.tokopedia.play.broadcaster.view.state.PlayLivePusherViewState


/**
 * Created by mzennis on 18/06/21.
 */
class PlayLivePusherDebugView : ScrollView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val tvStatus: AppCompatTextView
    private val tvPushInfo: AppCompatTextView
    private val tvPushUpdatedInfo: AppCompatTextView
    private val tvFullLog: AppCompatTextView

    init {
        val view = View.inflate(context, R.layout.view_play_live_pusher_debug, this)
        tvStatus = view.findViewById(R.id.tvt_status)
        tvPushInfo = view.findViewById(R.id.tv_debug_view_push_info)
        tvPushUpdatedInfo = view.findViewById(R.id.tv_debug_view_push_updated_info)
        tvFullLog = view.findViewById(R.id.tv_debug_view_full_log)
        val imgClose = view.findViewById<AppCompatImageView>(R.id.img_close)

        imgClose.setOnClickListener { hide() }
        tvFullLog.text = "\nSTATUS HISTORY"
    }

    fun setLiveInfo(liveInfo: PlayLiveInfoUiModel) {
        val info = StringBuilder()
        info.append("\n\n")
        info.append("Initial Configuration\n")
        info.append("URL: ${liveInfo.ingestUrl}\n")
        info.append("Size: ${liveInfo.videoWidth}x${liveInfo.videoHeight}\n")
        info.append("FPS: ${liveInfo.fps}\n")
        info.append("Bitrate: ${liveInfo.initialBitrate}\n")
        tvPushInfo.text = info.toString()
    }

    fun updateStats(stats: PlayLivePusherStatistic) {
        val info = StringBuilder()
        info.append("\n\n")
        info.append("Current Bandwidth: ${stats.getBandwidth()}\n")
        info.append("Current FPS: ${stats.getFps()}\n")
        info.append("Network Usage: ${stats.getTraffic()}\n")
        tvPushUpdatedInfo.text = info.toString()
    }

    fun updateState(state: PlayLivePusherViewState) {
        val status = when(state) {
            PlayLivePusherViewState.Connecting -> "CONNECTING"
            is PlayLivePusherViewState.Error -> "ERROR: \ntype:${state.error.type}\nreason:${state.error.reason}"
            PlayLivePusherViewState.Paused -> "PAUSED"
            PlayLivePusherViewState.Recovered -> "RECOVERED"
            is PlayLivePusherViewState.Resume -> if (state.isResumed) "RESUMED" else "RESUME"
            PlayLivePusherViewState.Started -> "STARTED"
            is PlayLivePusherViewState.Stopped -> "STOPPED"
        }
        tvStatus.text = status
        tvFullLog.append("\n$status")
    }
}