package com.tokopedia.product.detail.view.widget.campaign.timebased.ongoing

import androidx.compose.runtime.Immutable

@Immutable
data class UpcomingCampaignUiModel(
    val logoUrl: String = "",
    val title: String = "",
    val endTimeUnix: Long = 0,
    val timerLabel: String = "",
    val labelButton: String = "",
    val paymentSpecific: String = "",
    val backgroundColorString: String = "",
    val showRemainderButton: Boolean = false
)

val Long.secondToMs
    get() = this * 1000L
