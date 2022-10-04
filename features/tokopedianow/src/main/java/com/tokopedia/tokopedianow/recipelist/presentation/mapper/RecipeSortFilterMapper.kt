package com.tokopedia.tokopedianow.recipelist.presentation.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.tokopedianow.common.model.TokoNowChipListUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSectionHeaderUiModel
import com.tokopedia.tokopedianow.recipelist.domain.model.RecipeFilterSortDataResponse
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeFilterSectionUiModel.RecipeSortSectionHeader

object RecipeSortFilterMapper {

    private const val INGREDIENT_IDS = "ingredient_ids"

    fun MutableList<Visitable<*>>.addSortSection(
        response: RecipeFilterSortDataResponse,
        selectedFilterIds: List<String>
    ) {
        val sortChipItems = response.sort.map {
            TokoNowChipUiModel(
                id = it.value,
                parentId = it.key,
                text = it.name,
                selected = selectedFilterIds.contains(it.value)
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
        selectedFilterIds: List<String>
    ) {
        response.filter.forEach {
            val parentId = it.options.first().key

            val sectionHeader = if(parentId == INGREDIENT_IDS) {
                TokoNowSectionHeaderUiModel(
                    title = it.title,
                    seeAllAppLink = ApplinkConstInternalTokopediaNow.RECIPE_INGREDIENT_BOTTOM_SHEET
                )
            } else {
                TokoNowSectionHeaderUiModel(title = it.title)
            }

            val filterChipList = TokoNowChipListUiModel(
                parentId = parentId,
                items = it.options
                    .filter { option -> option.isPopular }
                    .map { option ->
                        TokoNowChipUiModel(
                            id = option.value,
                            parentId = option.key,
                            text = option.name,
                            imageUrl = option.icon,
                            selected = selectedFilterIds.contains(option.value)
                        )
                    }
            )

            add(sectionHeader)
            add(filterChipList)
        }
    }
}