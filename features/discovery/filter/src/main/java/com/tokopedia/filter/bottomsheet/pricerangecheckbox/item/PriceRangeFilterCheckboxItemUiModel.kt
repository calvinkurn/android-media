package com.tokopedia.filter.bottomsheet.pricerangecheckbox.item

import android.os.Parcelable
import com.tokopedia.filter.common.data.Option
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PriceRangeFilterCheckboxItemUiModel(
    val option: Option
): Parcelable {
    var isSelected: Boolean = false
}