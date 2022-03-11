package com.tokopedia.play.broadcaster.ui.state

import com.tokopedia.play.broadcaster.ui.model.LiveDurationUiModel
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play_common.model.result.NetworkResult

/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
data class PlayBroadcastSummaryUiState(
    val liveReport: LiveReportUiState,
)

data class LiveReportUiState(
    val trafficMetricsResult: NetworkResult<List<TrafficMetricUiModel>>,
    val duration: LiveDurationUiModel,
)