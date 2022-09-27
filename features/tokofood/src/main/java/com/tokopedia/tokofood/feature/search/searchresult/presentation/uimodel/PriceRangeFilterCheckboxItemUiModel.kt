package com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel

import android.os.Parcelable
import com.tokopedia.filter.common.data.Option
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceRangeFilterCheckboxItemUiModel(
    val option: Option
): Parcelable {
    var isSelected: Boolean = false
}