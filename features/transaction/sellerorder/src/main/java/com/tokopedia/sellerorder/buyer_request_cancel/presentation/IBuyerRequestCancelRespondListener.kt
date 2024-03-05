package com.tokopedia.sellerorder.buyer_request_cancel.presentation

import com.tokopedia.sellerorder.common.presenter.viewmodel.SomOrderBaseViewModel

interface IBuyerRequestCancelRespondListener {
    fun registerBuyerRequestCancelRespondListenerMediator(mediator: Mediator)
    fun onBuyerRequestCancelRespondAcceptOrder()
    fun onBuyerRequestCancelRespondRejectOrder(reasonBuyer: String)
    fun onBuyerRequestCancelRespondRejectCancelRequest()
    fun onBuyerRequestCancelRespondDismissed()

    interface Mediator {
        fun getBuyerRequestCancelRespondOrderId(): String
        fun getBuyerRequestCancelRespondOrderInvoice(): String
        fun getBuyerRequestCancelRespondOrderStatusCode(): Int
        fun getBuyerRequestCancelRespondOrderStatusText(): String
        fun getBuyerRequestCancelRespondL2CancellationReason(): String
        fun getBuyerRequestCancelRespondDescription(): String
        fun getBuyerRequestCancelRespondPrimaryTextButton(): String
        fun getBuyerRequestCancelRespondSecondaryTextButton(): String
        fun getBuyerRequestCancelRespondViewModel(): SomOrderBaseViewModel
    }
}
