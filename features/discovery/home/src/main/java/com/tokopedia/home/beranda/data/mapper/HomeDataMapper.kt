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
    companion object {
        private const val ATF_ERROR_MESSAGE = "Showing cache data because atf is error"
        private const val DC_ERROR_MESSAGE = "Showing cache data because dynamic channel is error"
        private const val SHIMMERING_CHANNEL_ID_0 = "0"
        private const val SHIMMERING_CHANNEL_ID_1 = "1"
    }

    fun mapToHomeRevampViewModel(homeData: HomeData?, isCache: Boolean, addShimmeringChannel: Boolean = false, isLoadingAtf: Boolean = false, haveCachedData: Boolean = false): HomeDynamicChannelModel {
        BenchmarkHelper.beginSystraceSection(TRACE_MAP_TO_HOME_VIEWMODEL_REVAMP)
        if (homeData == null) return HomeDynamicChannelModel(isCache = isCache, flowCompleted = true)
        var processingAtf = homeData.atfData?.isProcessingAtf ?: false
        var processingDynamicChannel = homeData.isProcessingDynamicChannel

        if (isCache) {
            processingAtf = false
            processingDynamicChannel = false
        } else {
            if (homeData.atfData?.dataList?.isEmpty() == true && haveCachedData) {
                throw IllegalStateException(ATF_ERROR_MESSAGE)
            }
            if (homeData.dynamicHomeChannel.channels.isEmpty() && haveCachedData) {
                throw IllegalStateException(DC_ERROR_MESSAGE)
            }
        }
        val firstPage = homeData.token.isNotEmpty()
        val factory: HomeVisitableFactory = homeVisitableFactory.buildVisitableList(
            homeData,
            isCache,
            trackingQueue,
            context,
            homeDynamicChannelDataMapper
        )
            .addHomeHeaderOvo()
            .addAtfComponentVisitable(processingAtf, isCache)

        if (!processingDynamicChannel && !isLoadingAtf) {
            factory.addDynamicChannelVisitable(firstPage, true)
                .build()
        }

        BenchmarkHelper.endSystraceSection()
        val mutableVisitableList = factory.build().toMutableList()
        if (addShimmeringChannel && mutableVisitableList.size > 1) {
            mutableVisitableList.add(ShimmeringChannelDataModel(SHIMMERING_CHANNEL_ID_0))
            mutableVisitableList.add(ShimmeringChannelDataModel(SHIMMERING_CHANNEL_ID_1))
        }

        return HomeDynamicChannelModel(
            list = mutableVisitableList,
            isCache = isCache,
            isFirstPage = firstPage,
            homeChooseAddressData = HomeChooseAddressData(true),
            flowCompleted = false
        )
    }

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
