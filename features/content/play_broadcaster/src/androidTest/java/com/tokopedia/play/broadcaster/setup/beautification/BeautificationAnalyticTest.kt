package com.tokopedia.play.broadcaster.setup.beautification

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.test.application.annotations.CassavaTest
import io.mockk.coEvery
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on April 12, 2023
 */
@CassavaTest
@RunWith(AndroidJUnit4ClassRunner::class)
class BeautificationAnalyticTest {

    private val beautificationRobot = BeautificationRobot()

    init {
        beautificationRobot.init()
    }

    @Before
    fun setUp() {
        beautificationRobot.setUp()
    }

    @Test
    fun testAnalytic_clickBeautificationEntryPointOnPreparationPage() {
        beautificationRobot.launch()
            .clickBeautificationMenu()
            .verifyEventAction("click - beautification entry point")
    }

    @Test
    fun testAnalytic_viewBeautificationCoachmark() {
        coEvery {
            beautificationRobot.mockContentCoachMarkSharedPref.hasBeenShown(
                ContentCoachMarkSharedPref.Key.PlayBroadcasterFaceFilter,
                any(),
            )
        } returns false

        beautificationRobot.launch()
            .verifyEventAction("view - beautification coachmark")
    }

    @Test
    fun testAnalytic_clickCloseBeautificationCoachmark() {
        coEvery {
            beautificationRobot.mockContentCoachMarkSharedPref.hasBeenShown(
                ContentCoachMarkSharedPref.Key.PlayBroadcasterFaceFilter,
                any(),
            )
        } returns false

        beautificationRobot.launch()
            .performDelay(1000)
            .clickCloseBeautificationCoachMark()
            .verifyEventAction("click - close beautification coachmark")
    }

    @Test
    fun testAnalytic_openScreenBeautificationEntryPointOnPreparationPage() {
        beautificationRobot.launch()
            .performDelay()
            .verifyOpenScreen("/play broadcast - beautification filter entry point")
    }

    @Test
    fun testAnalytic_openScreenBeautificationBottomSheet() {
        beautificationRobot.launch()
            .clickBeautificationMenu()
            .verifyOpenScreen("/play broadcast - beauty filter creation bottomsheet")
    }

    @Test
    fun testAnalytic_clickCustomFace() {
        beautificationRobot.launch()
            .clickBeautificationMenu()
            .clickCustomFace(1)
            .verifyEventAction("click - custom face shaping")
    }

    @Test
    fun testAnalytic_clickNoneCustomFace() {
        beautificationRobot.launch()
            .clickBeautificationMenu()
            .clickCustomFace(0)
            .verifyEventAction("click - none beauty effects")
    }

    @Test
    fun testAnalytic_clickBeautificationTab() {
        beautificationRobot.launch()
            .clickBeautificationMenu()
            .clickBeautificationPresetTab()
            .verifyEventAction("click - beauty filter tab")
    }
}
