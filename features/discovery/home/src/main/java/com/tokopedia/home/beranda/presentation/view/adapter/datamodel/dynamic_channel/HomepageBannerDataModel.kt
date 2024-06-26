package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.analytics.performance.perf.performanceTracing.components.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by errysuprayogi on 11/28/17.
 */

class HomepageBannerDataModel : ImpressHolder(), HomeVisitable, LoadableComponent by BlocksLoadableComponent(
    customBlocksName = "HomePageBanner"
) {
    var slides: List<BannerSlidesModel>? = null
    set(value) {
        field = value
        finishLoading()
    }
    var createdTimeMillis = ""
    private var isCache: Boolean = false
    private var trackingData: Map<String, Any>? = null
    private var trackingDataForCombination: List<Any>? = null
    private var isCombined: Boolean = false

    override fun equalsWith(b: Any?): Boolean {
        this.finishLoading()
        return if (b is HomepageBannerDataModel) {
            createdTimeMillis == b.createdTimeMillis
        }
        else false
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
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
