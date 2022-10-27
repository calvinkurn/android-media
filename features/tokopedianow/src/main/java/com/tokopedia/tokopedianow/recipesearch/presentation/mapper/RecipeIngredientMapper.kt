package com.tokopedia.tokopedianow.recipesearch.presentation.mapper

import com.tokopedia.tokopedianow.recipelist.domain.model.RecipeFilterSortDataResponse
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_INGREDIENT_ID
import com.tokopedia.tokopedianow.recipesearch.presentation.uimodel.RecipeSearchIngredientUiModel
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter

object RecipeIngredientMapper {

    fun MutableList<RecipeSearchIngredientUiModel>.addIngredients(
        response: RecipeFilterSortDataResponse,
        selectedFilters: List<SelectedFilter>
    ) {
        val selectedFilterIds = selectedFilters
            .filter { it.parentId == PARAM_INGREDIENT_ID }
            .map { it.id }

        val ingredients = response.filter.filter {
            it.options.firstOrNull { option -> option.key == PARAM_INGREDIENT_ID } != null
        }

        ingredients.forEach {
            val filterChipList = it.options.map { option ->
                RecipeSearchIngredientUiModel(
                    id = option.value,
                    imgUrl = option.icon,
                    title = option.name,
                    isChecked = selectedFilterIds.contains(option.value)
                )
            }

            addAll(filterChipList)
        }
    }

    fun MutableList<RecipeSearchIngredientUiModel>.updateIngredients(id: String, isChecked: Boolean) {
        firstOrNull { it.id == id }?.let {
            set(indexOf(it), it.copy(isChecked = isChecked))
        }
    }
}