package com.tokopedia.home.beranda.data.mapper

import android.content.Context
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.data.model.HomeChooseAddressData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_MAP_TO_HOME_VIEWMODEL_REVAMP
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ShimmeringChannelDataModel
import com.tokopedia.trackingoptimizer.TrackingQueue

class HomeDataMapper(
    private val context: Context,
    private val homeVisitableFactory: HomeVisitableFactory,
    private val trackingQueue: TrackingQueue,
    private val homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper
) {

    /**
     * for the new atf mechanism.
     * this function only map dynamic channel, ignoring header & atf
     */
    fun mapDynamicChannel(homeData: HomeData?, isCache: Boolean): HomeDynamicChannelModel {
        if (homeData == null) return HomeDynamicChannelModel(isCache = isCache, flowCompleted = true)
        var processingDynamicChannel = homeData.isProcessingDynamicChannel

        if (isCache) {
            processingDynamicChannel = false
        }
        val firstPage = homeData.token.isNotEmpty()
        val factory: HomeVisitableFactory = homeVisitableFactory.buildVisitableList(
            homeData,
            isCache,
            trackingQueue,
            context,
            homeDynamicChannelDataMapper
        )

        if (!processingDynamicChannel) {
            factory.addDynamicChannelVisitable(firstPage, true)
                .build()
        }
        val mutableVisitableList = factory.build().toMutableList()

        return HomeDynamicChannelModel(
            list = mutableVisitableList,
            isCache = isCache,
            isFirstPage = firstPage,
            homeChooseAddressData = HomeChooseAddressData(true),
            flowCompleted = false
        )
    }
}
