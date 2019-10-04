package com.tokopedia.home.beranda.data.mapper.factory

import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BannerViewModel

class HomeVisitableFactoryImpl : HomeVisitableFactory {
    override fun createBannerVisitable(slides: MutableList<BannerSlidesModel>,
                                       isCache: Boolean): BannerViewModel {
        val bannerViewModel = BannerViewModel()
        bannerViewModel.slides = slides
        bannerViewModel.isCache = isCache
        slides.forEachIndexed { index, bannerSlidesModel ->
            bannerSlidesModel.position = index+1
        }
        return bannerViewModel
    }
}