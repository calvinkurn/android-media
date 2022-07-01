package com.tokopedia.tokofood.common.presentation.mapper

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProduct
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariant
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariantSelectionRules
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CustomListItemType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail
import com.tokopedia.tokofood.feature.merchant.presentation.model.OptionUiModel

object CustomOrderDetailsMapper {

    fun mapTokoFoodProductsToCustomOrderDetails(tokoFoodProducts: List<CheckoutTokoFoodProduct>): MutableList<CustomOrderDetail> {
        return tokoFoodProducts.map { product ->
            CustomOrderDetail(
                cartId = product.cartId,
                subTotal = product.price,
                subTotalFmt = product.price.getCurrencyFormatted(),
                qty = product.quantity,
                customListItems = mapTokoFoodVariantsToCustomListItems(
                    variants = product.variants,
                    orderNote = product.notes
                )
            )
        }.toMutableList()
    }

    private fun mapTokoFoodVariantsToCustomListItems(variants: List<CheckoutTokoFoodProductVariant>, orderNote: String): List<CustomListItem> {
        val customListItems = mutableListOf<CustomListItem>()
        val addOns = variants.map { variant ->
            val options = variant.options.map { option ->
                OptionUiModel(
                    isSelected = option.isSelected,
                    id = option.optionId,
                    name = option.name,
                    price = option.price,
                    priceFmt = option.priceFmt,
                    selectionControlType = mapSelectionRulesToSelectionControlType(variant.rules.selectionRules)
                )
            }
            CustomListItem(
                listItemType = CustomListItemType.PRODUCT_ADD_ON,
                addOnUiModel = AddOnUiModel(
                    id = variant.variantId,
                    name = variant.name,
                    isRequired = variant.rules.selectionRules.isRequired,
                    isSelected = variant.options.count { it.isSelected } != Int.ZERO,
                    maxQty = variant.rules.selectionRules.maxQuantity,
                    minQty = variant.rules.selectionRules.minQuantity,
                    options = options,
                    selectedAddOns = options.filter { it.isSelected }.map { it.name }
                )
            )
        }
        val noteInput = CustomListItem(listItemType = CustomListItemType.ORDER_NOTE_INPUT, orderNote = orderNote, addOnUiModel = null)
        customListItems.addAll(addOns)
        customListItems.add(noteInput)
        return customListItems.toList()
    }

    private fun mapSelectionRulesToSelectionControlType(selectionRules: CheckoutTokoFoodProductVariantSelectionRules): SelectionControlType {
        return when {
            selectionRules.type == CheckoutTokoFoodProductVariantSelectionRules.SELECT_MANY -> SelectionControlType.MULTIPLE_SELECTION
            selectionRules.type == CheckoutTokoFoodProductVariantSelectionRules.SELECT_ONE -> SelectionControlType.SINGLE_SELECTION
            selectionRules.maxQuantity > Int.ONE -> SelectionControlType.MULTIPLE_SELECTION
            else -> SelectionControlType.SINGLE_SELECTION
        }
    }

}