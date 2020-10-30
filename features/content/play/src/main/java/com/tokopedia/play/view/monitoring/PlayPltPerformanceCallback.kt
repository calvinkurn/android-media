package com.tokopedia.play.view.monitoring

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.play.PLAY_TRACE_PAGE
import com.tokopedia.play.PLAY_TRACE_PREPARE_PAGE
import com.tokopedia.play.PLAY_TRACE_RENDER_PAGE
import com.tokopedia.play.PLAY_TRACE_REQUEST_NETWORK
import com.tokopedia.play.di.PlayScope
import javax.inject.Inject

/**
 * Created by jegul on 22/09/20
 */
@PlayScope
class PlayPltPerformanceCallback @Inject constructor() : PageLoadTimePerformanceCallback(
        PLAY_TRACE_PREPARE_PAGE,
        PLAY_TRACE_REQUEST_NETWORK,
        PLAY_TRACE_RENDER_PAGE
) {

    fun startPlayMonitoring() {
        startMonitoring(PLAY_TRACE_PAGE)
    }
}