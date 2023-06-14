package com.tokopedia.inbox.universalinbox.test

import com.tokopedia.inbox.universalinbox.test.base.BaseUniversalInboxTest
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class UniversalInboxWidgetMetaTest : BaseUniversalInboxTest() {

    @Test
    fun should_show_widget_based_on_response() {
        // When
        launchActivity()

        // Then
    }

    @Test
    fun should_show_widget_based_on_counter() {
        // Given

        // When

        // Then
    }

    @Test
    fun should_not_show_widget_when_empty() {
        // Given

        // When

        // Then
    }

    @Test
    fun should_not_show_widget_when_zero_counter() {
        // Given

        // When

        // Then
    }
}
