package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.Utils.hideKeyboard
import com.tokopedia.sellerorder.databinding.BottomsheetSecondaryBinding
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.unifycomponents.ticker.Ticker

class SomBottomSheetCourierProblem(
        context: Context,
        private var rejectReason: SomReasonRejectData.Data.SomRejectReason,
        private var orderId: String,
        private val listener: SomRejectOrderBottomSheetListener
) : SomBaseRejectOrderBottomSheet<BottomsheetSecondaryBinding>(context, LAYOUT, SomConsts.TITLE_COURIER_PROBLEM), SomBottomSheetCourierProblemsAdapter.ActionListener {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_secondary
    }

    private var somBottomSheetCourierProblemsAdapter: SomBottomSheetCourierProblemsAdapter = SomBottomSheetCourierProblemsAdapter(this)
    private var reasonCourierProblemText: String = ""
    private val cancelReasonTextWatcher by lazy {
        object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding?.btnPrimary?.isEnabled = !s.isNullOrBlank()
            }
        }
    }

    override fun bind(view: View): BottomsheetSecondaryBinding {
        return BottomsheetSecondaryBinding.bind(view)
    }

    override fun setupChildView() {
        binding?.run {
            setCourierProblemOptions()
            rvBottomsheetSecondary.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = somBottomSheetCourierProblemsAdapter
            }

            setupTicker()

            btnPrimary.show()
            btnPrimary.setOnClickListener {
                dismiss()
                val orderRejectRequest = SomRejectRequestParam()
                orderRejectRequest.orderId = orderId
                orderRejectRequest.rCode = rejectReason.reasonCode.toString()

                if (tfExtraNotes.visibility == View.VISIBLE) {
                    orderRejectRequest.reason = tfExtraNotes.textFieldInput.text.toString()
                    if (checkReasonRejectIsNotEmpty(tfExtraNotes.textFieldInput.text?.toString())) {
                        listener.onDoRejectOrder(orderRejectRequest)
                    } else {
                        showToasterError(context.getString(R.string.cancel_order_notes_empty_warning))
                    }
                } else {
                    orderRejectRequest.reason = reasonCourierProblemText
                    listener.onDoRejectOrder(orderRejectRequest)
                }
            }
            hideKeyboardHandler.attachListener(btnPrimary)
        }
    }

    override fun show() {
        reset()
        super.show()
    }

    override fun onChooseOptionCourierProblem(optionCourierProblem: SomReasonRejectData.Data.SomRejectReason.Child) {
        binding?.run {
            if (optionCourierProblem.reasonText.equals(SomConsts.VALUE_COURIER_PROBLEM_OTHERS, ignoreCase = true)) {
                tfExtraNotes.visible()
                tfExtraNotes.setLabelStatic(true)
                tfExtraNotes.setPlaceholder(context.getString(R.string.placeholder_reject_reason))
                tfExtraNotes.textFieldInput.addTextChangedListener(cancelReasonTextWatcher)
                btnPrimary.isEnabled = !tfExtraNotes.textFieldInput.text.isNullOrBlank()
            } else {
                if (tfExtraNotes.visibility == View.VISIBLE) {
                    tfExtraNotes.textFieldInput.removeTextChangedListener(cancelReasonTextWatcher)
                }
                reasonCourierProblemText = optionCourierProblem.reasonText
                tfExtraNotes.gone()
                btnPrimary.isEnabled = true
            }
            root.hideKeyboard()
        }
    }

    private fun reset() {
        binding?.run {
            reasonCourierProblemText = ""
            tfExtraNotes.textFieldInput.setText("")
            tfExtraNotes.gone()
            btnPrimary.isEnabled = false
            somBottomSheetCourierProblemsAdapter.selectedRadio = RecyclerView.NO_POSITION
        }
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

    private fun setCourierProblemOptions() {
        somBottomSheetCourierProblemsAdapter.listChildCourierProblems = rejectReason.listChild.toMutableList()
        somBottomSheetCourierProblemsAdapter.notifyDataSetChanged()
    }

    fun setOrderId(orderId: String) {
        this.orderId = orderId
    }

    fun setRejectReason(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        this.rejectReason = rejectReason
        setupTicker()
        setCourierProblemOptions()
    }
}