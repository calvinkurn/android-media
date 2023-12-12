package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.ShopFlashSaleTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetListener
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetDataModel
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ShopFlashSaleWidgetCallback(
    private val homeCategoryListener: HomeCategoryListener,
    private val homeRevampViewModel: HomeRevampViewModel,
): ShopFlashSaleWidgetListener {
    override fun onShopTabClicked(
        shopFlashSaleWidgetDataModel: ShopFlashSaleWidgetDataModel,
        trackingAttributionModel: TrackingAttributionModel,
        channelGrid: ChannelGrid
    ) {
        ShopFlashSaleTracking.sendClickShopTab(trackingAttributionModel, channelGrid)
        homeRevampViewModel.getShopFlashSale(shopFlashSaleWidgetDataModel, channelGrid.id)
    }

    override fun onShopTabImpressed(
        trackingAttributionModel: TrackingAttributionModel,
        channelGrid: ChannelGrid
    ) {
        val impression = ShopFlashSaleTracking.getImpressionShopTab(trackingAttributionModel, channelGrid)
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(impression)
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

    override fun onRefreshClick(
        shopFlashSaleWidgetDataModel: ShopFlashSaleWidgetDataModel,
        channelGrid: ChannelGrid
    ) {
        homeRevampViewModel.getShopFlashSale(shopFlashSaleWidgetDataModel, channelGrid.id)
    }
}
