package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_product_detail.data.model.data.DigitalAtcResult

interface RechargeAddToCartRepository {
    suspend fun addToCart(digitalCheckoutPassData: DigitalCheckoutPassData,
                          digitalIdentifierParam: RequestBodyIdentifier,
                          digitalSubscriptionParams: DigitalSubscriptionParams,
                          userId: String): DigitalAtcResult
}