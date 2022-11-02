package com.tokopedia.common_digital.atc

import com.google.gson.reflect.TypeToken
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.common_digital.atc.utils.DigitalAtcMapper
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.presentation.model.DigitalAtcTrackingModel
import com.tokopedia.network.data.model.response.DataResponse
import javax.inject.Inject

/**
 * @author Created By : Muhammad Furqan on Aug 5, 2022
 */
class DigitalAddToCartUseCase @Inject constructor(
    private val restUseCase: DigitalAddToCartRestUseCase,
    private val gqlUseCase: RechargeAddToCartGqlUseCase
) {
    suspend fun execute(
        digitalCheckoutPassData: DigitalCheckoutPassData,
        userId: String,
        digitalIdentifierParam: RequestBodyIdentifier,
        digitalSubscriptionParams: DigitalSubscriptionParams,
        idemPotencyKeyHeader: String?,
        isUseGql: Boolean
    ): DigitalAtcTrackingModel? =
        if (isUseGql) {
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

            returnResult
        } else {
            restUseCase.setRequestParams(
                DigitalAddToCartRestUseCase.getRequestBodyAtcDigital(
                    digitalCheckoutPassData,
                    userId,
                    digitalIdentifierParam,
                    digitalSubscriptionParams
                ),
                idemPotencyKeyHeader
            )
            val response = restUseCase.executeOnBackground()

            val token = object : TypeToken<DataResponse<ResponseCartData>>() {}.type
            val restResponse = response[token]?.getData<DataResponse<*>>()?.data as ResponseCartData

            restResponse.id?.let {
                DigitalAtcMapper.mapToDigitalAtcTrackingModel(
                    restResponse,
                    null,
                    digitalCheckoutPassData,
                    userId
                )
            }
        }
}
