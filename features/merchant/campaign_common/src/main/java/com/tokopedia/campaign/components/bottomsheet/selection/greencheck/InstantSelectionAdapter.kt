package com.tokopedia.campaign.components.bottomsheet.selection.greencheck

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.bottomsheet.selection.entity.SingleSelectionItem
import com.tokopedia.campaign.databinding.ItemInstantSelectionBinding
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible

class InstantSelectionAdapter : RecyclerView.Adapter<InstantSelectionAdapter.ViewHolder>() {

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
        val binding = ItemInstantSelectionBinding.inflate(
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

    inner class ViewHolder(private val binding: ItemInstantSelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SingleSelectionItem) {
            binding.tpgName.text = item.name
            binding.radioButton.isChecked = item.isSelected
            binding.icRadioCheck.apply { if (item.isSelected) visible() else invisible() }
            binding.root.setOnClickListener { onItemClicked(item) }
        }
    }

    fun submit(newItems: List<SingleSelectionItem>) { differ.submitList(newItems) }

    fun updateSelectedItem(selectedItem: SingleSelectionItem) {
        differ.submitList(differ.currentList.map {
            if (it.id == selectedItem.id) it.copy(isSelected = true) else it.copy(isSelected = false)
        })
    }

}
