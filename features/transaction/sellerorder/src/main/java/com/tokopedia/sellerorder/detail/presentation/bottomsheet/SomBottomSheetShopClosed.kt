package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.convertMonth
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.databinding.BottomsheetShopClosedBinding
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import java.util.*

class SomBottomSheetShopClosed(
        context: Context,
        private val childFragmentManager: FragmentManager,
        private var rejectReason: SomReasonRejectData.Data.SomRejectReason,
        private var orderId: String,
        private val listener: SomRejectOrderBottomSheetListener
) : SomBaseRejectOrderBottomSheet<BottomsheetShopClosedBinding>(context, LAYOUT, SomConsts.TITLE_ATUR_TOKO_TUTUP) {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_shop_closed
    }

    private var defaultDateNow: String = ""

    override fun bind(view: View): BottomsheetShopClosedBinding {
        return BottomsheetShopClosedBinding.bind(view)
    }

    override fun setupChildView() {
        binding?.run {
            // setup closing end date
            tfEndShopClosed.textFieldWrapper.hint = context.getString(R.string.end_shop_closed_label)
            tfEndShopClosed.textFieldInput.setText(defaultDateNow)
            tfEndShopClosed.textFieldInput.isEnabled = false
            tfEndShopClosed.setFirstIcon(R.drawable.ic_som_filter_calendar)
            tfEndShopClosed.textFieldIcon1.setOnClickListener {
                showDatePicker(tfEndShopClosed)
            }

            // setup closing additional notes
            tfShopClosedNotes.setLabelStatic(true)
            tfShopClosedNotes.textFiedlLabelText.text = context.getString(R.string.shop_closed_note_label)
            tfShopClosedNotes.textFieldInput.hint = context.getString(R.string.shop_closed_note_placeholder)
            tfShopClosedNotes.textFieldInput.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    btnRejectShopClosed.isEnabled = !s.isNullOrBlank()
                }
            })

            btnRejectShopClosed.setOnClickListener {
                dismiss()
                val orderRejectRequest = SomRejectRequestParam(
                        orderId = this@SomBottomSheetShopClosed.orderId,
                        rCode = rejectReason.reasonCode.toString(),
                        closedNote = tfShopClosedNotes.textFieldInput.text.toString(),
                        closeEnd = tfEndShopClosed.textFieldInput.text.toString()
                )
                if (checkReasonRejectIsNotEmpty(tfShopClosedNotes.textFieldInput.text.toString())) {
                    listener.onDoRejectOrder(orderRejectRequest)
                } else {
                    showToasterError(context.getString(R.string.cancel_order_notes_empty_warning))
                }
            }
            hideKeyboardHandler.attachListener(btnRejectShopClosed)
        }
    }

    override fun show() {
        reset()
        super.show()
    }

    private fun reset() {
        binding?.run {
            val now = Calendar.getInstance()
            defaultDateNow = now.time.toFormattedString("dd/MM/yyyy")
            updateClosingEndDate("${now.time.toFormattedString("dd")} ${convertMonth(now.get(Calendar.MONTH))} ${now.time.toFormattedString("yyyy")}")
            tfShopClosedNotes.textFieldInput.setText("")
            tfEndShopClosed.textFieldInput.setText(defaultDateNow)
            btnRejectShopClosed.isEnabled = false
            setupStartDate()
        }
    }

    private fun setupStartDate() {
        binding?.run {
            // setup closing start date
            tfStartShopClosed.textFieldWrapper.hint = context.getString(R.string.start_shop_closed_label)
            tfStartShopClosed.textFieldInput.setText(defaultDateNow)
            tfStartShopClosed.textFieldInput.isEnabled = false
        }
    }

    private fun setupTicker() {
        binding?.run {
            if (rejectReason.reasonTicker.isNotEmpty()) {
                tickerPenaltyShopClosed.visible()
                tickerPenaltyShopClosed.tickerType = Ticker.TYPE_ANNOUNCEMENT
                tickerPenaltyShopClosed.setHtmlDescription(rejectReason.reasonTicker)
            } else {
                tickerPenaltyShopClosed.gone()
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
        binding?.shopClosedEndnotes?.text = customString.spannedString
    }

    fun setOrderId(orderId: String) {
        this.orderId = orderId
    }

    fun setRejectReason(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        this.rejectReason = rejectReason
        setupTicker()
    }
}