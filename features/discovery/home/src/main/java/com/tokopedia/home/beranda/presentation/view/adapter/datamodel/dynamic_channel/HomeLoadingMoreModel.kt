package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

class HomeLoadingMoreModel : HomeVisitable {

    override fun type(adapterTypeFactory: HomeTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

    override fun setTrackingData(trackingData: Map<String, Any>) {

    }

    override fun getTrackingData(): Map<String, Any>? {
        return null
    }

    override fun getTrackingDataForCombination(): List<Any>? {
        return null
    }

    override fun setTrackingDataForCombination(`object`: List<Any>) {

    }

    override fun isTrackingCombined(): Boolean {
        return false
    }

    override fun setTrackingCombined(isCombined: Boolean) {

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