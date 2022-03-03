package com.tokopedia.play.broadcaster.pusher.revamp

import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.broadcaster.revamp.BroadcasterManager

/**
 * Created by meyta.taliti on 03/03/22.
 */
class PlayBroadcaster(
    broadcaster: Broadcaster
) : Broadcaster by broadcaster {

    companion object {

        fun newInstance(): PlayBroadcaster {
            return PlayBroadcaster(
                BroadcasterManager()
            )
        }
    }
}