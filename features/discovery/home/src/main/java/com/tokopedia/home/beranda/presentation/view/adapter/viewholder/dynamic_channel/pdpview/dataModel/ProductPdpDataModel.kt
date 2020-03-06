package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.typeFactory.PdpViewTypeFactory
import com.tokopedia.productcard.v2.BlankSpaceConfig

class ProductPdpDataModel (
        val grid: DynamicHomeChannel.Grid,
        val blankSpaceConfig: BlankSpaceConfig
): Visitable<PdpViewTypeFactory>{
    override fun type(typeFactory: PdpViewTypeFactory): Int {
        return typeFactory.type(this)
    }
}