package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.timebased.upcoming

import androidx.compose.runtime.Immutable

@Immutable
data class UpcomingCampaignComposeUiModel(
    val logoUrl: String = "",
    val title: String = "",
    val endTimeUnix: Long = 0,
    val timerLabel: String = "",
    val labelButton: String = "",
    val paymentSpecific: String = "",
    val backgroundColorString: String = "",
    val showRemainderButton: Boolean = true,
    val isOwner: Boolean = false
)

val Long.secondToMs
    get() = this * 1000L
