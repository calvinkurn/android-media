package com.tokopedia.play.broadcaster.view.contract

import com.tokopedia.play.broadcaster.pusher.PlayBroadcaster

/**
 * Created by meyta.taliti on 03/03/22.
 */
interface PlayBroadcasterContract {

    fun getBroadcaster(): PlayBroadcaster

}