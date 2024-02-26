package com.tokopedia.content.common.util

import com.tokopedia.content.common.onboarding.view.uimodel.event.UGCOnboardingUiEvent
import com.tokopedia.content.common.onboarding.view.uimodel.state.UGCOnboardingUiState
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
inline fun <reified T> Any.assertType(
    whenType: (T) -> Unit
) {
    Assertions
        .assertThat(this)
        .isInstanceOf(T::class.java)

    whenType(this as T)
}

fun <T : Any> T.assertNotEqualTo(expected: T) {
    Assertions
        .assertThat(this)
        .isNotEqualTo(expected)
}
