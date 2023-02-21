package com.tokopedia.campaign.components.bottomsheet.selection.greencheck

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.bottomsheet.selection.entity.MultipleSelectionItem
import com.tokopedia.campaign.databinding.ItemMultipleSelectionGreenCheckBinding
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible

class MultipleCheckSelectionAdapter : RecyclerView.Adapter<MultipleCheckSelectionAdapter.ViewHolder>() {

    private var onItemSelected: (MultipleSelectionItem, Boolean) -> Unit = { _, _ -> }

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
        val binding = ItemMultipleSelectionGreenCheckBinding.inflate(
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

    fun setOnItemClicked(onItemSelected: (MultipleSelectionItem, Boolean) -> Unit) {
        this.onItemSelected = onItemSelected
    }

    inner class ViewHolder(private val binding: ItemMultipleSelectionGreenCheckBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MultipleSelectionItem) {
            binding.checkBox.setOnCheckedChangeListener(null)
            binding.tpgName.text = item.name
            binding.checkBox.isChecked = item.isSelected
            binding.icGreenCheck.apply { if (item.isSelected) visible() else invisible() }
            binding.root.setOnClickListener {
                val newCheckedStatus = !binding.checkBox.isChecked
                onItemSelected(item, newCheckedStatus)
            }
            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                onItemSelected(item, isChecked)
            }
        }
    }

    fun submit(newItems: List<MultipleSelectionItem>) {
        differ.submitList(newItems)
    }

    fun getSelectedItems(): List<MultipleSelectionItem> = differ.currentList.filter { it.isSelected }

    fun updateCheckChangedItem(selectedItem: MultipleSelectionItem, isSelected: Boolean) {
        differ.submitList(differ.currentList.map {
            if (it.id == selectedItem.id) it.copy(isSelected = isSelected) else it
        })
    }

    fun updateCheckedItems(items: List<MultipleSelectionItem>, checkedItems: List<String>) {
        if (checkedItems.isNotEmpty())
            differ.submitList(items.map {
                if (it.id in checkedItems) it.copy(isSelected = true) else it.copy(isSelected = false)
            })
        else differ.submitList(items)
    }

}
