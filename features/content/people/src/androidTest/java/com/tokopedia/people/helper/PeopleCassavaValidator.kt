package com.tokopedia.people.helper

import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.test.cassava.containsEventAction
import com.tokopedia.content.test.cassava.containsScreenName

/**
 * Created By : Jonathan Darwin on June 14, 2023
 */
class PeopleCassavaValidator private constructor(
    private val cassavaTestRule: CassavaTestRule,
    private val analyticFile: String
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
        fun buildForUserReview(cassavaTestRule: CassavaTestRule) = PeopleCassavaValidator(
            cassavaTestRule = cassavaTestRule,
            analyticFile = "tracker/content/people/user_profile_review.json"
        )
    }
}
