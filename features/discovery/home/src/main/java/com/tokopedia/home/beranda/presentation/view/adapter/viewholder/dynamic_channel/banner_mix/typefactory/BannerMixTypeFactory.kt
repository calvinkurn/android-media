package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.typefactory

import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.datamodel.ProductBannerMixDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix.datamodel.SeeMoreBannerMixDataModel

interface BannerMixTypeFactory {
    fun type(productBannerMixDataModel: ProductBannerMixDataModel) : Int
    fun type(seeMoreBannerMixDataModel: SeeMoreBannerMixDataModel) : Int
}