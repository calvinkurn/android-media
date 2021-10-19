package com.tokopedia.buyerorderdetail.presentation.helper

import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTrackerConstant
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderExtensionTracker
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailActionButtonKey
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.BuyerOrderDetailBottomSheetManager
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.usecase.coroutines.Success

class BuyerOrderDetailStickyActionButtonHandler(
    private val bottomSheetManager: BuyerOrderDetailBottomSheetManager,
    private val cacheManager: SaveInstanceCacheManager,
    private val navigator: BuyerOrderDetailNavigator,
    private val viewModel: BuyerOrderDetailViewModel
) : ActionButtonClickListener {
    override fun onActionButtonClicked(
        isFromPrimaryButton: Boolean,
        button: ActionButtonsUiModel.ActionButton
    ) {
//        val buttonName = when (button.key) {
//            BuyerOrderDetailActionButtonKey.ASK_SELLER -> {
//                onAskSellerActionButtonClicked()
//                BuyerOrderDetailTrackerConstant.BUTTON_NAME_CHAT_SELLER
//            }
//            BuyerOrderDetailActionButtonKey.REQUEST_CANCEL -> {
//                onRequestCancelActionButtonClicked(button)
//                BuyerOrderDetailTrackerConstant.BUTTON_NAME_CANCEL_ORDER
//            }
//            BuyerOrderDetailActionButtonKey.TRACK_SHIPMENT -> {
//                onTrackShipmentActionButtonClicked(button)
//                BuyerOrderDetailTrackerConstant.BUTTON_NAME_TRACK_ORDER
//            }
//            BuyerOrderDetailActionButtonKey.REQUEST_COMPLAINT -> {
//                onComplaintActionButtonClicked(button.url)
//                BuyerOrderDetailTrackerConstant.BUTTON_NAME_COMPLAINT_ORDER
//            }
//            BuyerOrderDetailActionButtonKey.VIEW_COMPLAINT -> {
//                onViewComplaintActionButtonClicked(button.url)
//                if (isFromPrimaryButton) "" else BuyerOrderDetailTrackerConstant.BUTTON_NAME_VIEW_COMPLAINT_ORDER
//            }
//            BuyerOrderDetailActionButtonKey.FINISH_ORDER, BuyerOrderDetailActionButtonKey.RECEIVE_CONFIRMATION -> {
//                onReceiveConfirmationActionButtonClicked(button)
//                BuyerOrderDetailTrackerConstant.BUTTON_NAME_FINISH_ORDER
//            }
//            BuyerOrderDetailActionButtonKey.HELP -> {
//                onHelpActionButtonClicked(button)
//                BuyerOrderDetailTrackerConstant.BUTTON_NAME_HELP
//            }
//            BuyerOrderDetailActionButtonKey.BUY_AGAIN -> {
//                onBuyAgainAllProductButtonClicked()
//                trackBuyAgainProduct()
//                ""
//            }
//            BuyerOrderDetailActionButtonKey.GIVE_REVIEW -> {
//                onGiveReviewActionButtonClicked(button.url)
//                BuyerOrderDetailTrackerConstant.BUTTON_NAME_REVIEW_ORDER
//            }
//            BuyerOrderDetailActionButtonKey.ORDER_EXTENSION -> {
//                trackRespondToSubmissionOrderExtensionClicked()
//                onRespondToSubmissionOrderExtensionClicked()
//                ""
//            }
//            else -> ""
//        }
        onRespondToSubmissionOrderExtensionClicked()
//        if (buttonName.isNotBlank()) {
//            trackClickActionButton(isFromPrimaryButton, buttonName)
//        }
    }

    private fun trackRespondToSubmissionOrderExtensionClicked() {
        BuyerOrderExtensionTracker.eventClickConfirmationOrderExtension(viewModel.getOrderId())
    }

    private fun onRespondToSubmissionOrderExtensionClicked() {
        navigator.goToOrderExtension(viewModel.getOrderId())
    }

    private fun onAskSellerActionButtonClicked() {
        viewModel.buyerOrderDetailResult.value?.let {
            if (it is Success) {
                navigator.goToAskSeller(it.data)
            }
        }
    }

    private fun onRequestCancelActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        navigator.goToRequestCancellationPage(
            viewModel.buyerOrderDetailResult.value,
            button,
            cacheManager
        )
    }

    private fun onTrackShipmentActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        viewModel.buyerOrderDetailResult.value?.let {
            if (it is Success) {
                val newUrl = button.url.substringAfter("url=", "")
                navigator.goToTrackShipmentPage(
                    it.data.orderStatusUiModel.orderStatusHeaderUiModel.orderId,
                    newUrl
                )
            }
        }
    }

    private fun onComplaintActionButtonClicked(complaintUrl: String) {
        navigator.goToCreateResolution(complaintUrl)
    }

    private fun onViewComplaintActionButtonClicked(url: String) {
        navigator.openAppLink(url, false)
        viewModel.buyerOrderDetailResult.value.let {
            if (it is Success) {
                BuyerOrderDetailTracker.eventClickSeeComplaint(
                    it.data.orderStatusUiModel.orderStatusHeaderUiModel.orderStatusId,
                    it.data.orderStatusUiModel.orderStatusHeaderUiModel.orderId
                )
            }
        }
    }

    private fun onReceiveConfirmationActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        bottomSheetManager.showReceiveConfirmationBottomSheet(
            button,
            bottomSheetManager,
            navigator,
            viewModel
        )
    }

    private fun onHelpActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        navigator.openAppLink(button.url, false)
    }

    private fun onBuyAgainAllProductButtonClicked() {
        viewModel.addMultipleToCart()
    }

    private fun onGiveReviewActionButtonClicked(url: String) {
        navigator.openAppLink(url, true)
    }

    private fun trackBuyAgainProduct() {
        BuyerOrderDetailTracker.eventClickBuyAgain(
            viewModel.getOrderId(),
            viewModel.getUserId()
        )
    }

    private fun trackClickActionButton(fromPrimaryButton: Boolean, buttonName: String) {
        viewModel.buyerOrderDetailResult.value?.let {
            if (it is Success) {
                BuyerOrderDetailTracker.eventClickActionButton(
                    isPrimaryButton = fromPrimaryButton,
                    buttonName = buttonName,
                    orderId = it.data.orderStatusUiModel.orderStatusHeaderUiModel.orderId,
                    orderStatusCode = it.data.orderStatusUiModel.orderStatusHeaderUiModel.orderStatusId
                )
            }
        }
    }
}