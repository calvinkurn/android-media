package com.tokopedia.tokofood.common.presentation.mapper

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddress
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataTokoFood
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataTokoFoodWithVariant
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataVariantTokoFood
import com.tokopedia.tokofood.common.domain.param.ATCTokofoodParam
import com.tokopedia.tokofood.common.domain.param.ATCTokofoodParamBusinessData
import com.tokopedia.tokofood.common.domain.param.ATCTokofoodParamCustomRequest
import com.tokopedia.tokofood.common.domain.param.ATCTokofoodParamMetadata
import com.tokopedia.tokofood.common.domain.param.ATCTokofoodParamProduct
import com.tokopedia.tokofood.common.domain.param.ATCTokofoodParamVariant
import com.tokopedia.tokofood.common.domain.param.CartItemTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.CartTokoFoodParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductParam

object UpdateProductMapper {

    fun getAtcProductParamById(updateProductParams: List<UpdateProductParam>,
                               chosenAddress: TokoFoodChosenAddress,
                               shopId: String, source: String): ATCTokofoodParam {
        // TODO: Add businessId
        return ATCTokofoodParam(
            source = source,
            businessData = listOf(
                ATCTokofoodParamBusinessData(
                    businessId = String.EMPTY,
                    customRequest = ATCTokofoodParamCustomRequest(
                        chosenAddress = chosenAddress
                    ),
                    productList = updateProductParams.map {
                        ATCTokofoodParamProduct(
                            productId = it.productId,
                            quantity = it.quantity,
                            metadata = ATCTokofoodParamMetadata(
                                notes = it.notes,
                                variants = it.variants.map { variant ->
                                    ATCTokofoodParamVariant(
                                        optionId = variant.optionId,
                                        variantId = variant.variantId
                                    )
                                }
                            ),
                            shopId = shopId
                        )
                    }
                )
            )
        )
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
