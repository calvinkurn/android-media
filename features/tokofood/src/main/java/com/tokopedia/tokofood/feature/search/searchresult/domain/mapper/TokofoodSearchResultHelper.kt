package com.tokopedia.tokofood.feature.search.searchresult.domain.mapper

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.getSortFilterCount
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodSearchResultHelper.getSearchQuery

object TokofoodSearchResultHelper {

    fun Option.setAppliedInputState(paramMap: HashMap<String, String>?) {
        inputState = paramMap?.get(key)?.takeIf { it.isNotBlank() }.let { selectedParams ->
            (selectedParams?.split(TokofoodFilterSortMapper.OPTION_SEPARATOR)
                ?.contains(value) == true).toString()
        }
    }

    fun HashMap<String, String>.resetParams(queryParams: Map<String, String>) {
        clear()
        putAll(queryParams)
    }

    fun HashMap<String, String>.getActiveCount(): Int {
        val searchMap = filter { it.value.isNotEmpty() }
        return getSortFilterCount(searchMap)
    }

    fun HashMap<String, String>.hasFilterSortApplied(): Boolean {
        return if (contains(SearchApiConst.Q)) {
            entries.filter { it.value.isNotBlank() }.size > Int.ONE
        } else {
            entries.filter { it.value.isNotBlank() }.size > Int.ZERO
        }
    }

    fun getCurrentSearchParameter(currentSearchParameterMap: HashMap<String, String>?): HashMap<String, String> {
        return currentSearchParameterMap ?: hashMapOf()
    }

    fun getCurrentKeyword(currentSearchParameterMap: HashMap<String, String>?): String {
        return currentSearchParameterMap?.getSearchQuery().orEmpty()
    }

    fun getIsHasNextPage(pageKey: String?): Boolean {
        return !pageKey.isNullOrEmpty()
    }

    private fun HashMap<String, String>.getSearchQuery(): String {
        return when {
            contains(SearchApiConst.Q) -> get(SearchApiConst.Q).orEmpty()
            contains(SearchApiConst.KEYWORD) -> get(SearchApiConst.KEYWORD).orEmpty()
            else -> String.EMPTY
        }
    }

}