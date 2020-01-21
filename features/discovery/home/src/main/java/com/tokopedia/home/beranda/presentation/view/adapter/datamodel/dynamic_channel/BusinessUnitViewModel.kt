package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import java.util.concurrent.TimeUnit

class BusinessUnitViewModel(var title: String?, var position: Int, var forceRefresh: Boolean = false, var updatedTime: Long = 0) : HomeVisitable {
    private var isCache: Boolean = false
    private var trackingData: Map<String, Any>? = null
    private var trackingDataForCombination: List<Any>? = null
    private var isCombined: Boolean = false

    override fun equalsWith(b: Any?): Boolean {
        return !forceRefresh
    }
    override fun isCache(): Boolean {
        return isCache
    }
    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }
    override fun visitableId(): String? {
        return "dcSection"
    }

    fun setCache(cache: Boolean) {
        isCache = cache
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun setTrackingData(trackingData: Map<String, Any>) {
        this.trackingData = trackingData
    }

    override fun getTrackingData(): Map<String, Any>? {
        return trackingData
    }

    override fun getTrackingDataForCombination(): List<Any>? {
        return trackingDataForCombination
    }

    override fun setTrackingDataForCombination(trackingDataForCombination: List<Any>) {
        this.trackingDataForCombination = trackingDataForCombination
    }

    override fun isTrackingCombined(): Boolean {
        return isCombined
    }

    override fun setTrackingCombined(isCombined: Boolean) {
        this.isCombined = isCombined
    }
}
