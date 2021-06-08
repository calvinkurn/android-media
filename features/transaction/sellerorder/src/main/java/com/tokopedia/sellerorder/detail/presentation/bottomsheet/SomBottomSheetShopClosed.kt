package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.FragmentManager
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.convertFormatDate
import com.tokopedia.kotlin.extensions.convertMonth
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.bottomsheet_cancel_order.view.*
import kotlinx.android.synthetic.main.bottomsheet_secondary.view.*
import kotlinx.android.synthetic.main.bottomsheet_shop_closed.view.*
import java.util.*

class SomBottomSheetShopClosed(
        context: Context,
        private val childFragmentManager: FragmentManager,
        private var rejectReason: SomReasonRejectData.Data.SomRejectReason,
        private var orderId: String,
        private val listener: SomRejectOrderBottomSheetListener
) : SomBaseRejectOrderBottomSheet(context, LAYOUT, SomConsts.TITLE_ATUR_TOKO_TUTUP) {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_shop_closed
    }

    private var defaultDateNow: String = ""

    override fun setupChildView() {
        childViews?.run {
            // setup closing end date
            tf_end_shop_closed?.textFieldWrapper?.hint = context.getString(R.string.end_shop_closed_label)
            tf_end_shop_closed?.textFieldInput?.apply {
                setText(defaultDateNow)
            }
            tf_end_shop_closed?.textFieldInput?.isEnabled = false
            tf_end_shop_closed?.setFirstIcon(R.drawable.ic_som_filter_calendar)
            tf_end_shop_closed?.textFieldIcon1?.setOnClickListener {
                showDatePicker(tf_end_shop_closed)
            }

            // setup closing additional notes
            tf_shop_closed_notes?.setLabelStatic(true)
            tf_shop_closed_notes?.textFiedlLabelText?.text = context.getString(R.string.shop_closed_note_label)
            tf_shop_closed_notes?.textFieldInput?.hint = context.getString(R.string.shop_closed_note_placeholder)
            tf_shop_closed_notes?.textFieldInput?.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    childViews?.btn_reject_shop_closed?.isEnabled = !s.isNullOrBlank()
                }
            })

            btn_reject_shop_closed?.setOnClickListener {
                dismiss()
                val orderRejectRequest = SomRejectRequestParam(
                        orderId = this@SomBottomSheetShopClosed.orderId,
                        rCode = rejectReason.reasonCode.toString(),
                        closedNote = tf_shop_closed_notes?.textFieldInput?.text.toString(),
                        closeEnd = tf_end_shop_closed?.textFieldInput?.text.toString()
                )
                if (checkReasonRejectIsNotEmpty(tf_shop_closed_notes?.textFieldInput?.text?.toString())) {
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
        val now = Calendar.getInstance()
        defaultDateNow = now.time.toFormattedString("dd/MM/yyyy")
        updateClosingEndDate("${now.time.toFormattedString("dd")} ${convertMonth(now.get(Calendar.MONTH))} ${now.time.toFormattedString("yyyy")}")
        childViews?.tf_shop_closed_notes?.textFieldInput?.setText("")
        childViews?.tf_end_shop_closed?.textFieldInput?.setText(defaultDateNow)
        childViews?.btn_reject_shop_closed?.isEnabled = false
        setupStartDate()
    }

    private fun setupStartDate() {
        // setup closing start date
        childViews?.tf_start_shop_closed?.textFieldWrapper?.hint = context.getString(R.string.start_shop_closed_label)
        childViews?.tf_start_shop_closed?.textFieldInput?.setText(defaultDateNow)
        childViews?.tf_start_shop_closed?.textFieldInput?.isEnabled = false
    }

    private fun setupTicker() {
        childViews?.run {
            if (rejectReason.reasonTicker.isNotEmpty()) {
                ticker_penalty_shop_closed?.visible()
                ticker_penalty_shop_closed?.tickerType = Ticker.TYPE_ANNOUNCEMENT
                ticker_penalty_shop_closed?.setHtmlDescription(rejectReason.reasonTicker)
            } else {
                ticker_penalty_shop_closed?.gone()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker(tfEndShopClosed: TextFieldUnify) {
        val dateNow = Calendar.getInstance()
        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.YEAR, 100)

        val datePicker = DateTimePickerUnify(
                context = context,
                minDate = dateNow,
                defaultDate = dateNow,
                maxDate = maxDate,
                type = DateTimePickerUnify.TYPE_DATEPICKER)
        datePicker.setTitle(context.getString(R.string.end_shop_closed_label))
        datePicker.show(childFragmentManager, "")
        datePicker.datePickerButton.setOnClickListener {
            val resultDate = datePicker.getDate()
            tfEndShopClosed.textFieldInput.setText(resultDate.time.toFormattedString("dd/MM/yyyy"))
            updateClosingEndDate("${resultDate.time.toFormattedString("dd")} ${convertMonth(resultDate.get(Calendar.MONTH))} ${resultDate.time.toFormattedString("yyyy")}")
            datePicker.dismiss()
        }
        datePicker.setCloseClickListener { datePicker.dismiss() }
    }

    private fun updateClosingEndDate(endDate: String) {
        val endNote1 = context.getString(R.string.shop_closed_endnote1)
        val endNote2 = context.getString(R.string.shop_closed_endnote2)
        val endNote3 = context.getString(R.string.shop_closed_endnote3)

        val customString = HtmlLinkHelper(context, "$endNote1 <b>$endNote2</b>$endNote3 $endDate")
        childViews?.shop_closed_endnotes?.text = customString.spannedString
    }

    fun setOrderId(orderId: String) {
        this.orderId = orderId
    }

    fun setRejectReason(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        this.rejectReason = rejectReason
        setupTicker()
    }
}