package com.tokopedia.product.manage.common.view.ongoingpromotion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.R
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductCampaignType
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class OngoingPromotionAdapter(private val itemList: List<ProductCampaignType>): RecyclerView.Adapter<OngoingPromotionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_manage_ongoing_promotion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.count()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val campaignThumbnailImage: ImageUnify? = itemView.findViewById(R.id.iv_product_manage_ongoing_promotion_item)
        private val campaignNameText: Typography? = itemView.findViewById(R.id.tv_product_manage_ongoing_promotion_item)

        fun bind(campaign: ProductCampaignType) {
            with(campaign) {
                campaignThumbnailImage?.setImageUrl(iconUrl.orEmpty())
                campaignNameText?.text = name.orEmpty()
            }
        }

    }
}