package com.tokopedia.play.broadcaster.helper

import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule

/**
 * Created By : Jonathan Darwin on April 12, 2023
 */
class PlayBroadcastCassavaValidator private constructor(
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
        fun buildForShorts(cassavaTestRule: CassavaTestRule) = PlayBroadcastCassavaValidator(
            cassavaTestRule = cassavaTestRule,
            analyticFile = "tracker/content/playbroadcaster/play_shorts.json"
        )

        fun buildForBeautification(cassavaTestRule: CassavaTestRule) = PlayBroadcastCassavaValidator(
            cassavaTestRule = cassavaTestRule,
            analyticFile = "tracker/content/playbroadcaster/play_broadcaster_beautification_analytic.json"
        )
    }
}
