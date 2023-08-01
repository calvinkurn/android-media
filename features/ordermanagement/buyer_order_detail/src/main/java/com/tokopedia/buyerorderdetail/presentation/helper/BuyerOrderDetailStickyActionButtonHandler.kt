package com.tokopedia.buyerorderdetail.presentation.helper

import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTrackerConstant
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderExtensionTracker
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailActionButtonKey
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.BuyerOrderDetailBottomSheetManager
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager

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
        when (button.key) {
            BuyerOrderDetailActionButtonKey.ASK_SELLER -> {
                onAskSellerActionButtonClicked()
                trackClickActionButtonSOM(
                    isFromPrimaryButton,
                    BuyerOrderDetailTrackerConstant.BUTTON_NAME_CHAT_SELLER
                )
            }
            BuyerOrderDetailActionButtonKey.REQUEST_CANCEL -> {
                onRequestCancelActionButtonClicked(button)
                trackClickActionButtonSOM(
                    isFromPrimaryButton,
                    BuyerOrderDetailTrackerConstant.BUTTON_NAME_CANCEL_ORDER
                )
            }
            BuyerOrderDetailActionButtonKey.TRACK_SHIPMENT -> {
                onTrackShipmentActionButtonClicked(button)
                trackClickActionButtonSOM(
                    isFromPrimaryButton,
                    BuyerOrderDetailTrackerConstant.BUTTON_NAME_TRACK_ORDER
                )
            }
            BuyerOrderDetailActionButtonKey.REQUEST_COMPLAINT -> {
                onComplaintActionButtonClicked(button.url)
                trackClickActionButtonSOM(
                    isFromPrimaryButton,
                    BuyerOrderDetailTrackerConstant.BUTTON_NAME_COMPLAINT_ORDER
                )
            }
            BuyerOrderDetailActionButtonKey.VIEW_COMPLAINT -> {
                onViewComplaintActionButtonClicked(button.url)
                if (!isFromPrimaryButton) {
                    trackClickActionButtonSOM(
                        false,
                        BuyerOrderDetailTrackerConstant.BUTTON_NAME_VIEW_COMPLAINT_ORDER
                    )
                }
            }
            BuyerOrderDetailActionButtonKey.FINISH_ORDER,
            BuyerOrderDetailActionButtonKey.RECEIVE_CONFIRMATION -> {
                onReceiveConfirmationActionButtonClicked(button)
                trackClickActionButtonSOM(
                    isFromPrimaryButton,
                    BuyerOrderDetailTrackerConstant.BUTTON_NAME_FINISH_ORDER
                )
            }
            BuyerOrderDetailActionButtonKey.HELP -> {
                onHelpActionButtonClicked(button)
                trackClickActionButtonSOM(
                    isFromPrimaryButton,
                    BuyerOrderDetailTrackerConstant.BUTTON_NAME_HELP
                )
            }
            BuyerOrderDetailActionButtonKey.BUY_AGAIN, BuyerOrderDetailActionButtonKey.REORDER -> {
                onBuyAgainAllProductButtonClicked()
                trackBuyAgainProduct()
            }
            BuyerOrderDetailActionButtonKey.GIVE_REVIEW -> {
                onGiveReviewActionButtonClicked(button.url)
                trackClickActionButtonSOM(
                    isFromPrimaryButton,
                    BuyerOrderDetailTrackerConstant.BUTTON_NAME_REVIEW_ORDER
                )
            }
            BuyerOrderDetailActionButtonKey.RESPONSE_EXTEND_ORDER -> {
                trackRespondToSubmissionOrderExtensionClicked()
                onRespondToSubmissionOrderExtensionClicked()
            }
            BuyerOrderDetailActionButtonKey.SEE_POD -> {
                onSeePODButtonClicked(button.url)
                trackClickActionButtonPG(
                    isFromPrimaryButton,
                    BuyerOrderDetailTrackerConstant.BUTTON_NAME_SEE_POD,
                    ""
                )
            }
            BuyerOrderDetailActionButtonKey.RE_UPLOAD_PRESCRIPTION -> {
                onReUploadPrescriptionClicked(button.url)
                trackClickActionButtonPG(
                    isFromPrimaryButton,
                    BuyerOrderDetailTrackerConstant.BUTTON_NAME_RE_UPLOAD_PRESCRIPTION,
                    BuyerOrderDetailTrackerConstant.TRACKER_ID_RE_UPLOAD_PRESCRIPTION
                )
            }
            BuyerOrderDetailActionButtonKey.CHECK_PRESCRIPTION -> {
                onCheckPrescriptionClicked(button.url)
                trackClickActionButtonPG(
                    isFromPrimaryButton,
                    BuyerOrderDetailTrackerConstant.BUTTON_NAME_CHECK_PRESCRIPTION,
                    BuyerOrderDetailTrackerConstant.TRACKER_ID_CHECK_PRESCRIPTION
                )
            }
            BuyerOrderDetailActionButtonKey.PARTIAL_ORDER_FULFILLMENT -> {
                onRespondToPartialOrderFulfillmentClicked()
                trackClickActionButtonPG(
                    isFromPrimaryButton,
                    BuyerOrderDetailTrackerConstant.BUTTON_NAME_CONFIRMATION_POF,
                    BuyerOrderDetailTrackerConstant.TRACKER_ID_CLICK_CONFIRMATION_POF
                )
            }
        }
    }

    private fun trackRespondToSubmissionOrderExtensionClicked() {
        BuyerOrderExtensionTracker.eventClickConfirmationOrderExtension(viewModel.getOrderId())
    }

    private fun onRespondToSubmissionOrderExtensionClicked() {
        navigator.goToOrderExtension(viewModel.getOrderId())
    }

    private fun onRespondToPartialOrderFulfillmentClicked() {
        navigator.goToPartialOrderFulfillment(viewModel.getOrderId())
    }

    private fun onReUploadPrescriptionClicked(url: String) {
        navigator.openAppLink(url, true)
    }

    private fun onCheckPrescriptionClicked(url: String) {
        navigator.openAppLink(url, false)
    }

    private fun onAskSellerActionButtonClicked() {
        viewModel.buyerOrderDetailUiState.value.let { uiState ->
            if (uiState is BuyerOrderDetailUiState.HasData) {
                navigator.goToAskSeller(
                    orderStatusUiModel = uiState.orderStatusUiState.data,
                    productListUiModel = uiState.productListUiState.data,
                    paymentInfoUiModel = uiState.paymentInfoUiState.data
                )
            }
        }
    }

    private fun onRequestCancelActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        navigator.goToRequestCancellationPage(
            viewModel.buyerOrderDetailUiState.value,
            button,
            cacheManager
        )
    }

    private fun onTrackShipmentActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        viewModel.buyerOrderDetailUiState.value.let { uiState ->
            if (uiState is BuyerOrderDetailUiState.HasData) {
                navigator.goToTrackShipmentPage(button.url)
            }
        }
    }

    private fun onComplaintActionButtonClicked(complaintUrl: String) {
        navigator.goToCreateResolution(complaintUrl)
    }

    private fun onViewComplaintActionButtonClicked(url: String) {
        navigator.openAppLink(url, false)
        viewModel.buyerOrderDetailUiState.value.let { uiState ->
            if (uiState is BuyerOrderDetailUiState.HasData) {
                BuyerOrderDetailTracker.eventClickSeeComplaint(
                    uiState.orderStatusUiState.data.orderStatusHeaderUiModel.orderStatusId,
                    uiState.orderStatusUiState.data.orderStatusHeaderUiModel.orderId
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

    private fun trackClickActionButtonSOM(fromPrimaryButton: Boolean, buttonName: String) {
        viewModel.buyerOrderDetailUiState.value.let { uiState ->
            if (uiState is BuyerOrderDetailUiState.HasData) {
                BuyerOrderDetailTracker.eventClickActionButtonSOM(
                    isPrimaryButton = fromPrimaryButton,
                    buttonName = buttonName,
                    orderId = uiState.orderStatusUiState.data.orderStatusHeaderUiModel.orderId,
                    orderStatusCode = uiState.orderStatusUiState.data.orderStatusHeaderUiModel.orderStatusId
                )
            }
        }
    }

    private fun trackClickActionButtonPG(
        fromPrimaryButton: Boolean,
        buttonName: String,
        trackerId: String
    ) {
        viewModel.buyerOrderDetailUiState.value.let { uiState ->
            if (uiState is BuyerOrderDetailUiState.HasData) {
                BuyerOrderDetailTracker.eventClickActionButtonPG(
                    isPrimaryButton = fromPrimaryButton,
                    buttonName = buttonName,
                    trackerId = trackerId,
                    orderId = uiState.orderStatusUiState.data.orderStatusHeaderUiModel.orderId,
                    orderStatusCode = uiState.orderStatusUiState.data.orderStatusHeaderUiModel.orderStatusId
                )
            }
        }
    }

    private fun onSeePODButtonClicked(url: String) {
        navigator.openAppLink(url, false)
    }
}
