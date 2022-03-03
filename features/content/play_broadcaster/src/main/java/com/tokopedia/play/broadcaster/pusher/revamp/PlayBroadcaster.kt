package com.tokopedia.play.broadcaster.pusher.revamp

import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.broadcaster.revamp.BroadcasterManager

/**
 * Created by meyta.taliti on 03/03/22.
 */
class PlayBroadcaster(
    private val broadcaster: Broadcaster
) : Broadcaster by broadcaster {

    // call this onDestroy() to avoid memory leak
    fun destroy() {
        broadcaster.setCallback(null)
    }

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