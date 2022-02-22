package com.tokopedia.campaignlist.page.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CampaignStatusSelection(
        val statusId: List<Int> = emptyList(),
        val statusText: String = "",
        var isSelected: Boolean = false
) : Parcelable