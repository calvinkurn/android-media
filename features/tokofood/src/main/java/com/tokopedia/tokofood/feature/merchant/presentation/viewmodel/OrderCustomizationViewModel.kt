package com.tokopedia.tokofood.feature.merchant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.feature.merchant.presentation.mapper.TokoFoodMerchantUiModelMapper
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import javax.inject.Inject

class OrderCustomizationViewModel @Inject constructor(
) : ViewModel() {

    var baseProductPrice: Double = 0.0

    fun calculateSubtotalPrice(baseProductPrice: Double, quantity: Int, addOnUiModels: List<AddOnUiModel?>): Double {
        var subTotalPrice = baseProductPrice
        addOnUiModels.forEach { addOnUiModel ->
            addOnUiModel?.run {
                val selectedOptions = this.options.filter { optionUiModel ->  optionUiModel.isSelected }
                val subTotalOptionPrice = selectedOptions.sumOf { it.price }
                subTotalPrice += subTotalOptionPrice
            }
        }
        return subTotalPrice * quantity
    }

    fun formatSubtotalPrice(subTotalPrice: Double): String {
        return subTotalPrice.getCurrencyFormatted()
    }

    fun getCustomListItems(cartId: String, productUiModel: ProductUiModel): List<CustomListItem> {
        return if (cartId.isBlank() || productUiModel.customOrderDetails.isEmpty()) { resetMasterData(productUiModel.customListItems) }
        else productUiModel.customOrderDetails.firstOrNull { it.cartId == cartId }?.customListItems ?: listOf()
    }

    private fun resetMasterData(customListItems: List<CustomListItem>): List<CustomListItem> {
        customListItems.forEach {
            it.orderNote = String.EMPTY
            it.addOnUiModel?.run {
                isSelected = false
                options.forEach { optionUiModel ->
                    optionUiModel.isSelected = false
                }
                selectedAddOns = listOf()
            }
        }
        return customListItems
    }

    fun isEditingCustomOrder(cartId: String): Boolean = cartId.isNotBlank()

    fun validateCustomOrderInput(customListItems: List<CustomListItem>): Pair<Boolean, List<CustomListItem>> {
        val mutableCustomListItems = customListItems.toMutableList()
        // exclude the last custom list item which contain order note information
        mutableCustomListItems.forEach { customListItem ->
            if (customListItem.addOnUiModel != null) {
                val optionUiModels = customListItem.addOnUiModel.options
                customListItem.addOnUiModel.isError = optionUiModels.filter { it.isSelected }
                    .count() < customListItem.addOnUiModel.minQty
            }
        }
        val isError = isCustomOrderContainError(
                mutableCustomListItems.mapNotNull { it.addOnUiModel }
        )
        return isError to mutableCustomListItems.toList()
    }

    private fun isCustomOrderContainError(addOnUiModels: List<AddOnUiModel>): Boolean {
        return addOnUiModels.firstOrNull { it.isError } != null
    }

    fun generateRequestParam(
            shopId: String,
            productUiModel: ProductUiModel,
            cartId: String,
            orderNote: String,
            orderQty: Int,
            addOnUiModels: List<AddOnUiModel>
    ): UpdateParam {
        productUiModel.cartId = cartId
        productUiModel.orderNote = orderNote
        productUiModel.orderQty = orderQty
        return TokoFoodMerchantUiModelMapper.mapProductUiModelToAtcRequestParam(
                shopId = shopId,
                productUiModels = listOf(productUiModel),
                addOnUiModels = addOnUiModels
        )
    }
}
