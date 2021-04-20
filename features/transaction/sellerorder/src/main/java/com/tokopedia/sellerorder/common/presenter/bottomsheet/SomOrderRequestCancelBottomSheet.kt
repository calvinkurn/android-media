package com.tokopedia.sellerorder.common.presenter.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.model.PopUp
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_buyer_request_cancel_order.view.*

class SomOrderRequestCancelBottomSheet : BottomSheetUnify() {

    companion object {
        const val TAG = "SomOrderRequestCancelBottomSheet"
    }

    private var listener: SomOrderRequestCancelBottomSheetListener? = null
    private var popUp: PopUp? = null

    private var cancelReason: String = ""
    private var orderStatusCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFullpage = false
        setTitle(getString(R.string.som_request_cancel_bottomsheet_title))
        val childViews = View.inflate(context, R.layout.bottomsheet_buyer_request_cancel_order, null).apply {
            tickerPerformanceInfo?.setTextDescription(getString(R.string.som_shop_performance_info))
            tv_buyer_request_cancel?.text = popUp?.body.orEmpty()
            tvBuyerRequestCancelNotes?.text = cancelReason.replace("\\n", System.getProperty("line.separator") ?: "")
            setupBuyerRequestCancelBottomSheetButtons(this, cancelReason, popUp?.actionButtons.orEmpty(), orderStatusCode)
        }
        setChild(childViews)
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
                                secondaryButtonText = getString(R.string.som_buyer_cancellation_cancel_button),
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
            SomConsts.STATUS_CODE_ORDER_CREATED -> getString(R.string.som_buyer_cancellation_confirm_accept_order_button)
            SomConsts.STATUS_CODE_ORDER_ORDER_CONFIRMED -> getString(R.string.som_buyer_cancellation_confirm_shipping_button)
            else -> ""
        }
    }

    private fun getBuyerRequestCancellationPopUpDescription(statusCode: Int): String {
        return when (statusCode) {
            SomConsts.STATUS_CODE_ORDER_CREATED -> getString(R.string.som_buyer_cancellation_confirm_accept_order_description)
            SomConsts.STATUS_CODE_ORDER_ORDER_CONFIRMED -> getString(R.string.som_buyer_cancellation_confirm_shipping_description)
            else -> ""
        }
    }

    private fun getBuyerRequestCancellationPopupTitle(statusCode: Int): String {
        return when (statusCode) {
            SomConsts.STATUS_CODE_ORDER_CREATED -> getString(R.string.som_buyer_cancellation_confirm_accept_order_title)
            SomConsts.STATUS_CODE_ORDER_ORDER_CONFIRMED -> getString(R.string.som_buyer_cancellation_confirm_shipping_title)
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
                title = getString(R.string.som_buyer_cancellation_confirm_accept_cancellation_title),
                description = getString(R.string.som_buyer_cancellation_confirm_accept_cancellation_description),
                primaryButtonText = getString(R.string.som_buyer_cancellation_confirm_accept_cancellation_button),
                secondaryButtonText = getString(R.string.som_buyer_cancellation_cancel_button),
                primaryButtonClickAction = {
                    dismiss()
                    listener?.onRejectOrder(reasonBuyer)
                }
        )
    }

    fun init(popUp: PopUp, reason: String, orderStatusCode: Int) {
        this.popUp = popUp
        this.cancelReason = reason
        this.orderStatusCode = orderStatusCode
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