package com.tokopedia.broadcaster.lib

import com.wmspanel.libstream.ConnectionConfig

data class BroadcasterConnection(
    var connectionId: Int? = null
) : ConnectionConfig()