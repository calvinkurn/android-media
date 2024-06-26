package com.tokopedia.shop.common.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.NULL_VALUE
import com.tokopedia.shop.common.constant.*

class ShopProductFilterParameter() : Parcelable {
    private var mapParameter: MutableMap<String, String> = mutableMapOf()

    constructor(parcel: Parcel) : this() {
        parcel.readMap(mapParameter as Map<*, *>, String::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeMap(mapParameter as Map<*, *>?)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun clearParameter() {
        mapParameter.clear()
    }

    fun setMapData(mapParameter: Map<String, String>) {
        this.mapParameter = mapParameter.toMutableMap()
    }

    fun getMapData(): Map<String, String> {
        return mapParameter
    }

    fun getSortId(): String {
        return mapParameter[SORT_PARAM_KEY].orEmpty()
    }

    fun setSortId(sortId: String) {
        mapParameter[SORT_PARAM_KEY] = sortId
    }

    fun getIsFulfillment(): String {
        return mapParameter[IS_FULFILLMENT_KEY].orEmpty()
    }

    fun getRating(): String {
        return mapParameter[RATING_PARAM_KEY].orEmpty()
    }

    fun getPmin(): Int {
        return mapParameter[PMIN_PARAM_KEY].toIntOrZero()
    }

    fun getPmax(): Int {
        return mapParameter[PMAX_PARAM_KEY].toIntOrZero()
    }

    fun getCategory(): Int? {
        return mapParameter[CATEGORY_PARAM_KEY]?.toIntOrNull()
    }

    fun getMapDataWithDefaultSortId(): Map<String, String> {
        return mapOf(
            SORT_PARAM_KEY to DEFAULT_SORT_ID
        )
    }

    fun getExtraParam(): String {
        return "$IS_FULFILLMENT_KEY=${getIsFulfillment()}".takeIf {
            getIsFulfillment().isNotEmpty()
        } ?: String.EMPTY
    }

    fun getListFilterForTracking(): String {
        return mutableListOf<String>().apply {
            add(getSortIdFilterTrackingValue())
            add(getPriceRangeFilterTrackingValue())
            add(getRatingTrackingValue())
            add(getCategoryIdTrackingValue())
            add(getLayananTokopediaTrackingValue())
        }.joinToString(" - ")
    }

    private fun getSortIdFilterTrackingValue(): String {
        return getSortId().takeIf { it.isNotEmpty() } ?: NULL_VALUE
    }

    private fun getPriceRangeFilterTrackingValue(): String {
        return "${getPmin()}_${getPmax()}"
    }

    private fun getRatingTrackingValue(): String {
        return getRating().takeIf { it.isNotEmpty() } ?: NULL_VALUE
    }

    private fun getCategoryIdTrackingValue(): String {
        return getCategory().toString()
    }

    private fun getLayananTokopediaTrackingValue(): String {
        return getIsFulfillment().takeIf { it.isNotEmpty() } ?: NULL_VALUE
    }

    companion object CREATOR : Parcelable.Creator<ShopProductFilterParameter> {
        override fun createFromParcel(parcel: Parcel): ShopProductFilterParameter {
            return ShopProductFilterParameter(parcel)
        }

        override fun newArray(size: Int): Array<ShopProductFilterParameter?> {
            return arrayOfNulls(size)
        }
    }
}
