package com.tokopedia.withdraw.auto_withdrawal.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.auto_withdrawal.domain.model.Schedule

class ScheduleAdapter(private val scheduleList: ArrayList<Schedule>,
                      private var currentSelectedSchedule: Schedule?,
                      private val scheduleChangeListener: ScheduleChangeListener?)
    : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val inflater = LayoutInflater.from(
                parent.context)
        val v = inflater.inflate(R.layout.swd_item_awd_schedule_timing,
                parent, false)
        return ScheduleViewHolder(v)
    }

    override fun getItemCount(): Int = scheduleList.size

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(scheduleList[position], scheduleChangeListener)
    }

    inner class ScheduleViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvScheduleType: TextView = itemView.findViewById(R.id.tvScheduleType)
        private val tvScheduleTiming: TextView = itemView.findViewById(R.id.tvScheduleTiming)
        private val rbScheduleStatus: RadioButtonUnify = itemView.findViewById(R.id.rbScheduleStatus)

        fun bind(schedule: Schedule, scheduleChangeListener: ScheduleChangeListener?) {
            rbScheduleStatus.setOnCheckedChangeListener { _, _ -> }
            tvScheduleType.text = schedule.title
            tvScheduleTiming.text = schedule.desc
            currentSelectedSchedule?.apply {
                rbScheduleStatus.isChecked = schedule.equals(currentSelectedSchedule)
            }
            rbScheduleStatus.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked)
                    currentSelectedSchedule = schedule
                scheduleChangeListener?.onScheduleSelected(schedule)
            }
        }
    }
}

interface ScheduleChangeListener {
    fun onScheduleSelected(schedule: Schedule)
}