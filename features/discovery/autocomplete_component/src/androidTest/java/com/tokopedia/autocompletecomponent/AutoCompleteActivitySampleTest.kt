package com.tokopedia.autocompletecomponent

import androidx.test.espresso.intent.rule.IntentsTestRule
import org.junit.Rule
import org.junit.Test

class AutoCompleteActivitySampleTest {

    @get:Rule
    val activityRule = IntentsTestRule(
        AutoCompleteActivitySample::class.java,
        false,
        true,
    )

    @Test
    fun testAutoCompleteActivitySample() {

    }
}