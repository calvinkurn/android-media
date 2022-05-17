package com.tokopedia.createpost.producttag.view.uimodel.event

import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on April 27, 2022
 */
sealed interface ProductTagUiEvent {

    data class ProductSelected(val product: ProductUiModel): ProductTagUiEvent
    object ShowSourceBottomSheet: ProductTagUiEvent
    data class OpenAutoCompletePage(val query: String): ProductTagUiEvent
    /** TODO: gonna change the param */
    data class OpenSortFilterBottomSheet(val query: String): ProductTagUiEvent
}