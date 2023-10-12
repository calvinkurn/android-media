package com.tokopedia.deals.common.ui.dataview

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DealsChipsDataView(
    val chipList: List<ChipDataView> = emptyList(),
    val showingLimit: Int = 0
) : DealsBaseItemDataView(), Parcelable
