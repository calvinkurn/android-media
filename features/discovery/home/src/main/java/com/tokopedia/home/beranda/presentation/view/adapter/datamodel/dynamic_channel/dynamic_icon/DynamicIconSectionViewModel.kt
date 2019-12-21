package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon

import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

import java.util.ArrayList

class DynamicIconSectionViewModel : HomeVisitable {
    override fun visitableId(): String {
        return "dynamicIcon"
    }

    private var trackingData: Map<String, Any>? = null
    private var isCombined: Boolean = false
    private var trackingDataForCombination: List<Any> = emptyList()
    val itemList: MutableList<HomeIconItem>
    private var isCache: Boolean = false
    var dynamicIconWrap: Boolean = false

    override fun isCache(): Boolean {
        return isCache
    }

    fun setCache(cache: Boolean) {
        isCache = cache
    }

    init {
        itemList = ArrayList()
    }

    fun addItem(item: HomeIconItem) {
        this.itemList.add(item)
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
