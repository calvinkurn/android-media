package com.tokopedia.stories.view.model

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.view.ContentTaggedProductUiModel

data class StoriesUiState(
    val storiesGroup: StoriesGroupUiModel,
    val storiesDetail: StoriesDetailUiModel,
    val bottomSheetStatus: Map<BottomSheetType, Boolean>,
    val productSheet: ProductBottomSheetUiState,
    val combineState: CombineState,
) {
    companion object {
        val Empty
            get() = StoriesUiState(
                storiesDetail = StoriesDetailUiModel(),
                storiesGroup = StoriesGroupUiModel(),
                bottomSheetStatus = BottomSheetStatusDefault,
                productSheet = ProductBottomSheetUiState.Empty,
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
    Kebab, Product, Sharing, GVBS, Unknown;
}

data class ProductBottomSheetUiState(
    val products: List<ContentTaggedProductUiModel>,
    val resultState: ResultState
) {
    companion object {
        val Empty
            get() = ProductBottomSheetUiState(
                products = emptyList(),
                resultState = ResultState.Loading,
            )
    }
}

val Map<BottomSheetType, Boolean>.isAnyShown: Boolean
    get() = values.any { it }
val BottomSheetStatusDefault: Map<BottomSheetType, Boolean>
    get() = mapOf(
        BottomSheetType.Sharing to false,
        BottomSheetType.Product to false,
        BottomSheetType.Kebab to false,
        BottomSheetType.GVBS to false,
    )


