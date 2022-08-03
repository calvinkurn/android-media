package com.tokopedia.digital_product_detail.data.mapper

import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.digital_product_detail.data.model.data.DigitalAtcResult
import com.tokopedia.digital_product_detail.di.DigitalPDPScope
import javax.inject.Inject

@DigitalPDPScope
class DigitalAtcMapper @Inject constructor() {

    fun mapAtcToResult(atc: ResponseCartData): DigitalAtcResult {
        return DigitalAtcResult(
            cartId = atc.id ?: "",
            categoryId = atc.relationships?.category?.data?.id ?: "",
            priceProduct = atc.attributes?.price ?: "",
            channelId = atc.attributes?.channelId ?: "",
            errorAtc = atc.errors
        )
    }
}