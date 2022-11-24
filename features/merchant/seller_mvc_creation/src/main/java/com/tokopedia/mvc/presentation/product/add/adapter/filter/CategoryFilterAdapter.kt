package com.tokopedia.mvc.presentation.product.add.adapter.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.bottomsheet.selection.entity.MultipleSelectionItem
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.mvc.databinding.SmvcItemFilterBinding

class CategoryFilterAdapter : RecyclerView.Adapter<CategoryFilterAdapter.ViewHolder>() {

    private var onItemClicked: (MultipleSelectionItem) -> Unit = {}

    private val differCallback = object : DiffUtil.ItemCallback<MultipleSelectionItem>() {
        override fun areItemsTheSame(
            oldItem: MultipleSelectionItem,
            newItem: MultipleSelectionItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MultipleSelectionItem,
            newItem: MultipleSelectionItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SmvcItemFilterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        viewHolder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    fun setOnItemClicked(onItemClicked: (MultipleSelectionItem) -> Unit) {
        this.onItemClicked = onItemClicked
    }

    inner class ViewHolder(private val binding: SmvcItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MultipleSelectionItem) {
            binding.tpgFilterName.text = item.name
            binding.iconCheckmarkState.isVisible = item.isSelected
            binding.root.setOnClickListener { onItemClicked(item) }
        }
    }

    fun submit(newItems: List<MultipleSelectionItem>) {
        differ.submitList(newItems)
    }

    fun markAsSelected(newItem : MultipleSelectionItem) {
        val current = snapshot()

        val newList = current.map {  item ->
            if (item.id == newItem.id) {
                item.copy(isSelected = !item.isSelected)
            } else {
                item
            }
        }

        submit(newList)
    }

    fun getSelectedItems(): List<MultipleSelectionItem> {
        return snapshot().filter { it.isSelected }
    }

    private fun snapshot(): List<MultipleSelectionItem> {
        return differ.currentList
    }
}
