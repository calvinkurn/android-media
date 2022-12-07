package com.tokopedia.play.broadcaster.pusher.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import com.tokopedia.broadcaster.revamp.state.BroadcastInitState
import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterMetric
import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState


/**
 * Created by mzennis on 18/06/21.
 */
class PlayLivePusherDebugView : ScrollView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    fun logAspectRatio(aspectRatio: Double) {
    }

    fun logBroadcastInitState(state: BroadcastInitState) {
    }

    fun logBroadcastState(state: PlayBroadcasterState) {
    }

    fun logBroadcastStatistic(metric: BroadcasterMetric) {
    }
}