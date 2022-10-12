package com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokofoodQuickSortUiModel(
    val name: String,
    val key: String,
    val value: String,
    var isSelected: Boolean
): Parcelable