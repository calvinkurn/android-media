package com.tokopedia.tokofood.common.presentation.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataTokoFood
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataTokoFoodWithVariant
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataVariantTokoFood
import com.tokopedia.tokofood.common.domain.param.ATCCartItemTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.ATCCartTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.CartItemTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.CartTokoFoodParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductParam

object UpdateProductMapperOld {

    fun getAtcProductParamById(updateProductParams: List<UpdateProductParam>,
                               additionalAttributes: String,
                               shopId: String): ATCCartTokoFoodParam {
        val cartList = updateProductParams
            .map { param ->
                val metadataString = getMetadataString(param)
                ATCCartItemTokoFoodParam(
                    productId = param.productId,
                    shopId = shopId,
                    quantity = param.quantity,
                    metadata = metadataString
                )
            }
        return ATCCartTokoFoodParam(additionalAttributes, cartList)
    }

    fun getUpdateProductParamById(updateProductParams: List<UpdateProductParam>,
                                  additionalAttributes: String,
                                  shopId: String): CartTokoFoodParam {
        val cartList = updateProductParams
            .map { param ->
                val metadataString = getMetadataString(param)
                CartItemTokoFoodParam(
                    cartId = param.cartId.toLongOrZero(),
                    productId = param.productId,
                    shopId = shopId,
                    quantity = param.quantity,
                    metadata = metadataString
                )
            }
        return CartTokoFoodParam(additionalAttributes, cartList)
    }

    private fun getMetadataString(param: UpdateProductParam): String {
        return if (param.variants.isEmpty()) {
            CartMetadataTokoFood(
                notes = param.notes
            ).generateString()
        } else {
            CartMetadataTokoFoodWithVariant(
                variants = param.variants.map { variant ->
                    CartMetadataVariantTokoFood(
                        variantId = variant.variantId,
                        optionId = variant.optionId
                    )
                },
                notes = param.notes
            ).generateString()
        }
    }

}
