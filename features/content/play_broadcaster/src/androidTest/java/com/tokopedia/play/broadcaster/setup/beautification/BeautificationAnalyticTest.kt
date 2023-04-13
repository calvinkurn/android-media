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
        beautificationRobot
            .launch()
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

        beautificationRobot
            .launch()
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

        beautificationRobot
            .launch()
            .performDelay(1000)
            .clickCloseBeautificationCoachMark()
            .verifyEventAction("click - close beautification coachmark")
    }

    @Test
    fun testAnalytic_openScreenBeautificationEntryPointOnPreparationPage() {
        beautificationRobot
            .launch()
            .performDelay()
            .verifyOpenScreen("/play broadcast - beautification filter entry point")
    }

    @Test
    fun testAnalytic_openScreenBeautificationBottomSheet() {
        beautificationRobot
            .launch()
            .clickBeautificationMenu()
            .verifyOpenScreen("/play broadcast - beauty filter creation bottomsheet")
    }

    @Test
    fun testAnalytic_clickCustomFace_preparationPage() {
        beautificationRobot
            .launch()
            .clickBeautificationMenu()
            .clickCustomFace(1)
            .verifyEventAction("click - custom face shaping")
    }

    @Test
    fun testAnalytic_clickCustomFace_livePage() {
        beautificationRobot
            .launchLive()
            .clickBeautificationMenuOnLivePage()
            .clickCustomFace(2)
            .verifyEventAction("click - custom face shaping")
    }

    @Test
    fun testAnalytic_clickNoneCustomFace_preparationPage() {
        beautificationRobot
            .launch()
            .clickBeautificationMenu()
            .clickCustomFace(0)
            .verifyEventAction("click - none beauty effects")
    }

    @Test
    fun testAnalytic_clickNoneCustomFace_livePage() {
        beautificationRobot
            .launchLive()
            .clickBeautificationMenuOnLivePage()
            .clickCustomFace(0)
            .verifyEventAction("click - none beauty effects")
    }

    @Test
    fun testAnalytic_clickBeautificationTab_customFaceTab_preparationPage() {
        beautificationRobot
            .launch()
            .clickBeautificationMenu()
            .clickBeautificationPresetTab()
            .clickBeautificationCustomFaceTab()
            .verifyEventAction("click - beauty filter tab")
    }

    @Test
    fun testAnalytic_clickBeautificationTab_customFaceTab_livePage() {
        beautificationRobot
            .launchLive()
            .clickBeautificationMenuOnLivePage()
            .clickBeautificationPresetTab()
            .clickBeautificationCustomFaceTab()
            .verifyEventAction("click - beauty filter tab")
    }

    @Test
    fun testAnalytic_clickBeautificationTab_presetTab_preparationPage() {
        beautificationRobot
            .launch()
            .clickBeautificationMenu()
            .clickBeautificationPresetTab()
            .verifyEventAction("click - beauty filter tab")
    }

    @Test
    fun testAnalytic_clickBeautificationTab_presetTab_livePage() {
        beautificationRobot
            .launchLive()
            .clickBeautificationMenuOnLivePage()
            .clickBeautificationPresetTab()
            .verifyEventAction("click - beauty filter tab")
    }

    @Test
    fun testAnalytic_clickBeautyFilterReset_preparationPage() {
        beautificationRobot
            .launch()
            .clickBeautificationMenu()
            .clickResetFilter()
            .verifyEventAction("click - beauty filter reset")
    }

    @Test
    fun testAnalytic_clickBeautyFilterReset_livePage() {
        beautificationRobot
            .launchLive()
            .clickBeautificationMenuOnLivePage()
            .clickResetFilter()
            .verifyEventAction("click - beauty filter reset")
    }

    @Test
    fun testAnalytic_clickSliderBeautyFilter_customFace_preparationPage() {
        beautificationRobot
            .launch()
            .clickBeautificationMenu()
            .clickCustomFace(1)
            .performDelay()
            .slideBeautificationSlider(200f)
            .performDelay(1000)
            .verifyEventAction("click - slider beauty filter")
    }

    @Test
    fun testAnalytic_clickSliderBeautyFilter_customFace_livePage() {
        beautificationRobot
            .launchLive()
            .clickBeautificationMenuOnLivePage()
            .clickCustomFace(1)
            .performDelay()
            .slideBeautificationSlider(200f)
            .performDelay(1000)
            .verifyEventAction("click - slider beauty filter")
    }

    @Test
    fun testAnalytic_clickSliderBeautyFilter_preset_preparationPage() {
        beautificationRobot
            .launch()
            .clickBeautificationMenu()
            .clickBeautificationPresetTab()
            .clickPreset(1)
            .performDelay()
            .slideBeautificationSlider(200f)
            .performDelay(1000)
            .verifyEventAction("click - slider beauty filter")
    }

    @Test
    fun testAnalytic_clickSliderBeautyFilter_preset_livePage() {
        beautificationRobot
            .launchLive()
            .clickBeautificationMenuOnLivePage()
            .clickBeautificationPresetTab()
            .clickPreset(1)
            .performDelay()
            .slideBeautificationSlider(200f)
            .performDelay(1000)
            .verifyEventAction("click - slider beauty filter")
    }

    @Test
    fun testAnalytic_viewResetFilterPopup_customFace_preparationPage() {
        beautificationRobot
            .launch()
            .clickBeautificationMenu()
            .clickResetFilter()
            .verifyEventAction("view - reset filter bottomsheet")
    }

    @Test
    fun testAnalytic_viewResetFilterPopup_preset_preparationPage() {
        beautificationRobot
            .launch()
            .clickBeautificationMenu()
            .clickBeautificationPresetTab()
            .clickResetFilter()
            .verifyEventAction("view - reset filter bottomsheet")
    }

    @Test
    fun testAnalytic_viewResetFilterPopup_customFace_livePage() {
        beautificationRobot
            .launchLive()
            .clickBeautificationMenuOnLivePage()
            .clickResetFilter()
            .verifyEventAction("view - reset filter bottomsheet")
    }

    @Test
    fun testAnalytic_viewResetFilterPopup_preset_livePage() {
        beautificationRobot
            .launchLive()
            .clickBeautificationMenuOnLivePage()
            .clickBeautificationPresetTab()
            .clickResetFilter()
            .verifyEventAction("view - reset filter bottomsheet")
    }

    @Test
    fun testAnalytic_clickYesResetFilter_preparationPage() {
        beautificationRobot
            .launch()
            .clickBeautificationMenu()
            .clickResetFilter()
            .clickDialogPrimaryCTA()
            .verifyEventAction("click - yes reset filter")
    }

    @Test
    fun testAnalytic_clickYesResetFilter_livePage() {
        beautificationRobot
            .launchLive()
            .clickBeautificationMenuOnLivePage()
            .clickResetFilter()
            .clickDialogPrimaryCTA()
            .verifyEventAction("click - yes reset filter")
    }

    @Test
    fun testAnalytic_clickPresetMakeup_preparationPage() {
        beautificationRobot
            .launch()
            .clickBeautificationMenu()
            .clickBeautificationPresetTab()
            .clickPreset(1)
            .verifyEventAction("click - preset makeup")
    }

    @Test
    fun testAnalytic_clickPresetMakeup_livePage() {
        beautificationRobot
            .launchLive()
            .clickBeautificationMenuOnLivePage()
            .clickBeautificationPresetTab()
            .clickPreset(1)
            .verifyEventAction("click - preset makeup")
    }

    @Test
    fun testAnalytic_clickNonePreset_preparationPage() {
        beautificationRobot.launch()
            .clickBeautificationMenu()
            .clickBeautificationPresetTab()
            .clickPreset(0)
            .verifyEventAction("click - none reset preset makeup")
    }

    @Test
    fun testAnalytic_clickNonePreset_livePage() {
        beautificationRobot
            .launchLive()
            .clickBeautificationMenuOnLivePage()
            .clickBeautificationPresetTab()
            .clickPreset(0)
            .verifyEventAction("click - none reset preset makeup")
    }

    @Test
    fun testAnalytic_clickDownloadPreset_preparationPage() {
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
    fun testAnalytic_clickDownloadPreset_livePage() {
        beautificationRobot
            .launchLiveWithNoDownloadedPreset()
            .clickBeautificationMenuOnLivePage()
            .clickBeautificationPresetTab()
            .clickPreset(1)
            .verifyEventAction("click - download asset preset makeup")
    }

    @Test
    fun testAnalytic_clickRetryDownloadPreset_preparationPage() {
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
            .clickToasterCTA()
            .verifyEventAction("click - retry download preset makeup")
    }

    @Test
    fun testAnalytic_clickRetryDownloadPreset_livePage() {
        beautificationRobot
            .launchLiveWithNoDownloadedPreset()
            .clickBeautificationMenuOnLivePage()
            .clickBeautificationPresetTab()
            .mock {
                coEvery { beautificationRobot.mockRepo.downloadPresetAsset(any(), any()) } returns false
            }
            .clickPreset(1)
            .performDelay()
            .clickToasterCTA()
            .verifyEventAction("click - retry download preset makeup")
    }

    @Test
    fun testAnalytic_viewFailDownloadPreset_preparationPage() {
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
            .verifyEventAction("view - failed download preset makeup")
    }

    @Test
    fun testAnalytic_viewFailDownloadPreset_livePage() {
        beautificationRobot
            .launchLiveWithNoDownloadedPreset()
            .clickBeautificationMenuOnLivePage()
            .clickBeautificationPresetTab()
            .mock {
                coEvery { beautificationRobot.mockRepo.downloadPresetAsset(any(), any()) } returns false
            }
            .clickPreset(1)
            .verifyEventAction("view - failed download preset makeup")
    }

    @Test
    fun testAnalytic_viewFailApplyBeautyFilter_preparationPage() {
        beautificationRobot
            .launch()
            .clickBeautificationMenu()
            .clickCustomFace(0)
            .mock {
                coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns false
            }
            .clickCustomFace(1)
            .verifyEventAction("view - failed apply beauty filter")
    }

    @Test
    fun testAnalytic_viewFailApplyBeautyFilter_livePage() {
        beautificationRobot
            .launchLive()
            .clickBeautificationMenuOnLivePage()
            .clickCustomFace(0)
            .mock {
                coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns false
            }
            .clickCustomFace(1)
            .verifyEventAction("view - failed apply beauty filter")
    }

    @Test
    fun testAnalytic_clickRetryApplyBeautyFilter_preparationPage() {
        beautificationRobot
            .launch()
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
    fun testAnalytic_clickRetryApplyBeautyFilter_livePage() {
        beautificationRobot
            .launchLive()
            .clickBeautificationMenuOnLivePage()
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
        beautificationRobot
            .launchLive()
            .clickBeautificationMenuOnLivePage()
            .verifyEventAction("click - beauty filter ongoing livestream")
    }

    @Test
    fun testAnalytic_viewBeautificationEntryPointOnLivePage() {
        beautificationRobot
            .launchLive()
            .verifyEventAction("view - beauty filter ongoing livestream")
    }

    @Test
    fun testAnalytic_clickRetryReapplyBeautyFilter() {

        coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns false

        beautificationRobot.launch()
            .performDelay(1000)
            .clickToasterCTA()
            .verifyEventAction("click - reapply beauty filter")
    }

    @Test
    fun testAnalytic_viewFailReapplyBeautyFilter() {

        coEvery { beautificationRobot.mockBroadcaster.setFaceFilter(any(), any()) } returns false

        beautificationRobot.launch()
            .performDelay(1000)
            .verifyEventAction("view - reapply beauty filter")
    }
}
