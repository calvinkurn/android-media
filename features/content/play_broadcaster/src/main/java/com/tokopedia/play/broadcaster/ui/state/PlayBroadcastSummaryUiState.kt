package com.tokopedia.play.broadcaster.ui.state

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagItem
import com.tokopedia.play_common.model.result.NetworkResult

/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
data class PlayBroadcastSummaryUiState(
    val channelSummary: ChannelSummaryUiState,
    val liveReport: LiveReportUiState,
    val tag: NetworkResult<TagUiState>,
)

data class ChannelSummaryUiState(
    val title: String,
    val coverUrl: String,
    val date: String,
    val duration: String,
    val author: ContentAccountUiModel,
) {
    fun isEmpty() = title.isEmpty() && coverUrl.isEmpty() && date.isEmpty() && duration.isEmpty()

    companion object {
        fun empty() = ChannelSummaryUiState(
            title = "",
            coverUrl = "",
            date = "",
            duration = "",
            author = ContentAccountUiModel.Empty,
        )
    }
}

data class LiveReportUiState(
    val trafficMetricsResult: NetworkResult<List<TrafficMetricUiModel>>,
    val trafficMetricHighlight: NetworkResult<List<LiveStatsUiModel>>,
)

data class TagUiState(
    val tags: List<PlayTagItem>,
)
