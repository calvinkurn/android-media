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
        val options: List<OptionUiModel> = listOf()
) : Parcelable {
    @IgnoredOnParcel
    val isMultipleMandatory = minQty > Int.ONE

    @IgnoredOnParcel
    val addOnItems = options.map { optionUiModel ->
        ListItemUnify(
                title = optionUiModel.name,
                description = optionUiModel.priceFmt
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

//    @IgnoredOnParcel
//    var selectedAddOns = options.filter { it.isSelected }.map { it.name }
}