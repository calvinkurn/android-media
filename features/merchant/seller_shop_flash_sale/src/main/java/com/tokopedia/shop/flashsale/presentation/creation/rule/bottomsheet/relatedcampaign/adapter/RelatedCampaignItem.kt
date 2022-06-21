package com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RelatedCampaignItem(
    val id: Long,
    val name: String,
    val isSelected: Boolean,
    val isMaxSelected: Boolean = false
) : Parcelable {
    val isNotSelectable: Boolean
        get() = isMaxSelected && !isSelected
}