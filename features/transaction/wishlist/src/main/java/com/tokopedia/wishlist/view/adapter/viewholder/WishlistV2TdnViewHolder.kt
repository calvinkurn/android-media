package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2TdnItemBinding

class WishlistV2TdnViewHolder(private val binding: WishlistV2TdnItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WishlistV2TypeLayoutData) {
        if (item.dataObject is TopAdsImageViewModel) {
            binding.wishlistTdnBanner.run {
                setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
                    override fun onTopAdsImageViewClicked(applink: String?) {
                        RouteManager.route(itemView.context, applink)
                    }
                })

                loadImage(item.dataObject)
            }
        }
    }
}