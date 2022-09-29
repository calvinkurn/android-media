package com.tokopedia.tokopedianow.recipelist.domain.mapper

import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeFilterId
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter

object FilterParamMapper {

    fun mapToSortBy(filters: List<SelectedFilter>): String? {
        return filters.firstOrNull { it.parentId == RecipeFilterId.SORT.id }?.id
    }

    fun mapToIngredientIds(filters: List<SelectedFilter>): String {
        return filters.filter { it.parentId == RecipeFilterId.INGREDIENT.id }
            .joinToString(",") { it.id }
    }
}