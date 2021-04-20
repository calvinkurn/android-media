package com.tokopedia.home.beranda.data.mapper.factory

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.trackingoptimizer.TrackingQueue

interface HomeDynamicChannelVisitableFactory {
    fun buildVisitableList(homeChannelData: HomeChannelData, isCache: Boolean, trackingQueue: TrackingQueue, context: Context): HomeDynamicChannelVisitableFactory
    fun addDynamicChannelVisitable(addLoadingMore: Boolean, useDefaultWhenEmpty: Boolean, startPosition: Int): HomeDynamicChannelVisitableFactory
    fun build(): List<Visitable<*>>
}