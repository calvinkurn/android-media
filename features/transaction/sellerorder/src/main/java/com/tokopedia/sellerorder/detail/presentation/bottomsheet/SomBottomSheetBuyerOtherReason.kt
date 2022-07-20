package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.databinding.BottomsheetSecondaryBinding
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.unifycomponents.ticker.Ticker

class SomBottomSheetBuyerOtherReason(
        context: Context,
        private var rejectReason: SomReasonRejectData.Data.SomRejectReason,
        private var orderId: String,
        private val listener: SomRejectOrderBottomSheetListener
) : SomBaseRejectOrderBottomSheet<BottomsheetSecondaryBinding>(context, LAYOUT, SomConsts.VALUE_REASON_OTHER) {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_secondary
    }

    override fun bind(view: View): BottomsheetSecondaryBinding {
        return BottomsheetSecondaryBinding.bind(view)
    }

    override fun setupChildView() {
        binding?.run {
            rvBottomsheetSecondary.gone()
            tfExtraNotes.visible()
            tfExtraNotes.setLabelStatic(true)
            tfExtraNotes.textFiedlLabelText.text = context.getString(R.string.other_reason_resp_label)
            tfExtraNotes.setPlaceholder(context.getString(R.string.other_reason_resp_placeholder))
            tfExtraNotes.textFieldInput.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    btnPrimary.isEnabled = !s.isNullOrBlank()
                }
            })
            btnPrimary.visible()
            btnPrimary.setOnClickListener {
                dismiss()
                val orderRejectRequest = SomRejectRequestParam().apply {
                    orderId = this@SomBottomSheetBuyerOtherReason.orderId
                    rCode = rejectReason.reasonCode.toString()
                    reason = tfExtraNotes.textFieldInput.text.toString()
                }
                if (checkReasonRejectIsNotEmpty(tfExtraNotes.textFieldInput.text.toString())) {
                    listener.onDoRejectOrder(orderRejectRequest)
                } else {
                    showToasterError(context.getString(R.string.cancel_order_notes_empty_warning))
                }
            }
            hideKeyboardHandler.attachListener(btnPrimary)
        }
    }

    override fun show() {
        reset()
        super.show()
    }

    private fun reset() {
        binding?.tfExtraNotes?.textFieldInput?.setText("")
    }

    private fun setupTicker() {
        binding?.run {
            if (rejectReason.reasonTicker.isNotEmpty()) {
                tickerPenaltySecondary.visible()
                tickerPenaltySecondary.tickerType = Ticker.TYPE_ANNOUNCEMENT
                tickerPenaltySecondary.setHtmlDescription(rejectReason.reasonTicker)
            } else {
                tickerPenaltySecondary.gone()
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