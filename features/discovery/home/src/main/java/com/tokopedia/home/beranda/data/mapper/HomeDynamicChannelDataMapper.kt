package com.tokopedia.home.beranda.data.mapper

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.mapper.factory.HomeDynamicChannelVisitableFactory
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_MAP_TO_HOME_VIEWMODEL
import com.tokopedia.trackingoptimizer.TrackingQueue

class HomeDynamicChannelDataMapper(
        private val context: Context,
        private val homeDynamicChannelVisitableFactory: HomeDynamicChannelVisitableFactory,
        private val trackingQueue: TrackingQueue
) {
    fun mapToDynamicChannelDataModel(homeChannelData: HomeChannelData? = null,
                                     isCache: Boolean,
                                     addLoadingMore: Boolean,
                                     useDefaultWhenEmpty: Boolean = true,
                                     startPosition: Int = 0): List<Visitable<*>>{
        BenchmarkHelper.beginSystraceSection(TRACE_MAP_TO_HOME_VIEWMODEL)
        if (homeChannelData == null) return listOf()
        val list: List<Visitable<*>> = homeDynamicChannelVisitableFactory.buildVisitableList(
                homeChannelData, isCache, trackingQueue, context)
                .addDynamicChannelVisitable(addLoadingMore, useDefaultWhenEmpty, startPosition)
                .build()
        BenchmarkHelper.endSystraceSection()
        return list
    }

}
