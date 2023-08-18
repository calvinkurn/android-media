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
    get() = values.any { it }
val BottomSheetStatusDefault: Map<BottomSheetType, Boolean>
    get() = mapOf(
        BottomSheetType.Sharing to false,
        BottomSheetType.Product to false,
        BottomSheetType.Kebab to false,
    )


