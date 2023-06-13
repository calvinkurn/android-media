package com.tokopedia.campaign.components.bottomsheet.selection.multiple

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.databinding.ItemMultipleSelectionItemBinding
import com.tokopedia.campaign.components.bottomsheet.selection.entity.MultipleSelectionItem

internal class MultipleSelectionAdapter :
    RecyclerView.Adapter<MultipleSelectionAdapter.ViewHolder>() {

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
        val binding = ItemMultipleSelectionItemBinding.inflate(
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


    inner class ViewHolder(private val binding: ItemMultipleSelectionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MultipleSelectionItem) {
            binding.checkboxUnify.setOnCheckedChangeListener(null)
            binding.tpgName.text = item.name
            binding.checkboxUnify.isChecked = item.isSelected
            binding.root.setOnClickListener {
                val newCheckedStatus = !binding.checkboxUnify.isChecked
                onItemSelected(item, newCheckedStatus)
            }
            binding.checkboxUnify.setOnCheckedChangeListener { _, isChecked ->
                onItemSelected(item, isChecked)
            }
        }
    }

    fun submit(newItems: List<MultipleSelectionItem>) {
        differ.submitList(newItems)
    }

    fun snapshot(): List<MultipleSelectionItem> {
        return differ.currentList
    }
}
