package com.tokopedia.play.broadcaster.setup.beautification

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.play.broadcaster.setup.buildBeautificationConfig
import com.tokopedia.play.broadcaster.setup.buildConfigurationUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationAssetStatus
import com.tokopedia.test.application.annotations.CassavaTest
import io.mockk.coEvery
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on April 12, 2023
 */
@CassavaTest
@RunWith(AndroidJUnit4ClassRunner::class)
class BeautificationAnalyticTest {

    @get:Rule
    val permissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.RECORD_AUDIO
    )

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
                any()
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
            .clickCustomFace(CUSTOM_FACE_1)
            .verifyEventAction("click - custom face shaping")
            .clickCustomFace(NONE)
            .verifyEventAction("click - none beauty effects")
            .clickBeautificationPresetTab()
            .clickBeautificationCustomFaceTab()
            .verifyEventAction("click - beauty filter tab")
            .clickResetFilter()
            .clickDialogSecondaryCTA()
            .verifyEventAction("click - beauty filter reset")
            .clickCustomFace(CUSTOM_FACE_1)
            .performDelay()
            .slideBeautificationSlider(200f)
            .performDelay(1000)
            .verifyEventAction("click - slider beauty filter")
            .clickResetFilter()
            .clickDialogPrimaryCTA()
            .verifyEventAction("view - reset filter bottomsheet")
            .verifyEventAction("click - yes reset filter")
            .clickBeautificationPresetTab()
            .clickPreset(PRESET_1)
            .verifyEventAction("click - preset makeup")
            .clickPreset(NONE)
            .verifyEventAction("click - none reset preset makeup")
            .mock {
                coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns false
            }
            .clickBeautificationCustomFaceTab()
            .clickCustomFace(CUSTOM_FACE_2)
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
            .performDelay(1000L)
            .clickBeautificationMenu()
            .clickBeautificationPresetTab()
            .mock {
                coEvery { beautificationRobot.mockRepo.downloadPresetAsset(any(), any()) } returns false
            }
            .clickPreset(PRESET_1)
            .performDelay()
            .mock {
                coEvery { beautificationRobot.mockRepo.downloadPresetAsset(any(), any()) } returns true
            }
            .clickToasterCTA()
            .verifyEventAction("click - download asset preset makeup")
            .verifyEventAction("click - retry download preset makeup")
            .verifyEventAction("view - failed download preset makeup")
            .clickBeautificationCustomFaceTab()
            .clickCustomFace(NONE)
            .mock {
                coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns false
            }
            .clickCustomFace(CUSTOM_FACE_1)
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
            .clickCustomFace(CUSTOM_FACE_2)
            .verifyEventAction("click - custom face shaping")
            .clickCustomFace(NONE)
            .verifyEventAction("click - none beauty effects")
            .clickBeautificationPresetTab()
            .clickBeautificationCustomFaceTab()
            .verifyEventAction("click - beauty filter tab")
            .clickResetFilter()
            .clickDialogSecondaryCTA()
            .verifyEventAction("click - beauty filter reset")
            .verifyEventAction("view - reset filter bottomsheet")
            .clickCustomFace(CUSTOM_FACE_1)
            .performDelay()
            .slideBeautificationSlider(200f)
            .performDelay(1000)
            .verifyEventAction("click - slider beauty filter")
            .clickBeautificationPresetTab()
            .clickPreset(PRESET_1)
            .performDelay()
            .slideBeautificationSlider(200f)
            .performDelay(1000)
            .verifyEventAction("click - slider beauty filter")
            .clickResetFilter()
            .clickDialogPrimaryCTA()
            .verifyEventAction("click - yes reset filter")
            .clickBeautificationPresetTab()
            .clickPreset(PRESET_2)
            .verifyEventAction("click - preset makeup")
            .clickPreset(NONE)
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
            .clickPreset(PRESET_1)
            .performDelay()
            .mock {
                coEvery { beautificationRobot.mockRepo.downloadPresetAsset(any(), any()) } returns true
            }
            .clickToasterCTA()
            .verifyEventAction("click - download asset preset makeup")
            .verifyEventAction("click - retry download preset makeup")
            .verifyEventAction("view - failed download preset makeup")
            .clickBeautificationCustomFaceTab()
            .clickCustomFace(NONE)
            .mock {
                coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns false
            }
            .clickCustomFace(CUSTOM_FACE_1)
            .performDelay()
            .mock {
                coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns true
            }
            .clickToasterCTA()
            .verifyEventAction("view - failed apply beauty filter")
            .verifyEventAction("click - failed apply beauty filter")
    }

    /** This test is flaky, will revisit this later */
//    @Test
//    fun testAnalytic_beautification_reapplyBeautyFilter() {
//        coEvery { beautificationRobot.mockValueWrapper.rebindEffectToasterDuration } returns Toaster.LENGTH_INDEFINITE
//        coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns false
//
//        beautificationRobot
//            .launch()
//            .performDelay(3000)
//            .mock {
//                coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns true
//            }
//            .clickToasterCTA()
//            .verifyEventAction("view - reapply beauty filter")
//            .verifyEventAction("click - reapply beauty filter")
//    }

    companion object {
        private const val NONE = 0
        private const val CUSTOM_FACE_1 = 1
        private const val CUSTOM_FACE_2 = 2
        private const val PRESET_1 = 1
        private const val PRESET_2 = 2
    }
}
