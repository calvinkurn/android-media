package com.tokopedia.tokopedianow.recipelist.domain.param

import com.tokopedia.tokopedianow.recipelist.domain.query.GetRecipeList
import com.tokopedia.usecase.RequestParams
import java.util.HashMap

class RecipeListParam {
    var page: Int = DEFAULT_PAGE
    var perPage: Int = DEFAULT_PER_PAGE
    var warehouseID: String? = null
    var title: String? = null
    var tagID: List<String>? = null
    var ingredientID: List<String>? = null
    var categoryID: List<String>? = null
    var fromDuration: Int? = null
    var toDuration: Int? = null
    var fromPortion: Int? = null
    var toPortion: Int? = null
    var sortBy: RecipeSortBy? = null
    
    companion object {
        private const val DEFAULT_PAGE = 1
        private const val DEFAULT_PER_PAGE = 5
    }

    fun create(): HashMap<String, Any> {
        return RequestParams.create().apply {
            putInt(GetRecipeList.PARAM_PAGE, page)
            putInt(GetRecipeList.PARAM_PER_PAGE, perPage)

            warehouseID?.let {
                putString(GetRecipeList.PARAM_WAREHOUSE_ID, it)
            }

            title?.let {
                putString(GetRecipeList.PARAM_TITLE, it)
            }

            tagID?.let {
                putObject(GetRecipeList.PARAM_TAG_ID, it)
            }

            ingredientID?.let {
                putObject(GetRecipeList.PARAM_INGREDIENT_ID, it)
            }

            categoryID?.let {
                putObject(GetRecipeList.PARAM_CATEGORY_ID, it)
            }

            fromDuration?.let {
                putInt(GetRecipeList.PARAM_FROM_DURATION, it)
            }

            toDuration?.let {
                putInt(GetRecipeList.PARAM_TO_DURATION, it)
            }

            fromPortion?.let {
                putInt(GetRecipeList.PARAM_FROM_PORTION, it)
            }

            toPortion?.let {
                putInt(GetRecipeList.PARAM_TO_PORTION, it)
            }

            sortBy?.let {
                putString(GetRecipeList.PARAM_SORT_BY, it.name)
            }
        }.parameters
    }
}