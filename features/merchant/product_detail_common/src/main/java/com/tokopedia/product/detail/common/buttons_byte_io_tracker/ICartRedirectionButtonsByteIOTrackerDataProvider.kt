package com.tokopedia.product.detail.common.buttons_byte_io_tracker

import com.tokopedia.analytics.byteio.ButtonClickAnalyticData
import com.tokopedia.analytics.byteio.ButtonClickCompletedAnalyticData
import com.tokopedia.analytics.byteio.ButtonShowAnalyticData
import com.tokopedia.analytics.byteio.EventName
import com.tokopedia.analytics.byteio.ProductType
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel

/**
 * This interface is used as a contract for providing data for the cart redirection buttons ByteIO
 * tracker handled by the [ICartRedirectionButtonsByteIOTracker].
 *
 * Tracker Sheet: https://bytedance.sg.larkoffice.com/sheets/YVaGsNyMfhqbjzt7HJvlH4FIgof?sheet=3a360f
 */

interface ICartRedirectionButtonsByteIOTrackerDataProvider {
    fun registerCartRedirectionButtonsByteIOTrackerDataProvider(mediator: Mediator)

    /**
     * This function is to provide the tracker data for the [EventName.PDP_BUTTON_SHOW] tracker
     * which handled by the [ICartRedirectionButtonsByteIOTracker.trackOnButtonsShowed]
     */
    fun getButtonShowTrackData(cartType: String): ButtonShowAnalyticData?

    /**
     * This function is to provide the tracker data for the [EventName.PDP_BUTTON_CLICK] tracker
     * which handled by the [ICartRedirectionButtonsByteIOTracker.trackOnButtonClick]
     */
    fun getButtonClickTrackData(buttonAction: Int): ButtonClickAnalyticData?

    /**
     * This function is to provide the tracker data for the [EventName.PDP_BUTTON_CLICK_COMPLETED] tracker
     * which handled by the [ICartRedirectionButtonsByteIOTracker.trackOnButtonClickCompleted]
     */
    fun getButtonClickCompletedTrackData(
        buttonActionType: Int,
        data: AddToCartDataModel
    ): ButtonClickCompletedAnalyticData?

    interface Mediator {
        fun getParentProductId(): String?
        fun isSingleSku(): Boolean
        fun getSkuId(): String?
        fun getProductMinOrder(): Int?
        fun getProductType(): ProductType?
        fun getProductOriginalPrice(): Double?
        fun getProductSalePrice(): Double?
        fun isFollowShop(): Boolean
        fun getShopId(): String
    }
}
