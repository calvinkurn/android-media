package com.tokopedia.campaign.components.bottomsheet.selection.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SingleSelectionItem(
    val id: String,
    val name: String,
    val isSelected: Boolean = false,
    val direction: String = ""
) : Parcelable
