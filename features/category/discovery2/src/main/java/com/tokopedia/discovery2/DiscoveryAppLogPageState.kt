package com.tokopedia.discovery2

import com.tokopedia.analytics.byteio.RefreshType

/**
 * Record page state whether open or refresh
 */
object DiscoveryAppLogPageState {

    private var _lastState = RefreshType.OPEN

    fun update(state: RefreshType) {
        _lastState = state
    }

    fun getLastState(): RefreshType {
        return _lastState
    }
}
