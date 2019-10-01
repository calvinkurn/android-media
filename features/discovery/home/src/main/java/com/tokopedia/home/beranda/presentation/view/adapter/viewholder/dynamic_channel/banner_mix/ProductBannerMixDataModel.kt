package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel

class ProductBannerMixDataModel(
        val grid: DynamicHomeChannel.Grid,
        val channel: DynamicHomeChannel.Channels
) : Visitable<BannerMixTypeFactory> {
    override fun type(typeFactory: BannerMixTypeFactory): Int {
        return typeFactory.type(this)
    }
}