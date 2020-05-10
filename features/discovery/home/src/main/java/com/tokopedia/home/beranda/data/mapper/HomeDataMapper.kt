package com.tokopedia.home.beranda.data.mapper

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.BenchmarkHelper
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.trackingoptimizer.TrackingQueue

class HomeDataMapper(
        private val context: Context,
        private val homeVisitableFactory: HomeVisitableFactory,
        private val trackingQueue: TrackingQueue
) {
    fun mapToHomeViewModel(homeData: HomeData?, isCache: Boolean): HomeDataModel{
        BenchmarkHelper.beginSystraceSection("HomeDataMapper.mapToHomeViewModel")
        if (homeData == null) return HomeDataModel(isCache = isCache)
        val list: List<Visitable<*>> = homeVisitableFactory.buildVisitableList(
                homeData, isCache, trackingQueue, context)
                .addBannerVisitable()
                .addTickerVisitable()
                .addUserWalletVisitable()
                .addDynamicIconVisitable()
                .addGeolocationVisitable()
                .addDynamicChannelVisitable()
                .build()
        BenchmarkHelper.endSystraceSection()
        return HomeDataModel(homeData.homeFlag, list, isCache)
    }
}
