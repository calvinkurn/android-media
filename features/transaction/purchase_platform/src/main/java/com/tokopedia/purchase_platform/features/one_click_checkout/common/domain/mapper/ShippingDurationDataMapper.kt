package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper

import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.RatesGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.Services
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.ShippingNoPriceResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping.Response
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice.ServicesItemModelNoPrice
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice.ShippingListModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.ShippingDataModel

interface ShippingDurationDataMapper {
    fun convertToDomainModel(response: ShippingNoPriceResponse): ShippingListModel
}

interface ShippingDurationDataWithPriceMapper {
    fun convertToDomainModelWithPrice(response: ShippingRecommendationData): ShippingListModel
}