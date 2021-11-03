package com.tokopedia.play.broadcaster.pusher

import com.wmspanel.libstream.ConnectionConfig


/**
 * Created by mzennis on 15/06/21.
 */
data class PlayLivePusherConnection(
    var connectionId: Int? = null
) : ConnectionConfig()
