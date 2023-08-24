package com.tokopedia.logisticseller.ui.requestpickup.presentation.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.databinding.ItemSchedulePickupTodayBinding
import com.tokopedia.logisticseller.ui.requestpickup.data.model.ScheduleTime
import com.tokopedia.logisticseller.ui.requestpickup.util.DateMapper
import com.tokopedia.utils.view.binding.viewBinding

class SchedulePickupAdapter(private val listener: SomConfirmSchedulePickupAdapterListener) :
    RecyclerView.Adapter<SchedulePickupAdapter.ScheduleTimeViewHolder>() {

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

        private val binding by viewBinding<ItemSchedulePickupTodayBinding>()

        @SuppressLint("SetTextI18n")
        fun bind(item: ScheduleTime) {
            binding?.run {
                val startTime = DateMapper.formatDate(item.start)
                val endTime = DateMapper.formatDate(item.end)
                val formattedTime = "$startTime - $endTime WIB"
                timeToday.text = formattedTime
                rbSelected.isChecked = item.key == selectedKey

                if (layoutPosition == scheduleTime.size - 1) {
                    dividerLabel.gone()
                } else {
                    dividerLabel.visible()
                }

                rlOptionItem.setOnClickListener {
                    listener.onSchedulePickupClicked(item, formattedTime)
                }
            }
        }
    }
}
