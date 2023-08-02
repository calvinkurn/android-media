package com.tokopedia.people.util

import com.tokopedia.people.views.uimodel.event.UserProfileSettingsEvent
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import org.assertj.core.api.Assertions

/**
 * Created By : Jonathan Darwin on July 05, 2022
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

fun UserProfileUiEvent.assertEvent(event: UserProfileUiEvent) {
    Assertions
        .assertThat(this)
        .isInstanceOf(event::class.java)
}

fun UserProfileSettingsEvent.assertEvent(event: UserProfileSettingsEvent) {
    Assertions
        .assertThat(this)
        .isInstanceOf(event::class.java)
}
