package com.tokopedia.play.broadcaster.setup.beautification

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.play.broadcaster.setup.buildBeautificationConfig
import com.tokopedia.play.broadcaster.setup.buildConfigurationUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationAssetStatus
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.unifycomponents.Toaster
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
    fun testAnalytic_beautification_preparation_happyPath() {
        coEvery {
            beautificationRobot.mockContentCoachMarkSharedPref.hasBeenShown(
                ContentCoachMarkSharedPref.Key.PlayBroadcasterFaceFilter,
                any(),
            )
        } returns false

        beautificationRobot
            .launch()

            .performDelay(1000)
            .clickCloseBeautificationCoachMark()
            .verifyEventAction("view - beautification coachmark")
            .verifyEventAction("click - close beautification coachmark")
            .verifyOpenScreen("/play broadcast - beautification filter entry point")

            .clickBeautificationMenu()
            .verifyEventAction("click - beautification entry point")
            .verifyOpenScreen("/play broadcast - beauty filter creation bottomsheet")

            .clickCustomFace(1)
            .verifyEventAction("click - custom face shaping")
            .clickCustomFace(0)
            .verifyEventAction("click - none beauty effects")

            .clickBeautificationPresetTab()
            .clickBeautificationCustomFaceTab()
            .verifyEventAction("click - beauty filter tab")

            .clickResetFilter()
            .clickDialogSecondaryCTA()
            .verifyEventAction("click - beauty filter reset")

            .clickCustomFace(1)
            .performDelay()
            .slideBeautificationSlider(200f)
            .performDelay(1000)
            .verifyEventAction("click - slider beauty filter")

            .clickResetFilter()
            .clickDialogPrimaryCTA()
            .verifyEventAction("view - reset filter bottomsheet")
            .verifyEventAction("click - yes reset filter")

            .clickBeautificationPresetTab()
            .clickPreset(1)
            .verifyEventAction("click - preset makeup")

            .clickPreset(0)
            .verifyEventAction("click - none reset preset makeup")

            .mock {
                coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns false
            }
            .clickBeautificationCustomFaceTab()
            .clickCustomFace(2)
            .verifyEventAction("view - failed apply beauty filter")
    }

    @Test
    fun testAnalytic_beautification_preparation_edgeCasePath() {
        coEvery {
            beautificationRobot.mockRepo.getChannelConfiguration(any(), any())
        } returns buildConfigurationUiModel(
            beautificationConfig = buildBeautificationConfig(
                assetStatus = BeautificationAssetStatus.NotDownloaded
            )
        )

        beautificationRobot
            .launch()
            .clickBeautificationMenu()
            .clickBeautificationPresetTab()
            .mock {
                coEvery { beautificationRobot.mockRepo.downloadPresetAsset(any(), any()) } returns false
            }
            .clickPreset(1)
            .performDelay()
            .mock {
                coEvery { beautificationRobot.mockRepo.downloadPresetAsset(any(), any()) } returns true
            }
            .clickToasterCTA()
            .verifyEventAction("click - download asset preset makeup")
            .verifyEventAction("click - retry download preset makeup")
            .verifyEventAction("view - failed download preset makeup")

            .clickBeautificationCustomFaceTab()
            .clickCustomFace(0)
            .mock {
                coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns false
            }
            .clickCustomFace(1)
            .performDelay()
            .mock {
                coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns true
            }
            .clickToasterCTA()
            .verifyEventAction("view - failed apply beauty filter")
            .verifyEventAction("click - failed apply beauty filter")
    }

    @Test
    fun testAnalytic_beautification_live_happyPath() {
        beautificationRobot
            .launchLive()

            .clickBeautificationMenuOnLivePage()
            .verifyEventAction("view - beauty filter ongoing livestream")
            .verifyEventAction("click - beauty filter ongoing livestream")

            .clickCustomFace(2)
            .verifyEventAction("click - custom face shaping")

            .clickCustomFace(0)
            .verifyEventAction("click - none beauty effects")

            .clickBeautificationPresetTab()
            .clickBeautificationCustomFaceTab()
            .verifyEventAction("click - beauty filter tab")

            .clickResetFilter()
            .clickDialogSecondaryCTA()
            .verifyEventAction("click - beauty filter reset")
            .verifyEventAction("view - reset filter bottomsheet")

            .clickCustomFace(1)
            .performDelay()
            .slideBeautificationSlider(200f)
            .performDelay(1000)
            .verifyEventAction("click - slider beauty filter")

            .clickBeautificationPresetTab()
            .clickPreset(1)
            .performDelay()
            .slideBeautificationSlider(200f)
            .performDelay(1000)
            .verifyEventAction("click - slider beauty filter")

            .clickResetFilter()
            .clickDialogPrimaryCTA()
            .verifyEventAction("click - yes reset filter")

            .clickBeautificationPresetTab()
            .clickPreset(2)
            .verifyEventAction("click - preset makeup")

            .clickPreset(0)
            .verifyEventAction("click - none reset preset makeup")
    }

    @Test
    fun testAnalytic_beautification_live_edgeCasePath() {
        beautificationRobot
            .launchLiveWithNoDownloadedPreset()
            .clickBeautificationMenuOnLivePage()
            .clickBeautificationPresetTab()
            .mock {
                coEvery { beautificationRobot.mockRepo.downloadPresetAsset(any(), any()) } returns false
            }
            .clickPreset(1)
            .performDelay()
            .mock {
                coEvery { beautificationRobot.mockRepo.downloadPresetAsset(any(), any()) } returns true
            }
            .clickToasterCTA()
            .verifyEventAction("click - download asset preset makeup")
            .verifyEventAction("click - retry download preset makeup")
            .verifyEventAction("view - failed download preset makeup")

            .clickBeautificationCustomFaceTab()
            .clickCustomFace(0)
            .mock {
                coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns false
            }
            .clickCustomFace(1)
            .performDelay()
            .mock {
                coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns true
            }
            .clickToasterCTA()
            .verifyEventAction("view - failed apply beauty filter")
            .verifyEventAction("click - failed apply beauty filter")
    }

    @Test
    fun testAnalytic_beautification_reapplyBeautyFilter() {
        coEvery { beautificationRobot.mockValueWrapper.rebindEffectToasterDuration } returns Toaster.LENGTH_INDEFINITE
        coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns false

        beautificationRobot.launch()
            .performDelay(1000)
            .clickToasterCTA()
            .verifyEventAction("click - reapply beauty filter")
            .verifyEventAction("view - reapply beauty filter")
    }
}
