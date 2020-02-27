package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper

import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping.Response
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping.ShippingDataModel

interface ShippingDurationDataMapper {
    fun convertToDomainModel(response: Response): ShippingDataModel
}

interface ShippingDurationDataWithPriceMapper {
    fun convertToDomainModelWithPrice(response: Response): ShippingDataModel
}