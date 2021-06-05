package com.tokopedia.play.broadcaster.pusher


/**
 * Created by mzennis on 04/06/21.
 */
interface PlayLivePusherListener {

    fun onNewLivePusherState(pusherState: PlayLivePusherState)
}