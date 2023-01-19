package com.tokopedia.sellerorder.orderextension.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.sellerorder.databinding.BottomSheetPickTimeOrderExtentionBinding
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import com.tokopedia.sellerorder.R

class CalendarOrderExtensionBottomSheet(
    val orderExtensionDate: OrderExtensionRequestInfoUiModel.OrderExtentionDate,
    val onSelectDate: (OrderExtensionRequestInfoUiModel.OrderExtentionDate.EligbleDateUIModel) -> Unit,
    val onErrorSelectDate: () -> Unit,
    private val currentSelectDate: OrderExtensionRequestInfoUiModel.OrderExtentionDate.EligbleDateUIModel,
) : BottomSheetUnify() {


    companion object {
        val TAG: String = CalendarOrderExtensionBottomSheet::class.java.simpleName
        private const val FORMAT_DATE_DEADLINETIME = "dd MMMM yyyy"
    }

    var calendar: CalendarPickerView? = null

    private var binding by autoClearedNullable<BottomSheetPickTimeOrderExtentionBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetPickTimeOrderExtentionBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTitle(getString(R.string.bottomsheet_order_extension_title_pick_date))
        calendar = binding?.calendarView?.calendarPickerView

        val currentDate = Calendar.getInstance()

        calendar?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                val selectedDate = orderExtensionDate.eligbleDates.find {
                    it.date == date
                }
                dismiss()
                if (selectedDate != null) {
                    onSelectDate.invoke(selectedDate)
                } else {
                    onErrorSelectDate.invoke()
                }
            }

            override fun onDateUnselected(date: Date) {}
        })
        val activeDates = orderExtensionDate.eligbleDates.map {
            it.date
        }

        binding?.tvDeadline?.text = getString(
            R.string.bottomsheet_order_extension_request_deadline,
            orderExtensionDate.deadLineTime.toFormattedString(FORMAT_DATE_DEADLINETIME)
        ).parseAsHtml()

        if (currentSelectDate.extensionTime != Int.ZERO) {
            calendar?.init(
                currentDate.time,
                orderExtensionDate.deadLineTime,
                listOf(),
                activeDates
            )?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                ?.withSelectedDate(currentSelectDate.date)
        } else {
            calendar?.init(
                currentDate.time,
                orderExtensionDate.deadLineTime,
                listOf(),
                activeDates
            )?.inMode(CalendarPickerView.SelectionMode.SINGLE)
        }

    }

}
