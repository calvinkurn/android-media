package com.tokopedia.sellerorder.orderextension.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.databinding.BottomSheetPickTimeOrderExtentionBinding
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.orderextension.presentation.util.toColorString
import com.tokopedia.unifycomponents.HtmlLinkHelper

class CalendarOrderExtensionBottomSheet(
    val orderExtensionDate: OrderExtensionRequestInfoUiModel.OrderExtensionDate,
    val onSelectDate: (OrderExtensionRequestInfoUiModel.OrderExtensionDate.EligibleDateUIModel) -> Unit,
    val onErrorSelectDate: () -> Unit,
    private var currentSelectDate: OrderExtensionRequestInfoUiModel.OrderExtensionDate.EligibleDateUIModel,
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
                val selectedDate = orderExtensionDate.eligibleDates.find {
                    it.date == date
                }
                dismiss()
                if (selectedDate == null) {
                    onErrorSelectDate.invoke()
                } else {
                    onSelectDate.invoke(selectedDate)
                }
            }

            override fun onDateUnselected(date: Date) {}
        })
        val activeDates = orderExtensionDate.eligibleDates.map {
            it.date
        }

        context.let {

        }

        context?.let {
            binding?.tvDeadline?.text = HtmlLinkHelper(
                it,
                it.getString(
                    R.string.bottomsheet_order_extension_request_deadline,
                    ContextCompat.getColor(
                        it,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950
                    ).toColorString(),
                    orderExtensionDate.deadLineTime.toFormattedString(FORMAT_DATE_DEADLINETIME),
                )
            ).spannedString ?: ""
        }

        if (currentSelectDate.extensionTime == Int.ZERO) {
            calendar?.init(
                currentDate.time,
                orderExtensionDate.deadLineTime,
                listOf(),
                activeDates
            )?.inMode(CalendarPickerView.SelectionMode.SINGLE)
        } else {
            calendar?.init(
                currentDate.time,
                orderExtensionDate.deadLineTime,
                listOf(),
                activeDates
            )?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                ?.withSelectedDate(currentSelectDate.date)
        }

    }

    fun setSelectedDate(currentSelectDate: OrderExtensionRequestInfoUiModel.OrderExtensionDate.EligibleDateUIModel) {
        this.currentSelectDate = currentSelectDate
    }

}
