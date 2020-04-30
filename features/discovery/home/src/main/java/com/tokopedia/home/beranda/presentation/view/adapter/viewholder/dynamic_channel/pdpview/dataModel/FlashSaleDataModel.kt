package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.listener.FlashSaleCardListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.typeFactory.FlashSaleCardTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardFlashSaleModel
import com.tokopedia.productcard.v2.BlankSpaceConfig

class FlashSaleDataModel (
        val productModel: ProductCardFlashSaleModel,
        val blankSpaceConfig: BlankSpaceConfig,
        val grid: DynamicHomeChannel.Grid,
        val impressHolder: ImpressHolder = ImpressHolder(),
        val applink: String = "",
        val listener: FlashSaleCardListener
): Visitable<FlashSaleCardTypeFactory>{
    override fun type(typeFactory: FlashSaleCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}