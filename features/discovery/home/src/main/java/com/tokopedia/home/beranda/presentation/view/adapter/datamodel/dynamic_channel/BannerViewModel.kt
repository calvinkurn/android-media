package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by errysuprayogi on 11/28/17.
 */

class BannerViewModel : ImpressHolder(), HomeVisitable {

    var slides: List<BannerSlidesModel>? = null
    private var isCache: Boolean = false
    private var trackingData: Map<String, Any>? = null
    private var trackingDataForCombination: List<Any>? = null
    private var isCombined: Boolean = false

    override fun equalsWith(b: Any?): Boolean {
        if (b is BannerViewModel) {
            return slides == b.slides
        }
        return false
    }

    override fun isCache(): Boolean {
        return isCache
    }

    override fun visitableId(): String {
        return "bannerSlider"
    }

    fun setCache(cache: Boolean) {
        isCache = cache
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    //avoid setter and gettter for tracking from parent class, implement your tracker in viewholders
    @Deprecated("")
    override fun setTrackingData(trackingData: Map<String, Any>) {
        this.trackingData = trackingData
    }

    //avoid setter and gettter for tracking from parent class, implement your tracker in viewholders
    @Deprecated("")
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
