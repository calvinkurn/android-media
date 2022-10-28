package com.tokopedia.play.broadcaster.ui.model.campaign

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
data class CampaignUiModel(
    val id: String,
    val title: String,
    val imageUrl: String,
    val startDateFmt: String,
    val endDateFmt: String,
    val status: CampaignStatusUiModel,
    val totalProduct: Int,
)