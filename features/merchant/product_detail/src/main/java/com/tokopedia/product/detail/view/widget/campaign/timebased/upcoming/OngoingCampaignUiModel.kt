package com.tokopedia.product.detail.view.widget.campaign.timebased.upcoming

import androidx.compose.runtime.Immutable

@Immutable
data class OngoingCampaignUiModel(
    val logoUrl: String = "",
    val title: String = "",
    val endTimeUnix: Long = 0,
    val timerLabel: String = "",
    val stockPercentage: Int = 0,
    val stockLabel: String = "",
    val paymentSpecific: String = "",
    val backgroundColorString: String = ""
)
