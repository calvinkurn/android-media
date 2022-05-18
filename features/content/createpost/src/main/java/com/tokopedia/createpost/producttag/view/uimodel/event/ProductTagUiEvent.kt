package com.tokopedia.createpost.producttag.view.uimodel.event

import com.tokopedia.createpost.producttag.view.uimodel.NetworkResult
import com.tokopedia.createpost.producttag.view.uimodel.PagedState
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.SearchParamUiModel
import com.tokopedia.filter.common.data.DynamicFilterModel

/**
 * Created By : Jonathan Darwin on April 27, 2022
 */
sealed interface ProductTagUiEvent {

    data class ProductSelected(val product: ProductUiModel): ProductTagUiEvent
    object ShowSourceBottomSheet: ProductTagUiEvent
    data class OpenAutoCompletePage(val query: String): ProductTagUiEvent
    data class OpenSortFilterBottomSheet(val param: SearchParamUiModel, val data: DynamicFilterModel): ProductTagUiEvent
    data class SetFilterProductCount(val result: NetworkResult<String>): ProductTagUiEvent
}