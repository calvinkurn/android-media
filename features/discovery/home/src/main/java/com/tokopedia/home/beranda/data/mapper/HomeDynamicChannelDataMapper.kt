package com.tokopedia.home.beranda.data.mapper

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.mapper.factory.HomeDynamicChannelVisitableFactory
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_MAP_TO_HOME_VIEWMODEL
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.trackingoptimizer.TrackingQueue

class HomeDynamicChannelDataMapper(
        private val context: Context,
        private val homeDynamicChannelVisitableFactory: HomeDynamicChannelVisitableFactory,
        private val trackingQueue: TrackingQueue
) {
    fun mapToDynamicChannelDataModel(homeChannelData: HomeChannelData?, isCache: Boolean): List<Visitable<*>>{
        BenchmarkHelper.beginSystraceSection(TRACE_MAP_TO_HOME_VIEWMODEL)
        if (homeChannelData == null) return listOf()
        val list: List<Visitable<*>> = homeDynamicChannelVisitableFactory.buildVisitableList(
                homeChannelData, isCache, trackingQueue, context)
                .addDynamicChannelVisitable()
                .build()
        BenchmarkHelper.endSystraceSection()
        return list
    }
}
