package com.tokopedia.home.beranda.data.mapper

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_MAP_TO_HOME_VIEWMODEL
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.trackingoptimizer.TrackingQueue

class HomeDataMapper(
        private val context: Context,
        private val homeVisitableFactory: HomeVisitableFactory,
        private val trackingQueue: TrackingQueue,
        private val homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper
) {
    fun mapToHomeViewModel(homeData: HomeData?, isCache: Boolean, showGeolocation: Boolean = true): HomeDataModel{
        BenchmarkHelper.beginSystraceSection(TRACE_MAP_TO_HOME_VIEWMODEL)
        if (homeData == null) return HomeDataModel(isCache = isCache)
        val addLoadingMore = homeData.token.isNotEmpty()
        val factory: HomeVisitableFactory = homeVisitableFactory.buildVisitableList(
                homeData, isCache, trackingQueue, context, homeDynamicChannelDataMapper)
                .addBannerVisitable()
                .addTickerVisitable()
                .addUserWalletVisitable()
                .addDynamicIconVisitable()

        if (showGeolocation) factory.addGeolocationVisitable()

        factory.addDynamicChannelVisitable(addLoadingMore)
                .build()

        BenchmarkHelper.endSystraceSection()
        return HomeDataModel(homeData.homeFlag, factory.build(), isCache, addLoadingMore)
    }
}
