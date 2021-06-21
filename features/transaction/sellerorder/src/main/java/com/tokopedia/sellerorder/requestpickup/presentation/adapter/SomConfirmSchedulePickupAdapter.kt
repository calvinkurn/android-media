package com.tokopedia.sellerorder.requestpickup.presentation.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.requestpickup.data.model.*
import com.tokopedia.sellerorder.requestpickup.util.DateMapper
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class SomConfirmSchedulePickupAdapter(private val listener: SomConfirmSchedulePickupAdapterListener) : RecyclerView.Adapter<SomConfirmSchedulePickupAdapter.ScheduleTimeViewHolder>() {

    val scheduleTime = mutableListOf<ScheduleTime>()
    private var selectedKey = ""

    interface SomConfirmSchedulePickupAdapterListener {
        fun onSchedulePickupClicked(scheduleTime: ScheduleTime, formattedTime: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleTimeViewHolder {
        return ScheduleTimeViewHolder(parent.inflateLayout(R.layout.item_schedule_pickup_today))
    }

    override fun getItemCount(): Int {
        return scheduleTime.size
    }

    override fun onBindViewHolder(holder: ScheduleTimeViewHolder, position: Int) {
        holder.bind(scheduleTime[position])
    }

    fun setData(data: List<ScheduleTime>, key: String) {
        scheduleTime.clear()
        scheduleTime.addAll(data)
        selectedKey = key
        notifyDataSetChanged()
    }

    inner class ScheduleTimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val time = itemView.findViewById<Typography>(R.id.time_today)
        private val itemScheduleRadio = itemView.findViewById<RadioButtonUnify>(R.id.rb_selected)
        private val dividerLabel = itemView.findViewById<View>(R.id.divider_label)
        private val itemScheduleLayout = itemView.findViewById<RelativeLayout>(R.id.rl_option_item)

        @SuppressLint("SetTextI18n")
        fun bind(item: ScheduleTime) {
            val startTime = DateMapper.formatDate(item.start)
            val endTime = DateMapper.formatDate(item.end)
            val formattedTime = "$startTime - $endTime WIB"
            time.text = formattedTime
            itemScheduleRadio.isChecked = item.key == selectedKey

            if (layoutPosition == scheduleTime.size -1 ) {
                dividerLabel.gone()
            } else {
                dividerLabel.visible()
            }

            itemScheduleLayout.setOnClickListener {
                listener.onSchedulePickupClicked(item, formattedTime)
            }

        }
    }

}