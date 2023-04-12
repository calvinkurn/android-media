package com.tokopedia.common_digital.atc

import com.tokopedia.common_digital.atc.utils.DigitalAtcMapper
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.presentation.model.DigitalAtcTrackingModel
import javax.inject.Inject

/**
 * @author Created By : Muhammad Furqan on Aug 5, 2022
 */
class DigitalAddToCartUseCase @Inject constructor(
    private val gqlUseCase: RechargeAddToCartGqlUseCase
) {
    suspend fun execute(
        digitalCheckoutPassData: DigitalCheckoutPassData,
        userId: String,
        digitalIdentifierParam: RequestBodyIdentifier
    ): DigitalAtcTrackingModel? {
        var returnResult: DigitalAtcTrackingModel? = null

        gqlUseCase.setParams(digitalCheckoutPassData, userId, digitalIdentifierParam)

        val result = gqlUseCase.executeOnBackground().atcResponse

        returnResult = when {
            result.data.id.isNotEmpty() -> {
                DigitalAtcMapper.mapToDigitalAtcTrackingModel(
                    null,
                    result,
                    digitalCheckoutPassData,
                    userId
                )
            }
            result.errors.isNotEmpty() -> {
                DigitalAtcMapper.mapToDigitalAtcTrackingModel(
                    null,
                    result,
                    digitalCheckoutPassData,
                    userId,
                    error = DigitalAtcMapper.mapErrorToErrorAtc(result.errors)
                )
            }
            else -> {
                null
            }
        }

        return returnResult
    }
}
