package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.typeFactory.FlashSaleCardTypeFactory
import com.tokopedia.productcard.v2.BlankSpaceConfig

class MixTopProductDataModel(
        val grid: DynamicHomeChannel.Grid,
        val channel: DynamicHomeChannel.Channels,
        val blankSpaceConfig: BlankSpaceConfig,
        val positionOnWidgetHome: String
) : Visitable<FlashSaleCardTypeFactory> {
    override fun type(typeFactory: FlashSaleCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}