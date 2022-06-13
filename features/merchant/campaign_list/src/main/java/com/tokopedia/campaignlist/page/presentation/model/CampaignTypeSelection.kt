package com.tokopedia.campaignlist.page.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CampaignTypeSelection(
        val campaignTypeId: String = "",
        val campaignTypeName: String = "",
        val statusText: String = "",
        var isSelected: Boolean = false
) : Parcelable