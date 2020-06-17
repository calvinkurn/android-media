package com.tokopedia.oneclickcheckout.common.domain.mapper

import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.oneclickcheckout.common.data.model.response.shipping.ShippingNoPriceResponse
import com.tokopedia.oneclickcheckout.common.domain.model.shipping.ShippingListModel

interface ShippingDurationDataMapper {
    fun convertToDomainModel(response: ShippingNoPriceResponse): ShippingListModel
}

interface ShippingDurationDataWithPriceMapper {
    fun convertToDomainModelWithPrice(response: ShippingRecommendationData): ShippingListModel
}