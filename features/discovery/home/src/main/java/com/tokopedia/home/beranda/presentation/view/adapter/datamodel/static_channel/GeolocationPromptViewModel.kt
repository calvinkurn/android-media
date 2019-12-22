package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel

import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

class GeolocationPromptViewModel : HomeVisitable {
    override fun visitableId(): String {
        return "geolocation"
    }

    override fun equalsWith(b: Any?): Boolean {
        if (b is GeolocationPromptViewModel) {
            return visitableId() == b.visitableId()
        }
        return false
    }

    private var isCache: Boolean = false
    private var trackingData: Map<String, Any>? = null
    private var isCombined: Boolean = false
    private var trackingDataForCombination: List<Any> = emptyList()

    override fun isCache(): Boolean {
        return isCache
    }

    fun setCache(cache: Boolean) {
        isCache = cache
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun setTrackingData(trackingData: MutableMap<String, Any>) {
        this.trackingData = trackingData
    }

    override fun getTrackingData(): Map<String, Any>? = trackingData

    override fun getTrackingDataForCombination(): List<Any> = trackingDataForCombination

    override fun setTrackingDataForCombination(list: List<Any>) {
        this.trackingDataForCombination = list
    }

    override fun isTrackingCombined(): Boolean = isCombined

    override fun setTrackingCombined(isCombined: Boolean) {
        this.isCombined = isCombined
    }
}