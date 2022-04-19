package com.tokopedia.autocompletecomponent.suggestion

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test
import org.junit.Rule

@UiTest
class SuggestionTest {

    @get:Rule
    val activityTestRule = IntentsTestRule(
        SuggestionActivityTest::class.java,
        false,
        true,
    )

    @Test
    fun testSuggestion() {
        Thread.sleep(5_000)
    }
}