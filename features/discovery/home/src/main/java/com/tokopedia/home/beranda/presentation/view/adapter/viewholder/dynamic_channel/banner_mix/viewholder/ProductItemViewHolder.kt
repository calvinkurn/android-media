package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.BannerOrganicViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.datamodel.ProductBannerMixDataModel
import com.tokopedia.home.beranda.presentation.view.customview.ThematicCardView

class ProductItemViewHolder(view: View,
                            val homeCategoryListener: HomeCategoryListener): AbstractViewHolder<ProductBannerMixDataModel>(view) {
    companion object {
        @LayoutRes
        val LAYOUT_ITEM = R.layout.home_banner_item
        val LAYOUT_ITEM_CAROUSEL = R.layout.home_banner_item_carousel
    }
    private val productCardView: ThematicCardView? by lazy { view.findViewById<ThematicCardView>(R.id.banner_item) }

    override fun bind(productBannerMixDataModel: ProductBannerMixDataModel) {
        productCardView?.run {
            val gridItem = productBannerMixDataModel.grid
            setItemWithWrapBlankSpaceConfig(gridItem, productBannerMixDataModel.blankSpaceConfig)
            setOnClickListener {
                val bannerType = when(productBannerMixDataModel.layoutType) {
                    DynamicChannelViewHolder.TYPE_BANNER -> BannerOrganicViewHolder.TYPE_NON_CAROUSEL
                    DynamicChannelViewHolder.TYPE_BANNER_CAROUSEL -> BannerOrganicViewHolder.TYPE_CAROUSEL
                    else -> BannerOrganicViewHolder.TYPE_NON_CAROUSEL
                }
                HomePageTracking.eventClickProductChannelMix(bannerType, productBannerMixDataModel.channel,  gridItem.freeOngkir.isActive ?: false, adapterPosition)
                homeCategoryListener.onSectionItemClicked(gridItem.applink)
            }
        }
    }
}