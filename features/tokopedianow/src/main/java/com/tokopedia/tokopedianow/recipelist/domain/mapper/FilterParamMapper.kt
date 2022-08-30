package com.tokopedia.tokopedianow.recipelist.domain.mapper

import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeFilterId
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeSortBy
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter

object FilterParamMapper {

    fun mapToSortBy(filters: List<SelectedFilter>): RecipeSortBy? {
        return when (filters.firstOrNull { it.parentId == RecipeFilterId.SORT.id }?.id) {
            RecipeSortBy.Newest.name -> RecipeSortBy.Newest
            RecipeSortBy.Oldest.name -> RecipeSortBy.Oldest
            RecipeSortBy.LeastPortion.name -> RecipeSortBy.LeastPortion
            RecipeSortBy.MostPortion.name -> RecipeSortBy.MostPortion
            RecipeSortBy.ShortestDuration.name -> RecipeSortBy.ShortestDuration
            RecipeSortBy.LongestDuration.name -> RecipeSortBy.LongestDuration
            else -> null
        }
    }

    fun mapToIngredientIds(filters: List<SelectedFilter>): List<String> {
        return filters.filter { it.parentId == RecipeFilterId.INGREDIENT.id }.map { it.id }
    }
}