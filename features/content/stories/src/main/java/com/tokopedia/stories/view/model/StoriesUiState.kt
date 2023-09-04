package com.tokopedia.stories.view.model

import com.tokopedia.content.common.types.ResultState

data class StoriesUiState(
    val storiesGroup: StoriesGroupUiModel,
    val storiesDetail: StoriesDetailUiModel,
    val bottomSheetStatus: Map<BottomSheetType, Boolean>,
    val combineState: CombineState,
) {
    companion object {
        val Empty
            get() = StoriesUiState(
                storiesDetail = StoriesDetailUiModel(),
                storiesGroup = StoriesGroupUiModel(),
                bottomSheetStatus = BottomSheetStatusDefault,
                combineState = CombineState.Empty,
            )
    }

    data class CombineState(
        val deleteState: ResultState,
    ) {
        companion object {
            val Empty get() = CombineState(deleteState = ResultState.Loading)
        }
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


