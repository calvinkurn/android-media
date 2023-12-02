package com.tokopedia.home_explore_category.presentation.uimodel

sealed class ExploreCategoryUiEvent {

    data class OnExploreCategoryItemClicked(val categoryId: String) :
        ExploreCategoryUiEvent()

    data class OnSubExploreCategoryItemClicked(
        val subExploreCategoryUiModel: ExploreCategoryUiModel.SubExploreCategoryUiModel
    ) : ExploreCategoryUiEvent()

    object OnPrimaryButtonErrorClicked : ExploreCategoryUiEvent()

    object OnSecondaryButtonErrorClicked : ExploreCategoryUiEvent()
}
