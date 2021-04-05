package com.tokopedia.play.broadcaster.pusher.state


/**
 * Created by mzennis on 17/03/21.
 */
interface ApsaraLivePusherStateProcessor {

    fun onStateChanged(state: ApsaraLivePusherState)
    fun onError(code: Int, throwable: Throwable)
}