package com.tokopedia.sellerorder.requestpickup.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.requestpickup.data.model.*
import com.tokopedia.unifyprinciples.Typography

class SomConfirmSchedulePickupAdapter(private val listener: SomConfirmSchedulePickupAdapterListener) : RecyclerView.Adapter<SomConfirmSchedulePickupAdapter.BaseViewHolder<*>>() {

    private val scheduleTime = mutableListOf<SchedulePickupModelVisitable>()

    interface SomConfirmSchedulePickupAdapterListener {
        fun onSchedulePickupTodayClicked(today: Today)
        fun onSchedulePickupTomorrowClicked(tomorrow: Tomorrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_schedule_pickup_today -> ScheduleTodayViewHolder(view)
            else -> ScheduleTomorrowViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return scheduleTime.size
    }

    override fun getItemViewType(position: Int): Int = when(scheduleTime[position]) {
        is Today -> R.layout.item_schedule_pickup_today
        is Tomorrow -> R.layout.item_schedule_pickup_tomorrow
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val data = scheduleTime[position]
        when (holder) {
            is ScheduleTodayViewHolder -> {
                holder.bind(data, holder.adapterPosition)
            }
            is ScheduleTomorrowViewHolder -> {
                holder.bind(data, holder.adapterPosition)
            }
        }
    }

    fun setData(data: SchedulePickupModel) {
        scheduleTime.clear()
        scheduleTime.addAll(data.today)
        scheduleTime.addAll(data.tomorrow)
        notifyDataSetChanged()
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
    }

    inner class ScheduleTodayViewHolder(itemView: View) : SomConfirmSchedulePickupAdapter.BaseViewHolder<SchedulePickupModelVisitable>(itemView) {

        private val tvToday = itemView.findViewById<Typography>(R.id.tv_today)
        private val timeToday = itemView.findViewById<Typography>(R.id.time_today)
        private var showTitle = true

        override fun bind(item: SchedulePickupModelVisitable, position: Int) {
            if (item is Today) {
                if (showTitle) {
                    tvToday.visible()
                    showTitle = false
                } else {
                    tvToday.gone()
                }
                val time = item.startToday + "-" + item.endToday
                timeToday.text = time

                timeToday.setOnClickListener {
                    listener.onSchedulePickupTodayClicked(item)
                }
            }
        }
    }

    inner class ScheduleTomorrowViewHolder(itemView: View) : SomConfirmSchedulePickupAdapter.BaseViewHolder<SchedulePickupModelVisitable>(itemView) {

        private val tvTomorrow = itemView.findViewById<Typography>(R.id.tv_tomorrow)
        private val timeTomorrow = itemView.findViewById<Typography>(R.id.time_tomorrow)
        private var showTitle = true

        override fun bind(item: SchedulePickupModelVisitable, position: Int) {
            if (item is Tomorrow) {
                if (showTitle) {
                    tvTomorrow.visible()
                    showTitle = false
                } else {
                    tvTomorrow.gone()
                }
                val time = item.startTomorrow + "-" + item.endTomorrow
                timeTomorrow.text = time

                timeTomorrow.setOnClickListener {
                    listener.onSchedulePickupTomorrowClicked(item)
                }
            }
        }

    }
}