package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.bottomsheet_secondary.view.*

class SomBottomSheetBuyerOtherReason(
        context: Context,
        private var rejectReason: SomReasonRejectData.Data.SomRejectReason,
        private var orderId: String,
        private val listener: SomRejectOrderBottomSheetListener
) : SomBaseRejectOrderBottomSheet(context, LAYOUT, SomConsts.VALUE_REASON_OTHER) {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_secondary
    }

    override fun setupChildView() {
        childViews?.run {
            rv_bottomsheet_secondary?.gone()
            tf_extra_notes?.visible()
            tf_extra_notes?.setLabelStatic(true)
            tf_extra_notes?.textFiedlLabelText?.text = context.getString(R.string.other_reason_resp_label)
            tf_extra_notes?.setPlaceholder(context.getString(R.string.other_reason_resp_placeholder))
            fl_btn_primary?.visible()
            fl_btn_primary?.setOnClickListener {
                dismiss()
                val orderRejectRequest = SomRejectRequestParam().apply {
                    orderId = this@SomBottomSheetBuyerOtherReason.orderId
                    rCode = rejectReason.reasonCode.toString()
                    reason = tf_extra_notes?.textFieldInput?.text.toString()
                }
                if (checkReasonRejectIsNotEmpty(tf_extra_notes?.textFieldInput?.text?.toString())) {
                    listener.onDoRejectOrder(orderRejectRequest)
                } else {
                    showToasterError(context.getString(R.string.cancel_order_notes_empty_warning))
                }
            }
        }
    }

    override fun show() {
        reset()
        super.show()
    }

    private fun reset() {
        childViews?.tf_extra_notes?.textFieldInput?.setText("")
    }

    private fun setupTicker() {
        childViews?.run {
            if (rejectReason.reasonTicker.isNotEmpty()) {
                ticker_penalty_secondary?.visible()
                ticker_penalty_secondary?.tickerType = Ticker.TYPE_ANNOUNCEMENT
                ticker_penalty_secondary?.setHtmlDescription(rejectReason.reasonTicker)
            } else {
                ticker_penalty_secondary?.gone()
            }
        }
    }

    fun setOrderId(orderId: String) {
        this.orderId = orderId
    }

    fun setRejectReason(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        this.rejectReason = rejectReason
        setupTicker()
    }
}