package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.analytics.performance.perf.performanceTracing.components.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HeaderInterface
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

class PullToRefreshDataModel : HeaderInterface {
    override fun equalsWith(b: Any?): Boolean {
        return false
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun setTrackingData(trackingData: MutableMap<String, Any>?) { }

    override fun getTrackingData(): MutableMap<String, Any> = mutableMapOf()

    override fun getTrackingDataForCombination(): MutableList<Any> = mutableListOf()

    override fun setTrackingDataForCombination(`object`: MutableList<Any>?) { }

    override fun isTrackingCombined(): Boolean = false

    override fun setTrackingCombined(isCombined: Boolean) { }

    override fun isCache(): Boolean = false

    override fun visitableId(): String {
        return "pullToRefresh"
    }
}

interface HeaderInterface: HomeVisitable
