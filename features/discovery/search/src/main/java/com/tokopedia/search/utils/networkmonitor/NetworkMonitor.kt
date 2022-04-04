package com.tokopedia.search.utils.networkmonitor

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val wifiConnectionState: Flow<Boolean>
}