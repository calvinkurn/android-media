package com.tokopedia.common_digital.atc.utils

import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.presentation.model.DigitalAtcTrackingModel

/**
 * @author by jessica on 04/03/21
 */

object DigitalAtcMapper {
    fun mapToDigitalAtcTrackingModel(responseCartData: ResponseCartData,
                                     digitalCheckoutPassData: DigitalCheckoutPassData, userId: String): DigitalAtcTrackingModel {
        return DigitalAtcTrackingModel(
                cartId = responseCartData.id ?: "",
                productId = digitalCheckoutPassData.productId ?: "",
                operatorName = responseCartData.attributes?.operatorName ?: "",
                categoryId = digitalCheckoutPassData.categoryId ?: "",
                categoryName = responseCartData.attributes?.categoryName ?: "",
                priceText = responseCartData.attributes?.price ?: "",
                pricePlain = responseCartData.attributes?.pricePlain ?: 0.0,
                isInstantCheckout = responseCartData.attributes?.isInstantCheckout ?: false,
                source = digitalCheckoutPassData.source,
                userId = userId

        )
    }
}