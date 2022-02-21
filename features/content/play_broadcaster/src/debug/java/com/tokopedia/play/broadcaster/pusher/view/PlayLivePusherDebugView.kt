package com.tokopedia.play.broadcaster.pusher.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorState
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherStatistic
import com.tokopedia.play.broadcaster.ui.model.pusher.PlayLiveLogState
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 18/06/21.
 */
class PlayLivePusherDebugView : ScrollView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val tvStatus: Typography
    private val tvPushInfo: Typography
    private val tvPushUpdatedInfo: Typography
    private val tvFullLog: Typography

    init {
        val view = View.inflate(context, R.layout.view_play_live_pusher_debug, this)
        tvStatus = view.findViewById(R.id.tvt_status)
        tvPushInfo = view.findViewById(R.id.tv_debug_view_push_info)
        tvPushUpdatedInfo = view.findViewById(R.id.tv_debug_view_push_updated_info)
        tvFullLog = view.findViewById(R.id.tv_debug_view_full_log)

        findViewById<IconUnify>(R.id.ic_close).setOnClickListener {
            hide()
        }

        tvFullLog.text = "\nSTATUS HISTORY"
    }

    fun setLiveInfo(liveInfo: PlayLiveLogState.Init) {
        val info = StringBuilder()
        info.append("\n\n")
        info.append("Initial Configuration\n")
        info.append("URL: ${liveInfo.ingestUrl}\n")
        info.append("Size: ${liveInfo.videoWidth}x${liveInfo.videoHeight}\n")
        info.append("FPS: ${liveInfo.fps}\n")
        info.append("Bitrate: ${liveInfo.bitrate}\n")
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

    fun updateState(state: PlayLivePusherMediatorState) {
        tvStatus.text = state.tag
        tvFullLog.append("\n${state.tag}")
    }
}