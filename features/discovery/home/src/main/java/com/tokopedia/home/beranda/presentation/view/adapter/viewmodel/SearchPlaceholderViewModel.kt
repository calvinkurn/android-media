package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel

import com.tokopedia.home.beranda.domain.model.SearchPlaceholder
import com.tokopedia.home.beranda.presentation.view.adapter.TrackedVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

class SearchPlaceholderViewModel : TrackedVisitable<HomeTypeFactory> {

    companion object{
        const val SEARCH_PLACE_HOLDER = 93812
    }

    private var searchPlaceholder: SearchPlaceholder? = null
    private var trackingData: Map<String, Any>? = null
    private var isCombined: Boolean = false
    private var trackingDataForCombination: List<Any> = emptyList()

    fun getSearchPlaceholder() = searchPlaceholder

    fun setSearchPlaceholder(placeholder: SearchPlaceholder) {
        this.searchPlaceholder = placeholder
    }

    override fun setTrackingData(trackingData: MutableMap<String, Any>) {
        this.trackingData = trackingData
    }

    override fun getTrackingData(): Map<String, Any>? = trackingData

    override fun getTrackingDataForCombination(): List<Any> = trackingDataForCombination

    override fun setTrackingDataForCombination(list: List<Any>) {
        this.trackingDataForCombination = list
    }

    override fun isTrackingCombined(): Boolean = isCombined

    override fun setTrackingCombined(isCombined: Boolean) {
        this.isCombined = isCombined
    }

    override fun type(typeFactory: HomeTypeFactory): Int = typeFactory.type(this)
}