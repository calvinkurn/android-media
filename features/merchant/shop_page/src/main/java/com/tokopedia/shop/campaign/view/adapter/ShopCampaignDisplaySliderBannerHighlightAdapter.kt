package com.tokopedia.shop.campaign.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignDisplaySliderBannerHighlightProductImageItemViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignDisplaySliderBannerHighlightViewHolder
import com.tokopedia.shop.campaign.view.model.ShopWidgetDisplaySliderBannerHighlightUiModel
import com.tokopedia.shop.databinding.ItemShopCampaignSliderBannerHighlightProductImageItemBinding

class ShopCampaignDisplaySliderBannerHighlightAdapter(
    private val listener: ShopCampaignDisplaySliderBannerHighlightViewHolder.Listener,
    private val widgetUiModel: ShopWidgetDisplaySliderBannerHighlightUiModel?
) : RecyclerView.Adapter<ShopCampaignDisplaySliderBannerHighlightProductImageItemViewHolder>() {

    companion object{
        private const val INT_TWO = 2
    }

    private val differCallback = object :
        DiffUtil.ItemCallback<ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData>() {
        override fun areItemsTheSame(
            oldItem: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData,
            newItem: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData
        ): Boolean {
            return oldItem.appLink == newItem.appLink
        }

        override fun areContentsTheSame(
            oldItem: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData,
            newItem: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopCampaignDisplaySliderBannerHighlightProductImageItemViewHolder {
        val binding = ItemShopCampaignSliderBannerHighlightProductImageItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShopCampaignDisplaySliderBannerHighlightProductImageItemViewHolder(
            binding,
            widgetUiModel,
            listener
        )
    }

    override fun onBindViewHolder(
        holder: ShopCampaignDisplaySliderBannerHighlightProductImageItemViewHolder,
        position: Int
    ) {
        val adjustedPosition = position % differ.currentList.size
        holder.bind(differ.currentList[adjustedPosition])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size * INT_TWO
    }

    fun submit(data: List<ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData>) {
        differ.submitList(data)
    }

}
