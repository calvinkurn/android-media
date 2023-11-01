package com.tokopedia.privacycenter.ui.dsar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toCalendar
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.BottomSheetRangePickerBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toString
import java.util.*

class DsarHistoryTransactionBottomSheet :
    BottomSheetUnify() {

    private var rangePickerDialogBinding: BottomSheetRangePickerBinding? = null

    private var startDate: Date? = null
    private var endDate: Date? = null

    private var dismissDialogListener: (String, Boolean, Pair<Date, Date>?) -> Unit = {_,_,_ ->}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rangePickerDialogBinding = BottomSheetRangePickerBinding.inflate(LayoutInflater.from(requireContext()))
        setChild(rangePickerDialogBinding?.root)

        val currentDate = GregorianCalendar(LocaleUtils.getCurrentLocale(requireContext()))
        startDate = currentDate.time
        endDate = currentDate.time
    }

    private fun getCurrentDate(): Calendar = GregorianCalendar(LocaleUtils.getCurrentLocale(requireContext()))
    private fun getMinDate(): Calendar = getCurrentDate().apply {
        add(Calendar.YEAR, -3)
    }

    private fun getStartDatePicker() {
        val minDate = getMinDate()
        val maxDate = getCurrentDate()

        val defaultDate = startDate?.toCalendar() ?: getCurrentDate()
        val datePicker = DateTimePickerUnify(context = requireContext(), minDate = minDate, defaultDate = defaultDate, maxDate = maxDate)
        datePicker.setTitle(getString(R.string.dsar_date_picker_start_title))
        datePicker.datePickerButton.setOnClickListener {
            val pickedDate = datePicker.getDate().time
            startDate = pickedDate
            rangePickerDialogBinding?.txtStartDate?.editText?.setText(
                pickedDate.toString(DateUtil.DEFAULT_VIEW_FORMAT)
            )

            if (pickedDate > endDate) {
                endDate = pickedDate
                rangePickerDialogBinding?.txtEndDate?.editText?.setText(
                    pickedDate.toString(DateUtil.DEFAULT_VIEW_FORMAT)
                )
            }

            datePicker.dismiss()
        }
        datePicker.show(parentFragmentManager, TAG_START_DATE_BOTTOM_SHEET)
    }

    private fun getEndDatePicker() {
        val maxDate = getCurrentDate()
        val minDate = getMinDate()

        val defaultDate = endDate?.toCalendar() ?: getCurrentDate()
        val datePicker = DateTimePickerUnify(context = requireContext(), minDate = minDate, defaultDate = defaultDate, maxDate = maxDate)
        datePicker.setTitle(getString(R.string.dsar_date_picker_end_title))
        datePicker.datePickerButton.setOnClickListener {
            val pickedDate = datePicker.getDate().time
            endDate = pickedDate
            val textDate = pickedDate.toString(DateUtil.DEFAULT_VIEW_FORMAT)

            rangePickerDialogBinding?.txtEndDate?.editText?.setText(textDate)

            if (pickedDate < startDate) {
                startDate = pickedDate
                rangePickerDialogBinding?.txtStartDate?.editText?.setText(textDate)
            }
            datePicker.dismiss()
        }
        datePicker.show(parentFragmentManager, TAG_END_DATE_BOTTOM_SHEET)
    }

    private fun initView() {
        val currentDate = GregorianCalendar(LocaleUtils.getCurrentLocale(requireContext()))
        val date = currentDate.time.toString(DateUtil.DEFAULT_VIEW_FORMAT)

        rangePickerDialogBinding?.txtStartDate?.editText?.apply {
            isFocusable = false
            setText(date)
        }
        rangePickerDialogBinding?.txtEndDate?.editText?.apply {
            isFocusable = false
            setText(date)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        rangePickerDialogBinding?.txtStartDate?.editText?.setOnClickListener {
            getStartDatePicker()
        }

        rangePickerDialogBinding?.txtEndDate?.editText?.setOnClickListener {
            getEndDatePicker()
        }

        rangePickerDialogBinding?.radioGroup?.setOnCheckedChangeListener { _, checkedId ->
            val checked = rangePickerDialogBinding?.root?.findViewById<RadioButtonUnify>(checkedId)
            rangePickerDialogBinding?.layoutCustomDate?.showWithCondition(checked?.text.toString() == DsarConstants.LABEL_RANGE_CUSTOM)
        }

        rangePickerDialogBinding?.btnApplyFilter?.setOnClickListener {
            val checked = rangePickerDialogBinding?.radioGroup?.checkedRadioButtonId?.let {
                rangePickerDialogBinding?.root?.findViewById<RadioButtonUnify>(
                    it
                )
            }
            if (checked?.text.toString() == DsarConstants.LABEL_RANGE_CUSTOM) {
                if (startDate != null && endDate != null) {
                    dismissDialogListener(checked?.text.toString(), true, Pair(startDate!!, endDate!!))
                }
            } else {
                dismissDialogListener(checked?.text.toString(), true, null)
            }
            dismiss()
        }

        boldText()
    }

    // can't set via xml, because it's not yet supported for RadioButtonUnify
    private fun boldText() {
        rangePickerDialogBinding?.radioGroup?.childCount?.apply {
            for (i in 0 until this) {
                val radio = rangePickerDialogBinding?.radioGroup?.getChildAt(i)
                if (radio is RadioButtonUnify) {
                    radio.setTextBold(true)
                }
            }
        }
    }

    fun setOnDismissListener(dismissDialogListener: (String, Boolean, Pair<Date, Date>?) -> Unit) {
        this.dismissDialogListener = dismissDialogListener
    }

    companion object {
        private const val TAG_START_DATE_BOTTOM_SHEET = "TAG_START_DATE_BOTTOM_SHEET"
        private const val TAG_END_DATE_BOTTOM_SHEET = "TAG_END_DATE_BOTTOM_SHEET"
    }
}
