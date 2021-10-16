package com.tokopedia.broadcaster.data

import com.wmspanel.libstream.ConnectionConfig

data class BroadcasterConnection(
    var connectionId: Int? = null
) : ConnectionConfig()