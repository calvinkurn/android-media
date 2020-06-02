package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon

import android.os.Bundle
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

data class DynamicIconSectionDataModel (
        val itemList: List<DynamicHomeIcon.DynamicIcon> = listOf(),
        val dynamicIconWrap: Boolean = false,
        private var trackingData: Map<String, Any>? = null,
        private var isCombined: Boolean = false,
        private var trackingDataForCombination: List<Any> = emptyList(),
        private var isCache: Boolean = false
): HomeVisitable {
    override fun equalsWith(b: Any?): Boolean {
        if (b is DynamicIconSectionDataModel) {
            return itemList == b.itemList
        }
        return false
    }

    override fun visitableId(): String {
        return "dynamicIcon"
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    override fun isCache(): Boolean {
        return isCache
    }

    fun setCache(cache: Boolean) {
        isCache = cache
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
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
}
