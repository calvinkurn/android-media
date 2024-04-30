package com.tokopedia.product.detail.common.buttons_byte_io_tracker

import com.tokopedia.analytics.byteio.EventName
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.product.detail.common.ProductDetailCommonConstant

/**
 * This interface is used as a contract for handling ByteIO tracker on the cart redirection buttons
 * such as:
 * 1. OCS button ([ProductDetailCommonConstant.KEY_OCS_BUTTON])
 *
 * Tracker Sheet: https://bytedance.sg.larkoffice.com/sheets/YVaGsNyMfhqbjzt7HJvlH4FIgof?sheet=3a360f
 */

interface ICartRedirectionButtonsByteIOTracker {
    fun registerCartRedirectionButtonsByteIOTracker(mediator: Mediator)

    /**
     * This function is for the [EventName.PDP_BUTTON_SHOW] tracker
     */
    fun trackOnButtonsShowed(buttonCartTypes: List<String>)

    /**
     * This function is for the [EventName.PDP_BUTTON_CLICK] tracker
     */
    fun trackOnButtonClick(buttonAction: Int)

    /**
     * This function is for the [EventName.PDP_BUTTON_CLICK] tracker
     */
    fun trackOnButtonClick(cartType: String)

    /**
     * This function is for the [EventName.PDP_BUTTON_CLICK_COMPLETED] tracker
     */
    fun trackOnButtonClickCompleted(data: AddToCartDataModel)

    interface Mediator {
        fun getCartRedirectionButtonsByteIOTrackerViewModel(): ICartRedirectionButtonsByteIOTrackerDataProvider
        fun getCartRedirectionButtonsByteIOTrackerActionType(): Int
    }
}
