package com.tokopedia.people.testcase

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.people.views.fragment.TestFragment
import com.tokopedia.test.application.compose.createAndroidIntentComposeRule
import org.junit.Rule
import org.junit.Test
import com.tokopedia.people.R
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on August 09, 2023
 */
class TestUiTest {

    @get:Rule
    val composeActivityTestRule = createEmptyComposeRule()

    @Test
    fun testFragment() {

        val scenario = launchFragmentInContainer(themeResId = R.style.AppTheme) {
            TestFragment()
        }

        scenario.moveToState(Lifecycle.State.RESUMED)

        Thread.sleep(5000)

        composeActivityTestRule.onNodeWithTag("review_toggle").performClick()
    }
}
