package com.tokopedia.tokofood.feature.merchant.presentation.model

import android.os.Parcelable
import com.tokopedia.tokofood.feature.merchant.common.constants.MerchantPageConstants.STATUS_INACTIVE
import com.tokopedia.tokofood.feature.merchant.common.constants.MerchantPageConstants.STATUS_OUT_OF_STOCK
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class OptionUiModel(
        var isSelected: Boolean = false,
        val id: String = "",
        val status: Int = 0,
        val name: String = "",
        val price: Double = 0.0,
        val priceFmt: String? = "",
        val selectionControlType: SelectionControlType
) : Parcelable {
    @IgnoredOnParcel
    val isVisible = status != STATUS_INACTIVE
    @IgnoredOnParcel
    val isOutOfStock = status == STATUS_OUT_OF_STOCK
    @IgnoredOnParcel
    var dataSetPosition = 0
    @IgnoredOnParcel
    var maxSelected = 0
    @IgnoredOnParcel
    var canBeSelected = true
}
