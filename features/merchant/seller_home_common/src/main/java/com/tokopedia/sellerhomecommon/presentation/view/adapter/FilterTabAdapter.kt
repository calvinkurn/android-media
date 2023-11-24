package com.tokopedia.sellerhomecommon.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerhomecommon.databinding.ShcItemFilterTabBinding
import com.tokopedia.sellerhomecommon.presentation.model.FilterTabUiModel

class FilterTabAdapter(
    private val items: List<FilterTabUiModel>,
    private val listener: Listener?
): RecyclerView.Adapter<FilterTabAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ShcItemFilterTabBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item) {
            listener?.onFilterTabSelected(it)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        private val binding: ShcItemFilterTabBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: FilterTabUiModel,
            onFilterClicked: (FilterTabUiModel) -> Unit
        ) {
            with(binding) {
                tvShcItemFilterTab.text = item.tabName
                icShcItemFilterTab.showWithCondition(item.isSelected)
                root.setOnClickListener {
                    onFilterClicked(item)
                }
            }
        }

    }

    interface Listener {
        fun onFilterTabSelected(uiModel: FilterTabUiModel)
    }

}
