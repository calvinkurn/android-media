package com.tokopedia.sellerorder.buyer_request_cancel.presentation

import androidx.coordinatorlayout.widget.CoordinatorLayout

interface IBuyerRequestCancelRespondBottomSheetManager {

    var bottomSheetBuyerRequestCancelRespond: BuyerRequestCancelRespondBottomSheet?

    fun registerBuyerRequestCancelRespondBottomSheet(
        bottomSheetManagerMediator: Mediator,
        bottomSheetListenerMediator: IBuyerRequestCancelRespondListener.Mediator,
        bottomSheetListener: IBuyerRequestCancelRespondListener
    )

    fun showBuyerRequestCancelRespondBottomSheet()
    interface Mediator {
        fun getBottomSheetContainer(): CoordinatorLayout?
    }
}
