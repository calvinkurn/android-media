package com.tokopedia.tokofood.feature.merchant.presentation.mapper

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.tokofood.common.constants.ShareComponentConstants
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataVariantTokoFood
import com.tokopedia.tokofood.common.domain.response.CartTokoFood
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductVariantParam
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodCategoryFilter
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CategoryFilterListUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CategoryFilterWrapperUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CategoryUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel

object TokoFoodMerchantUiModelMapper {

    fun mapProductUiModelToAtcRequestParam(
            shopId: String,
            productUiModels: List<ProductUiModel>,
            addOnUiModels: List<AddOnUiModel> = listOf()
    ): UpdateParam {
        return UpdateParam(
            shopId = shopId,
            productList = productUiModels.map {
                UpdateProductParam(
                    productId = it.id,
                    cartId = it.cartId,
                    notes = it.orderNote,
                    quantity = it.orderQty,
                    variants = mapCustomListItemsToVariantParams(addOnUiModels)
                )
            }
        )
    }

    fun mapCustomOrderDetailToAtcRequestParam(
            shopId: String,
            cartId: String,
            productId: String,
            customOrderDetails: List<CustomOrderDetail>
    ): UpdateParam {
        return UpdateParam(
                shopId = shopId,
                productList = customOrderDetails.map { customOrderDetail ->
                    val addOnUiModels = customOrderDetail.customListItems
                            .filter { it.addOnUiModel != null }
                            .map { customListItem -> customListItem.addOnUiModel ?: AddOnUiModel() }
                    UpdateProductParam(
                            productId = productId,
                            cartId = cartId,
                            notes = customOrderDetail.orderNote,
                            quantity = customOrderDetail.qty,
                            variants = mapCustomListItemsToVariantParams(addOnUiModels)
                    )
                }
        )
    }

    private fun mapCustomListItemsToVariantParams(addOnUiModels: List<AddOnUiModel>): List<UpdateProductVariantParam> {
        val variantParams = mutableListOf<UpdateProductVariantParam>()
        // selected variant e.g. sugar level
        addOnUiModels.filter { it.isSelected }.forEach { addOnUiModel ->
            val variantId = addOnUiModel.id
            variantParams.addAll(addOnUiModel.options
                    .filter { it.isSelected } // selected options
                    .map { optionUiModel ->
                        UpdateProductVariantParam(
                                variantId = variantId,
                                optionId = optionUiModel.id
                        )
                    }
            )
        }
        return variantParams.toList()
    }

    fun mapCartTokoFoodToCustomOrderDetails(cartTokoFood: CartTokoFood, productUiModel: ProductUiModel): CustomOrderDetail {
        val selectedCustomListItems = mapCartTokoFoodVariantsToSelectedCustomListItems(
                orderNote = cartTokoFood.getMetadata()?.notes.orEmpty(),
                masterData = productUiModel.customListItems,
                selectedVariants = cartTokoFood.getMetadata()?.variants ?: listOf()
        )
        val subTotal = calculateSubtotalPrice(
                baseProductPrice = productUiModel.price,
                quantity = productUiModel.orderQty,
                addOnUiModels = selectedCustomListItems.map { it.addOnUiModel }
        )
        val subTotalFmt = subTotal.getCurrencyFormatted()
        return CustomOrderDetail(
                cartId = cartTokoFood.cartId,
                subTotal = subTotal,
                subTotalFmt = subTotalFmt,
                qty = cartTokoFood.quantity,
                customListItems = selectedCustomListItems
        )
    }

    private fun calculateSubtotalPrice(baseProductPrice: Double, quantity: Int, addOnUiModels: List<AddOnUiModel?>): Double {
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

    private fun mapCartTokoFoodVariantsToSelectedCustomListItems(
        orderNote: String,
        masterData: List<CustomListItem>,
        selectedVariants: List<CartMetadataVariantTokoFood>
    ): List<CustomListItem> {
        val selectedCustomListItems = masterData.toMutableList()
        val optionMap = selectedVariants.groupBy { it.variantId }
        optionMap.keys.forEach { variantId ->
            val selectedOptionIds = optionMap[variantId]?.map { it.optionId } ?: listOf()
            val selectedCustomListItem = selectedCustomListItems.first { it.addOnUiModel?.id == variantId }
            selectedCustomListItem.addOnUiModel?.isSelected = true
            selectedCustomListItem.addOnUiModel?.options?.forEach {
                if (selectedOptionIds.contains(it.id)) it.isSelected = true
            }
            val options = selectedCustomListItem.addOnUiModel?.options?: listOf()
            selectedCustomListItem.addOnUiModel?.selectedAddOns = options.filter { it.isSelected }.map { it.name }
        }
        // set order note
        val customOrderWidgetUiModel = selectedCustomListItems.firstOrNull() { it.addOnUiModel == null }
        customOrderWidgetUiModel?.orderNote = orderNote
        return selectedCustomListItems.toList()
    }

    fun mapProductListItemToCategoryFilterUiModel(
        tokoFoodCategoryFilterList: List<TokoFoodCategoryFilter>,
        filterNameSelected: String
    ): CategoryFilterWrapperUiModel {
        val categoryFilterListUiModel = tokoFoodCategoryFilterList.map {
            val isSelected = filterNameSelected == it.title
            CategoryFilterListUiModel(
                categoryUiModel = CategoryUiModel(
                    key = it.key,
                    title = it.title
                ), it.subtitle, isSelected
            )
        }
        return CategoryFilterWrapperUiModel(categoryFilterListUiModel)
    }

    fun getMerchantFoodAppLink(merchantId: String, productId: String): String {
        return UriUtil.buildUri(ApplinkConst.TokoFood.MERCHANT, mapOf(ShareComponentConstants.Merchant.MERCHANT_ID to merchantId)).apply {
            UriUtil.buildUriAppendParam(this, mapOf(ShareComponentConstants.Merchant.PRODUCT_ID to productId))
        }
    }
}