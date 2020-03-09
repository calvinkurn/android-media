package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.typeFactory.MixTopTypeFactory

class MixTopSeeMoreDataModel(
        val channel: DynamicHomeChannel.Channels
) : MixTopVisitable {
    override fun type(typeFactory: MixTopTypeFactory): Int {
        return typeFactory.type(this)
    }
}