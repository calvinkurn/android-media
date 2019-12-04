package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.datamodel.ProductBannerMixDataModel
import com.tokopedia.home.beranda.presentation.view.customview.ThematicCardView
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid

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
            initFreeOngkir(gridItem.freeOngkir.isActive, gridItem.freeOngkir.imageUrl)
            initSlashedPrice(gridItem.slashedPrice)
            initProductPrice(gridItem.price)
            initProductImage(gridItem.imageUrl)
            initProductName(gridItem.name)
            initLabelDiscount(gridItem.discount)
            setOnClickListener {
                HomePageTracking.eventClickProductChannelMix(context, productBannerMixDataModel.channel,  gridItem.freeOngkir.isActive ?: false, adapterPosition)
                homeCategoryListener.onSectionItemClicked(gridItem.applink)
            }
        }
    }

    val productCard: ProductCardViewSmallGrid by lazy { view.findViewById<ProductCardViewSmallGrid>(R.id.banner_item) }
}