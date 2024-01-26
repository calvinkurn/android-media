package com.tokopedia.sellerorder.buyer_request_cancel.presentation

import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam

class BuyerRequestCancelRespondListenerImpl: IBuyerRequestCancelRespondListener {

    private lateinit var _mediator: IBuyerRequestCancelRespondListener.Mediator

    override fun registerBuyerRequestCancelRespondListenerMediator(mediator: IBuyerRequestCancelRespondListener.Mediator) {
        _mediator = mediator
    }

    override fun onBuyerRequestCancelRespondAcceptOrder() {
        _mediator.getBuyerRequestCancelRespondViewModel().acceptOrder(_mediator.getBuyerRequestCancelRespondOrderId(), _mediator.getBuyerRequestCancelRespondOrderInvoice())
    }

    override fun onBuyerRequestCancelRespondRejectOrder(reasonBuyer: String) {
        SomAnalytics.eventClickButtonTolakPesananPopup(
            _mediator.getBuyerRequestCancelRespondOrderStatusCode().toString(),
            _mediator.getBuyerRequestCancelRespondOrderStatusText()
        )
        val orderRejectRequest = SomRejectRequestParam(
            orderId = _mediator.getBuyerRequestCancelRespondOrderId(),
            rCode = "0",
            reason = reasonBuyer
        )
        _mediator.getBuyerRequestCancelRespondViewModel().rejectOrder(orderRejectRequest, _mediator.getBuyerRequestCancelRespondOrderInvoice())
        SomAnalytics.eventClickTolakPesanan(_mediator.getBuyerRequestCancelRespondOrderStatusText(), orderRejectRequest.reason)
    }

    override fun onBuyerRequestCancelRespondRejectCancelRequest() {
        SomAnalytics.eventClickButtonTolakPesananPopup(
            _mediator.getBuyerRequestCancelRespondOrderStatusCode().toString(),
            _mediator.getBuyerRequestCancelRespondOrderStatusText()
        )
        _mediator.getBuyerRequestCancelRespondViewModel().rejectCancelOrder(_mediator.getBuyerRequestCancelRespondOrderId(), _mediator.getBuyerRequestCancelRespondOrderInvoice())
    }

    override fun onBuyerRequestCancelRespondDismissed() {
        // noop
    }
}
