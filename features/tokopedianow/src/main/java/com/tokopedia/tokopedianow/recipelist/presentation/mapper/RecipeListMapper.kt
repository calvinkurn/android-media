package com.tokopedia.tokopedianow.recipelist.presentation.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.TagUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeChipFilterUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeChipFilterUiModel.ChipType.MORE_FILTER
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeCountUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeFilterUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeHeaderUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeEmptyStateUiModel
import com.tokopedia.tokopedianow.recipelist.domain.model.GetRecipeListResponse

object RecipeListMapper {

    private const val MAX_LABEL_COUNT = 3
    private const val TAKE_LABEL_COUNT = 4

    fun MutableList<Visitable<*>>.addHeaderItem() {
        add(RecipeHeaderUiModel)
    }

    fun MutableList<Visitable<*>>.addFilterItems() {
        // Temporary Hardcode
        val chips = listOf(
            RecipeChipFilterUiModel(
                id = "1",
                title = "Filter",
                type = MORE_FILTER
            ),
            RecipeChipFilterUiModel(
                id = "2",
                title = "Diskon"
            ),
            RecipeChipFilterUiModel(
                id = "3",
                title = "Halal"
            ),
            RecipeChipFilterUiModel(
                id = "4",
                title = "Organik",
            )
        )

        add(RecipeFilterUiModel(chips))
    }

    fun MutableList<Visitable<*>>.addRecipeItems(response: GetRecipeListResponse) {
        val recipeItems = response.data.recipes.map {
            RecipeUiModel(
                id = it.id,
                title = it.title,
                portion = it.portion,
                duration = it.duration.orZero(),
                labels = it.tags.take(TAKE_LABEL_COUNT).mapIndexed { index, tag ->
                    val position = index + 1
                    if (position > MAX_LABEL_COUNT) {
                        val otherLabelCount = (it.tags.count() - MAX_LABEL_COUNT).toString()
                        TagUiModel(tag = otherLabelCount, shouldFormatTag = true)
                    } else {
                        TagUiModel(tag = tag.name, shouldFormatTag = false)
                    }
                },
                thumbnail = it.images.first().urlOriginal,
                isBookmarked = it.isBookmarked
            )
        }
        val recipeCount = RecipeCountUiModel(response.metadata.total)

        add(recipeCount)
        addAll(recipeItems)
    }

    fun MutableList<Visitable<*>>.addEmptyStateItem(isFilterSelected: Boolean, title: String) {
        add(
            RecipeEmptyStateUiModel(
                isFilterSelected = isFilterSelected,
                title = title
            )
        )
    }
}