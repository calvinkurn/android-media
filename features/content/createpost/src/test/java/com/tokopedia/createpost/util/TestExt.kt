package com.tokopedia.createpost.util

import com.tokopedia.createpost.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.createpost.producttag.view.uimodel.state.ProductTagUiState

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */

fun ProductTagUiState.andThen(fn: ProductTagUiState.() -> Unit) {
    fn()
}

fun List<ProductTagUiEvent>.andThen(fn: List<ProductTagUiEvent>.() -> Unit) {
    fn()
}