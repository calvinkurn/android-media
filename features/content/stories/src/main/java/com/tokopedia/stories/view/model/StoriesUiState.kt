package com.tokopedia.stories.view.model

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse

data class StoriesUiState(
    val storiesGroup: List<StoriesGroupUiModel>,
    val storiesDetail: StoriesDetailUiModel,
    val bottomSheetStatus: Map<BottomSheetType, Boolean>,
    val productSheet: ProductBottomSheetUiState,
) {
    companion object {
        val Empty
            get() = StoriesUiState(
                storiesDetail = StoriesDetailUiModel.Empty,
                storiesGroup = emptyList(),
                bottomSheetStatus = BottomSheetStatusDefault,
                productSheet = ProductBottomSheetUiState.Empty,
            )
    }
}

enum class BottomSheetType {
    Kebab, Product, Sharing, Unknown;
}

data class ProductBottomSheetUiState(
    val products: ProductList,
    val vouchers: TokopointsCatalogMVCSummaryResponse,
) {
    data class ProductList(
        val products: List<ContentTaggedProductUiModel>,
        val resultState: ResultState
    ) {
        companion object {
            val Empty
                get() = ProductList(
                    products = emptyList(),
                    resultState = ResultState.Loading
                )
        }
    }

    companion object {
        val Empty
            get() = ProductBottomSheetUiState(
                products = ProductList.Empty,
                vouchers = TokopointsCatalogMVCSummaryResponse(),
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
    )


