package com.tokopedia.tokofood.feature.merchant.presentation.model

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.ONE
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnUiModel(
        val id: String = "",
        val name: String = "",
        var isError: Boolean = false,
        val isRequired: Boolean = false,
        var isSelected: Boolean = false,
        var selectedAddOns: List<String> = listOf(),
        val maxQty: Int = 0,
        val minQty: Int = 0,
        val options: List<OptionUiModel> = listOf(),
        val outOfStockWording: String = ""
) : Parcelable {
    @IgnoredOnParcel
    val isMultipleMandatory = minQty >= Int.ONE && maxQty > minQty

    @IgnoredOnParcel
    val filteredOptions = options.filter { it.isVisible }
}
