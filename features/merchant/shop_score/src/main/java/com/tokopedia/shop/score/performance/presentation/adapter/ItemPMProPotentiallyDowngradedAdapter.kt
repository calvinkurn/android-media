package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.databinding.ItemPmProPotentiallyDowngradedBinding
import com.tokopedia.shop.score.performance.presentation.model.ItemPotentiallyDowngradedUiModel

/**
 * Created by @ilhamsuaib on 06/06/22.
 */

class ItemPMProPotentiallyDowngradedAdapter :
    RecyclerView.Adapter<ItemPMProPotentiallyDowngradedAdapter.ViewHolder>() {

    private val items = mutableListOf<ItemPotentiallyDowngradedUiModel>()

    fun setItems(items: List<ItemPotentiallyDowngradedUiModel>) {
        if (this.items.isEmpty()) {
            this.items.clear()
            this.items.addAll(items)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPmProPotentiallyDowngradedBinding.inflate(
            layoutInflater, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        private val binding: ItemPmProPotentiallyDowngradedBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemPotentiallyDowngradedUiModel) {
            binding.run {
                imgSsPmProPotentiallyDowngraded.loadImage(item.imgUrl)
                tvSsPmProPotentiallyDowngradedTitle.text = root.context.getString(item.titleRes)
                tvSsPmProPotentiallyDowngradedDesc.text = root.context.getString(item.desRes)
            }
        }
    }
}