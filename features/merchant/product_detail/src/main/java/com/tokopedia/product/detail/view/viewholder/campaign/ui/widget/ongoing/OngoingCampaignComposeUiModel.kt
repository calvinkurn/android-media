package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.ongoing

import androidx.compose.runtime.Immutable

@Immutable
data class OngoingCampaignComposeUiModel(
    val logoUrl: String = "",
    val title: String = "",
    val endTimeUnix: Long = 0,
    val timerLabel: String = "",
    val stockPercentage: Int = 0,
    val stockLabel: String = "",
    val paymentSpecific: String = "",
    val backgroundColorString: String = ""
)
