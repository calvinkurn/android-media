package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper

import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.Services
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.ShippingNoPriceResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping.Response
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping.ShippingDataModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice.ServicesItemModelNoPrice
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice.ShippingListModel

interface ShippingDurationDataMapper {
    fun convertToDomainModel(response: ShippingNoPriceResponse): ShippingListModel
}

interface ShippingDurationDataWithPriceMapper {
    fun convertToDomainModelWithPrice(response: Response): ShippingDataModel
}