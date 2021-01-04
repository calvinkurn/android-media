package com.tokopedia.travelcalendar.view.adapter

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.tokopedia.travelcalendar.R
import com.tokopedia.travelcalendar.view.getColorCalendar
import com.tokopedia.travelcalendar.view.getDrawableCalendar
import com.tokopedia.travelcalendar.view.getZeroTime
import com.tokopedia.travelcalendar.view.model.CellDate
import com.tokopedia.travelcalendar.view.model.HolidayResult
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by nabillasabbaha on 10/05/18.
 */
class GridCalendarAdapter(private val monthlyDates: List<CellDate>,
                          private val currentDate: Calendar,
                          private val maxDate: Calendar,
                          private val minDate: Calendar,
                          private val holidayResultList: List<HolidayResult>,
                          private val actionListener: ActionListener)
    : RecyclerView.Adapter<GridCalendarAdapter.ViewHolder>() {

    private lateinit var context: Context

    private val displayMonthInt: Int
        get() {
            var displayMonthInt = currentDate.get(Calendar.MONTH) - DEDUCTION_MONTH
            if (displayMonthInt < 0) {
                displayMonthInt = MAX_MONTH_IN_YEAR
            }
            return displayMonthInt
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        return ViewHolder(layoutInflater.inflate(R.layout.view_calendar_picker_day, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(monthlyDates[position])
    }

    override fun getItemCount(): Int {
        return monthlyDates.size
    }

    /**
     * old date compareTo today date = -1
     * today date compareTo next date = 1
     * today date compareTo today date = 0
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val cellNumber = view.findViewById(R.id.date) as AppCompatTextView
        internal var container = view.findViewById(R.id.date_layout) as RelativeLayout

        fun bind(cellDate: CellDate) {
            val dateCal = Calendar.getInstance()
            dateCal.time = cellDate.date
            val dayValue = dateCal.get(Calendar.DAY_OF_MONTH)
            val dateCalMonth = dateCal.get(Calendar.MONTH)

            if (dateCalMonth == displayMonthInt) {
                cellNumber.visibility = View.VISIBLE
                cellNumber.text = dayValue.toString()
            } else {
                cellNumber.visibility = View.GONE
            }

            if (cellDate.isSelected) {
                cellNumber.background = context.resources.getDrawableCalendar(context, R.drawable.bg_calendar_picker_today_selected)
                cellNumber.setTextColor(context.resources.getColorCalendar(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            } else {
                if (isDateOutOfRange(dateCal.time)) {
                    cellNumber.setTextColor(context.resources.getColorCalendar(context, com.tokopedia.unifyprinciples.R.color.Unify_N100))
                } else {
                    cellNumber.background = context.resources.getDrawableCalendar(context, R.drawable.bg_calendar_picker_default)
                    cellNumber.setTextColor(context.resources.getColorCalendar(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))

                    //set holiday in sunday as red color
                    val dayAtDate = SimpleDateFormat(DAY_DATE_FORMAT, Locale.ENGLISH).format(dateCal.time)
                    if (dayAtDate.equals(SUNDAY, ignoreCase = true)) {
                        cellNumber.setTextColor(context.resources.getColorCalendar(context, com.tokopedia.unifyprinciples.R.color.Unify_R600))
                    }
                    val dateNumber = Calendar.getInstance()
                    for (i in holidayResultList.indices) {
                        dateNumber.time = holidayResultList[i].attributes.dateHoliday
                        if (dayValue == dateNumber.get(Calendar.DATE)) {
                            cellNumber.setTextColor(context.resources.getColorCalendar(context, com.tokopedia.unifyprinciples.R.color.Unify_R600))
                        }
                    }
                }
            }

            container.setOnClickListener {
                val dateSelected = cellDate.date
                val calendarMonth = Calendar.getInstance()
                calendarMonth.time = dateSelected
                if (calendarMonth.get(Calendar.MONTH) == displayMonthInt && isDateInRange(dateCal.time)) {
                    actionListener.onClickDate(cellDate)
                }
            }
        }

        fun isDateOutOfRange(dateTime: Date): Boolean {
            val calendar = Calendar.getInstance()
            return calendar.getZeroTime(dateTime).compareTo(
                    calendar.getZeroTime(minDate.time)) < 0 ||
                    calendar.getZeroTime(dateTime).compareTo(
                            calendar.getZeroTime(maxDate.time)) > 0
        }

        fun isDateInRange(dateTime: Date): Boolean {
            val calendar = Calendar.getInstance()
            return calendar.getZeroTime(dateTime).compareTo(
                    calendar.getZeroTime(minDate.time)) >= 0 &&
                    calendar.getZeroTime(dateTime).compareTo(
                            calendar.getZeroTime(maxDate.time)) <= 0
        }
    }

    interface ActionListener {
        fun onClickDate(cellDate: CellDate)
    }

    companion object {
        private val DAY_DATE_FORMAT = "EEEE"
        private val SUNDAY = "Sunday"
        private val MAX_MONTH_IN_YEAR = 11
        private val DEDUCTION_MONTH = 1
    }
}
