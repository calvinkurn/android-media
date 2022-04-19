package com.tokopedia.discovery.common.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.URLParser

class SearchParameter(private val deepLinkUri: String = "") : Parcelable {

    private var searchParameterHashMap = URLParser(deepLinkUri).paramKeyValueMapDecoded

    constructor(parcel: Parcel) : this(parcel.readString() ?: "") {
        parcel.readMap(searchParameterHashMap as Map<*, *>, String::class.java.classLoader)
    }

    constructor(searchParameter: SearchParameter) : this(searchParameter.deepLinkUri) {
        setSearchParameterHashMap(HashMap(searchParameter.searchParameterHashMap))

        cleanUpNullValuesInMap()
    }

    fun cleanUpNullValuesInMap() {
        searchParameterHashMap.values.remove(null)
    }

    private fun setSearchParameterHashMap(searchParameterHashMap: HashMap<String, String>) {
        this.searchParameterHashMap = searchParameterHashMap
    }

    fun getSearchParameterHashMap(): HashMap<String, String> {
        return searchParameterHashMap
    }

    fun getSearchParameterMap(): Map<String, Any> {
        return searchParameterHashMap as Map<String, Any>
    }

    fun contains(key: String): Boolean {
        return searchParameterHashMap.contains(key)
    }

    fun set(key: String, value: String) {
        searchParameterHashMap[key] = value
    }

    fun get(key: String): String {
        return searchParameterHashMap[key] ?: ""
    }

    fun remove(key: String) {
        searchParameterHashMap.remove(key)
    }

    fun getBoolean(key: String): Boolean {
        return get(key).toBoolean()
    }

    fun getInteger(key: String): Int {
        return try {
            get(key).toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }

    fun setSearchQuery(query: String) {
        set(SearchApiConst.Q, query)
    }

    fun getSearchQuery(): String {
        return when {
            contains(SearchApiConst.Q) -> get(SearchApiConst.Q)
            contains(SearchApiConst.KEYWORD) -> get(SearchApiConst.KEYWORD)
            else -> ""
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(deepLinkUri)
        parcel.writeMap(searchParameterHashMap as Map<*, *>?)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchParameter> {
        override fun createFromParcel(parcel: Parcel): SearchParameter {
            return SearchParameter(parcel)
        }

        override fun newArray(size: Int): Array<SearchParameter?> {
            return arrayOfNulls(size)
        }
    }
}