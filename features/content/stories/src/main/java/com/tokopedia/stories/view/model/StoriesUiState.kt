package com.tokopedia.stories.view.model

data class StoriesUiState(
    val storiesGroup: StoriesGroupUiModel,
    val storiesDetail: StoriesDetailUiModel,
    val bottomSheetStatus: Map<BottomSheetType, Boolean>
) {
    companion object {
        val Empty
            get() = StoriesUiState(
                storiesDetail = StoriesDetailUiModel.Empty,
                storiesGroup = emptyList(),
                bottomSheetStatus = BottomSheetStatusDefault
            )
    }
}

enum class BottomSheetType {
    Kebab, Product, Sharing, Unknown;
}

val Map<BottomSheetType, Boolean>.isAnyShown: Boolean
    get() = values.any { it }
val BottomSheetStatusDefault: Map<BottomSheetType, Boolean>
    get() = mapOf(
        BottomSheetType.Sharing to false,
        BottomSheetType.Product to false,
        BottomSheetType.Kebab to false,
    )


