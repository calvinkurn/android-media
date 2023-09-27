package com.tokopedia.stories.view.viewmodel.state

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.view.model.StoriesCampaignUiModel
import com.tokopedia.stories.view.model.StoriesUiModel

data class StoriesUiState(
    val storiesMainData: StoriesUiModel,
    val bottomSheetStatus: Map<BottomSheetType, Boolean>,
    val productSheet: ProductBottomSheetUiState,
) {
    companion object {
        val Empty
            get() = StoriesUiState(
                storiesMainData = StoriesUiModel(),
                bottomSheetStatus = BottomSheetStatusDefault,
                productSheet = ProductBottomSheetUiState.Empty,
                //combineState = CombineState.Empty,
            )
    }
}

enum class BottomSheetType {
    Kebab, Product, Sharing, GVBS, Unknown;
}

data class ProductBottomSheetUiState(
    val products: List<ContentTaggedProductUiModel>,
    val campaign: StoriesCampaignUiModel,
    val resultState: ResultState,
) {
    companion object {
        val Empty
            get() = ProductBottomSheetUiState(
                products = emptyList(),
                campaign = StoriesCampaignUiModel.Unknown,
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
