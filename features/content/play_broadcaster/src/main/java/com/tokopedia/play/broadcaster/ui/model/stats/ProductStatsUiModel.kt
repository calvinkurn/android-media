package com.tokopedia.play.broadcaster.ui.model.stats

/**
 * Created by Jonathan Darwin on 04 March 2024
 */
data class ProductStatsUiModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val addToCart: String,
    val visitPdp: String,
    val productSoldQty: String,
    val estimatedIncome: String,
)
