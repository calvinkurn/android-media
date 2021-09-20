package com.tokopedia.tokopedianow.datefilter.presentation.viewholder

import TokoNowDateFilterBottomSheet.Companion.CUSTOM_DATE_POSITION
import android.text.format.DateFormat
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.datefilter.presentation.uimodel.DateFilterUiModel
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class DateFilterViewHolder(
    itemView: View,
    val listener: DateFilterViewHolderListener
): AbstractViewHolder<DateFilterUiModel>(itemView) {

    companion object {
        private const val MIN_KEYWORD_CHARACTER_COUNT = 3
        private const val START_DATE = "start_date"
        private const val END_DATE = "end_date"

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_date_filter
    }

    private var tpSortTitle: Typography? = null
    private var rbSort: RadioButtonUnify? = null
    private var container: ConstraintLayout? = null
    private var divider: View? = null
    private var tfStartDate: TextFieldUnify? = null
    private var tfEndDate: TextFieldUnify? = null

    init {
        tpSortTitle = itemView.findViewById(R.id.tp_sort_title)
        rbSort = itemView.findViewById(R.id.rb_sort)
        container = itemView.findViewById(R.id.container)
        divider = itemView.findViewById(R.id.divider)
        tfStartDate = itemView.findViewById(R.id.tf_start_date)
        tfEndDate = itemView.findViewById(R.id.tf_end_date)
    }

    override fun bind(element: DateFilterUiModel) {
        tpSortTitle?.text = getString(element.titleRes.orZero())
        rbSort?.isChecked = element.isChecked == true
        divider?.showWithCondition(!element.isLastItem)
        container?.setOnClickListener {
            listener.onClickItem(rbSort?.isChecked == true, adapterPosition, element.startDate, element.endDate)
        }
        rbSort?.setOnClickListener {
            listener.onClickItem(rbSort?.isChecked == true, adapterPosition, element.startDate, element.endDate)
        }

        if (adapterPosition == CUSTOM_DATE_POSITION && element.isChecked) {
            tfStartDate?.show()
            tfEndDate?.show()

            setupTextField(element.startDate, element.endDate)
        } else {
            tfStartDate?.hide()
            tfEndDate?.hide()
        }
    }

    private fun setupTextField(startDate: String, endDate: String) {
        tfStartDate?.textFieldInput?.setText(convertCalendarToStringWithFormat(getGregorianCalendar(startDate)))
        tfStartDate?.textFieldInput?.isFocusable = false
        tfStartDate?.textFieldInput?.isClickable = true
        tfStartDate?.textFieldInput?.setOnClickListener {
            listener.onOpenBottomSheet(START_DATE)
        }

        tfEndDate?.textFieldInput?.setText(convertCalendarToStringWithFormat(getGregorianCalendar(endDate)))
        tfEndDate?.textFieldInput?.isFocusable = false
        tfEndDate?.textFieldInput?.isClickable = true
        tfEndDate?.textFieldInput?.setOnClickListener {
            listener.onOpenBottomSheet(END_DATE)
        }
    }

    private fun convertCalendarToStringWithFormat(date: GregorianCalendar?): String {
        return date?.let { it -> calendarToStringFormat(it, "dd MMM yyyy") }.toString()
    }

    private fun calendarToStringFormat(dateParam: GregorianCalendar, format: String) : CharSequence {
        return DateFormat.format(format, dateParam.time)
    }

    private fun getGregorianCalendar(date: String): GregorianCalendar {
        var returnDate = GregorianCalendar()
        val splitDefDate = date.split("-")
        if (splitDefDate.isNotEmpty() && splitDefDate.size == MIN_KEYWORD_CHARACTER_COUNT) {
            returnDate = stringToCalendar("${splitDefDate[0].toInt()}-${(splitDefDate[1].toInt()-1)}-${splitDefDate[2].toInt()}")
        }
        return returnDate
    }

    private fun stringToCalendar(stringParam: CharSequence) : GregorianCalendar {
        val split = stringParam.split("-")
        return if (split.isNotEmpty() && split.size == MIN_KEYWORD_CHARACTER_COUNT) {
            GregorianCalendar(split[0].toInt(), split[1].toInt(), split[2].toInt())
        } else GregorianCalendar()
    }


    interface DateFilterViewHolderListener {
        fun onClickItem(isChecked: Boolean, position: Int, startDate: String, endDate: String)
        fun onOpenBottomSheet(flag: String)
    }
}