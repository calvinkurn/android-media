package com.tokopedia.product.detail.common.buttons_byte_io_tracker

import com.tokopedia.analytics.byteio.ButtonClickAnalyticData
import com.tokopedia.analytics.byteio.ButtonClickCompletedAnalyticData
import com.tokopedia.analytics.byteio.ButtonShowAnalyticData
import com.tokopedia.analytics.byteio.ProductType
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel

interface ICartRedirectionButtonsByteIOTrackerViewModelDelegate {
    fun register(mediator: Mediator)
    fun getButtonShowTrackData(cartType: String): ButtonShowAnalyticData?
    fun getButtonClickTrackData(buttonAction: Int): ButtonClickAnalyticData?
    fun getButtonClickCompletedTrackData(buttonActionType: Int, data: AddToCartDataModel): ButtonClickCompletedAnalyticData?

    interface Mediator {
        fun getParentProductId(): String?
        fun isSingleSku(): Boolean
        fun getSkuId(): String?
        fun getProductMinOrder(): Int?
        fun getProductType(): ProductType?
        fun getProductOriginalPrice(): Double?
        fun getProductSalePrice(): Double?
        fun isFollowShop(): Boolean
    }
}
