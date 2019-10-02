package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.typefactory.BannerMixTypeFactory

class SeeMoreBannerMixDataModel(
        val channel: DynamicHomeChannel.Channels
) : Visitable<BannerMixTypeFactory> {
    override fun type(typeFactory: BannerMixTypeFactory): Int {
        return typeFactory.type(this)
    }
}