package com.tokopedia.travelcalendar.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.travelcalendar.R
import com.tokopedia.travelcalendar.view.model.HolidayResult
import kotlinx.android.synthetic.main.item_holiday.view.*

/**
 * Created by nabillasabbaha on 15/05/18.
 */
class HolidayAdapter(private val holidayResults: MutableList<HolidayResult>)
    : RecyclerView.Adapter<HolidayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_holiday, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val holidayResult = holidayResults.get(position)

        val dateString = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD,
                "d MMM", holidayResult.attributes.date)

        holder.dateHoliday.text = dateString
        holder.eventHoliday.text = holidayResult.attributes.label
    }

    override fun getItemCount(): Int {
        return holidayResults.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val dateHoliday = view.date_holiday
        val eventHoliday = view.event_holiday
    }

}
