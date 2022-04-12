package com.tokopedia.video_widget.util.networkmonitor

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val wifiConnectionState: Flow<Boolean>
}