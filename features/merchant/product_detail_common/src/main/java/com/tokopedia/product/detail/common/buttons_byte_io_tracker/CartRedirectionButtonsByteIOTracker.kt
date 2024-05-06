package com.tokopedia.product.detail.common.buttons_byte_io_tracker

import com.tokopedia.analytics.byteio.pdp.AppLogPdp
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.product.detail.common.ProductDetailCommonConstant

@Suppress("LateinitUsage")
class CartRedirectionButtonsByteIOTracker : ICartRedirectionButtonsByteIOTracker {

    private lateinit var _mediator: ICartRedirectionButtonsByteIOTracker.Mediator
    override fun registerCartRedirectionButtonsByteIOTracker(mediator: ICartRedirectionButtonsByteIOTracker.Mediator) {
        _mediator = mediator
    }

    override fun trackOnButtonsShowed(buttonCartTypes: List<String>) {
        buttonCartTypes.forEach(::trackOnButtonShowed)
    }

    override fun trackOnButtonClick(buttonAction: Int) {
        val analyticData = _mediator
            .getCartRedirectionButtonsByteIOTrackerViewModel()
            .getButtonClickTrackData(buttonAction = buttonAction) ?: return
        AppLogPdp.sendButtonClick(analyticData)
    }

    override fun trackOnButtonClick(cartType: String) {
        when (cartType) {
            ProductDetailCommonConstant.KEY_OCS_BUTTON -> trackOnButtonClick(
                ProductDetailCommonConstant.OCS_BUTTON
            )
        }
    }

    override fun trackOnButtonClickCompleted(data: AddToCartDataModel) {
        with(_mediator) {
            getCartRedirectionButtonsByteIOTrackerViewModel().run {
                val analyticData = getButtonClickCompletedTrackData(
                    getCartRedirectionButtonsByteIOTrackerActionType(),
                    data
                ) ?: return
                AppLogPdp.sendButtonConfirmSku(analyticData)
            }
        }
    }

    private fun trackOnButtonShowed(cartType: String) {
        val analyticData = _mediator
            .getCartRedirectionButtonsByteIOTrackerViewModel()
            .getButtonShowTrackData(cartType) ?: return
        AppLogPdp.sendButtonShow(analyticData)
    }
}
