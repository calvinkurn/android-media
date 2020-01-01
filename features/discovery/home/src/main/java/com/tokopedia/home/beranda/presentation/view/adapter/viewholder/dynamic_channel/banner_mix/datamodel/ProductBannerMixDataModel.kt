package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.typefactory.BannerMixTypeFactory
import com.tokopedia.productcard.v2.BlankSpaceConfig

class ProductBannerMixDataModel(
        val grid: DynamicHomeChannel.Grid,
        val channel: DynamicHomeChannel.Channels,
        val blankSpaceConfig: BlankSpaceConfig
) : Visitable<BannerMixTypeFactory> {
    override fun type(typeFactory: BannerMixTypeFactory): Int {
        return typeFactory.type(this)
    }
}