package com.tokopedia.play.broadcaster.util.state

import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorListener


/**
 * Created by mzennis on 04/08/21.
 */
interface PlayLiveTimerStateListener : PlayLivePusherMediatorListener {

    override fun onLiveTimerActive(timeInMillis: Long) {
//        onLiveTimerStateChanged(PlayLiveTimerState.Active(timeInMillis))
    }

    override fun onLiveTimerFinish() {
//        onLiveTimerStateChanged(PlayLiveTimerState.Finish)
    }

//    fun onLiveTimerStateChanged(timerState: PlayLiveTimerState)

}