package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import java.util.*

/**
 * @author by errysuprayogi on 11/28/17.
 */

/**
 * No further development for this datamodel
 * Use dynamic icon section instead
 */

@Deprecated("")
class UseCaseIconSectionDataModel : HomeVisitable {

    private val itemList: MutableList<DynamicHomeIcon.DynamicIcon>
    private var isCache: Boolean = false
    private var trackingData: Map<String, Any>? = null
    private var trackingDataForCombination: List<Any>? = null
    private var isCombined: Boolean = false

    override fun equalsWith(b: Any?): Boolean {
        if (b is UseCaseIconSectionDataModel) {
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

    fun getItemList(): List<DynamicHomeIcon.DynamicIcon> {
        return itemList
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

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }
}
