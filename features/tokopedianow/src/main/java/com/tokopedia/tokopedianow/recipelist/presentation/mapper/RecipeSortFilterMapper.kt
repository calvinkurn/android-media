package com.tokopedia.tokopedianow.recipelist.presentation.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.tokopedianow.common.model.TokoNowChipListUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowNotChipUiModel
import com.tokopedia.tokopedianow.recipelist.domain.model.RecipeFilterOptionResponse
import com.tokopedia.tokopedianow.recipelist.domain.model.RecipeFilterSortDataResponse
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_INGREDIENT_ID
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_SORT_BY
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam.Companion.PARAM_TAG_ID
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeFilterSectionUiModel.RecipeFilterSectionHeader
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeFilterSectionUiModel.RecipeSortSectionHeader
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter

object RecipeSortFilterMapper {

    private val multiSelectFilterTypes = listOf(
        PARAM_INGREDIENT_ID,
        PARAM_TAG_ID
    )

    fun MutableList<Visitable<*>>.addSortSection(
        response: RecipeFilterSortDataResponse,
        selectedFilterIds: List<SelectedFilter>
    ) {
        val selectedSortIds = selectedFilterIds.filter { it.parentId == PARAM_SORT_BY }.map { it.id }
        val sortChipItems = response.sort.map {
            TokoNowChipUiModel(
                id = it.value,
                parentId = it.key,
                text = it.name,
                selected = selectedSortIds.contains(it.value)
            )
        }

        val sortChipListItem = TokoNowChipListUiModel(
            parentId = sortChipItems.first().parentId,
            items = sortChipItems,
            isMultiSelect = false
        )

        val sortItems = listOf(
            RecipeSortSectionHeader,
            sortChipListItem
        )

        addAll(sortItems)
    }

    fun MutableList<Visitable<*>>.addFilterSection(
        response: RecipeFilterSortDataResponse,
        selectedFilters: List<SelectedFilter>
    ) {
        response.filter.forEach { recipeResponse ->
            val parentId = recipeResponse.options.first().key

            val sectionHeader = if(parentId == PARAM_INGREDIENT_ID) {
                RecipeFilterSectionHeader(
                    text = recipeResponse.title,
                    appLink = ApplinkConstInternalTokopediaNow.RECIPE_INGREDIENT_BOTTOM_SHEET
                )
            } else {
                RecipeFilterSectionHeader(text = recipeResponse.title)
            }

            /**
             * filter the option which is popular or the option has been selected before
             */
            val selectedFilterPopular = mutableListOf<RecipeFilterOptionResponse>()
            for (option in recipeResponse.options) {
                if (option.isPopular) {
                    selectedFilterPopular.add(option)
                } else {
                    for (filter in selectedFilters) {
                        if (option.value == filter.id && option.key == filter.parentId) {
                            selectedFilterPopular.add(option)
                            break
                        }
                    }
                }
            }

            val filterChipList = TokoNowChipListUiModel(
                parentId = parentId,
                items = selectedFilterPopular
                    .map { option ->
                        val selectedFilterIds = selectedFilters
                            .filter { filter -> filter.parentId == option.key }
                            .map { filter -> filter.id }

                        /**
                         * option which is popular will be added as TokoNowChipUiModel while selected option will be added as TokoNowNotChipUiModel
                         */
                        if (option.isPopular) {
                            TokoNowChipUiModel(
                                id = option.value,
                                parentId = option.key,
                                text = option.name,
                                imageUrl = option.icon,
                                selected = selectedFilterIds.contains(option.value)
                            )
                        } else {
                            TokoNowNotChipUiModel(
                                id = option.value,
                                parentId = option.key,
                                text = option.name,
                                selected = selectedFilterIds.contains(option.value)
                            )
                        }
                    },
                isMultiSelect = multiSelectFilterTypes.contains(parentId)
            )

            add(sectionHeader)
            add(filterChipList)
        }
    }
}