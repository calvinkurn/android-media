package com.tokopedia.digital_product_detail.data.repository

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common_digital.atc.DigitalAddToCartUseCase
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_product_detail.domain.repository.RechargeAddToCartRepository
import com.tokopedia.network.data.model.response.DataResponse
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeAddToCartRepositoryImpl @Inject constructor(
    private val getDigitalAddToCartUseCase: DigitalAddToCartUseCase,
    private val dispatchers: CoroutineDispatchers
): RechargeAddToCartRepository {

    override suspend fun addToCart(
        digitalCheckoutPassData: DigitalCheckoutPassData,
        digitalIdentifierParam: RequestBodyIdentifier,
        digitalSubscriptionParams: DigitalSubscriptionParams,
        userId: String
    ): String = withContext(dispatchers.io) {
        val addToCart = getDigitalAddToCartUseCase.apply {
            setRequestParams(
                DigitalAddToCartUseCase.getRequestBodyAtcDigital(
                    digitalCheckoutPassData,
                    userId,
                    digitalIdentifierParam,
                    digitalSubscriptionParams
                ), digitalCheckoutPassData.idemPotencyKey)
        }.executeOnBackground()

        val token = object : TypeToken<DataResponse<ResponseCartData>>() {}.type
        val restResponse = addToCart[token]?.getData<DataResponse<*>>()?.data as ResponseCartData

        return@withContext if (restResponse.id != null) {
            restResponse.relationships?.category?.data?.id
                ?: ""
        } else throw DigitalAddToCartViewModel.DigitalFailGetCartId()
    }
}