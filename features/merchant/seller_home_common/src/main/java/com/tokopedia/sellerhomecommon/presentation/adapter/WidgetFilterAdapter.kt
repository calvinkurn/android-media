package com.tokopedia.sellerhomecommon.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhomecommon.databinding.ShcItemWidgetFilterBinding
import com.tokopedia.sellerhomecommon.presentation.model.WidgetFilterUiModel

/**
 * Created By @ilhamsuaib on 06/11/20
 */

class WidgetFilterAdapter(
    private val filterItems: List<WidgetFilterUiModel>,
    private val listener: Listener
) : RecyclerView.Adapter<WidgetFilterAdapter.WidgetFilterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetFilterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ShcItemWidgetFilterBinding.inflate(inflater, parent, false)
        return WidgetFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WidgetFilterViewHolder, position: Int) {
        holder.bind(filterItems[position], listener)
    }

    override fun getItemCount(): Int = filterItems.size

    fun getItems() = filterItems

    class WidgetFilterViewHolder(
        private val binding: ShcItemWidgetFilterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WidgetFilterUiModel, listener: Listener) = with(binding) {

            tvShcWidgetFilterName.text = item.name
            radShcWidgetFilter.isChecked = item.isSelected

            radShcWidgetFilter.setOnClickListener {
                listener.onItemClick(item)
            }
            root.setOnClickListener {
                listener.onItemClick(item)
            }
        }
    }

    interface Listener {
        fun onItemClick(item: WidgetFilterUiModel)
    }
}