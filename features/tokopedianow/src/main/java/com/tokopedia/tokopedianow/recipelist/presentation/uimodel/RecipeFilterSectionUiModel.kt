package com.tokopedia.tokopedianow.recipelist.presentation.uimodel

import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowSectionHeaderUiModel

sealed class RecipeFilterSectionUiModel(
    titleId: Int?
): TokoNowSectionHeaderUiModel(titleResId = titleId) {

    object RecipeSortSectionHeader: RecipeFilterSectionUiModel(R.string.tokopedianow_sort_filter_title)
}