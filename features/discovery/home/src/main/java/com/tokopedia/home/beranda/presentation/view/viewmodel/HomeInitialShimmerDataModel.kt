package com.tokopedia.home.beranda.presentation.view.viewmodel

import android.os.Bundle
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecommendationTabDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

data class HomeInitialShimmerDataModel(
        val id: Int = 1234
) : HomeVisitable {

    //keep this section if exist, because this viewholder is heavy to render
    override fun equalsWith(b: Any?): Boolean {
        return true
    }

    override fun setTrackingData(trackingData: MutableMap<String, Any>?) {

    }

    override fun getTrackingData(): MutableMap<String, Any>? {
        return null
    }

    override fun getTrackingDataForCombination(): MutableList<Any>? {
        return null
    }

    override fun setTrackingDataForCombination(`object`: MutableList<Any>?) {

    }

    override fun isTrackingCombined(): Boolean {
        return false
    }

    override fun setTrackingCombined(isCombined: Boolean) {

    }

    override fun isCache(): Boolean {
        return isCache
    }

    override fun visitableId(): String {
        return "homeInitialShimmer"
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }
}