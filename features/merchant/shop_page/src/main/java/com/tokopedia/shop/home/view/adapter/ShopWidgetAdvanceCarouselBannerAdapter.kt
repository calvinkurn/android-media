package com.tokopedia.shop.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.databinding.ShopHomeDisplayAdvanceCarouselBannerItemBinding
import com.tokopedia.shop.home.view.adapter.viewholder.advance_carousel_banner.ShopHomeDisplayAdvanceCarouselBannerItemViewHolder
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

class ShopWidgetAdvanceCarouselBannerAdapter(
    private val parentUiModel: ShopHomeDisplayWidgetUiModel
) : RecyclerView.Adapter<ShopHomeDisplayAdvanceCarouselBannerItemViewHolder>() {

    private val differCallback = object :
        DiffUtil.ItemCallback<ShopHomeDisplayWidgetUiModel.DisplayWidgetItem>() {
        override fun areItemsTheSame(
            oldItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem,
            newItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem
        ): Boolean {
            return oldItem.appLink == newItem.appLink
        }

        override fun areContentsTheSame(
            oldItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem,
            newItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopHomeDisplayAdvanceCarouselBannerItemViewHolder {
        val binding = ShopHomeDisplayAdvanceCarouselBannerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShopHomeDisplayAdvanceCarouselBannerItemViewHolder(
            binding,
            parentUiModel
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(
        holder: ShopHomeDisplayAdvanceCarouselBannerItemViewHolder,
        position: Int
    ) {
        differ.currentList.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    fun submit(data: List<ShopHomeDisplayWidgetUiModel.DisplayWidgetItem>) {
        differ.submitList(data)
    }

}
