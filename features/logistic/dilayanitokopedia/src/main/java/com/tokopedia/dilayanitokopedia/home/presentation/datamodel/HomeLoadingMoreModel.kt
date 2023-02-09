package com.tokopedia.dilayanitokopedia.home.presentation.datamodel

import android.os.Bundle
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.HomeTypeFactory

/**
 * Loading for recommendation feed
 */
class HomeLoadingMoreModel : HomeVisitable {

    override fun type(adapterTypeFactory: HomeTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

    override fun setTrackingData(trackingData: Map<String, Any>) {
        // no-op
    }

    override fun getTrackingData(): Map<String, Any>? {
        return null
    }

    override fun getTrackingDataForCombination(): List<Any>? {
        return null
    }

    override fun setTrackingDataForCombination(`object`: List<Any>) {
        // no-op
    }

    override fun isTrackingCombined(): Boolean {
        return false
    }

    override fun setTrackingCombined(isCombined: Boolean) {
        // no-op
    }

    override fun isCache(): Boolean {
        return false
    }

    override fun visitableId(): String? {
        return "loadingMoreModel"
    }

    override fun equalsWith(b: Any): Boolean {
        return false
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }
}
