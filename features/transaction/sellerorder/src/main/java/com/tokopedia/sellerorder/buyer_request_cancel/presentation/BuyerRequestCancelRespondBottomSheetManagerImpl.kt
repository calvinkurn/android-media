package com.tokopedia.sellerorder.buyer_request_cancel.presentation

class BuyerRequestCancelRespondBottomSheetManagerImpl : IBuyerRequestCancelRespondBottomSheetManager {

    @Suppress("LateinitUsage")
    private lateinit var _bottomSheetManagerMediator: IBuyerRequestCancelRespondBottomSheetManager.Mediator
    @Suppress("LateinitUsage")
    private lateinit var _bottomSheetListenerMediator: IBuyerRequestCancelRespondListener.Mediator
    @Suppress("LateinitUsage")
    private lateinit var _bottomSheetListener: IBuyerRequestCancelRespondListener

    override var bottomSheetBuyerRequestCancelRespond: BuyerRequestCancelRespondBottomSheet? = null

    override fun registerBuyerRequestCancelRespondBottomSheet(
        bottomSheetManagerMediator: IBuyerRequestCancelRespondBottomSheetManager.Mediator,
        bottomSheetListenerMediator: IBuyerRequestCancelRespondListener.Mediator,
        bottomSheetListener: IBuyerRequestCancelRespondListener
    ) {
        _bottomSheetManagerMediator = bottomSheetManagerMediator
        _bottomSheetListenerMediator = bottomSheetListenerMediator
        _bottomSheetListener = bottomSheetListener
    }

    override fun showBuyerRequestCancelRespondBottomSheet() {
        _bottomSheetManagerMediator.getBottomSheetContainer()?.let { container ->
            bottomSheetBuyerRequestCancelRespond = bottomSheetBuyerRequestCancelRespond?.apply {
                setupBuyerRequestCancelBottomSheet(
                    buyerRequestCancelRespondBottomSheet = this,
                    orderStatusCode = _bottomSheetListenerMediator.getBuyerRequestCancelRespondOrderStatusCode(),
                    reason = _bottomSheetListenerMediator.getBuyerRequestCancelRespondL2CancellationReason(),
                    description = _bottomSheetListenerMediator.getBuyerRequestCancelRespondDescription(),
                    primaryButtonText = _bottomSheetListenerMediator.getBuyerRequestCancelRespondPrimaryTextButton(),
                    secondaryButtonText = _bottomSheetListenerMediator.getBuyerRequestCancelRespondSecondaryTextButton()
                )
            } ?: BuyerRequestCancelRespondBottomSheet(container.context).apply {
                setupBuyerRequestCancelBottomSheet(
                    buyerRequestCancelRespondBottomSheet = this,
                    orderStatusCode = _bottomSheetListenerMediator.getBuyerRequestCancelRespondOrderStatusCode(),
                    reason = _bottomSheetListenerMediator.getBuyerRequestCancelRespondL2CancellationReason(),
                    description = _bottomSheetListenerMediator.getBuyerRequestCancelRespondDescription(),
                    primaryButtonText = _bottomSheetListenerMediator.getBuyerRequestCancelRespondPrimaryTextButton(),
                    secondaryButtonText = _bottomSheetListenerMediator.getBuyerRequestCancelRespondSecondaryTextButton()
                )
            }
            bottomSheetBuyerRequestCancelRespond?.setOnDismiss { _bottomSheetListener.onBuyerRequestCancelRespondDismissed() }
            bottomSheetBuyerRequestCancelRespond?.init(container)
            bottomSheetBuyerRequestCancelRespond?.show()
        }
    }

    private fun setupBuyerRequestCancelBottomSheet(
        buyerRequestCancelRespondBottomSheet: BuyerRequestCancelRespondBottomSheet,
        orderStatusCode: Int = 0,
        reason: String = "",
        description: String,
        primaryButtonText: String,
        secondaryButtonText: String
    ) {
        buyerRequestCancelRespondBottomSheet.apply {
            _bottomSheetListener.registerBuyerRequestCancelRespondListenerMediator(_bottomSheetListenerMediator)
            setListener(_bottomSheetListener)
            init(reason, orderStatusCode, description, primaryButtonText, secondaryButtonText)
            hideKnob()
            showCloseButton()
        }
    }
}
