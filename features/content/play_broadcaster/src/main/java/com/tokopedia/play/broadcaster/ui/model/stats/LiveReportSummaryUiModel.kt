package com.tokopedia.play.broadcaster.ui.model.stats

/**
 * Created by Jonathan Darwin on 08 March 2024
 */
data class LiveReportSummaryUiModel(
    val liveStats: List<LiveStatsUiModel>,
    val timestamp: String,
) {
    companion object {
        val Empty: LiveReportSummaryUiModel
            get() = LiveReportSummaryUiModel(
                liveStats = emptyList(),
                timestamp = "",
            )
    }
}
