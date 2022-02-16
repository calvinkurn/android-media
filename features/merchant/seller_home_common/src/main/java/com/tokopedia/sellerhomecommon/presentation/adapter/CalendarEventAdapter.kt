package com.tokopedia.sellerhomecommon.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.databinding.ShcCalendarWidgetItemBinding
import com.tokopedia.sellerhomecommon.presentation.model.CalendarEventUiModel
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

class CalendarEventAdapter(
    private val onItemClick: (CalendarEventUiModel) -> Unit
) : RecyclerView.Adapter<CalendarEventAdapter.ViewHolder>() {

    companion object {
        private const val ONE = 1
    }

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
        holder.setOnItemClicked(onItemClick)
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

        private var onItemClick: ((CalendarEventUiModel) -> Unit)? = null

        fun bind(item: CalendarEventUiModel) {
            with(binding) {
                tvShcCalendarEventTitle.text = item.eventName
                tvShcCalendarEventDesc.text = item.description

                if (adapterPosition < items.size.minus(ONE)) {
                    horLineShcCalendarEvent.visible()
                } else {
                    horLineShcCalendarEvent.invisible()
                }

                showDateEvent(item)

                root.setOnClickListener {
                    onItemClick?.invoke(item)
                }
            }
        }

        fun setOnItemClicked(onItemClick: (CalendarEventUiModel) -> Unit) {
            this.onItemClick = onItemClick
        }

        private fun showDateEvent(item: CalendarEventUiModel) {
            with(binding) {
                tvShcCalendarStartDate.text = DateTimeUtil.format(
                    item.startDate, DateTimeUtil.FORMAT_DD_MM_YYYY, DateTimeUtil.FORMAT_DD
                )
                tvShcCalendarStartMonth.text = DateTimeUtil.format(
                    item.startDate, DateTimeUtil.FORMAT_DD_MM_YYYY, DateTimeUtil.FORMAT_MMM
                )
                val resColor: Int = if (item.isOnGoingEvent) {
                    com.tokopedia.unifyprinciples.R.color.Unify_RN500
                } else {
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                }
                tvShcCalendarStartDate.setTextColor(root.context.getResColor(resColor))
                tvShcCalendarStartMonth.setTextColor(root.context.getResColor(resColor))
                imgShcEventItemDash.setImage(newLightEnable = root.context.getResColor(resColor))

                lblShcCalendarEventLabel.setLabel(item.label)

                val isSingleDayEvent = item.startDate == item.endDate
                tvShcCalendarEndDate.isVisible = !isSingleDayEvent
                tvShcCalendarEndMonth.isVisible = !isSingleDayEvent
                imgShcEventItemDash.isVisible = !isSingleDayEvent

                if (isSingleDayEvent) return

                tvShcCalendarEndDate.text = DateTimeUtil.format(
                    item.endDate, DateTimeUtil.FORMAT_DD_MM_YYYY, DateTimeUtil.FORMAT_DD
                )
                tvShcCalendarEndMonth.text = DateTimeUtil.format(
                    item.endDate, DateTimeUtil.FORMAT_DD_MM_YYYY, DateTimeUtil.FORMAT_MMM
                )
                tvShcCalendarEndDate.setTextColor(root.context.getResColor(resColor))
                tvShcCalendarEndMonth.setTextColor(root.context.getResColor(resColor))
            }
        }
    }
}