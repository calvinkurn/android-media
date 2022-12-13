package com.tokopedia.play.broadcaster.shorts.helper

import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.play.broadcaster.helper.containsEventAction
import com.tokopedia.play.broadcaster.shorts.const.DEFAULT_DELAY

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
}
