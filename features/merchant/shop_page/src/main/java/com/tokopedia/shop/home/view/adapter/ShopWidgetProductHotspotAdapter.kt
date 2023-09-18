package com.tokopedia.shop.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.databinding.ShopHomeDisplayBannerProductHotspotItemBinding
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeDisplayBannerProductHotspotItemViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeDisplayBannerProductHotspotViewHolder
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerProductHotspotUiModel

class ShopWidgetProductHotspotAdapter(
    private val listener: ShopHomeDisplayBannerProductHotspotViewHolder.Listener? = null,
    private val widgetUiModel: ShopWidgetDisplayBannerProductHotspotUiModel? = null
) : RecyclerView.Adapter<ShopHomeDisplayBannerProductHotspotItemViewHolder>() {

    private val differCallback = object :
        DiffUtil.ItemCallback<ShopWidgetDisplayBannerProductHotspotUiModel.Data>() {
        override fun areItemsTheSame(
            oldItem: ShopWidgetDisplayBannerProductHotspotUiModel.Data,
            newItem: ShopWidgetDisplayBannerProductHotspotUiModel.Data
        ): Boolean {
            return oldItem.appLink == newItem.appLink
        }

        override fun areContentsTheSame(
            oldItem: ShopWidgetDisplayBannerProductHotspotUiModel.Data,
            newItem: ShopWidgetDisplayBannerProductHotspotUiModel.Data
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopHomeDisplayBannerProductHotspotItemViewHolder {
        val binding = ShopHomeDisplayBannerProductHotspotItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShopHomeDisplayBannerProductHotspotItemViewHolder(
            binding,
            widgetUiModel,
            listener,
            widgetUiModel?.header?.ratio
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(
        holder: ShopHomeDisplayBannerProductHotspotItemViewHolder,
        position: Int
    ) {
        differ.currentList.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    fun submit(data: List<ShopWidgetDisplayBannerProductHotspotUiModel.Data>) {
        differ.submitList(data)
    }

}
