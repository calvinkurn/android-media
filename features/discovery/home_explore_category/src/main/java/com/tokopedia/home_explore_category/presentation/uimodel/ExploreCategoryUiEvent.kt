package com.tokopedia.home_explore_category.presentation.uimodel

sealed class ExploreCategoryUiEvent {

    data class OnExploreCategoryItemClicked(val categoryUiModel: ExploreCategoryUiModel) :
        ExploreCategoryUiEvent()

    data class OnSubExploreCategoryItemClicked(
        val categoryName: String,
        val subExploreCategoryUiModel: ExploreCategoryUiModel.SubExploreCategoryUiModel,
        val position: Int
    ) : ExploreCategoryUiEvent()

    data class OnSubExploreCategoryItemImpressed(
        val categoryName: String,
        val subExploreCategoryUiModel: ExploreCategoryUiModel.SubExploreCategoryUiModel,
        val position: Int
    ) : ExploreCategoryUiEvent()

    object OnPrimaryButtonErrorClicked : ExploreCategoryUiEvent()

    object OnSecondaryButtonErrorClicked : ExploreCategoryUiEvent()
}
