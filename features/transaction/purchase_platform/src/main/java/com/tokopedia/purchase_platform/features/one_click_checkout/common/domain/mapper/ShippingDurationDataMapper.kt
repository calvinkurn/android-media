package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper

import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping.ShippingNoPriceResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping.ShippingListModel

interface ShippingDurationDataMapper {
    fun convertToDomainModel(response: ShippingNoPriceResponse): ShippingListModel
}

interface ShippingDurationDataWithPriceMapper {
    fun convertToDomainModelWithPrice(response: ShippingRecommendationData): ShippingListModel
}