package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

class SearchPlaceholderViewModel : HomeVisitable {
    override fun visitableId(): String {
        return "search"
    }

    override fun equalsWith(b: Any?): Boolean {
        if (b is SearchPlaceholderViewModel) {
            return searchPlaceholder == b.searchPlaceholder
        }
        return false
    }

    override fun isCache(): Boolean {
        return cache
    }

    companion object{
        const val SEARCH_PLACE_HOLDER = 93812
    }

    private var searchPlaceholder: SearchPlaceholder? = null
    private var trackingData: Map<String, Any>? = null
    private var isCombined: Boolean = false
    private var trackingDataForCombination: List<Any> = emptyList()
    var cache = false
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

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }
}