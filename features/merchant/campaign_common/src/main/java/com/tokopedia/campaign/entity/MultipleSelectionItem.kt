package com.tokopedia.campaign.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MultipleSelectionItem(val id: String, val name : String, val isSelected: Boolean = false): Parcelable
