package com.tokopedia.tokofood.common.presentation.mapper

import com.tokopedia.tokofood.common.address.TokoFoodChosenAddress
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.param.ATCTokofoodParam
import com.tokopedia.tokofood.common.domain.param.ATCTokofoodParamBusinessData
import com.tokopedia.tokofood.common.domain.param.ATCTokofoodParamCustomRequest
import com.tokopedia.tokofood.common.domain.param.ATCTokofoodParamMetadata
import com.tokopedia.tokofood.common.domain.param.ATCTokofoodParamProduct
import com.tokopedia.tokofood.common.domain.param.ATCTokofoodParamVariant
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductParam

object UpdateProductMapper {

    fun getAtcProductParamById(updateProductParams: List<UpdateProductParam>,
                               chosenAddress: TokoFoodChosenAddress,
                               shopId: String): ATCTokofoodParam {
        return ATCTokofoodParam(
            source = TokoFoodCartUtil.SOURCE_MERCHANT_PAGE,
            businessData = listOf(
                ATCTokofoodParamBusinessData(
                    businessId = TokoFoodCartUtil.getBusinessId(),
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
                                  chosenAddress: TokoFoodChosenAddress,
                                  shopId: String,
                                  source: String): ATCTokofoodParam {
        return ATCTokofoodParam(
            source = source,
            businessData = listOf(
                ATCTokofoodParamBusinessData(
                    businessId = TokoFoodCartUtil.getBusinessId(),
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

}
