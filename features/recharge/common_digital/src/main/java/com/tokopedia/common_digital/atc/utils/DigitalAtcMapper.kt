package com.tokopedia.common_digital.atc.utils

import com.tokopedia.common_digital.atc.data.gql.response.RechargeATCResponse
import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.presentation.model.DigitalAtcTrackingModel

/**
 * @author by jessica on 04/03/21
 */

object DigitalAtcMapper {
    fun mapToDigitalAtcTrackingModel(
        responseCartData: ResponseCartData?,
        responseGqlCartData: RechargeATCResponse?,
        digitalCheckoutPassData: DigitalCheckoutPassData,
        userId: String
    ): DigitalAtcTrackingModel? {

        responseCartData?.let {
            return DigitalAtcTrackingModel(
                cartId = it.id ?: "",
                productId = digitalCheckoutPassData.productId ?: "",
                operatorName = it.attributes?.operatorName ?: "",
                categoryId = digitalCheckoutPassData.categoryId ?: "",
                categoryName = it.attributes?.categoryName ?: "",
                priceText = it.attributes?.price ?: "",
                pricePlain = it.attributes?.pricePlain ?: 0.0,
                isInstantCheckout = it.attributes?.isInstantCheckout ?: false,
                source = digitalCheckoutPassData.source,
                userId = userId,
                isSpecialProduct = digitalCheckoutPassData.isSpecialProduct,
                channelId = it.attributes?.channelId ?: "",
            )
        }

        responseGqlCartData?.let {
            return DigitalAtcTrackingModel(
                cartId = it.data.id,
                productId = digitalCheckoutPassData.productId ?: "",
                operatorName = it.data.attributes.operatorName,
                categoryId = digitalCheckoutPassData.categoryId ?: "",
                categoryName = it.data.attributes.categoryName,
                priceText = it.data.attributes.price,
                pricePlain = it.data.attributes.pricePlain,
                isInstantCheckout = it.data.attributes.isInstantCheckout,
                source = digitalCheckoutPassData.source,
                userId = userId,
                isSpecialProduct = digitalCheckoutPassData.isSpecialProduct,
                channelId = it.data.attributes.channelId.toString(),
            )
        }

        return null
    }
}