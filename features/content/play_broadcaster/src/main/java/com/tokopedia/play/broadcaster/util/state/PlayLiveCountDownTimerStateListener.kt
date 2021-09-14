package com.tokopedia.play.broadcaster.util.state

import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorListener
import com.tokopedia.play.broadcaster.view.state.PlayLiveCountDownTimerState


/**
 * Created by mzennis on 04/08/21.
 */
interface PlayLiveCountDownTimerStateListener : PlayLivePusherMediatorListener {

    override fun onLiveCountDownTimerActive(timeInMillis: Long) {
        onLiveCountDownTimerStateChanged(PlayLiveCountDownTimerState.Active(timeInMillis))
    }

    override fun onLiveCountDownTimerFinish() {
        onLiveCountDownTimerStateChanged(PlayLiveCountDownTimerState.Finish)
    }

    fun onLiveCountDownTimerStateChanged(countDownTimerState: PlayLiveCountDownTimerState)

}