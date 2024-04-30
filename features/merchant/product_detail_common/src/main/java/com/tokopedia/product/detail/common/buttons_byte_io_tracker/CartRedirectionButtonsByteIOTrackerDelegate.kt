package com.tokopedia.product.detail.common.buttons_byte_io_tracker

import com.tokopedia.analytics.byteio.pdp.AppLogPdp
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.product.detail.common.ProductDetailCommonConstant

class CartRedirectionButtonsByteIOTrackerDelegate : ICartRedirectionButtonsByteIOTrackerDelegate {

    private var _mediator: ICartRedirectionButtonsByteIOTrackerDelegate.Mediator? = null
    override fun register(mediator: ICartRedirectionButtonsByteIOTrackerDelegate.Mediator) {
        _mediator = mediator
    }

    override fun trackOnButtonsShowed(buttonCartTypes: List<String>) {
        buttonCartTypes.forEach(::trackOnButtonShowed)
    }

    override fun trackOnButtonClick(buttonAction: Int) {
        _mediator?.getViewModel()?.run {
            val analyticData = getButtonClickTrackData(buttonAction = buttonAction) ?: return
            AppLogPdp.sendButtonClick(analyticData)
        }
    }

    override fun trackOnButtonClick(cartType: String) {
        when (cartType) {
            ProductDetailCommonConstant.KEY_OCS_BUTTON -> trackOnButtonClick(ProductDetailCommonConstant.OCS_BUTTON)
        }
    }

    override fun trackOnButtonClickCompleted(data: AddToCartDataModel) {
        _mediator?.run {
            getViewModel().run {
                val analyticData = getButtonClickCompletedTrackData(getButtonActionType(), data) ?: return
                AppLogPdp.sendButtonClickCompleted(analyticData)
            }
        }
        _mediator?.getViewModel()?.run {

        }
    }

    private fun trackOnButtonShowed(cartType: String) {
        _mediator?.getViewModel()?.run {
            val analyticData = getButtonShowTrackData(cartType) ?: return
            AppLogPdp.sendButtonShow(analyticData)
        }
    }
}
