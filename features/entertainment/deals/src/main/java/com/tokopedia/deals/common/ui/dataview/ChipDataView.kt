package com.tokopedia.deals.common.ui.dataview

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChipDataView(
    val title: String = "",
    val id: String = "",
    var isSelected: Boolean = false
): Parcelable
