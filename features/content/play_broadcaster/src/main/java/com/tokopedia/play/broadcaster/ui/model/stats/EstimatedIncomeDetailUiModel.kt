package com.tokopedia.play.broadcaster.ui.model.stats

/**
 * Created by Jonathan Darwin on 04 March 2024
 */
data class EstimatedIncomeDetailUiModel(
    val totalStatsList: List<LiveStatsUiModel>,
    val productStatsList: List<ProductStatsUiModel>
)
