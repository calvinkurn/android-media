package com.tokopedia.home.beranda.data.mapper.factory

import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BannerViewModel

interface HomeVisitableFactory {
    fun createBannerVisitable(
            slides: MutableList<BannerSlidesModel>,
            isCache: Boolean): BannerViewModel
}