package com.tokopedia.tokofood.common.presentation.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataTokoFood
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataVariantTokoFood
import com.tokopedia.tokofood.common.domain.param.CartItemTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.CartTokoFoodParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductParam

object UpdateProductMapper {

    fun getProductParamById(updateProductParams: List<UpdateProductParam>,
                            additionalAttributes: String,
                            shopId: String): CartTokoFoodParam {
        val cartList = updateProductParams
            .map { param ->
                val metadata =
                    CartMetadataTokoFood(
                        variants = param.variants.map { variant ->
                            CartMetadataVariantTokoFood(
                                variantId = variant.variantId,
                                optionId = variant.optionId
                            )
                        },
                        notes = param.notes
                    )
                CartItemTokoFoodParam(
                    cartId = param.cartId.toLongOrZero(),
                    productId = param.productId,
                    shopId = shopId,
                    quantity = param.quantity,
                    metadata = metadata.generateString()
                )
            }
        return CartTokoFoodParam(additionalAttributes, cartList)
    }

}