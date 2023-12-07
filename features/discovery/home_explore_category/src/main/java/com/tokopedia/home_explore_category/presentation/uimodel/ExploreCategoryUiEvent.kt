package com.tokopedia.home_explore_category.presentation.uimodel

sealed class ExploreCategoryUiEvent {

    data class OnExploreCategoryItemClicked(val categoryUiModel: ExploreCategoryUiModel) :
        ExploreCategoryUiEvent()

    data class OnSubExploreCategoryItemClicked(
        val subExploreCategoryUiModel: ExploreCategoryUiModel.SubExploreCategoryUiModel,
        val position: Int
    ) : ExploreCategoryUiEvent()

    data class OnSubExploreCategoryItemImpressed(
        val subExploreCategoryUiModel: ExploreCategoryUiModel.SubExploreCategoryUiModel
    ) : ExploreCategoryUiEvent()

    object OnPrimaryButtonErrorClicked : ExploreCategoryUiEvent()

    object OnSecondaryButtonErrorClicked : ExploreCategoryUiEvent()
}
