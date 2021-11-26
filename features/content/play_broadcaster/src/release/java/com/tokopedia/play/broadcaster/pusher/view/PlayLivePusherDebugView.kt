package com.tokopedia.play.broadcaster.pusher.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorState
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherStatistic
import com.tokopedia.play.broadcaster.ui.model.pusher.PlayLiveLogState
import com.tokopedia.play.broadcaster.view.state.PlayLiveViewState


/**
 * Created by mzennis on 18/06/21.
 */
class PlayLivePusherDebugView : ScrollView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    fun setLiveInfo(liveInfo: PlayLiveLogState.Init) {
    }

    fun updateStats(stats: PlayLivePusherStatistic) {
    }

    fun updateState(state: PlayLivePusherMediatorState) {
    }
}