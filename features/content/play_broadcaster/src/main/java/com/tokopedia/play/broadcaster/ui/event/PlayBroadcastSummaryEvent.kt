package com.tokopedia.play.broadcaster.ui.event

import com.tokopedia.play.broadcaster.ui.model.LiveDurationUiModel
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play_common.model.result.NetworkResult

/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
sealed class PlayBroadcastSummaryEvent {
    data class LiveSummaryEvent(
        val trafficMetricsResult: NetworkResult<List<TrafficMetricUiModel>>,
        val duration: LiveDurationUiModel,
    ): PlayBroadcastSummaryEvent()
}