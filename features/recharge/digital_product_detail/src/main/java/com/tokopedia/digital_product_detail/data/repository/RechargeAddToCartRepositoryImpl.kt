package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common_digital.atc.DigitalAddToCartUseCase
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_product_detail.data.mapper.DigitalAtcMapper
import com.tokopedia.digital_product_detail.data.model.data.DigitalAtcResult
import com.tokopedia.digital_product_detail.domain.repository.RechargeAddToCartRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeAddToCartRepositoryImpl @Inject constructor(
    private val getDigitalAddToCartUseCase: DigitalAddToCartUseCase,
    private val mapAtcToResult: DigitalAtcMapper,
    private val dispatchers: CoroutineDispatchers
) : RechargeAddToCartRepository {

    override suspend fun addToCart(
        digitalCheckoutPassData: DigitalCheckoutPassData,
        digitalIdentifierParam: RequestBodyIdentifier,
        digitalSubscriptionParams: DigitalSubscriptionParams,
        userId: String,
        isUseGql: Boolean
    ): DigitalAtcResult = withContext(dispatchers.io) {
        val addToCart = getDigitalAddToCartUseCase.execute(
            digitalCheckoutPassData = digitalCheckoutPassData,
            userId = userId,
            digitalIdentifierParam = digitalIdentifierParam,
            digitalSubscriptionParams = digitalSubscriptionParams,
            idemPotencyKeyHeader = digitalCheckoutPassData.idemPotencyKey,
            isUseGql = isUseGql
        )

        addToCart?.let {
            if (addToCart.cartId.isNotEmpty() || addToCart.atcError != null) {
                return@withContext mapAtcToResult.mapAtcToResult(addToCart)
            } else throw DigitalAddToCartViewModel.DigitalFailGetCartId()
        } ?: throw DigitalAddToCartViewModel.DigitalFailGetCartId()

    }
}