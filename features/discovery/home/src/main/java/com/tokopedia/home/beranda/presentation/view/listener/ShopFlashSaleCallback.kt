package com.tokopedia.home.beranda.presentation.view.listener

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
        homeRevampViewModel.getShopFlashSale(shopFlashSaleWidgetDataModel, channelGrid.id)
    }

    override fun onShopNameClicked(
        trackingAttributionModel: TrackingAttributionModel,
        channelGrid: ChannelGrid
    ) {
        homeCategoryListener.onDynamicChannelClicked(channelGrid.applink)
    }

    override fun onSeeAllClick(trackingAttributionModel: TrackingAttributionModel, link: String) {
        homeCategoryListener.onDynamicChannelClicked(link)
    }
}
