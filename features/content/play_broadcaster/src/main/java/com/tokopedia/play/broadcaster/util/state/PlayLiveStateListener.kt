package com.tokopedia.play.broadcaster.util.state

import com.tokopedia.play.broadcaster.view.state.PlayLivePusherState


/**
 * Created by mzennis on 17/03/21.
 */
interface PlayLiveStateListener {

    fun onStateChanged(state: PlayLivePusherState)
}