package com.tokopedia.play.broadcaster.analytic


/**
 * Created by mzennis on 08/09/20.
 */

/**
 * Performance Monitoring for Play
 * Start: [com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity.onCreate] before super onCreate()
 * Stop: [com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity.observeConfiguration]
 */
const val PLAY_BROADCASTER_TRACE_PAGE = "plt_play_broadcaster_page"

/**
 * Monitor duration between open page until network requests
 * Start: [com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity.onCreate] before super onCreate()
 * Stop: [com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity.getConfiguration]
 */
const val PLAY_BROADCASTER_TRACE_PREPARE_PAGE = "plt_play_broadcaster_page_prepare_page"

/**
 * Monitor duration between network requests until receiving responses
 * Start: [com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity.getConfiguration]
 * Stop: [com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity.observeConfiguration]
 */
const val PLAY_BROADCASTER_TRACE_REQUEST_NETWORK = "plt_play_broadcaster_page_request_network"

/**
 * Monitor duration between receive response until the UI is rendered
 * Start: [com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity.observeConfiguration]
 * Stop: [com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity.observeConfiguration] after rendering
 */
const val PLAY_BROADCASTER_TRACE_RENDER_PAGE = "plt_play_broadcaster_page_render_page"