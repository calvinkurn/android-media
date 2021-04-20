package com.tokopedia.play.view.monitoring

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
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

    companion object {

        /**
         * Start: [com.tokopedia.play.view.activity.PlayActivity.onCreate]
         */
        private const val PLAY_TRACE_PAGE = "plt_play_page"

        /**
         * Monitor duration between open page until network requests
         * Start: [com.tokopedia.play.view.activity.PlayActivity.onCreate]
         * Stop: [com.tokopedia.play.view.fragment.PlayFragment.onResume]
         */
        private const val PLAY_TRACE_PREPARE_PAGE = "plt_play_page_prepare_page"

        /**
         * Monitor duration between network requests until receiving responses
         * Start: [com.tokopedia.play.view.viewmodel.PlayParentViewModel.loadNextPage]
         * Stop: [com.tokopedia.play.view.fragment.PlayUserInteractionFragment.triggerStartMonitoring]
         */
        private const val PLAY_TRACE_REQUEST_NETWORK = "plt_play_page_request_network"

        /**
         * Monitor duration between receive response until the UI is rendered
         * Start: [com.tokopedia.play.view.fragment.PlayUserInteractionFragment.triggerStartMonitoring]
         * Stop: [com.tokopedia.play.view.fragment.PlayUserInteractionFragment.triggerStartMonitoring]
         */
        private const val PLAY_TRACE_RENDER_PAGE = "plt_play_page_render_page"
    }
}