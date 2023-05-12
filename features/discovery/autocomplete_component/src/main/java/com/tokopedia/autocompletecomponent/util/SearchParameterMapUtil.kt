package com.tokopedia.autocompletecomponent.util

import com.tokopedia.discovery.common.constants.SearchApiConst

internal fun Map<String, String>.hasQuery(): Boolean {
    return contains(SearchApiConst.Q) && !get(SearchApiConst.Q).isNullOrBlank()
}

internal fun Map<String, String>.hasQuery1(): Boolean {
    return contains(SearchApiConst.Q1) && !get(SearchApiConst.Q1).isNullOrBlank()
}

internal fun Map<String, String>.hasQuery2(): Boolean {
    return contains(SearchApiConst.Q2) && !get(SearchApiConst.Q2).isNullOrBlank()
}

internal fun Map<String, String>.hasQuery3(): Boolean {
    return contains(SearchApiConst.Q3) && !get(SearchApiConst.Q3).isNullOrBlank()
}

internal fun Map<String, String>.hasMpsQuery() : Boolean {
    return hasQuery1() || hasQuery2() || hasQuery3()
}

internal fun Map<String, String>.getMpsSearchQueryString(): String {
    return getMpsQueryString()
}

private fun Map<String, String>.getMpsQueryString(separator: String = ", ") : String {
    return if (contains(SearchApiConst.Q)) {
        get(SearchApiConst.Q)
    } else {
        joinMpsQueryString(separator)
    } ?: ""
}

private fun Map<String, String>.joinMpsQueryString(
    separator: String = ", "
): String  {
    return mutableListOf<String>().apply {
        if (hasQuery1()) add(get(SearchApiConst.Q1).orEmpty())
        if (hasQuery2()) add(get(SearchApiConst.Q2).orEmpty())
        if (hasQuery3()) add(get(SearchApiConst.Q3).orEmpty())
    }
        .joinToString(separator)
}

internal fun Map<String, String>.getMpsTrackingSearchQueryString(): String {
    return getMpsQueryString(" ^ ")
}

internal fun Map<String, String>.isMps(): Boolean {
    return when {
        contains(SearchApiConst.ACTIVE_TAB) -> get(SearchApiConst.ACTIVE_TAB) == SearchApiConst.ACTIVE_TAB_MPS
        else -> false
    }
}

internal fun Map<String, String>.getSearchQuery(): String {
    return when {
        isMps() -> getMpsSearchQueryString()
        contains(SearchApiConst.Q) -> get(SearchApiConst.Q).orEmpty()
        contains(SearchApiConst.KEYWORD) -> get(SearchApiConst.KEYWORD).orEmpty()
        else -> ""
    }
}
internal fun Map<String, String>.getTrackingSearchQuery(): String {
    return when {
        isMps() -> getMpsTrackingSearchQueryString()
        contains(SearchApiConst.Q) -> get(SearchApiConst.Q).orEmpty()
        contains(SearchApiConst.KEYWORD) -> get(SearchApiConst.KEYWORD).orEmpty()
        else -> ""
    }
}

internal fun Map<String, String>.setSearchQueries(queries: List<String>): Map<String, String> {
    return HashMap(this).apply {
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
}

internal fun Map<String, String>.setSearchQuery(query: String): Map<String, String> {
    return HashMap(this).apply {
        set(SearchApiConst.Q, query)
        set(SearchApiConst.ACTIVE_TAB, SearchApiConst.ACTIVE_TAB_PRODUCT)
        remove(SearchApiConst.Q1)
        remove(SearchApiConst.Q2)
        remove(SearchApiConst.Q3)
    }
}

internal fun Map<String, String>.clearSearchQuery(): Map<String, String> {
    return HashMap(this).apply {
        remove(SearchApiConst.Q)
        remove(SearchApiConst.ACTIVE_TAB)
        remove(SearchApiConst.Q1)
        remove(SearchApiConst.Q2)
        remove(SearchApiConst.Q3)
    }
}
