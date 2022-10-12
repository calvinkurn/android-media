package com.tokopedia.tokopedianow.recipelist.presentation.viewmodel

import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.tokopedianow.common.model.TokoNowChipListUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.recipelist.domain.model.TokonowRecipesFilterSort
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeFilterSectionUiModel.RecipeFilterSectionHeader
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeFilterSectionUiModel.RecipeSortSectionHeader
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter
import com.tokopedia.unit.test.ext.verifyValueEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowRecipeFilterViewModelTest : TokoNowRecipeFilterViewModelTestFixture() {

    @Test
    fun `given sort filter response when getSortFilterOptions should map sort filter items`() {
        val tokoNowRecipesFilterSort = "recipelist/get_sort_filter_response.json"
            .jsonToObject<TokonowRecipesFilterSort>()
        val sortFilterResponse = tokoNowRecipesFilterSort.response.data

        onGetSortFilter_thenReturn(sortFilterResponse)

        viewModel.getSortFilterOptions(emptyList())

        val sortChipItems = listOf(
            TokoNowChipUiModel(
                id = "Newest",
                parentId = "sort_by",
                text = "Terbaru",
                selected = false
            ),
            TokoNowChipUiModel(
                id = "Oldest",
                parentId = "sort_by",
                text = "Terlama",
                selected = false
            )
        )

        val sortChipListItem = TokoNowChipListUiModel(
            parentId = "sort_by",
            items = sortChipItems,
            isMultiSelect = false
        )

        val ingredientHeader = RecipeFilterSectionHeader(
            text = "Bahan",
            appLink = ApplinkConstInternalTokopediaNow.RECIPE_INGREDIENT_BOTTOM_SHEET
        )

        val ingredientFilterChips = TokoNowChipListUiModel(
            parentId = "ingredient_ids",
            items = listOf(
                TokoNowChipUiModel(
                    id = "53",
                    parentId = "ingredient_ids",
                    text = "Balado",
                    imageUrl = "https://images-staging.tokopedia.net/img/MKEzJd/2022/9/29/4a72ad37-909f-4677-8f97-4076a278c285.jpg",
                    selected = false
                ),
                TokoNowChipUiModel(
                    id = "11",
                    parentId = "ingredient_ids",
                    text = "Test Create Ingredient 6",
                    imageUrl = "",
                    selected = false,
                    isPopular = false
                )
            ),
            isMultiSelect = true
        )

        val durationHeader = RecipeFilterSectionHeader(
            text = "Lama Masak",
            appLink = ""
        )

        val durationFilterChips = TokoNowChipListUiModel(
            parentId = "duration",
            items = listOf(
                TokoNowChipUiModel(
                    id = "LessThan30",
                    parentId = "duration",
                    text = "< 30 Menit",
                    imageUrl = "",
                    selected = false
                ),
                TokoNowChipUiModel(
                    id = "30To60",
                    parentId = "duration",
                    text = "30 - 60 Menit",
                    imageUrl = "",
                    selected = false,
                    isPopular = false
                ),
                TokoNowChipUiModel(
                    id = "MoreThan60",
                    parentId = "duration",
                    text = "> 60 Menit",
                    imageUrl = "",
                    selected = false,
                    isPopular = false
                )
            ),
            isMultiSelect = false
        )

        val portionHeader = RecipeFilterSectionHeader(
            text = "Porsi",
            appLink = ""
        )

        val portionFilterChips = TokoNowChipListUiModel(
            parentId = "portion",
            items = listOf(
                TokoNowChipUiModel(
                    id = "LessThan5",
                    parentId = "portion",
                    text = "< 5 Orang",
                    imageUrl = "",
                    selected = false
                ),
                TokoNowChipUiModel(
                    id = "5To10",
                    parentId = "portion",
                    text = "5 - 10 Orang",
                    imageUrl = "",
                    selected = false,
                    isPopular = false
                ),
                TokoNowChipUiModel(
                    id = "MoreThan10",
                    parentId = "portion",
                    text = "> 10 Orang",
                    imageUrl = "",
                    selected = false,
                    isPopular = false
                )
            ),
            isMultiSelect = false
        )

        val tagHeader = RecipeFilterSectionHeader(
            text = "Label",
            appLink = ""
        )

        val tagFilterChips = TokoNowChipListUiModel(
            parentId = "tag_ids",
            items = listOf(
                TokoNowChipUiModel(
                    id = "3",
                    parentId = "tag_ids",
                    text = "Test Create Tag 2",
                    imageUrl = "",
                    selected = false
                ),
                TokoNowChipUiModel(
                    id = "4",
                    parentId = "tag_ids",
                    text = "Test Create Tag 3",
                    imageUrl = "",
                    selected = false,
                    isPopular = false
                )
            ),
            isMultiSelect = true
        )

        val expectedVisitableItems = listOf(
            RecipeSortSectionHeader,
            sortChipListItem,
            ingredientHeader,
            ingredientFilterChips,
            durationHeader,
            durationFilterChips,
            portionHeader,
            portionFilterChips,
            tagHeader,
            tagFilterChips
        )

        viewModel.visitableItems
            .verifyValueEquals(expectedVisitableItems)
    }

    @Test
    fun `given selected filters when getSortFilterOptions should map selected true to sort filter items`() {
        val selectedFilters = listOf(
            SelectedFilter(
                id = "Oldest",
                parentId = "sort_by",
                text = "Terlama"
            ),
            SelectedFilter(
                id = "53",
                parentId = "ingredient_ids",
                text = "Balado"
            ),
            SelectedFilter(
                id = "11",
                parentId = "ingredient_ids",
                text = "Test Create Ingredient 6"
            ),
            SelectedFilter(
                id = "LessThan30",
                parentId = "duration",
                text = "< 30 Menit"
            ),
            SelectedFilter(
                id = "LessThan5",
                parentId = "portion",
                text = "< 5 Orang"
            ),
            SelectedFilter(
                id = "3",
                parentId = "tag_ids",
                text = "Test Create Tag 2"
            )
        )

        val tokoNowRecipesFilterSort = "recipelist/get_sort_filter_response.json"
            .jsonToObject<TokonowRecipesFilterSort>()
        val sortFilterResponse = tokoNowRecipesFilterSort.response.data

        onGetSortFilter_thenReturn(sortFilterResponse)

        viewModel.getSortFilterOptions(selectedFilters)

        val sortChipItems = listOf(
            TokoNowChipUiModel(
                id = "Newest",
                parentId = "sort_by",
                text = "Terbaru",
                selected = false
            ),
            TokoNowChipUiModel(
                id = "Oldest",
                parentId = "sort_by",
                text = "Terlama",
                selected = true
            )
        )

        val sortChipListItem = TokoNowChipListUiModel(
            parentId = "sort_by",
            items = sortChipItems,
            isMultiSelect = false
        )

        val ingredientHeader = RecipeFilterSectionHeader(
            text = "Bahan",
            appLink = ApplinkConstInternalTokopediaNow.RECIPE_INGREDIENT_BOTTOM_SHEET
        )

        val ingredientFilterChips = TokoNowChipListUiModel(
            parentId = "ingredient_ids",
            items = listOf(
                TokoNowChipUiModel(
                    id = "53",
                    parentId = "ingredient_ids",
                    text = "Balado",
                    imageUrl = "https://images-staging.tokopedia.net/img/MKEzJd/2022/9/29/4a72ad37-909f-4677-8f97-4076a278c285.jpg",
                    selected = true
                ),
                TokoNowChipUiModel(
                    id = "11",
                    parentId = "ingredient_ids",
                    text = "Test Create Ingredient 6",
                    imageUrl = "",
                    selected = true,
                    isPopular = false
                )
            ),
            isMultiSelect = true
        )

        val durationHeader = RecipeFilterSectionHeader(
            text = "Lama Masak",
            appLink = ""
        )

        val durationFilterChips = TokoNowChipListUiModel(
            parentId = "duration",
            items = listOf(
                TokoNowChipUiModel(
                    id = "LessThan30",
                    parentId = "duration",
                    text = "< 30 Menit",
                    imageUrl = "",
                    selected = true
                ),
                TokoNowChipUiModel(
                    id = "30To60",
                    parentId = "duration",
                    text = "30 - 60 Menit",
                    imageUrl = "",
                    selected = false,
                    isPopular = false
                ),
                TokoNowChipUiModel(
                    id = "MoreThan60",
                    parentId = "duration",
                    text = "> 60 Menit",
                    imageUrl = "",
                    selected = false,
                    isPopular = false
                )
            ),
            isMultiSelect = false
        )

        val portionHeader = RecipeFilterSectionHeader(
            text = "Porsi",
            appLink = ""
        )

        val portionFilterChips = TokoNowChipListUiModel(
            parentId = "portion",
            items = listOf(
                TokoNowChipUiModel(
                    id = "LessThan5",
                    parentId = "portion",
                    text = "< 5 Orang",
                    imageUrl = "",
                    selected = true
                ),
                TokoNowChipUiModel(
                    id = "5To10",
                    parentId = "portion",
                    text = "5 - 10 Orang",
                    imageUrl = "",
                    selected = false,
                    isPopular = false
                ),
                TokoNowChipUiModel(
                    id = "MoreThan10",
                    parentId = "portion",
                    text = "> 10 Orang",
                    imageUrl = "",
                    selected = false,
                    isPopular = false
                )
            ),
            isMultiSelect = false
        )

        val tagHeader = RecipeFilterSectionHeader(
            text = "Label",
            appLink = ""
        )

        val tagFilterChips = TokoNowChipListUiModel(
            parentId = "tag_ids",
            items = listOf(
                TokoNowChipUiModel(
                    id = "3",
                    parentId = "tag_ids",
                    text = "Test Create Tag 2",
                    imageUrl = "",
                    selected = true
                ),
                TokoNowChipUiModel(
                    id = "4",
                    parentId = "tag_ids",
                    text = "Test Create Tag 3",
                    imageUrl = "",
                    selected = false,
                    isPopular = false
                )
            ),
            isMultiSelect = true
        )

        val expectedVisitableItems = listOf(
            RecipeSortSectionHeader,
            sortChipListItem,
            ingredientHeader,
            ingredientFilterChips,
            durationHeader,
            durationFilterChips,
            portionHeader,
            portionFilterChips,
            tagHeader,
            tagFilterChips
        )

        viewModel.visitableItems
            .verifyValueEquals(expectedVisitableItems)
    }

    @Test
    fun `given sort filter error when getIngredients should map ingredient items`() {
        onGetSortFilter_thenReturn(NullPointerException())

        viewModel.getSortFilterOptions(emptyList())

        viewModel.visitableItems
            .verifyValueEquals(null)
    }
}

