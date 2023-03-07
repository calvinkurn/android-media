package com.tokopedia.tokopedianow.recipelist.domain.param

import com.tokopedia.tokopedianow.recipelist.domain.query.GetRecipeList
import com.tokopedia.tokopedianow.recipelist.domain.query.GetRecipeList.PARAM_QUERY_PARAM
import com.tokopedia.tokopedianow.recipelist.domain.query.GetRecipeList.PARAM_SOURCE_PAGE
import com.tokopedia.tokopedianow.recipelist.domain.query.GetRecipeList.PARAM_WAREHOUSE_ID
import com.tokopedia.usecase.RequestParams
import java.util.*

/***
 * Get Recipe List Params Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/TokoNow/pages/2033910971/Query+TokonowGetRecipes+GQL+FE
 */
class RecipeListParam {
    var page: Int = DEFAULT_PAGE
    var perPage: Int = DEFAULT_PER_PAGE
    var warehouseID: String? = null
    var sourcePage: String = ""
    val queryParamsMap = mutableMapOf<String, String?>()

    companion object {
        private const val DEFAULT_PAGE = 1
        private const val DEFAULT_PER_PAGE = 5

        const val PARAM_TITLE = "title"
        const val PARAM_TAG_ID = "tag_ids"
        const val PARAM_INGREDIENT_ID = "ingredient_ids"
        const val PARAM_SORT_BY = "sort_by"
        const val PARAM_DURATION = "duration"
        const val PARAM_PORTION = "portion"

        private const val AMP = "&"
        private const val EQUAL = "="
    }

    fun create(): HashMap<String, Any> {
        return RequestParams.create().apply {
            putInt(GetRecipeList.PARAM_PAGE, page)
            putInt(GetRecipeList.PARAM_PER_PAGE, perPage)

            warehouseID?.let {
                putString(PARAM_WAREHOUSE_ID, it)
            }

            if (sourcePage.isNotEmpty()) {
                putString(PARAM_SOURCE_PAGE, sourcePage)
            }

            val queryParams = generateQueryParams()
            putString(PARAM_QUERY_PARAM, queryParams)
        }.parameters
    }

    fun generateQueryParams(): String {
        val stringBuilder = StringBuilder()

        for ((key, value) in queryParamsMap) {
            if (!value.isNullOrBlank()) {
                if (stringBuilder.isNotBlank()) {
                    stringBuilder.append(AMP)
                }
                stringBuilder.append("$key$EQUAL$value")
            }
        }

        return stringBuilder.toString()
    }

    fun mapToQueryParamsMap(queryParams: String) {
        queryParams.split(AMP).forEach {
            val query = it.split(EQUAL)
            val key = query.first()
            val value = query.last()
            queryParamsMap[key] = value
        }
    }

    fun getValue(key: String): String {
        return queryParamsMap[key].orEmpty()
    }
}