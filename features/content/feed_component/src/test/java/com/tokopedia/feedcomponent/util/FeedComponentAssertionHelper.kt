package com.tokopedia.feedcomponent.util

import com.tokopedia.feedcomponent.onboarding.view.uimodel.event.FeedUGCOnboardingUiEvent
import org.assertj.core.api.Assertions

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
infix fun <T : Any> T.equalTo(expected: T) {
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

fun FeedUGCOnboardingUiEvent.assertEvent(event: FeedUGCOnboardingUiEvent) {
    Assertions
        .assertThat(this)
        .isInstanceOf(event::class.java)
}