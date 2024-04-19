package com.tokopedia.play.broadcaster.ui.model.report.product

import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsUiModel

/**
 * Created by Jonathan Darwin on 04 March 2024
 */
data class ProductReportSummaryUiModel(
    val totalStatsList: List<LiveStatsUiModel>,
    val productStatsList: List<ProductStatsUiModel>
)
