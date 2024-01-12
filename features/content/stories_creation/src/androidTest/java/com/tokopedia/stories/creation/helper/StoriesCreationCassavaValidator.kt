package com.tokopedia.stories.creation.helper

import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.test.cassava.containsEventAction
import com.tokopedia.content.test.cassava.containsScreenName

/**
 * Created By : Jonathan Darwin on October 20, 2023
 */
class StoriesCreationCassavaValidator private constructor(
    private val cassavaTestRule: CassavaTestRule,
    private val analyticFile: String,
) {

    fun verify(eventAction: String) {
        ViewMatchers.assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction(eventAction)
        )
    }

    fun verifyOpenScreen(screenName: String) {
        ViewMatchers.assertThat(
            cassavaTestRule.validate(analyticFile),
            containsScreenName(screenName)
        )
    }

    companion object {
        fun buildForStoriesCreation(cassavaTestRule: CassavaTestRule) = StoriesCreationCassavaValidator(
            cassavaTestRule = cassavaTestRule,
            analyticFile = "tracker/content/stories/stories_creation.json"
        )
    }
}
