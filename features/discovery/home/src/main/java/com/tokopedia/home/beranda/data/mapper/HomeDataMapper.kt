package com.tokopedia.home.beranda.data.mapper

import android.content.Context
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.data.model.HomeChooseAddressData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_MAP_TO_HOME_VIEWMODEL_REVAMP
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ShimmeringChannelDataModel
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment
import com.tokopedia.home.constant.AtfKey
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

    fun mapToHomeRevampViewModel(homeData: HomeData?, isCache: Boolean, addShimmeringChannel: Boolean = false, isLoadingAtf: Boolean = false, haveCachedData: Boolean = false): HomeDynamicChannelModel{
        BenchmarkHelper.beginSystraceSection(TRACE_MAP_TO_HOME_VIEWMODEL_REVAMP)
        if (homeData == null) return HomeDynamicChannelModel(isCache = isCache, flowCompleted = true)
        var processingAtf = homeData.atfData?.isProcessingAtf?: false
        var processingDynamicChannel = homeData.isProcessingDynamicChannel

        var isChannelBeautyFest = HomeRevampFragment.BEAUTY_FEST_NOT_SET
        if (isCache) {
            processingAtf = false
            processingDynamicChannel = false
        } else {
            if (homeData.atfData?.dataList?.isEmpty() == true && haveCachedData ) {
                throw IllegalStateException(ATF_ERROR_MESSAGE)
            }
            if (homeData.dynamicHomeChannel.channels.isEmpty() && haveCachedData) {
                throw IllegalStateException(DC_ERROR_MESSAGE)
            }
            var isDynamicChannelContainsBeautyFest = false
            var isAtfChannelContainsBeautyFest = false

            val beautyFestChannelId = arrayOf("129362",
                "129363",
                "129364",
                "129365",
                "129366",
                "129367",
                "129368",
                "129369",
                "129370",
                "129371")

            for (channel in homeData.dynamicHomeChannel.channels) {
                isDynamicChannelContainsBeautyFest = channel.id in beautyFestChannelId
                if(isDynamicChannelContainsBeautyFest)
                    break
            }
            for (atfChannel in homeData.atfData?.dataList?: listOf()) {
                if (atfChannel.component == AtfKey.TYPE_CHANNEL) {
                    isAtfChannelContainsBeautyFest = beautyFestChannelId.filter {
                        atfChannel.content?.contains(it)?:false }.isNotEmpty()
                }
            }
            isChannelBeautyFest = if (isAtfChannelContainsBeautyFest || isDynamicChannelContainsBeautyFest) {
                HomeRevampFragment.BEAUTY_FEST_TRUE
            } else {
                HomeRevampFragment.BEAUTY_FEST_FALSE
            }
        }
        val firstPage = homeData.token.isNotEmpty()
        val factory: HomeVisitableFactory = homeVisitableFactory.buildVisitableList(
                homeData, isCache, trackingQueue, context, homeDynamicChannelDataMapper)
                .addHomeHeaderOvo()
                .addAtfComponentVisitable(processingAtf)

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
                homeFlag = homeData.homeFlag,
                list = mutableVisitableList,
                isCache = isCache,
                isFirstPage = firstPage,
                homeChooseAddressData = HomeChooseAddressData(true),
                flowCompleted = false,
                isBeautyFest = isChannelBeautyFest
        )
    }
}
