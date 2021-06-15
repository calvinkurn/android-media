package com.tokopedia.home.beranda.presentation.view.viewmodel

import android.os.Bundle
import com.tokopedia.home.beranda.data.model.HomeChooseAddressData
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecommendationTabDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

/**
 * Created by devarafikry on 27/08/19.
 */

data class HomeRecommendationFeedDataModel(
        var homeChooseAddressData: HomeChooseAddressData? = null,
        var recommendationTabDataModel: List<RecommendationTabDataModel>? = null
) : HomeVisitable {
    var isNewData = true

    //keep this section if exist, because this viewholder is heavy to render
    override fun equalsWith(b: Any?): Boolean {
        (b as? HomeRecommendationFeedDataModel)?.let { newRecomDataModel ->
            return newRecomDataModel == this
        }
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

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }
}
