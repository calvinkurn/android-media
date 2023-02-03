package com.tokopedia.privacycenter.ui.dsar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.OnDateChangedListener
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.data.ItemRangeModel
import com.tokopedia.privacycenter.databinding.BottomSheetRangePickerBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toString
import java.util.*

class DsarHistoryTransactionBottomSheet(private val mContext: Context, val onClicked: (String, Boolean, Pair<Date, Date>?) -> Unit) :
    BottomSheetUnify(),
    OnDateChangedListener {

    private val rangePickerDialogBinding: BottomSheetRangePickerBinding =
        BottomSheetRangePickerBinding.inflate(LayoutInflater.from(mContext))

    private var datePicker: DateTimePickerUnify? = null

    private var state = DsarFragment.STATE_START

    private var startDate: Date? = null
    private var endDate: Date? = null

    init {
        setChild(rangePickerDialogBinding.root)
        overlayClickDismiss = false
    }

    private fun onDateSelected() {
        val selected = datePicker?.getDate()
        if (state == DsarFragment.STATE_START) {
            state = DsarFragment.STATE_END
            startDate = selected?.time
            datePicker?.setTitle(getString(R.string.dsar_date_picker_end_title))
        } else {
            state = DsarFragment.STATE_START
            datePicker?.setTitle(getString(R.string.dsar_date_picker_start_title))
            endDate = selected?.time
            showCustomDateLayout()
            datePicker?.dismiss()
        }
    }

    private fun showCustomDateLayout() {
        rangePickerDialogBinding.layoutCustomDate.show()
        rangePickerDialogBinding.txtStartDate.editText.setText(startDate?.toString(DateUtil.DEFAULT_VIEW_FORMAT))
        rangePickerDialogBinding.txtEndDate.editText.setText(endDate?.toString(DateUtil.DEFAULT_VIEW_FORMAT))
    }

    private fun getDatePicker(): DateTimePickerUnify? {
        if (datePicker == null) {
            val minDate = GregorianCalendar(LocaleUtils.getCurrentLocale(requireContext())).apply {
                add(Calendar.YEAR, -3)
            }

            val defaultDate = GregorianCalendar(LocaleUtils.getCurrentLocale(requireContext()))
            val maxDate = defaultDate

            datePicker = DateTimePickerUnify(
                requireContext(),
                minDate,
                defaultDate,
                maxDate,
                this,
                DateTimePickerUnify.TYPE_DATEPICKER
            ).also {
                setCloseClickListener { dismiss() }
                it.datePickerButton.text = mContext.getString(R.string.dsar_date_picker_choose_btn_title)
                it.datePickerButton.setOnClickListener {
                    onDateSelected()
                }
                datePicker?.setTitle(getString(R.string.dsar_date_picker_start_title))
            }
        }
        return datePicker
    }

    override fun onDateChanged(date: Long) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rangePickerDialogBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val checked = rangePickerDialogBinding.root.findViewById<RadioButtonUnify>(checkedId)
            if (checked.text.toString() == DsarConstants.LABEL_RANGE_CUSTOM) {
                getDatePicker()?.show(parentFragmentManager, "")
            } else {
                rangePickerDialogBinding.layoutCustomDate.hide()
            }
        }

        rangePickerDialogBinding.btnApplyFilter.setOnClickListener {
            val checked = rangePickerDialogBinding.root.findViewById<RadioButtonUnify>(rangePickerDialogBinding.radioGroup.checkedRadioButtonId)
            if (checked.text.toString() == DsarConstants.LABEL_RANGE_CUSTOM) {
                if (startDate != null && endDate != null) {
                    onClicked(checked.text.toString(), true, Pair(startDate!!, endDate!!))
                }
            } else {
                onClicked(checked.text.toString(), true, null)
            }
            dismiss()
        }

        boldText()
    }

    // can't set via xml, because it's not yet supported for RadioButtonUnify
    fun boldText() {
        for (i in 0 until rangePickerDialogBinding.radioGroup.childCount) {
            val radio = rangePickerDialogBinding.radioGroup.getChildAt(i)
            if (radio is RadioButtonUnify) {
                radio.setTextBold(true)
            }
        }
    }

    fun show(fragmentMgr: FragmentManager, selectedDate: ItemRangeModel? = null) {
        show(fragmentMgr, "")
    }
}
