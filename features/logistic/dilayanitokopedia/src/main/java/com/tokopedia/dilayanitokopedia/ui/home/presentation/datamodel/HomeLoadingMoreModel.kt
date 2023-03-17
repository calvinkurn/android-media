package com.tokopedia.dilayanitokopedia.ui.home.presentation.datamodel

import android.os.Bundle
import com.tokopedia.dilayanitokopedia.ui.home.presentation.adapter.HomeTypeFactory

/**
 * Loading for recommendation feed
 */
class HomeLoadingMoreModel : com.tokopedia.dilayanitokopedia.ui.home.presentation.datamodel.HomeVisitable {

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
