package com.tokopedia.stories.view.model

data class StoriesUiState(
    val storiesGroup: List<StoriesGroupUiModel>,
    val storiesDetail: StoriesDetailUiModel,
    val bottomSheetStatus: Map<BottomSheetType, Boolean>
)

enum class BottomSheetType {
    Kebab, Product, Sharing, Unknown;
}
