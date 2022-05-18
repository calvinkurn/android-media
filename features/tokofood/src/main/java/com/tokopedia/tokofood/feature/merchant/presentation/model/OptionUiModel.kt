package com.tokopedia.tokofood.feature.merchant.presentation.model

import android.os.Parcelable
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import kotlinx.parcelize.Parcelize

@Parcelize
data class OptionUiModel(
        var isSelected: Boolean = false,
        val id: String = "",
        val name: String = "",
        val price: Double = 0.0,
        val priceFmt: String = "",
        val selectionControlType: SelectionControlType
) : Parcelable
