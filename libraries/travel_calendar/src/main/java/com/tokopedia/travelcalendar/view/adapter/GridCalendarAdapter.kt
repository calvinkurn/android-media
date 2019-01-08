package com.tokopedia.travelcalendar.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.travelcalendar.R
import com.tokopedia.travelcalendar.view.DateCalendarUtil
import com.tokopedia.travelcalendar.view.getColor
import com.tokopedia.travelcalendar.view.getDrawable
import com.tokopedia.travelcalendar.view.model.CellDate
import com.tokopedia.travelcalendar.view.model.HolidayResult
import kotlinx.android.synthetic.main.view_calendar_picker_day.view.*
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
        internal val cellNumber = view.date
        internal var container = view.date_layout

        fun bind(cellDate: CellDate) {
            val dateCal = DateCalendarUtil.getCalendar()
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
                cellNumber.background = getDrawable(context, R.drawable.bg_calendar_picker_today_selected)
                cellNumber.setTextColor(getColor(context, R.color.white))
            } else {
                if (isDateOutOfRange(dateCal.time)) {
                    cellNumber.setTextColor(getColor(context, R.color.grey_300))
                } else {
                    cellNumber.background = getDrawable(context, R.drawable.bg_calendar_picker_default)
                    cellNumber.setTextColor(getColor(context, R.color.font_black_primary_70))

                    //set holiday in sunday as red color
                    val dayAtDate = SimpleDateFormat(DAY_DATE_FORMAT, Locale.ENGLISH).format(dateCal.time)
                    if (dayAtDate.equals(SUNDAY, ignoreCase = true)) {
                        cellNumber.setTextColor(getColor(context, R.color.red_a700))
                    }
                    val dateNumber = DateCalendarUtil.getCalendar()
                    for (i in holidayResultList.indices) {
                        dateNumber.time = holidayResultList[i].attributes.dateHoliday
                        if (dayValue == dateNumber.get(Calendar.DATE)) {
                            cellNumber.setTextColor(getColor(context, R.color.red_a700))
                        }
                    }
                }
            }

            container.setOnClickListener {
                val dateSelected = cellDate.date
                val calendarMonth = DateCalendarUtil.getCalendar()
                calendarMonth.time = dateSelected
                if (calendarMonth.get(Calendar.MONTH) == displayMonthInt && isDateInRange(dateCal.time)) {
                    actionListener.onClickDate(cellDate)
                }
            }
        }

        fun isDateOutOfRange(dateTime: Date): Boolean {
            return DateCalendarUtil.getZeroTimeDate(dateTime).compareTo(
                    DateCalendarUtil.getZeroTimeDate(minDate.time)) < 0 ||
                    DateCalendarUtil.getZeroTimeDate(dateTime).compareTo(
                            DateCalendarUtil.getZeroTimeDate(maxDate.time)) > 0
        }

        fun isDateInRange(dateTime: Date): Boolean {
            return DateCalendarUtil.getZeroTimeDate(dateTime).compareTo(
                    DateCalendarUtil.getZeroTimeDate(minDate.time)) >= 0 &&
                    DateCalendarUtil.getZeroTimeDate(dateTime).compareTo(
                            DateCalendarUtil.getZeroTimeDate(maxDate.time)) <= 0
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
