package com.tokopedia.content.product.picker.util.ugc

import com.tokopedia.content.product.picker.ugc.view.uimodel.PagedState
import com.tokopedia.content.product.picker.ugc.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.content.product.picker.ugc.view.uimodel.state.ProductTagUiState
import org.assertj.core.api.Assertions

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

inline fun PagedState.isSuccess() {
    Assertions
        .assertThat(this)
        .isInstanceOf(PagedState.Success::class.java)
}

inline fun PagedState.isError() {
    Assertions
        .assertThat(this)
        .isInstanceOf(PagedState.Error::class.java)
}

inline fun PagedState.assertError(error: Exception) {
    Assertions
        .assertThat(this)
        .isInstanceOf(PagedState.Error::class.java)

    Assertions
        .assertThat((this as PagedState.Error).error)
        .isEqualTo(error)
}

fun ProductTagUiEvent.assertEventError(error: Exception) {
    Assertions
        .assertThat(this)
        .isInstanceOf(ProductTagUiEvent.ShowError::class.java)

    Assertions
        .assertThat((this as ProductTagUiEvent.ShowError).throwable)
        .isEqualTo(error)
}

fun ProductTagUiEvent.assertEvent(event: ProductTagUiEvent) {
    Assertions
        .assertThat(this)
        .isInstanceOf(event::class.java)
}
