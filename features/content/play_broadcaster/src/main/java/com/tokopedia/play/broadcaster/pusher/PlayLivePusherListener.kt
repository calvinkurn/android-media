package com.tokopedia.play.broadcaster.pusher

import com.tokopedia.play.broadcaster.pusher.PlayLivePusherState


/**
 * Created by mzennis on 04/06/21.
 */
interface PlayLivePusherListener {

    fun onNewLivePusherState(pusherState: PlayLivePusherState)
}