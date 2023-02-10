package com.tokopedia.tokofood.common.presentation.mapper

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.tokofood.common.domain.response.CartListCartGroupCart
import com.tokopedia.tokofood.common.domain.response.CartListCartGroupCartSelectionRule
import com.tokopedia.tokofood.common.domain.response.CartListCartGroupCartVariant
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariantSelectionRules
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CustomListItemType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail
import com.tokopedia.tokofood.feature.merchant.presentation.model.OptionUiModel

object CustomOrderDetailsMapper {

    fun mapTokoFoodProductsToCustomOrderDetails(tokoFoodProducts: List<CartListCartGroupCart>): MutableList<CustomOrderDetail> {
        return tokoFoodProducts.map { product ->
            CustomOrderDetail(
                cartId = product.cartId,
                subTotal = product.price,
                subTotalFmt = product.price.getCurrencyFormatted(),
                qty = product.quantity,
                customListItems = mapTokoFoodVariantsToCustomListItems(
                    variants = product.customResponse.variants,
                    orderNote = product.customResponse.notes
                )
            )
        }.toMutableList()
    }

    private fun mapTokoFoodVariantsToCustomListItems(variants: List<CartListCartGroupCartVariant>, orderNote: String): List<CustomListItem> {
        val customListItems = mutableListOf<CustomListItem>()
        val addOns = variants.map { variant ->
            val options = variant.options.map { option ->
                val formattedPrice =
                    when {
                        option.priceFmt.isNotBlank() -> option.priceFmt
                        option.price > 0.0 -> option.price.getCurrencyFormatted()
                        else -> null
                    }
                OptionUiModel(
                    isSelected = option.isSelected,
                    id = option.optionId,
                    name = option.name,
                    price = option.price,
                    priceFmt = formattedPrice,
                    status = option.status,
                    selectionControlType = mapSelectionRulesToSelectionControlType(variant.rules.selectionRule)
                )
            }
            CustomListItem(
                listItemType = CustomListItemType.PRODUCT_ADD_ON,
                addOnUiModel = AddOnUiModel(
                    id = variant.variantId,
                    name = variant.name,
                    isRequired = variant.rules.selectionRule.isRequired,
                    isSelected = variant.options.count { it.isSelected } != Int.ZERO,
                    maxQty = variant.rules.selectionRule.maxQty,
                    minQty = variant.rules.selectionRule.minQty,
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

    private fun mapSelectionRulesToSelectionControlType(selectionRules: CartListCartGroupCartSelectionRule): SelectionControlType {
        return when {
            selectionRules.type == CheckoutTokoFoodProductVariantSelectionRules.SELECT_MANY -> SelectionControlType.MULTIPLE_SELECTION
            selectionRules.minQty == Int.ZERO && selectionRules.maxQty == Int.ONE -> SelectionControlType.MULTIPLE_SELECTION
            else -> SelectionControlType.SINGLE_SELECTION
        }
    }

}
