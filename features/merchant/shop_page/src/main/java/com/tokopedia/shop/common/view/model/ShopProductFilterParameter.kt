package com.tokopedia.shop.common.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.shop.common.constant.DEFAULT_SORT_ID
import com.tokopedia.shop.common.constant.SORT_PARAM_KEY

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

    fun getMapDataWithDefaultSortId(): Map<String, String> {
        return mapOf(
                SORT_PARAM_KEY to DEFAULT_SORT_ID
        )
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