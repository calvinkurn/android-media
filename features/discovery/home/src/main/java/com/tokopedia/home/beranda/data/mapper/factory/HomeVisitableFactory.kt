package com.tokopedia.home.beranda.data.mapper.factory

import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BannerViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderViewModel
import org.jetbrains.annotations.NotNull

interface HomeVisitableFactory {
    fun createBannerVisitable(
            bannerDataModel: BannerDataModel?,
            isCache: Boolean): BannerViewModel

    fun createOvoTokopointVisitable(
            hasTokopoints: Boolean,
            isCache: Boolean): HeaderViewModel?
}