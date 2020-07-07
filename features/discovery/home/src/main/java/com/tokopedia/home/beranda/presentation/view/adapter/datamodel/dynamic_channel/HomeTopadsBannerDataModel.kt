package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class HomeTopadsBannerDataModel(
        var topAdsImageViewModel: TopAdsImageViewModel
) : ImpressHolder(), HomeVisitable {
    override fun equalsWith(b: Any?): Boolean {
        if (b is HomeTopadsBannerDataModel) {
            return topAdsImageViewModel.imageUrl == b.topAdsImageViewModel.imageUrl
        }
        return false
    }
    override fun visitableId(): String {
        return topAdsImageViewModel.imageUrl?: DEFAULT_TOPADS_ID
    }

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

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    companion object{
        private const val DEFAULT_TOPADS_ID = "TOPADS_BANNER"
    }
}