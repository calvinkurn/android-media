package com.tokopedia.tokopedianow.recipelist.presentation.uimodel

import androidx.annotation.StringRes
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowSectionHeaderUiModel

sealed class RecipeFilterSectionUiModel(
    @StringRes
    titleId: Int? = null,
    title: String = "",
    appLink: String = ""
) : TokoNowSectionHeaderUiModel(
    titleResId = titleId,
    title = title,
    seeAllAppLink = appLink
) {

    object RecipeSortSectionHeader : RecipeFilterSectionUiModel(
        titleId = R.string.tokopedianow_sort_filter_title
    )

    data class RecipeFilterSectionHeader(
        val text: String,
        val appLink: String = ""
    ) : RecipeFilterSectionUiModel(
        title = text,
        appLink = appLink
    )
}