package com.tokopedia.content.common.util

import com.tokopedia.content.common.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.content.common.producttag.view.uimodel.state.ProductTagUiState

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */

infix fun ProductTagUiState.andThen(fn: ProductTagUiState.() -> Unit) {
    fn()
}

infix fun List<ProductTagUiEvent>.andThen(fn: List<ProductTagUiEvent>.() -> Unit) {
    fn()
}

infix fun Pair<ProductTagUiState, List<ProductTagUiEvent>>.andThen(
    fn: Pair<ProductTagUiState, List<ProductTagUiEvent>>.
        (ProductTagUiState,  List<ProductTagUiEvent>) -> Unit
) {
    fn(first, second)
}
