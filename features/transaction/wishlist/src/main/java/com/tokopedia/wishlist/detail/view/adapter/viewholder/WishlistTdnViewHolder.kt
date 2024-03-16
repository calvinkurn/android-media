package com.tokopedia.wishlist.detail.view.adapter.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import com.tokopedia.topads.sdk.v2.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.v2.tdnbanner.listener.TopAdsImageViewImpressionListener
import com.tokopedia.wishlist.databinding.WishlistTdnItemBinding
import com.tokopedia.wishlist.detail.data.model.WishlistTypeLayoutData
import com.tokopedia.wishlist.detail.view.adapter.WishlistAdapter

class WishlistTdnViewHolder(private val binding: WishlistTdnItemBinding, private val actionListener: WishlistAdapter.ActionListener?) :
    RecyclerView.ViewHolder(binding.root) {

    private companion object {
        private const val RADIUS_TOPADS = 24
    }

    fun bind(
            item: WishlistTypeLayoutData,
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
            if (item.dataObject is TopAdsImageUiModel) {
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
