package com.tokopedia.wishlist.view.adapter.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.WishlistV2TdnItemBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter

class WishlistV2TdnViewHolder(private val binding: WishlistV2TdnItemBinding, private val actionListener: WishlistV2Adapter.ActionListener?) :
    RecyclerView.ViewHolder(binding.root) {

    private companion object {
        private const val RADIUS_TOPADS = 24
    }

    fun bind(
        item: WishlistV2TypeLayoutData,
        adapterPosition: Int,
        isShowCheckbox: Boolean
    ) {
        if (isShowCheckbox) {
            binding.root.gone()
            val params = (binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).apply {
                height = 0
                width = 0
            }
            binding.root.layoutParams = params
        } else {
            if (item.dataObject is TopAdsImageViewModel) {
                binding.root.visible()
                val params = (binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).apply {
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    isFullSpan = true
                }
                binding.root.layoutParams = params
                binding.wishlistTdnBanner.run {
                    setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
                        override fun onTopAdsImageViewClicked(applink: String?) {
                            actionListener?.onBannerTopAdsClick(item.dataObject, adapterPosition)
                        }
                    })

                    loadImage(item.dataObject, RADIUS_TOPADS)

                    setTopAdsImageViewImpression(object : TopAdsImageViewImpressionListener {
                        override fun onTopAdsImageViewImpression(viewUrl: String) {
                            actionListener?.onBannerTopAdsImpression(item.dataObject, adapterPosition)
                        }
                    })
                }
            }
        }
    }
}
