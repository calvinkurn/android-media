package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel

class SeeMoreBannerMixDataModel(
        val textColor: String,
        val backImageUrl: String,
        val seeMoreApplink: String
) : Visitable<BannerMixTypeFactory> {
    override fun type(typeFactory: BannerMixTypeFactory): Int {
        return typeFactory.type(this)
    }
}