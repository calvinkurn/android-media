package com.tokopedia.sellerhomecommon.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.sellerhomecommon.databinding.ShcCalendarWidgetItemBinding
import com.tokopedia.sellerhomecommon.presentation.model.CalendarEventUiModel
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

class CalendarEventAdapter(
    private val onItemClick: (CalendarEventUiModel) -> Unit
) : RecyclerView.Adapter<CalendarEventAdapter.ViewHolder>() {

    private var items = listOf<CalendarEventUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ShcCalendarWidgetItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<CalendarEventUiModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ShcCalendarWidgetItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CalendarEventUiModel) {
            with(binding) {
                tvShcCalendarEventTitle.text = item.eventName
                tvShcCalendarEventDesc.text = item.description

                showDateEvent(item)
            }
        }

        private fun showDateEvent(item: CalendarEventUiModel) {
            with(binding) {
                tvShcCalendarStartDate.text = DateTimeUtil.format(
                    item.startDate, DateTimeUtil.FORMAT_DD_MM_YYYY, DateTimeUtil.FORMAT_DD
                )
                tvShcCalendarStartMonth.text = DateTimeUtil.format(
                    item.startDate, DateTimeUtil.FORMAT_DD_MM_YYYY, DateTimeUtil.FORMAT_MMM
                )
                lblShcCalendarEventLabel.setLabel(item.label)

                val isSingleDayEvent = item.startDate == item.endDate
                tvShcCalendarEndDate.isVisible = !isSingleDayEvent
                tvShcCalendarEndMonth.isVisible = !isSingleDayEvent

                if (isSingleDayEvent) return

                tvShcCalendarEndDate.text = DateTimeUtil.format(
                    item.endDate, DateTimeUtil.FORMAT_DD_MM_YYYY, DateTimeUtil.FORMAT_DD
                )
                tvShcCalendarEndMonth.text = DateTimeUtil.format(
                    item.endDate, DateTimeUtil.FORMAT_DD_MM_YYYY, DateTimeUtil.FORMAT_MMM
                )
            }
        }
    }
}