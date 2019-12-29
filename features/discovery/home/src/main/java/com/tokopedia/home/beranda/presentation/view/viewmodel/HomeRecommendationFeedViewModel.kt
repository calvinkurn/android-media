package com.tokopedia.home.beranda.presentation.view.viewmodel

import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.FeedTabModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

/**
 * Created by devarafikry on 27/08/19.
 */

class HomeRecommendationFeedViewModel : HomeVisitable {

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
        return "recommendationSection"
    }

    var feedTabModel: List<FeedTabModel>? = null

    var isNewData = true

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
