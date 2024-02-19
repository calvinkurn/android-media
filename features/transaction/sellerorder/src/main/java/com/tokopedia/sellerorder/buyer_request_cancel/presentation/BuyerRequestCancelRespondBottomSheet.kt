package com.tokopedia.sellerorder.buyer_request_cancel.presentation

import android.content.Context
import android.view.View
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.databinding.BottomsheetBuyerRequestCancelOrderBinding

class BuyerRequestCancelRespondBottomSheet(
    context: Context
) : SomBottomSheet<BottomsheetBuyerRequestCancelOrderBinding>(
    childViewsLayoutResourceId = LAYOUT,
    showOverlay = true,
    showCloseButton = true,
    showKnob = false,
    clearPadding = false,
    draggable = false,
    bottomSheetTitle = context.getString(R.string.som_request_cancel_bottomsheet_title),
    context = context,
    dismissOnClickOverlay = true
) {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_buyer_request_cancel_order
    }

    private var _listener: IBuyerRequestCancelRespondListener? = null

    override fun bind(view: View): BottomsheetBuyerRequestCancelOrderBinding {
        return BottomsheetBuyerRequestCancelOrderBinding.bind(view)
    }

    override fun setupChildView() {
        binding?.tickerPerformanceInfo?.setTextDescription(context.getString(R.string.som_shop_performance_info))
    }

    private fun setupBuyerRequestCancelBottomSheetButtons(
        reasonBuyer: String,
        statusCode: Int,
        primaryButtonText: String,
        secondaryButtonText: String
    ) {
        binding?.run {
            btnNegative.text = secondaryButtonText
            btnPositive.text = primaryButtonText
            btnNegative.setOnClickListener {
                showBuyerRequestCancelOnClickButtonDialog(
                    title = getBuyerRequestCancellationPopupTitle(statusCode),
                    description = getBuyerRequestCancellationPopUpDescription(statusCode),
                    primaryButtonText = getBuyerRequestCancellationRejectButton(statusCode),
                    secondaryButtonText = context.getString(R.string.som_buyer_cancellation_cancel_button),
                    primaryButtonClickAction = {
                        when (statusCode) {
                            SomConsts.STATUS_CODE_ORDER_CREATED -> _listener?.onBuyerRequestCancelRespondAcceptOrder()
                            SomConsts.STATUS_CODE_ORDER_ORDER_CONFIRMED,
                            SomConsts.STATUS_CODE_WAITING_PICKUP,
                            SomConsts.STATUS_CODE_READY_TO_SEND,
                            SomConsts.STATUS_CODE_RECEIPT_CHANGED -> _listener?.onBuyerRequestCancelRespondRejectCancelRequest()
                        }
                    }
                )
            }
            btnPositive.setOnClickListener {
                showPositiveButtonBuyerRequestCancelOnClickButtonDialog(
                    reasonBuyer,
                    statusCode
                )
            }
        }
    }

    private fun getBuyerRequestCancellationRejectButton(statusCode: Int): String {
        return when (statusCode) {
            SomConsts.STATUS_CODE_ORDER_CREATED -> context.getString(R.string.som_buyer_cancellation_confirm_accept_order_button)
            SomConsts.STATUS_CODE_ORDER_ORDER_CONFIRMED -> context.getString(R.string.som_buyer_cancellation_confirm_shipping_button)
            SomConsts.STATUS_CODE_WAITING_PICKUP,
            SomConsts.STATUS_CODE_READY_TO_SEND,
            SomConsts.STATUS_CODE_RECEIPT_CHANGED -> context.getString(R.string.som_request_cancellation_btn_primary_continue_send_order_dialog)
            else -> ""
        }
    }

    private fun getBuyerRequestCancellationPopUpDescription(statusCode: Int): String {
        return when (statusCode) {
            SomConsts.STATUS_CODE_ORDER_CREATED -> context.getString(R.string.som_buyer_cancellation_confirm_accept_order_description)
            SomConsts.STATUS_CODE_ORDER_ORDER_CONFIRMED -> context.getString(R.string.som_buyer_cancellation_confirm_shipping_description)
            SomConsts.STATUS_CODE_WAITING_PICKUP,
            SomConsts.STATUS_CODE_READY_TO_SEND,
            SomConsts.STATUS_CODE_RECEIPT_CHANGED -> context.getString(R.string.som_request_cancellation_desc_continue_send_order_dialog)
            else -> ""
        }
    }

    private fun getBuyerRequestCancellationPopupTitle(statusCode: Int): String {
        return when (statusCode) {
            SomConsts.STATUS_CODE_ORDER_CREATED -> context.getString(R.string.som_buyer_cancellation_confirm_accept_order_title)
            SomConsts.STATUS_CODE_ORDER_ORDER_CONFIRMED -> context.getString(R.string.som_buyer_cancellation_confirm_shipping_title)
            SomConsts.STATUS_CODE_WAITING_PICKUP,
            SomConsts.STATUS_CODE_READY_TO_SEND,
            SomConsts.STATUS_CODE_RECEIPT_CHANGED -> context.getString(R.string.som_request_cancellation_title_continue_send_order_dialog)
            else -> ""
        }
    }

    private fun showBuyerRequestCancelOnClickButtonDialog(
        title: String,
        description: String,
        primaryButtonText: String,
        secondaryButtonText: String,
        primaryButtonClickAction: () -> Unit
    ) {
        DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            if (DeviceScreenInfo.isTablet(context)) {
                dialogMaxWidth = getScreenWidth() / 2
            }
            setTitle(title)
            setDescription(description)
            setPrimaryCTAText(primaryButtonText)
            setSecondaryCTAText(secondaryButtonText)
            setPrimaryCTAClickListener {
                primaryButtonClickAction()
                dismiss()
            }
            setSecondaryCTAClickListener { dismiss() }
            show()
        }
    }

    private fun showPositiveButtonBuyerRequestCancelOnClickButtonDialog(
        reasonBuyer: String,
        orderStatusCode: Int
    ) {
        showBuyerRequestCancelOnClickButtonDialog(
            title = context.getString(R.string.som_buyer_cancellation_confirm_accept_cancellation_title),
            description = getDescPositiveButtonBuyerRequestCancelOnClickButtonDialog(orderStatusCode),
            primaryButtonText = context.getString(R.string.som_buyer_cancellation_confirm_accept_cancellation_button),
            secondaryButtonText = context.getString(R.string.som_buyer_cancellation_cancel_button),
            primaryButtonClickAction = {
                _listener?.onBuyerRequestCancelRespondRejectOrder(reasonBuyer)
            }
        )
    }

    private fun getDescPositiveButtonBuyerRequestCancelOnClickButtonDialog(orderStatusCode: Int): String {
        return when (orderStatusCode) {
            SomConsts.STATUS_CODE_WAITING_PICKUP,
            SomConsts.STATUS_CODE_READY_TO_SEND,
            SomConsts.STATUS_CODE_RECEIPT_CHANGED -> context.getString(R.string.som_request_cancellation_desc_confirm_cancellation_order_dialog)
            else -> context.getString(R.string.som_buyer_cancellation_confirm_accept_cancellation_description)
        }
    }

    fun init(
        reason: String,
        orderStatusCode: Int,
        description: String,
        primaryButtonText: String,
        secondaryButtonText: String
    ) {
        binding?.run {
            tvBuyerRequestCancel.text = description
            tvBuyerRequestCancelNotes.text = reason
                .replace(
                    oldValue = "\\n",
                    newValue = System.getProperty("line.separator").orEmpty()
                )
            setupBuyerRequestCancelBottomSheetButtons(
                reason,
                orderStatusCode,
                primaryButtonText,
                secondaryButtonText
            )
        }
    }

    fun setListener(listener: IBuyerRequestCancelRespondListener) {
        _listener = listener
    }
}
