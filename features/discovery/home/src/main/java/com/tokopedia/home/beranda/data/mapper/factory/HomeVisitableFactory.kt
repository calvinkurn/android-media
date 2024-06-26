package com.tokopedia.home.beranda.data.mapper.factory

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.trackingoptimizer.TrackingQueue

interface HomeVisitableFactory {
    fun buildVisitableList(homeData: HomeData, isCache: Boolean, trackingQueue: TrackingQueue, context: Context, dynamicChannelDataMapper: HomeDynamicChannelDataMapper): HomeVisitableFactory
    fun addBannerVisitable(): HomeVisitableFactory
    fun addHomeHeader(): HomeVisitableFactory
    fun addTickerVisitable(): HomeVisitableFactory
    fun addDynamicIconVisitable(isCache: Boolean): HomeVisitableFactory
    fun addAtfComponentVisitable(isProcessingAtf: Boolean, isCache: Boolean): HomeVisitableFactory
    fun addDynamicChannelVisitable(addLoadingMore: Boolean, useDefaultWhenEmpty: Boolean): HomeVisitableFactory
    fun build(): List<Visitable<*>>
}
