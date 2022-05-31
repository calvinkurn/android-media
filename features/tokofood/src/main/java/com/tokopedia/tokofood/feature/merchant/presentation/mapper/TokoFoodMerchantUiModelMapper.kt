package com.tokopedia.tokofood.feature.merchant.presentation.mapper

import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductVariantParam
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel

object TokoFoodMerchantUiModelMapper {

    fun mapProductUiModelToAtcRequestParam(
            shopId: String,
            cartId: String = "",
            productUiModels: List<ProductUiModel>,
            addOnUiModels: List<AddOnUiModel> = listOf()
    ): UpdateParam {
        return UpdateParam(
                shopId = shopId,
                productList = productUiModels.map {
                    UpdateProductParam(
                            productId = it.id,
                            cartId = cartId,
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
}