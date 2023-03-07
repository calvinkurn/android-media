package com.tokopedia.campaign.components.bottomsheet.selection.single

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.databinding.ItemSingleSelectionItemBinding
import com.tokopedia.campaign.components.bottomsheet.selection.entity.SingleSelectionItem

internal class SingleSelectionAdapter : RecyclerView.Adapter<SingleSelectionAdapter.ViewHolder>() {

    private var onItemClicked: (SingleSelectionItem) -> Unit = {}


    private val differCallback = object : DiffUtil.ItemCallback<SingleSelectionItem>() {
        override fun areItemsTheSame(
            oldItem: SingleSelectionItem,
            newItem: SingleSelectionItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SingleSelectionItem,
            newItem: SingleSelectionItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSingleSelectionItemBinding.inflate(
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


    fun setOnItemClicked(onItemClicked: (SingleSelectionItem) -> Unit) {
        this.onItemClicked = onItemClicked
    }

    inner class ViewHolder(private val binding: ItemSingleSelectionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SingleSelectionItem) {
            binding.radioButton.setOnCheckedChangeListener { _, _ -> onItemClicked(item) }
            binding.tpgName.text = item.name
            binding.radioButton.isChecked = item.isSelected
            binding.root.setOnClickListener { onItemClicked(item) }
        }
    }

    fun submit(newItems: List<SingleSelectionItem>) {
        differ.submitList(newItems)
    }

    fun snapshot(): List<SingleSelectionItem> {
        return differ.currentList
    }
}
