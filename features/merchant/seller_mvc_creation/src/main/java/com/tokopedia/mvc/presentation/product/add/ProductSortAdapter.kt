package com.tokopedia.mvc.presentation.product.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.bottomsheet.selection.entity.SingleSelectionItem
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_mvc_creation.databinding.SmvcItemProductSortBinding

class ProductSortAdapter : RecyclerView.Adapter<ProductSortAdapter.ViewHolder>() {

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
        val binding = SmvcItemProductSortBinding.inflate(
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

    inner class ViewHolder(private val binding: SmvcItemProductSortBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SingleSelectionItem) {
            binding.tpgSortName.text = item.name
            binding.iconCheckmark.isVisible = item.isSelected
            binding.root.setOnClickListener { onItemClicked(item) }
        }
    }

    fun submit(newItems: List<SingleSelectionItem>) {
        differ.submitList(newItems)
    }

    fun markAsSelected(newItem : SingleSelectionItem) {
        val current = snapshot()

        val newList = current.map {  item ->
            if (item.name == newItem.name) {
                item.copy(isSelected = true)
            } else {
                item.copy(isSelected = false)
            }
        }

        submit(newList)
    }

    fun getSelectedItem(): SingleSelectionItem? {
        return snapshot().find { it.isSelected }
    }

    private fun snapshot(): List<SingleSelectionItem> {
        return differ.currentList
    }
}
