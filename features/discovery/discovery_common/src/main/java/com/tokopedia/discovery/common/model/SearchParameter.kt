package com.tokopedia.discovery.common.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.URLParser

class SearchParameter(
    private val deepLinkUri: String = "",
) : Parcelable {

    private var searchParameterHashMap = URLParser(deepLinkUri).paramKeyValueMapDecoded

    constructor(parcel: Parcel) : this(parcel.readString() ?: "") {
        parcel.readMap(searchParameterHashMap as Map<*, *>, String::class.java.classLoader)
    }

    constructor(searchParameter: SearchParameter) : this(searchParameter.deepLinkUri) {
        setSearchParameterHashMap(HashMap(searchParameter.searchParameterHashMap))

        cleanUpNullValuesInMap()
    }

    constructor(
        searchParameter: SearchParameter,
        searchParameterMap: Map<String, String>,
    ) : this(searchParameter.deepLinkUri) {
        setSearchParameterMap(searchParameterMap)

        cleanUpNullValuesInMap()
    }

    fun cleanUpNullValuesInMap() {
        searchParameterHashMap.values.remove(null)
    }

    private fun setSearchParameterHashMap(searchParameterHashMap: HashMap<String, String>) {
        this.searchParameterHashMap = searchParameterHashMap
    }

    private fun setSearchParameterMap(searchParameterMap: Map<String, String>) {
        this.searchParameterHashMap = HashMap(searchParameterMap)
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
        set(SearchApiConst.ACTIVE_TAB, SearchApiConst.ACTIVE_TAB_PRODUCT)
        remove(SearchApiConst.Q1)
        remove(SearchApiConst.Q2)
        remove(SearchApiConst.Q3)
    }
    fun setMpsQuery(query: String) {
        set(SearchApiConst.Q, query)
        set(SearchApiConst.ACTIVE_TAB, SearchApiConst.ACTIVE_TAB_MPS)
        remove(SearchApiConst.Q1)
        remove(SearchApiConst.Q2)
        remove(SearchApiConst.Q3)
    }

    fun setSearchQueries(queries: List<String>) {
        remove(SearchApiConst.Q)
        set(SearchApiConst.ACTIVE_TAB, SearchApiConst.ACTIVE_TAB_MPS)
        remove(SearchApiConst.Q1)
        remove(SearchApiConst.Q2)
        remove(SearchApiConst.Q3)
        queries.forEachIndexed { index, query ->
            val key = when (index) {
                1 -> SearchApiConst.Q2
                2 -> SearchApiConst.Q3
                else -> SearchApiConst.Q1
            }
            set(key, query)
        }
    }

    fun hasQuery1(): Boolean {
        return contains(SearchApiConst.Q1) && get(SearchApiConst.Q1).isNotBlank()
    }

    fun hasQuery2(): Boolean {
        return contains(SearchApiConst.Q2) && get(SearchApiConst.Q2).isNotBlank()
    }

    fun hasQuery3(): Boolean {
        return contains(SearchApiConst.Q3) && get(SearchApiConst.Q3).isNotBlank()
    }

    fun isMps() : Boolean {
        return when {
            contains(SearchApiConst.ACTIVE_TAB) -> get(SearchApiConst.ACTIVE_TAB) == SearchApiConst.ACTIVE_TAB_MPS
            else -> false
        }
    }

    private fun getMpsSearchQueryString() : String {
        return if(contains(SearchApiConst.Q)) {
            get(SearchApiConst.Q)
        } else {
            mutableListOf<String>().apply {
                if(hasQuery1()) add(get(SearchApiConst.Q1))
                if(hasQuery2()) add(get(SearchApiConst.Q2))
                if(hasQuery3()) add(get(SearchApiConst.Q3))
            }.joinToString()
        }
    }

    fun getSearchQuery(): String {
        return when {
            isMps() -> getMpsSearchQueryString()
            contains(SearchApiConst.Q) -> get(SearchApiConst.Q)
            contains(SearchApiConst.KEYWORD) -> get(SearchApiConst.KEYWORD)
            else -> ""
        }
    }

    fun getSearchQueryMap(): Map<String?, String> =
        searchParameterHashMap.filter {
            it.key == SearchApiConst.Q ||
                it.key == SearchApiConst.Q1 ||
                it.key == SearchApiConst.Q2 ||
                it.key == SearchApiConst.Q3
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
