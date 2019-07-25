package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel

import com.tokopedia.home.beranda.presentation.view.adapter.TrackedVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

class GeolocationPromptViewModel : TrackedVisitable<HomeTypeFactory> {
    override fun setTrackingData(trackingData: MutableMap<String, Any>?) {

    }

    override fun getTrackingData(): MutableMap<String, Any> {
        return trackingData
    }

    override fun getTrackingDataForCombination(): MutableList<Any> {
        return arrayListOf()
    }

    override fun setTrackingDataForCombination(`object`: MutableList<Any>?) {

    }

    override fun isTrackingCombined(): Boolean {
        return false
    }

    override fun setTrackingCombined(isCombined: Boolean) {

    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

}