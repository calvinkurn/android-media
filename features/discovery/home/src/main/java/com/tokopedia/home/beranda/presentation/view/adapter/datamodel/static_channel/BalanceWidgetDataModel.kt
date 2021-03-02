package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel

import android.os.Bundle
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

/**
 * Created by yfsx on 3/1/21.
 */

class BalanceWidgetDataModel (
        val drawerMap: HashMap<Int, TokopointsDrawer> = hashMapOf(),
        val itemCount: Int = 0
): HomeVisitable {
    fun setCache(cache: Boolean) {
        isCache = cache
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }


    override fun setTrackingData(trackingData: Map<String, Any>) {

    }

    override fun getTrackingData(): Map<String, Any>? {
        return null
    }

    override fun getTrackingDataForCombination(): List<Any>? {
        return null
    }

    override fun setTrackingDataForCombination(`object`: List<Any>) {

    }

    override fun isTrackingCombined(): Boolean {
        return false
    }

    override fun setTrackingCombined(isCombined: Boolean) {

    }

    override fun isCache(): Boolean {
        return false
    }

    override fun visitableId(): String {
        return "balanceWidgetSection"
    }

    override fun equalsWith(b: Any?): Boolean {
        return equals(b)
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BalanceWidgetDataModel

        if (drawerMap != other.drawerMap) return false
        if (itemCount != other.itemCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = drawerMap?.hashCode() ?: 0
        result = HASH_CODE * result + itemCount.hashCode()
        return result
    }

    companion object {
        private const val HASH_CODE = 39
    }


}