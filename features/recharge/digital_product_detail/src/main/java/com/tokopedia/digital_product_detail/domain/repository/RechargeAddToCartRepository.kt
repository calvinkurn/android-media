package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData

interface RechargeAddToCartRepository {
    suspend fun addToCart(digitalCheckoutPassData: DigitalCheckoutPassData,
                          digitalIdentifierParam: RequestBodyIdentifier,
                          digitalSubscriptionParams: DigitalSubscriptionParams,
                          userId: String): String
}