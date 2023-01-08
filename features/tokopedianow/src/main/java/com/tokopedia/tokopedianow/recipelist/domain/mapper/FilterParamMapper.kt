package com.tokopedia.tokopedianow.recipelist.domain.mapper

import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_DURATION
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_INGREDIENT_ID
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_PORTION
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_SORT_BY
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_TAG_ID
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter

object FilterParamMapper {

    fun mapToSortBy(filters: List<SelectedFilter>): String? {
        return filters.firstOrNull { it.parentId == PARAM_SORT_BY }?.id
    }

    fun mapToIngredientIds(filters: List<SelectedFilter>): String {
        return filters.filter { it.parentId == PARAM_INGREDIENT_ID }
            .joinToString(",") { it.id }
    }

    fun mapToDuration(filters: List<SelectedFilter>): String? {
        return filters.firstOrNull { it.parentId == PARAM_DURATION }?.id
    }

    fun mapToPortion(filters: List<SelectedFilter>): String? {
        return filters.firstOrNull { it.parentId == PARAM_PORTION }?.id
    }

    fun mapToTagIds(filters: List<SelectedFilter>): String {
        return filters.filter { it.parentId == PARAM_TAG_ID }
            .joinToString(",") { it.id }
    }
}