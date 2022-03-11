package com.tokopedia.play.broadcaster.ui.state

import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play_common.model.result.NetworkResult

/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
data class PlayBroadcastSummaryUiState(
    val channelSummary: ChannelSummaryUiState,
    val liveReport: LiveReportUiState,
    val tag: TagUiState,
)

data class ChannelSummaryUiState(
    val title: String,
    val date: String,
    val duration: String,
    val isEligiblePostVideo: Boolean,
) {
    companion object {
        fun empty() = ChannelSummaryUiState("", "", "", false)
    }
}

data class LiveReportUiState(
    val trafficMetricsResult: NetworkResult<List<TrafficMetricUiModel>>,
)

data class TagUiState(
    val tags: List<PlayTagUiModel>,
)