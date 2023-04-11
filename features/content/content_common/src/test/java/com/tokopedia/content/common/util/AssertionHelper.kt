package com.tokopedia.content.common.util

import com.tokopedia.content.common.onboarding.view.uimodel.event.UGCOnboardingUiEvent
import com.tokopedia.content.common.onboarding.view.uimodel.state.UGCOnboardingUiState
import com.tokopedia.content.common.producttag.view.uimodel.PagedState
import com.tokopedia.content.common.producttag.view.uimodel.event.ProductTagUiEvent
import org.assertj.core.api.Assertions

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */
fun <T : Any> T.assertEqualTo(expected: T) {
    Assertions
        .assertThat(this)
        .isEqualTo(expected)
}

fun <T : Boolean> T.assertTrue() {
    Assertions
        .assertThat(this)
        .isEqualTo(true)
}

fun <T : Boolean> T.assertFalse() {
    Assertions
        .assertThat(this)
        .isEqualTo(false)
}

fun String.assertEmpty() {
    Assertions
        .assertThat(this)
        .isEqualTo("")
}

fun <T : Any> List<T>.assertEmpty() {
    Assertions
        .assertThat(this.size)
        .isEqualTo(0)
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

infix fun <T : Any> T.equalTo(expected: T) {
    this.assertEqualTo(expected)
}

infix fun UGCOnboardingUiState.andThen(fn: UGCOnboardingUiState.() -> Unit) {
    fn()
}

infix fun List<UGCOnboardingUiEvent>.andThen(fn: List<UGCOnboardingUiEvent>.() -> Unit) {
    fn()
}

infix fun Pair<UGCOnboardingUiState, List<UGCOnboardingUiEvent>>.andThen(
    fn: Pair<UGCOnboardingUiState, List<UGCOnboardingUiEvent>>.
        (UGCOnboardingUiState, List<UGCOnboardingUiEvent>) -> Unit
) {
    fn(first, second)
}

fun UGCOnboardingUiEvent.assertEvent(event: UGCOnboardingUiEvent) {
    Assertions
        .assertThat(this)
        .isInstanceOf(event::class.java)
}
