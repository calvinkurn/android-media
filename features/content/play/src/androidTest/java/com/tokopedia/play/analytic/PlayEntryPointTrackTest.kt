package com.tokopedia.play.analytic

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.play.view.activity.PlayActivity
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 11/08/22
 */
class PlayEntryPointTrackTest {
    @get:Rule
    val intentsTestRule = IntentsTestRule(PlayActivity::class.java, false, false)

    @Test
    fun validate() {

    }
}