package com.tokopedia.product.manage.common.view.ongoingpromotion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.ItemProductManageOngoingPromotionBinding
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductCampaignType
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class OngoingPromotionAdapter(private val itemList: List<ProductCampaignType>): RecyclerView.Adapter<OngoingPromotionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductManageOngoingPromotionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.count()

    class ViewHolder(private val binding: ItemProductManageOngoingPromotionBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(campaign: ProductCampaignType) {
            with(campaign) {
                binding.ivProductManageOngoingPromotionItem.setImageUrl(iconUrl.orEmpty())
                binding.tvProductManageOngoingPromotionItem.text = name.orEmpty()
            }
        }

    }
}