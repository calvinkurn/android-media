package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.HomeIconItem
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

import java.util.ArrayList

/**
 * @author by errysuprayogi on 11/28/17.
 */

/**
 * No further development for this datamodel
 * Use dynamic icon section instead
 */

@Deprecated("")
class UseCaseIconSectionViewModel : HomeVisitable {

    private val itemList: MutableList<HomeIconItem>
    private var isCache: Boolean = false
    private var trackingData: Map<String, Any>? = null
    private var trackingDataForCombination: List<Any>? = null
    private var isCombined: Boolean = false

    override fun equalsWith(b: Any?): Boolean {
        if (b is UseCaseIconSectionViewModel) {
            return itemList == b.itemList
        }
        return false
    }

    override fun isCache(): Boolean {
        return isCache
    }

    override fun visitableId(): String {
        return ""
    }

    fun setCache(cache: Boolean) {
        isCache = cache
    }

    init {
        itemList = ArrayList()
    }

    fun getItemList(): List<HomeIconItem> {
        return itemList
    }

    fun addItem(item: HomeIconItem) {
        this.itemList.add(item)
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }


    override fun setTrackingData(trackingData: Map<String, Any>) {
        this.trackingData = trackingData
    }

    override fun getTrackingData(): Map<String, Any>? {
        return trackingData
    }

    override fun getTrackingDataForCombination(): List<Any>? {
        return trackingDataForCombination
    }

    override fun setTrackingDataForCombination(trackingDataForCombination: List<Any>) {
        this.trackingDataForCombination = trackingDataForCombination
    }

    override fun isTrackingCombined(): Boolean {
        return isCombined
    }

    override fun setTrackingCombined(isCombined: Boolean) {
        this.isCombined = isCombined
    }
}
