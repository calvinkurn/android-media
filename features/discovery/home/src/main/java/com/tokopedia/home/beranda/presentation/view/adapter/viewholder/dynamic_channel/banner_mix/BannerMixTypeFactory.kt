package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.banner_mix

interface BannerMixTypeFactory {
    fun type(productBannerMixDataModel: ProductBannerMixDataModel) : Int
    fun type(seeMoreBannerMixDataModel: SeeMoreBannerMixDataModel) : Int
}