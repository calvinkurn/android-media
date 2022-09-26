package com.tokopedia.tokopedianow.recipelist.domain.param

import android.net.Uri
import com.tokopedia.tokopedianow.recipelist.domain.query.GetRecipeList
import com.tokopedia.tokopedianow.recipelist.domain.query.GetRecipeList.PARAM_QUERY_PARAM
import com.tokopedia.tokopedianow.recipelist.domain.query.GetRecipeList.PARAM_SOURCE_PAGE
import com.tokopedia.tokopedianow.recipelist.domain.query.GetRecipeList.PARAM_WAREHOUSE_ID
import com.tokopedia.usecase.RequestParams
import java.lang.StringBuilder
import java.util.HashMap

class RecipeListParam {
    var page: Int = DEFAULT_PAGE
    var perPage: Int = DEFAULT_PER_PAGE
    var warehouseID: String? = null
    var sourcePage: String = ""
    var title: String? = null
    var tagID: List<String> = emptyList()
    var ingredientID: List<String> = emptyList()
    var categoryID: List<String> = emptyList()
    var fromDuration: String? = null
    var toDuration: String? = null
    var fromPortion: String? = null
    var toPortion: String? = null
    var sortBy: RecipeSortBy? = null
    var queryParams = ""
        set(value) {
            field = value
            breakDownQueryParams()
        }
    
    companion object {
        private const val DEFAULT_PAGE = 1
        private const val DEFAULT_PER_PAGE = 5

        private const val PARAM_TITLE = "title"
        private const val PARAM_TAG_ID = "tag_ids"
        private const val PARAM_INGREDIENT_ID = "ingredient_ids"
        private const val PARAM_CATEGORY_ID = "category_ids"
        private const val PARAM_FROM_DURATION = "from_duration"
        private const val PARAM_TO_DURATION = "to_duration"
        private const val PARAM_FROM_PORTION = "from_portion"
        private const val PARAM_TO_PORTION = "to_portion"
        private const val PARAM_SORT_BY = "sort_by"

        private const val COMMA_SEPARATOR = ","
    }

    fun create(): HashMap<String, Any> {
        return RequestParams.create().apply {
            putInt(GetRecipeList.PARAM_PAGE, page)
            putInt(GetRecipeList.PARAM_PER_PAGE, perPage)

            warehouseID?.let {
                putString(PARAM_WAREHOUSE_ID, it)
            }

            if(sourcePage.isNotEmpty()) {
                putString(PARAM_SOURCE_PAGE, sourcePage)
            }

            queryParams = generateQueryParams()

            putString(PARAM_QUERY_PARAM, queryParams)
        }.parameters
    }

    fun generateQueryParams(): String {
        val stringBuilder = StringBuilder()
        val queryParamsMap = mutableMapOf<String, String>()

        title?.let {
            queryParamsMap[PARAM_TITLE] = it
        }

        if(tagID.isNotEmpty()) {
            queryParamsMap[PARAM_TAG_ID] = tagID.formatParam()
        }

        if(ingredientID.isNotEmpty()) {
            queryParamsMap[PARAM_INGREDIENT_ID] = ingredientID.formatParam()
        }

        if(categoryID.isNotEmpty()) {
            queryParamsMap[PARAM_CATEGORY_ID] = categoryID.formatParam()
        }

        fromDuration?.let {
            queryParamsMap[PARAM_FROM_DURATION] = it
        }

        toDuration?.let {
            queryParamsMap[PARAM_TO_DURATION] = it
        }

        fromPortion?.let {
            queryParamsMap[PARAM_FROM_PORTION] = it
        }

        toPortion?.let {
            queryParamsMap[PARAM_TO_PORTION] = it
        }

        sortBy?.let {
            queryParamsMap[PARAM_SORT_BY] = it.name
        }

        for((key, value) in queryParamsMap) {
            if(stringBuilder.isNotBlank()) {
                stringBuilder.append("&")
            }
            stringBuilder.append("$key=$value")
        }

        return stringBuilder.toString()
    }

    private fun breakDownQueryParams() {
        val uri = Uri.parse("?$queryParams")

        uri.getQueryParameter(PARAM_TITLE)?.let {
            title = it
        }

        uri.getQueryParameters(PARAM_TAG_ID)?.let {
            tagID = it
        }

        uri.getQueryParameters(PARAM_INGREDIENT_ID)?.let {
            ingredientID = it
        }

        uri.getQueryParameters(PARAM_CATEGORY_ID)?.let {
            categoryID = it
        }

        uri.getQueryParameter(PARAM_FROM_DURATION)?.let {
            fromDuration = it
        }

        uri.getQueryParameter(PARAM_TO_DURATION)?.let {
            toDuration = it
        }

        uri.getQueryParameter(PARAM_FROM_PORTION)?.let {
            fromPortion = it
        }

        uri.getQueryParameter(PARAM_TO_PORTION)?.let {
            toPortion = it
        }
    }

    private fun List<String>.formatParam(): String {
        return joinToString(COMMA_SEPARATOR)
    }
}