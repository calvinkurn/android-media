package com.tokopedia.stories.creation.testcase.preparation

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.content.test.util.pressBack
import com.tokopedia.stories.creation.R
import com.tokopedia.stories.creation.helper.StoriesCreationCassavaValidator
import com.tokopedia.stories.creation.view.activity.StoriesCreationActivity

/**
 * Created By : Jonathan Darwin on October 25, 2023
 */
class StoriesCreationPreparationRobot(
    private val context: Context,
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<StoriesCreationActivity>, StoriesCreationActivity>,
    private val cassavaTestRule: CassavaTestRule,
) {

    private val cassavaValidator = StoriesCreationCassavaValidator.buildForStoriesCreation(cassavaTestRule)

    fun verifyAction(eventAction: String) = chainable {
        cassavaValidator.verify(eventAction)
    }

    fun verifyOpenScreen(screenName: String) = chainable {
        cassavaValidator.verifyOpenScreen(screenName)
    }

    fun back() = chainable {
        pressBack()
    }

    fun performDelay(milliseconds: Long = 500L) = chainable {
        delay(milliseconds)
    }

    fun clickAddProduct() = chainable {
        composeTestRule
            .onNodeWithTag(getString(R.string.stories_creation_add_product_test_tag))
            .performClick()
    }

    fun clickUpload() = chainable {
        composeTestRule
            .onNodeWithTag(getString(R.string.stories_creation_upload_test_tag))
            .performClick()
    }

    private fun chainable(block: () -> Unit): StoriesCreationPreparationRobot {
        block()
        return this
    }

    private fun getString(@StringRes id: Int): String {
        return context.resources.getString(id)
    }
}
