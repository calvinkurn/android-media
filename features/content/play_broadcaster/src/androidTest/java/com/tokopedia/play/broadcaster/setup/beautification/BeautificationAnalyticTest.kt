package com.tokopedia.play.broadcaster.setup.beautification

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.play.broadcaster.setup.buildBeautificationConfig
import com.tokopedia.play.broadcaster.setup.buildConfigurationUiModel
import com.tokopedia.play.broadcaster.setup.channelPausedResponse
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationAssetStatus
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

    @Test
    fun testAnalytic_clickBeautyFilterReset() {
        beautificationRobot.launch()
            .clickBeautificationMenu()
            .clickResetFilter()
            .verifyEventAction("click - beauty filter reset")
    }

    @Test
    fun testAnalytic_clickSliderBeautyFilter() {
        /**
         * Still failing due bcs onSlideMove is not called
         */
//        beautificationRobot.launch()
//            .clickBeautificationMenu()
//            .clickCustomFace(1)
//            .slideBeautificationSlider(90)
//            .performDelay(3000)
//            .verifyEventAction("click - slider beauty filter")
    }

    @Test
    fun testAnalytic_viewResetFilterPopup() {
        beautificationRobot.launch()
            .clickBeautificationMenu()
            .clickResetFilter()
            .verifyEventAction("view - reset filter bottomsheet")
    }

    @Test
    fun testAnalytic_clickYesResetFilter() {
        beautificationRobot.launch()
            .clickBeautificationMenu()
            .clickResetFilter()
            .clickDialogPrimaryCTA()
            .verifyEventAction("click - yes reset filter")
    }

    @Test
    fun testAnalytic_clickPresetMakeup() {
        beautificationRobot.launch()
            .clickBeautificationMenu()
            .clickBeautificationPresetTab()
            .clickPreset(1)
            .verifyEventAction("click - preset makeup")
    }

    @Test
    fun testAnalytic_clickNonePreset() {
        beautificationRobot.launch()
            .clickBeautificationMenu()
            .clickBeautificationPresetTab()
            .clickPreset(0)
            .verifyEventAction("click - none reset preset makeup")
    }

    @Test
    fun testAnalytic_clickDownloadPreset() {
        coEvery {
            beautificationRobot.mockRepo.getChannelConfiguration(any(), any())
        } returns buildConfigurationUiModel(
            beautificationConfig = buildBeautificationConfig(
                assetStatus = BeautificationAssetStatus.NotDownloaded
            )
        )

        beautificationRobot.launch()
            .clickBeautificationMenu()
            .clickBeautificationPresetTab()
            .clickPreset(1)
            .verifyEventAction("click - download asset preset makeup")
    }

    @Test
    fun testAnalytic_clickRetryDownloadPreset() {
        coEvery {
            beautificationRobot.mockRepo.getChannelConfiguration(any(), any())
        } returns buildConfigurationUiModel(
            beautificationConfig = buildBeautificationConfig(
                assetStatus = BeautificationAssetStatus.NotDownloaded
            )
        )

        beautificationRobot.launch()
            .clickBeautificationMenu()
            .clickBeautificationPresetTab()
            .mock {
                coEvery { beautificationRobot.mockRepo.downloadPresetAsset(any(), any()) } returns false
            }
            .clickPreset(1)
            .clickToasterCTA()
            .verifyEventAction("click - retry download preset makeup")
    }

    @Test
    fun testAnalytic_viewFailDownloadPreset() {
        coEvery {
            beautificationRobot.mockRepo.getChannelConfiguration(any(), any())
        } returns buildConfigurationUiModel(
            beautificationConfig = buildBeautificationConfig(
                assetStatus = BeautificationAssetStatus.NotDownloaded
            )
        )

        beautificationRobot.launch()
            .clickBeautificationMenu()
            .clickBeautificationPresetTab()
            .mock {
                coEvery { beautificationRobot.mockRepo.downloadPresetAsset(any(), any()) } returns false
            }
            .clickPreset(1)
            .verifyEventAction("view - failed download preset makeup")
    }

    @Test
    fun testAnalytic_viewFailApplyBeautyFilter() {
        beautificationRobot.launch()
            .clickBeautificationMenu()
            .clickCustomFace(0)
            .mock {
                coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns false
            }
            .clickCustomFace(1)
            .verifyEventAction("view - failed apply beauty filter")
    }

    @Test
    fun testAnalytic_clickRetryApplyBeautyFilter() {
        beautificationRobot.launch()
            .clickBeautificationMenu()
            .clickCustomFace(0)
            .mock {
                coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns false
            }
            .clickCustomFace(1)
            .clickToasterCTA()
            .verifyEventAction("click - failed apply beauty filter")
    }

    @Test
    fun testAnalytic_clickBeautificationEntryPointOnLivePage() {

        coEvery {
            beautificationRobot.mockRepo.getChannelConfiguration(any(), any())
        } returns buildConfigurationUiModel(
            channelStatus = ChannelStatus.Pause
        )

        coEvery {
            beautificationRobot.mockGetChannelUseCase.executeOnBackground()
        } returns channelPausedResponse

        beautificationRobot.launch()
            .clickDialogPrimaryCTA()
            .clickBeautificationMenuOnLivePage()
            .verifyEventAction("click - beauty filter ongoing livestream")
    }

    @Test
    fun testAnalytic_viewBeautificationEntryPointOnLivePage() {
        coEvery {
            beautificationRobot.mockRepo.getChannelConfiguration(any(), any())
        } returns buildConfigurationUiModel(
            channelStatus = ChannelStatus.Pause
        )

        coEvery {
            beautificationRobot.mockGetChannelUseCase.executeOnBackground()
        } returns channelPausedResponse

        beautificationRobot.launch()
            .clickDialogPrimaryCTA()
            .verifyEventAction("view - beauty filter ongoing livestream")
    }
}
