package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2TdnItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistV2TdnViewHolder(private val binding: WishlistV2TdnItemBinding, private val actionListener: WishlistV2Adapter.ActionListener?) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WishlistV2TypeLayoutData, adapterPosition: Int) {
        if (item.dataObject is TopAdsImageViewModel) {
            binding.wishlistTdnBanner.run {
                setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
                    override fun onTopAdsImageViewClicked(applink: String?) {
                        actionListener?.onBannerTopAdsClick(item.dataObject, adapterPosition)
                        RouteManager.route(itemView.context, applink)
                    }
                })

                loadImage(item.dataObject)
                actionListener?.onBannerTopAdsImpression(item.dataObject, adapterPosition)
            }
        }
    }
}