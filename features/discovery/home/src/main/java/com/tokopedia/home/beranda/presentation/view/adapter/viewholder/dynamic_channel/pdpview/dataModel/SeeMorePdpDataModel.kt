package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.listener.FlashSaleCardListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.typeFactory.FlashSaleCardTypeFactory

class SeeMorePdpDataModel (
        val applink: String = "",
        val backgroundImage: String = "",
        val listener: FlashSaleCardListener
): Visitable<FlashSaleCardTypeFactory>{
    override fun type(typeFactory: FlashSaleCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}