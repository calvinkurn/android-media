package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.listener

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.FlashSaleDataModel

interface FlashSaleCardListener {
    //title listener
    fun onMixLeftSeeMoreClicked(applink: String, channel: DynamicHomeChannel.Channels)

    //flash sale card listener
    fun onFlashSaleCardImpressed(position: Int, channel: DynamicHomeChannel.Channels)
    fun onFlashSaleCardClicked(position: Int, channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid)
}