package com.tokopedia.sellerhomecommon.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterTabUiModel(
    val tabName: String,
    val tabKey: String,
    val isSelected: Boolean
): Parcelable
