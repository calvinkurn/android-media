package com.tokopedia.home.beranda.data.mapper.factory

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.trackingoptimizer.TrackingQueue

interface HomeVisitableFactory {
    fun buildVisitableList(homeData: HomeData, isCache: Boolean, trackingQueue: TrackingQueue, context: Context): HomeVisitableFactory
    fun addBannerVisitable(): HomeVisitableFactory
    fun addTickerVisitable(): HomeVisitableFactory
    fun addUserWalletVisitable(): HomeVisitableFactory
    fun addGeolocationVisitable(): HomeVisitableFactory
    fun addDynamicIconVisitable(): HomeVisitableFactory
    fun addDynamicChannelVisitable(): HomeVisitableFactory
    fun build(): List<Visitable<*>>
}