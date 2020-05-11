package com.tokopedia.home.beranda.presentation.view.adapter.viewholder

import android.content.Context
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeInspirationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TopAdsDynamicChannelModel
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.domain.model.Shop
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.widget.TopAdsDynamicChannelView

class TopAdsDynamicChannelViewHolder(itemView: View, private val listener: HomeInspirationListener) : AbstractViewHolder<TopAdsDynamicChannelModel>(itemView), TopAdsItemClickListener {
    private val topAdsDynamicChannelView: TopAdsDynamicChannelView
    private val context: Context

    init {
        this.context = itemView.context
        topAdsDynamicChannelView = itemView as TopAdsDynamicChannelView
        topAdsDynamicChannelView.setAdsItemClickListener(this)
        topAdsDynamicChannelView.setAdsItemImpressionListener(object : TopAdsItemImpressionListener() {
            override fun onImpressionProductAdsItem(position: Int, product: Product) {
                TopAdsGtmTracker.eventHomeProductView(context, product, position)
            }
        })
    }

    override fun bind(element: TopAdsDynamicChannelModel) {
        topAdsDynamicChannelView.setData(element.title, element.items!!)
    }

    override fun onProductItemClicked(position: Int, product: Product) {
        listener.onGoToProductDetailFromInspiration(
                product.id,
                product.image.m_ecs,
                product.name,
                product.priceFormat
        )
        TopAdsGtmTracker.eventHomeProductClick(context, product, position)
    }

    override fun onShopItemClicked(position: Int, shop: Shop) {}

    override fun onAddFavorite(position: Int, data: Data) {}

    companion object {

        val LAYOUT = R.layout.layout_item_dynamic_channel_ads
    }
}
