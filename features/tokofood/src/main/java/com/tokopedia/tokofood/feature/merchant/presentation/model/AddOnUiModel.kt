package com.tokopedia.tokofood.feature.merchant.presentation.model

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import com.tokopedia.unifycomponents.list.ListItemUnify
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
    val isMultipleMandatory = minQty > Int.ONE

    @IgnoredOnParcel
    val addOnItems = options
            .filter { it.isVisible }
            .map { optionUiModel ->
                var description = optionUiModel.priceFmt
                if (optionUiModel.isOutOfStock) description = outOfStockWording
                ListItemUnify(
                        title = optionUiModel.name,
                        description = description
                ).apply {
                    when (optionUiModel.selectionControlType) {
                        SelectionControlType.SINGLE_SELECTION -> {
                            setVariant(null, ListItemUnify.RADIO_BUTTON, null)
                        }
                        SelectionControlType.MULTIPLE_SELECTION -> {
                            setVariant(null, ListItemUnify.CHECKBOX, null)
                        }
                    }
                }
            }
}