package com.tokopedia.play.broadcaster.pusher.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.broadcaster.revamp.state.BroadcastInitState
import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterMetric
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 18/06/21.
 */
class PlayLivePusherDebugView : ScrollView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val tvStatus: Typography
    private val tvPushUpdatedInfo: Typography
    private val tvFullLog: Typography
    private val tvChannelId: Typography

    private var mChannelId = ""

    init {
        val view = View.inflate(context, R.layout.view_play_live_pusher_debug, this)
        tvStatus = view.findViewById(R.id.tvt_status)
        tvPushUpdatedInfo = view.findViewById(R.id.tv_debug_view_push_updated_info)
        tvFullLog = view.findViewById(R.id.tv_debug_view_full_log)
        tvChannelId = view.findViewById(R.id.tv_channel_id)

        findViewById<IconUnify>(R.id.ic_close).setOnClickListener {
            hide()
        }

        tvFullLog.text = "\nSTATUS HISTORY"

        tvChannelId.setOnClickListener {
            val clipboard = context.getSystemService(BaseActivity.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Broadcast Channel ID", mChannelId)
            clipboard.setPrimaryClip(clip)

            showToaster(message = "Channel ID copied!")
        }
    }

    fun logAspectRatio(aspectRatio: Double) {
        tvFullLog.append("\nAspect Ratio: $aspectRatio")
    }

    fun logBroadcastInitState(state: BroadcastInitState) {
        tvStatus.text = state.tag
        tvFullLog.append("\n${state.tag}")
    }

    fun logBroadcastState(state: PlayBroadcasterState) {
        tvStatus.text = state.tag
        tvFullLog.append("\n${state.tag}")
    }

    fun logBroadcastStatistic(metric: BroadcasterMetric) {
        val info = StringBuilder()
        info.append("\n\n")
        info.append("Video Bitrate: ${metric.videoBitrate}\n")
        info.append("Audio Bitrate: ${metric.audioBitrate}\n")
        info.append("Resolution W x H: ${metric.resolutionWidth}x${metric.resolutionHeight} \n")
        info.append("Current Bandwidth: ${metric.bandwidth}\n")
        info.append("Current FPS: ${metric.fps}\n")
        info.append("Network Usage: ${metric.traffic}\n")
        info.append("Packet Loss Increased: ${metric.packetLossIncreased}\n")
        info.append("Video Buffer Timestamp: ${metric.videoBufferTimestamp}\n")
        info.append("Audio Buffer Timestamp: ${metric.audioBufferTimestamp}\n")
        tvPushUpdatedInfo.text = info.toString()
    }

    fun logChannelId(channelId: String) {
        mChannelId = channelId
        tvChannelId.text = channelId + " (click here to copy)"
    }
}
