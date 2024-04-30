package com.tokopedia.product.detail.common.buttons_byte_io_tracker

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel

interface ICartRedirectionButtonsByteIOTrackerDelegate {
    fun register(mediator: Mediator)
    fun trackOnButtonsShowed(buttonCartTypes: List<String>)
    fun trackOnButtonClick(buttonAction: Int)
    fun trackOnButtonClick(cartType: String)
    fun trackOnButtonClickCompleted(data: AddToCartDataModel)

    interface Mediator {
        fun getViewModel(): ICartRedirectionButtonsByteIOTrackerViewModelDelegate
        fun getButtonActionType(): Int
    }
}
