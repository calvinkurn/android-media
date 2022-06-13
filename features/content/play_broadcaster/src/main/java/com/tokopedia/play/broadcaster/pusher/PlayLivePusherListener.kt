package com.tokopedia.play.broadcaster.pusher

import com.tokopedia.broadcaster.mediator.LivePusherStatistic


/**
 * Created by mzennis on 04/06/21.
 */
interface PlayLivePusherListener {

    fun onNewLivePusherState(pusherState: PlayLivePusherState)

    fun onUpdateLivePusherStatistic(pusherStatistic: LivePusherStatistic) { }
}