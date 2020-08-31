package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.listener

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.FlashSaleDataModel

interface FlashSaleCardListener {
    //title listener
    fun onBannerSeeMoreClicked(applink: String, channel: DynamicHomeChannel.Channels)

    //flash sale card listener
    fun onFlashSaleCardImpressed(position: Int, channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid)
    fun onFlashSaleCardClicked(position: Int, channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, applink: String)

}