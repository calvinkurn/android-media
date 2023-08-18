package com.tokopedia.stories.view.model

import com.tokopedia.kotlin.extensions.orFalse

data class StoriesUiState(
    val storiesGroup: List<StoriesGroupUiModel>,
    val storiesDetail: StoriesDetailUiModel,
    val bottomSheetStatus: Map<BottomSheetType, Boolean>
)

enum class BottomSheetType {
    Kebab, Product, Sharing, Unknown;
}

val Map<BottomSheetType, Boolean>.isAnyShown: Boolean
    get() = this[BottomSheetType.Product]?.orFalse() == true ||
        this[BottomSheetType.Kebab]?.orFalse() == true ||
        this[BottomSheetType.Sharing]?.orFalse() == true
