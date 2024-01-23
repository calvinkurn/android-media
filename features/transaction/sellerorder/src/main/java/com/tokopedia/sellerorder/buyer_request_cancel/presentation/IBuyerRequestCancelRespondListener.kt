package com.tokopedia.sellerorder.buyer_request_cancel.presentation

import com.tokopedia.sellerorder.common.presenter.viewmodel.SomOrderBaseViewModel

interface IBuyerRequestCancelRespondListener {
    fun registerBuyerRequestCancelRespondListenerMediator(mediator: Mediator)
    fun onBuyerRequestCancelRespondAcceptOrder()
    fun onBuyerRequestCancelRespondRejectOrder(reasonBuyer: String)
    fun onBuyerRequestCancelRespondRejectCancelRequest()

    interface Mediator {
        fun getBuyerRequestCancelRespondOrderId(): String
        fun getBuyerRequestCancelRespondOrderInvoice(): String
        fun getBuyerRequestCancelRespondOrderStatusCodeString(): String
        fun getBuyerRequestCancelRespondOrderStatusText(): String
        fun getBuyerRequestCancelRespondViewModel(): SomOrderBaseViewModel
    }
}
