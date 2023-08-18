package com.tokopedia.stories.view.model

import com.tokopedia.content.common.view.ContentTaggedProductUiModel

data class StoriesUiState(
    val storiesGroup: List<StoriesGroupUiModel>,
    val storiesDetail: StoriesDetailUiModel,
    val bottomSheetStatus: Map<BottomSheetType, Boolean>,
    val products: List<ContentTaggedProductUiModel>,
) {
    companion object {
        val Empty
            get() = StoriesUiState(
                storiesDetail = StoriesDetailUiModel.Empty,
                storiesGroup = emptyList(),
                bottomSheetStatus = BottomSheetStatusDefault,
                products = emptyList(),
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


