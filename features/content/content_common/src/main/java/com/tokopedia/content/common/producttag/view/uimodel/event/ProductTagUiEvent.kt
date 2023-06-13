package com.tokopedia.content.common.producttag.view.uimodel.event

import com.tokopedia.content.common.producttag.view.uimodel.*
import com.tokopedia.filter.common.data.DynamicFilterModel

/**
 * Created By : Jonathan Darwin on April 27, 2022
 */
sealed interface ProductTagUiEvent {
    data class ShowError(val throwable: Throwable, val action: (() -> Unit)? = null) : ProductTagUiEvent

    data class FinishProductTag(val products: List<SelectedProductUiModel>) : ProductTagUiEvent
    object ShowSourceBottomSheet : ProductTagUiEvent

    data class OpenAutoCompletePage(val query: String) : ProductTagUiEvent
    data class OpenProductSortFilterBottomSheet(val param: SearchParamUiModel, val data: DynamicFilterModel) : ProductTagUiEvent
    data class OpenShopSortFilterBottomSheet(val param: SearchParamUiModel, val data: DynamicFilterModel) : ProductTagUiEvent
    object OpenMyShopSortBottomSheet : ProductTagUiEvent

    data class SetProductFilterProductCount(val result: NetworkResult<String>) : ProductTagUiEvent
    data class SetShopFilterProductCount(val result: NetworkResult<String>) : ProductTagUiEvent

    object MaxSelectedProductReached : ProductTagUiEvent

    data class HitGlobalSearchProductTracker(
        val header: SearchHeaderUiModel,
        val param: SearchParamUiModel,
    ) : ProductTagUiEvent

    data class HitGlobalSearchShopTracker(
        val header: SearchHeaderUiModel,
        val param: SearchParamUiModel,
    ) : ProductTagUiEvent
}