package com.tokopedia.sellerorder.common.presenter.bottomsheet

import android.content.Context
import android.view.View
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.sellerorder.common.presenter.model.PopUp
import com.tokopedia.sellerorder.common.util.SomConsts
import kotlinx.android.synthetic.main.bottomsheet_buyer_request_cancel_order.view.*

class SomOrderRequestCancelBottomSheet(
        context: Context
) : SomBottomSheet(LAYOUT, true, true, false, context.getString(R.string.som_request_cancel_bottomsheet_title), context, true) {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_buyer_request_cancel_order
    }

    private var listener: SomOrderRequestCancelBottomSheetListener? = null

    override fun setupChildView() {
        childViews?.tickerPerformanceInfo?.setTextDescription(context.getString(R.string.som_shop_performance_info))
    }

    private fun setupBuyerRequestCancelBottomSheetButtons(
            view: View,
            reasonBuyer: String,
            actionButtons: List<PopUp.ActionButton>,
            statusCode: Int
    ) {
        with(view) {
            when (statusCode) {
                SomConsts.STATUS_CODE_ORDER_CREATED, SomConsts.STATUS_CODE_ORDER_ORDER_CONFIRMED -> {
                    val primaryButtonText = actionButtons.find {
                        it.type == SomConsts.KEY_PRIMARY_DIALOG_BUTTON
                    }?.displayName.orEmpty()
                    val secondaryButtonText = actionButtons.find {
                        it.type == SomConsts.KEY_SECONDARY_DIALOG_BUTTON
                    }?.displayName.orEmpty()
                    btnNegative?.text = secondaryButtonText
                    btnPositive?.text = primaryButtonText
                    btnNegative?.setOnClickListener {
                        showBuyerRequestCancelOnClickButtonDialog(
                                title = getBuyerRequestCancellationPopupTitle(statusCode),
                                description = getBuyerRequestCancellationPopUpDescription(statusCode),
                                primaryButtonText = getBuyerRequestCancellationRejectButton(statusCode),
                                secondaryButtonText = context.getString(R.string.som_buyer_cancellation_cancel_button),
                                primaryButtonClickAction = {
                                    dismiss()
                                    when (statusCode) {
                                        SomConsts.STATUS_CODE_ORDER_CREATED -> {
                                            listener?.onAcceptOrder(primaryButtonText)
                                        }
                                        SomConsts.STATUS_CODE_ORDER_ORDER_CONFIRMED -> {
                                            listener?.onRejectCancelRequest()
                                        }
                                    }
                                }
                        )
                    }
                    btnPositive?.setOnClickListener {
                        showPositiveButtonBuyerRequestCancelOnClickButtonDialog(reasonBuyer)
                    }
                }
                else -> containerButtonBuyerRequestCancel?.gone()
            }
        }
    }

    private fun getBuyerRequestCancellationRejectButton(statusCode: Int): String {
        return when (statusCode) {
            SomConsts.STATUS_CODE_ORDER_CREATED -> context.getString(R.string.som_buyer_cancellation_confirm_accept_order_button)
            SomConsts.STATUS_CODE_ORDER_ORDER_CONFIRMED -> context.getString(R.string.som_buyer_cancellation_confirm_shipping_button)
            else -> ""
        }
    }

    private fun getBuyerRequestCancellationPopUpDescription(statusCode: Int): String {
        return when (statusCode) {
            SomConsts.STATUS_CODE_ORDER_CREATED -> context.getString(R.string.som_buyer_cancellation_confirm_accept_order_description)
            SomConsts.STATUS_CODE_ORDER_ORDER_CONFIRMED -> context.getString(R.string.som_buyer_cancellation_confirm_shipping_description)
            else -> ""
        }
    }

    private fun getBuyerRequestCancellationPopupTitle(statusCode: Int): String {
        return when (statusCode) {
            SomConsts.STATUS_CODE_ORDER_CREATED -> context.getString(R.string.som_buyer_cancellation_confirm_accept_order_title)
            SomConsts.STATUS_CODE_ORDER_ORDER_CONFIRMED -> context.getString(R.string.som_buyer_cancellation_confirm_shipping_title)
            else -> ""
        }
    }

    private fun showBuyerRequestCancelOnClickButtonDialog(
            title: String,
            description: String,
            primaryButtonText: String,
            secondaryButtonText: String,
            primaryButtonClickAction: () -> Unit) {
        context?.run {
            DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
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
    }

    private fun showPositiveButtonBuyerRequestCancelOnClickButtonDialog(reasonBuyer: String) {
        showBuyerRequestCancelOnClickButtonDialog(
                title = context.getString(R.string.som_buyer_cancellation_confirm_accept_cancellation_title),
                description = context.getString(R.string.som_buyer_cancellation_confirm_accept_cancellation_description),
                primaryButtonText = context.getString(R.string.som_buyer_cancellation_confirm_accept_cancellation_button),
                secondaryButtonText = context.getString(R.string.som_buyer_cancellation_cancel_button),
                primaryButtonClickAction = {
                    dismiss()
                    listener?.onRejectOrder(reasonBuyer)
                }
        )
    }

    fun init(popUp: PopUp, reason: String, orderStatusCode: Int) {
        childViews?.apply {
            tv_buyer_request_cancel?.text = popUp.body
            tvBuyerRequestCancelNotes?.text = reason.replace("\\n", System.getProperty("line.separator") ?: "")
            setupBuyerRequestCancelBottomSheetButtons(this, reason, popUp.actionButtons, orderStatusCode)
        }
    }

    fun setListener(listener: SomOrderRequestCancelBottomSheetListener) {
        this.listener = listener
    }

    interface SomOrderRequestCancelBottomSheetListener {
        fun onAcceptOrder(actionName: String)
        fun onRejectOrder(reasonBuyer: String)
        fun onRejectCancelRequest()
    }
}