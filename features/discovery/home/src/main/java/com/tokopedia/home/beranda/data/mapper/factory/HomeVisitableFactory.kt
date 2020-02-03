package com.tokopedia.home.beranda.data.mapper.factory

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BannerViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue

interface HomeVisitableFactory {
    fun buildVisitableList(homeData: HomeData, isCache: Boolean, trackingQueue: TrackingQueue, context: Context): HomeVisitableFactory
    fun addBannerVisitable(): HomeVisitableFactory
    fun addTickerVisitable(): HomeVisitableFactory
    fun addUserWalletVisitable(): HomeVisitableFactory
    fun addGeolocationVisitable(): HomeVisitableFactory
    fun addDynamicIconVisitable(): HomeVisitableFactory
    fun addDynamicChannelVisitable(): HomeVisitableFactory
    fun build(): MutableList<Visitable<*>>
}