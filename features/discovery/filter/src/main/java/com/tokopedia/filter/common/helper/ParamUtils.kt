package com.tokopedia.filter.common.helper

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper

fun String.toMapParam(): Map<String, String> {
    if (this.isEmpty()) return mapOf()

    return split("&")
            .associateTo(HashMap()) { it.createKeyValuePair() }
            .apply { remove("") }
}

private fun String.createKeyValuePair(): Pair<String, String> {
    if (!this.contains("=")) return Pair("", "")

    val (key, value) = split("=")
    return key to value
}

private const val NON_FILTER_SRP_PREFIX = "srp_"
private const val NON_FILTER_USER_PREFIX = "user_"
private const val NON_FILTER_EXCLUDE_PREFIX = OptionHelper.EXCLUDE_PREFIX
val nonFilterParameterKeyList = setOf(
        SearchApiConst.Q,
        SearchApiConst.RF,
        SearchApiConst.ACTIVE_TAB,
        SearchApiConst.SOURCE,
        SearchApiConst.LANDING_PAGE,
        SearchApiConst.PREVIOUS_KEYWORD,
        SearchApiConst.ORIGIN_FILTER,
        SearchApiConst.SKIP_REWRITE,
        SearchApiConst.NAVSOURCE,
        SearchApiConst.SKIP_BROADMATCH,
        SearchApiConst.HINT,
        SearchApiConst.FIRST_INSTALL,
        SearchApiConst.SEARCH_REF
)

fun getSortFilterCount(mapParameter: Map<String, Any>): Int {
    var sortFilterCount = 0

    val mutableMapParameter = mapParameter.toMutableMap()
    val sortFilterParameter = mutableMapParameter.createAndCountSortFilterParameter {
        sortFilterCount += it
    }

    if (sortFilterParameter.hasMinAndMaxPriceFilter()) sortFilterCount -= 1
    if (isSortHasDefaultValue(sortFilterParameter)) sortFilterCount -= 1

    return sortFilterCount
}

private fun MutableMap<String, Any>.createAndCountSortFilterParameter(count: (Int) -> Unit): Map<String, Any> {
    val iterator = iterator()

    while (iterator.hasNext()) {
        val entry = iterator.next()

        if (entry.isNotSortAndFilterEntry()) {
            iterator.remove()
            continue
        }

        count(entry.value.toString().split(OptionHelper.OPTION_SEPARATOR).size)
    }

    return this
}

private fun Map.Entry<String, Any>.isNotSortAndFilterEntry(): Boolean {
    return isNotFilterAndSortKey() || isPriceFilterWithZeroValue()
}

fun Map.Entry<String, Any>.isNotFilterAndSortKey(): Boolean {
    return nonFilterParameterKeyList.contains(key)
            || key.matchesWithNonFilterPrefix()
}

private fun String?.matchesWithNonFilterPrefix(): Boolean {
    this ?: return false

    return startsWith(NON_FILTER_SRP_PREFIX)
            || startsWith(NON_FILTER_USER_PREFIX)
            || startsWith(NON_FILTER_EXCLUDE_PREFIX)
}

private fun Map.Entry<String, Any>.isPriceFilterWithZeroValue(): Boolean {
    return (key == SearchApiConst.PMIN && value.toString() == "0")
            || (key == SearchApiConst.PMAX && value.toString() == "0")
}

private fun Map<String, Any>.hasMinAndMaxPriceFilter(): Boolean {
    var hasMinPriceFilter = false
    var hasMaxPriceFilter = false

    for(entry in this) {
        if (entry.key == SearchApiConst.PMIN) hasMinPriceFilter = true
        if (entry.key == SearchApiConst.PMAX) hasMaxPriceFilter = true

        // Immediately return so it doesn't continue the loop
        if (hasMinPriceFilter && hasMaxPriceFilter) return true
    }

    return false
}

fun isSortHasDefaultValue(mapParameter: Map<String, Any>): Boolean {
    val sortValue = mapParameter[SearchApiConst.OB]

    return sortValue == SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
}

fun getSortFilterParamsString(mapParameter: Map<String?, Any?>): String {
    val sortAndFilterParameter = mapParameter
            .removeWithNonFilterPrefix()
            .minus(nonFilterParameterKeyList)

    return UrlParamUtils.generateUrlParamString(sortAndFilterParameter)
}

private fun <T> Map<String?, T?>.removeWithNonFilterPrefix(): Map<String?, T?> =
        filter { !it.key.matchesWithNonFilterPrefix() }

@Suppress("UNCHECKED_CAST")
fun getFilterParams(mapParameter: Map<String?, String?>): Map<String, String> {
    return mapParameter
            .removeWithNonFilterPrefix()
            .minus(nonFilterParameterKeyList + listOf(SearchApiConst.OB))
            as Map<String, String>
}