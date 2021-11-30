package com.tokopedia.campaignlist.page.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CampaignStatusSelection(
        val statusId: Int = 0,
        val statusText: String = "",
        var isSelected: Boolean = false
) : Parcelable