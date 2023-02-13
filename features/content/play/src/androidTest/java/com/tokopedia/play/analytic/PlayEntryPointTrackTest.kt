package com.tokopedia.play.analytic

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.play.PLAY_KEY_SOURCE_ID
import com.tokopedia.play.PLAY_KEY_SOURCE_TYPE
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.view.type.PlaySource
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 11/08/22
 */
@UiTest
class PlayEntryPointTrackTest {
    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val playSource: PlaySource = PlaySource(id = "112", type = "FEEDS")

    @get:Rule
    val activityRule : IntentsTestRule<PlayActivity> = object : IntentsTestRule<PlayActivity>(PlayActivity::class.java) {
        override fun getActivityIntent(): Intent {
            return Intent(targetContext, PlayActivity::class.java).apply {
                putExtra(PLAY_KEY_SOURCE_TYPE, playSource.type)
                putExtra(PLAY_KEY_SOURCE_ID, playSource.id)
            }
        }
    }

    @Test
    fun validate() {
        val extra = activityRule.activity.intent.extras
        assert(extra?.get(PLAY_KEY_SOURCE_ID) == playSource.id)
        assert(extra?.get(PLAY_KEY_SOURCE_TYPE) == playSource.type)
    }
}
