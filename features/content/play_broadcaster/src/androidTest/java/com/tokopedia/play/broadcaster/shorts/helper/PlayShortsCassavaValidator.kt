package com.tokopedia.play.broadcaster.shorts.helper

import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.play.broadcaster.helper.containsEventAction
import com.tokopedia.play.broadcaster.helper.containsScreenName

/**
 * Created By : Jonathan Darwin on December 12, 2022
 */
class PlayShortsCassavaValidator(
    private val cassavaTestRule: CassavaTestRule
) {

    private val analyticFile = "tracker/content/playbroadcaster/play_shorts.json"

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
}
