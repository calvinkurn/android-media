package com.tokopedia.mvc.presentation.product.add.adapter.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.mvc.databinding.SmvcItemFilterBinding
import com.tokopedia.mvc.domain.entity.ShopShowcase

class ShopShowcaseFilterAdapter : RecyclerView.Adapter<ShopShowcaseFilterAdapter.ViewHolder>() {

    private var onItemClicked: (ShopShowcase) -> Unit = {}

    private val differCallback = object : DiffUtil.ItemCallback<ShopShowcase>() {
        override fun areItemsTheSame(
            oldItem: ShopShowcase,
            newItem: ShopShowcase
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ShopShowcase,
            newItem: ShopShowcase
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


    fun setOnShowcaseClicked(onItemClicked: (ShopShowcase) -> Unit) {
        this.onItemClicked = onItemClicked
    }

    inner class ViewHolder(private val binding: SmvcItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShopShowcase) {
            binding.tpgFilterName.text = item.name
            binding.iconCheckmarkState.isVisible = item.isSelected
            binding.root.setOnClickListener { onItemClicked(item) }
        }
    }

    fun submit(newItems: List<ShopShowcase>) {
        differ.submitList(newItems)
    }

    fun markAsSelected(selectedShowcaseIds : List<Long>) {
        val current = snapshot()

        val newList = current.map { item ->
            if (item.id in selectedShowcaseIds) {
                item.copy(isSelected = !item.isSelected)
            } else {
                item
            }
        }

        submit(newList)
    }

    fun getSelectedItems(): List<ShopShowcase> {
        return snapshot().filter { it.isSelected }
    }

    fun snapshot(): List<ShopShowcase> {
        return differ.currentList
    }
}

