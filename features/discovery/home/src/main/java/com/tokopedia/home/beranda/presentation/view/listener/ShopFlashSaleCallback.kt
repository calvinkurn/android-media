package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.ShopFlashSaleTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleListener
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetDataModel
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ShopFlashSaleCallback(
    private val homeCategoryListener: HomeCategoryListener,
    private val homeRevampViewModel: HomeRevampViewModel,
): ShopFlashSaleListener {
    override fun onShopTabClicked(
        shopFlashSaleWidgetDataModel: ShopFlashSaleWidgetDataModel,
        trackingAttributionModel: TrackingAttributionModel,
        channelGrid: ChannelGrid
    ) {
        ShopFlashSaleTracking.sendClickShopTab(trackingAttributionModel)
        homeRevampViewModel.getShopFlashSale(shopFlashSaleWidgetDataModel, channelGrid.id)
    }

    override fun onShopNameClicked(
        trackingAttributionModel: TrackingAttributionModel,
        channelGrid: ChannelGrid
    ) {
        ShopFlashSaleTracking.sendClickShopName(trackingAttributionModel)
        homeCategoryListener.onDynamicChannelClicked(channelGrid.applink)
    }

    override fun onSeeAllClick(trackingAttributionModel: TrackingAttributionModel, link: String) {
        ShopFlashSaleTracking.sendClickViewAll(trackingAttributionModel)
        homeCategoryListener.onDynamicChannelClicked(link)
    }

    override fun onProductCardImpressed(
        trackingAttributionModel: TrackingAttributionModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {
        val impression = ShopFlashSaleTracking.getImpressionProduct(
            trackingAttributionModel,
            channelGrid,
            homeCategoryListener.userId
        )
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(impression)
    }

    override fun onProductCardClicked(
        trackingAttributionModel: TrackingAttributionModel,
        channelGrid: ChannelGrid,
        position: Int,
        applink: String
    ) {
        ShopFlashSaleTracking.sendClickProduct(
            trackingAttributionModel,
            channelGrid,
            homeCategoryListener.userId
        )
        homeCategoryListener.onDynamicChannelClicked(applink)
    }

    override fun onSeeMoreCardClicked(
        trackingAttributionModel: TrackingAttributionModel,
        applink: String
    ) {
        ShopFlashSaleTracking.sendClickViewAllCard(trackingAttributionModel)
        homeCategoryListener.onDynamicChannelClicked(applink)
    }
}
