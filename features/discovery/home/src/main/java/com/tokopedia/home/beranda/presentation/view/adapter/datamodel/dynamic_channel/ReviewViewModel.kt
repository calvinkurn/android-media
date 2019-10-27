package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

data class ReviewViewModel(
        var url: String = "",
        var applink: String = ""
) : HomeVisitable<HomeTypeFactory> {

    override fun setTrackingData(trackingData: MutableMap<String, Any>?) {
    }

    override fun getTrackingData(): MutableMap<String, Any> {
        return mutableMapOf()
    }

    override fun getTrackingDataForCombination(): MutableList<Any> {
        return mutableListOf()
    }

    override fun setTrackingDataForCombination(`object`: MutableList<Any>?) {
    }

    override fun isTrackingCombined(): Boolean {
        return false
    }

    override fun setTrackingCombined(isCombined: Boolean) {
    }

    override fun isCache(): Boolean {
        return false
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}