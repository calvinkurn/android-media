package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.Utils.hideKeyboard
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.bottomsheet_secondary.view.*

class SomBottomSheetCourierProblem(
        context: Context,
        private var rejectReason: SomReasonRejectData.Data.SomRejectReason,
        private var orderId: String,
        private val listener: SomRejectOrderBottomSheetListener
) : SomBaseRejectOrderBottomSheet(context, LAYOUT, SomConsts.TITLE_COURIER_PROBLEM), SomBottomSheetCourierProblemsAdapter.ActionListener {

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
                childViews?.btn_primary?.isEnabled = !s.isNullOrBlank()
            }
        }
    }

    override fun setupChildView() {
        childViews?.run {
            setCourierProblemOptions()
            rv_bottomsheet_secondary?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = somBottomSheetCourierProblemsAdapter
            }

            setupTicker()

            btn_primary?.show()
            btn_primary?.setOnClickListener {
                dismiss()
                val orderRejectRequest = SomRejectRequestParam()
                orderRejectRequest.orderId = orderId
                orderRejectRequest.rCode = rejectReason.reasonCode.toString()

                if (tf_extra_notes?.visibility == View.VISIBLE) {
                    orderRejectRequest.reason = tf_extra_notes?.textFieldInput?.text?.toString().orEmpty()
                    if (checkReasonRejectIsNotEmpty(tf_extra_notes?.textFieldInput?.text?.toString())) {
                        listener.onDoRejectOrder(orderRejectRequest)
                    } else {
                        showToasterError(context.getString(R.string.cancel_order_notes_empty_warning))
                    }
                } else {
                    orderRejectRequest.reason = reasonCourierProblemText
                    listener.onDoRejectOrder(orderRejectRequest)
                }
            }
            btn_primary?.setOnTouchListener(hideKeyboardTouchListener)
        }
    }

    override fun show() {
        reset()
        super.show()
    }

    override fun onChooseOptionCourierProblem(optionCourierProblem: SomReasonRejectData.Data.SomRejectReason.Child) {
        childViews?.run {
            if (optionCourierProblem.reasonText.equals(SomConsts.VALUE_COURIER_PROBLEM_OTHERS, ignoreCase = true)) {
                tf_extra_notes?.visible()
                tf_extra_notes?.setLabelStatic(true)
                tf_extra_notes?.setPlaceholder(context.getString(R.string.placeholder_reject_reason))
                tf_extra_notes?.textFieldInput?.addTextChangedListener(cancelReasonTextWatcher)
                childViews?.btn_primary?.isEnabled = !tf_extra_notes?.textFieldInput?.text.isNullOrBlank()
            } else {
                if (tf_extra_notes?.visibility == View.VISIBLE) {
                    tf_extra_notes?.textFieldInput?.removeTextChangedListener(cancelReasonTextWatcher)
                }
                reasonCourierProblemText = optionCourierProblem.reasonText
                tf_extra_notes?.gone()
                childViews?.btn_primary?.isEnabled = true
            }
            hideKeyboard()
        }
    }

    private fun reset() {
        reasonCourierProblemText = ""
        childViews?.tf_extra_notes?.textFieldInput?.setText("")
        childViews?.tf_extra_notes?.gone()
        childViews?.btn_primary?.isEnabled = false
        somBottomSheetCourierProblemsAdapter.selectedRadio = -1
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