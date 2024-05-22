package com.tokopedia.discovery2

import com.tokopedia.analytics.byteio.RefreshType

/**
 * Record page state whether open or refresh
 */
class DiscoveryAppLogPageState {

    private var _lastState = RefreshType.OPEN

    fun initiate() {
        _lastState = RefreshType.OPEN
    }

    fun onRefresh() {
        _lastState = RefreshType.REFRESH
    }

    fun getLastState(): RefreshType {
        return _lastState
    }
}
