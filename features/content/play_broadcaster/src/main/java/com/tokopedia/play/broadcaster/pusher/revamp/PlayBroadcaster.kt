package com.tokopedia.play.broadcaster.pusher.revamp

import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.broadcaster.revamp.BroadcasterManager
import com.tokopedia.broadcaster.revamp.util.log.DefaultBroadcasterLogger

/**
 * Created by meyta.taliti on 03/03/22.
 */
class PlayBroadcaster(
    private val broadcaster: Broadcaster
) : Broadcaster by broadcaster {

    private var isBroadcastStarted = false

    // call this onDestroy() to avoid memory leak
    fun destroy() {
        broadcaster.setCallback(null)
    }

    fun start(
        rtmpUrl: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit,
    ) {
        broadcaster.setLogger(object : DefaultBroadcasterLogger() {
            override fun e(msg: String) {
                super.e(msg)
                onError(msg)
            }
        })
        broadcaster.start(rtmpUrl)
        isBroadcastStarted = true
        onSuccess()
    }

    fun shouldStop() {
        isBroadcastStarted = false
        broadcaster.release() // todo: stop or just release?
    }

    fun pause() {
        // todo: set last millis duration
    }

    fun isBroadcastStartedBefore() = isBroadcastStarted // only set false when user manually click stop

    fun isEligibleContinueBroadcast() = true // todo: set pause duration

    companion object {

        fun newInstance(callback: Broadcaster.Callback): PlayBroadcaster {
            return PlayBroadcaster(
                BroadcasterManager().apply {
                    setCallback(callback)
                }
            )
        }
    }
}