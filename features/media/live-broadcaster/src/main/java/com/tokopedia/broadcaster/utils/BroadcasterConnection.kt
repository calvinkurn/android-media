package com.tokopedia.broadcaster.utils

import com.wmspanel.libstream.ConnectionConfig

data class BroadcasterConnection(
    var connectionId: Int? = null
) : ConnectionConfig()