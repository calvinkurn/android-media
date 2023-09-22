package com.tokopedia.travelcalendar.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.travelcalendar.R
import com.tokopedia.travelcalendar.databinding.ItemHolidayBinding
import com.tokopedia.travelcalendar.view.model.HolidayResult

/**
 * Created by nabillasabbaha on 15/05/18.
 */
class HolidayAdapter(private val holidayResults: MutableList<HolidayResult>) :
    RecyclerView.Adapter<HolidayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_holiday, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val holidayResult = holidayResults.get(position)
        val dateString = DateFormatUtils.formatDate(
            DateFormatUtils.FORMAT_YYYY_MM_DD,
            "d MMM",
            holidayResult.attributes.date
        )
        holder.bind(dateString, holidayResult.attributes.label)
    }

    override fun getItemCount(): Int {
        return holidayResults.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemHolidayBinding.bind(view)
        fun bind(dateHoliday: String, eventHoliday: String) {
            binding.dateHoliday.text = dateHoliday
            binding.eventHoliday.text = eventHoliday
        }
    }
}
