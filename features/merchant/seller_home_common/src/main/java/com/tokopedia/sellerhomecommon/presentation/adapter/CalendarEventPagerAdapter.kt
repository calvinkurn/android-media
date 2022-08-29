package com.tokopedia.sellerhomecommon.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhomecommon.databinding.ShcCalendarWidgetPageBinding
import com.tokopedia.sellerhomecommon.presentation.model.CalendarEventGroupUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarEventUiModel

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

class CalendarEventPagerAdapter : RecyclerView.Adapter<CalendarEventPagerAdapter.ViewHolder>() {

    var eventPages = listOf<CalendarEventGroupUiModel>()
    private var onItemClick: (CalendarEventUiModel) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ShcCalendarWidgetPageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val page = eventPages[position]
        holder.bind(page)
    }

    override fun getItemCount(): Int = eventPages.size

    fun setOnItemClicked(onItemClick: (CalendarEventUiModel) -> Unit) {
        this.onItemClick = onItemClick
    }

    inner class ViewHolder(
        private val binding: ShcCalendarWidgetPageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val pagerAdapter by lazy { CalendarEventAdapter(onItemClick) }
        private val layoutManager by lazy {
            object : LinearLayoutManager(itemView.context) {
                override fun canScrollVertically(): Boolean = false
            }
        }

        fun bind(page: CalendarEventGroupUiModel) {
            with(binding) {
                pagerAdapter.setItems(page.events)
                rvShcCalendarPage.layoutManager = layoutManager
                rvShcCalendarPage.adapter = pagerAdapter
            }
        }
    }
}