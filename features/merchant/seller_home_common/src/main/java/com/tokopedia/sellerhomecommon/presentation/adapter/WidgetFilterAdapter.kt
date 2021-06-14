package com.tokopedia.sellerhomecommon.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.WidgetFilterUiModel
import kotlinx.android.synthetic.main.shc_item_widget_filter.view.*

/**
 * Created By @ilhamsuaib on 06/11/20
 */

class WidgetFilterAdapter(
        private val filterItems: List<WidgetFilterUiModel>,
        private val listener: Listener
) : RecyclerView.Adapter<WidgetFilterAdapter.WidgetFilterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetFilterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.shc_item_widget_filter, parent, false)
        return WidgetFilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: WidgetFilterViewHolder, position: Int) {
        holder.bind(filterItems[position], listener)
    }

    override fun getItemCount(): Int = filterItems.size

    fun getItems() = filterItems

    class WidgetFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: WidgetFilterUiModel, listener: Listener) = with(itemView) {

            tvShcWidgetFilterName.text = item.name
            radShcWidgetFilter.isChecked = item.isSelected

            radShcWidgetFilter.setOnClickListener {
                listener.onItemClick(item)
            }
            setOnClickListener {
                listener.onItemClick(item)
            }
        }
    }

    interface Listener {
        fun onItemClick(item: WidgetFilterUiModel)
    }
}