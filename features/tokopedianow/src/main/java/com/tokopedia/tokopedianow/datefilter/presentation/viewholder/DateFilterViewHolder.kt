package com.tokopedia.tokopedianow.datefilter.presentation.viewholder

import TokoNowDateFilterBottomSheet.Companion.CUSTOM_DATE_POSITION
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.DateUtil.calendarToStringFormat
import com.tokopedia.tokopedianow.common.util.DateUtil.getGregorianCalendar
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowDateFilterBinding
import com.tokopedia.tokopedianow.datefilter.presentation.uimodel.DateFilterUiModel
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*

class DateFilterViewHolder(
    itemView: View,
    val listener: DateFilterViewHolderListener
): AbstractViewHolder<DateFilterUiModel>(itemView) {

    companion object {
        private const val START_DATE = "start_date"
        private const val END_DATE = "end_date"
        private const val DATE_FORMAT = "dd MMM yyyy"

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_date_filter
    }

    private var binding: ItemTokopedianowDateFilterBinding? by viewBinding()

    override fun bind(element: DateFilterUiModel) {
        binding?.apply {
            rbSort.isChecked = element.isChecked == true
            divider.showWithCondition(!element.isLastItem)
            element.titleRes?.let {
                tpSortTitle.text = getString(it)
            }
            container.setOnClickListener {
                listener.onClickItem(rbSort.isChecked, adapterPosition, element.startDate, element.endDate)
            }
            rbSort.setOnClickListener {
                listener.onClickItem(rbSort.isChecked, adapterPosition, element.startDate, element.endDate)
            }

            if (adapterPosition == CUSTOM_DATE_POSITION && element.isChecked) {
                tfStartDate.show()
                tfEndDate.show()

                setupTextField(element.startDate, element.endDate)
            } else {
                tfStartDate.hide()
                tfEndDate.hide()
            }
        }
    }

    private fun setupTextField(startDate: String, endDate: String) {
        binding?.apply {
            tfStartDate.textFieldInput.setText(convertCalendarToStringWithFormat(getGregorianCalendar(startDate)))
            tfStartDate.textFieldInput.isFocusable = false
            tfStartDate.textFieldInput.isClickable = true
            tfStartDate.textFieldInput.setOnClickListener {
                listener.onOpenBottomSheet(START_DATE)
            }

            tfEndDate.textFieldInput.setText(convertCalendarToStringWithFormat(getGregorianCalendar(endDate)))
            tfEndDate.textFieldInput.isFocusable = false
            tfEndDate.textFieldInput.isClickable = true
            tfEndDate.textFieldInput.setOnClickListener {
                listener.onOpenBottomSheet(END_DATE)
            }
        }
    }

    private fun convertCalendarToStringWithFormat(date: GregorianCalendar?): String {
        return date?.let { it -> calendarToStringFormat(it, DATE_FORMAT) }.toString()
    }

    interface DateFilterViewHolderListener {
        fun onClickItem(isChecked: Boolean, position: Int, startDate: String, endDate: String)
        fun onOpenBottomSheet(flag: String)
    }
}