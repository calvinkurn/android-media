package com.tokopedia.play.broadcaster.setup.beautification

import androidx.test.espresso.matcher.RootMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.content.test.util.*
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.di.PlayBroadcastInjector
import com.tokopedia.play.broadcaster.domain.model.GetAddedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.helper.PlayBroadcastActivityLauncher
import com.tokopedia.play.broadcaster.helper.PlayBroadcastCassavaValidator
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimer
import com.tokopedia.play.broadcaster.setup.*
import com.tokopedia.play.broadcaster.setup.di.DaggerPlayBroadcastTestComponent
import com.tokopedia.play.broadcaster.setup.di.PlayBroadcastRepositoryTestModule
import com.tokopedia.play.broadcaster.setup.di.PlayBroadcastTestModule
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationAssetStatus
import com.tokopedia.play.broadcaster.util.wrapper.PlayBroadcastValueWrapper
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule

/**
 * Created By : Jonathan Darwin on April 12, 2023
 */
class BeautificationRobot {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val context = InstrumentationRegistry.getInstrumentation().context

    private val activityLauncher = PlayBroadcastActivityLauncher(context)

    private val cassavaValidator = PlayBroadcastCassavaValidator.buildForBeautification(cassavaTestRule)

    val mockContentCoachMarkSharedPref: ContentCoachMarkSharedPref = mockk(relaxed = true)
    val mockDataStore: PlayBroadcastDataStore = mockk(relaxed = true)
    val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    val mockBroadcaster: Broadcaster = mockk(relaxed = true)
    val mockGetChannelUseCase: GetChannelUseCase = mockk(relaxed = true)
    val mockGetAddedTagUseCase: GetAddedChannelTagsUseCase = mockk(relaxed = true)
    val mockBroadcastTimer: PlayBroadcastTimer = mockk(relaxed = true)
    val mockValueWrapper: PlayBroadcastValueWrapper = mockk(relaxed = true)

    private val playBroadcastTestModule = PlayBroadcastTestModule(
        activityContext = context,
        mockUserSession = mockUserSession,
        mockBroadcaster = mockBroadcaster,
        mockCoachMarkSharedPref = mockContentCoachMarkSharedPref,
        mockBroadcastTimer = mockBroadcastTimer,
        mockGetChannelUseCase = mockGetChannelUseCase,
        mockGetAddedTagUseCase = mockGetAddedTagUseCase,
        mockValueWrapper = mockValueWrapper
    )

    private val playBroadcastRepositoryTestModule = PlayBroadcastRepositoryTestModule(
        mockRepo = mockRepo
    )

    fun init() {
        PlayBroadcastInjector.set(
            DaggerPlayBroadcastTestComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .playBroadcastTestModule(playBroadcastTestModule)
                .playBroadcastRepositoryTestModule(playBroadcastRepositoryTestModule)
                .build()
        )
    }

    fun setUp() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockBroadcastTimer.isPastPauseDuration } returns false

        coEvery { mockRepo.getAccountList() } returns accountListResponse()
        coEvery { mockRepo.getBroadcastingConfig(any(), any()) } returns buildBroadcastingConfigUiModel()
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns buildConfigurationUiModel()
        coEvery { mockRepo.getProductTagSummarySection(any()) } returns emptyList()
        coEvery { mockRepo.downloadLicense(any()) } returns true
        coEvery { mockRepo.downloadModel(any()) } returns true
        coEvery { mockRepo.downloadCustomFace(any()) } returns true
        coEvery { mockRepo.downloadPresetAsset(any(), any()) } returns true
        coEvery { mockRepo.saveBeautificationConfig(any(), any(), any()) } returns true

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns channelWithTitleResponse
        coEvery { mockGetAddedTagUseCase.executeOnBackground() } returns GetAddedChannelTagsResponse()

        coEvery { mockBroadcaster.setFaceFilter(any(), any()) } returns true
        coEvery { mockBroadcaster.setPreset(any(), any()) } returns true

        coEvery {
            mockContentCoachMarkSharedPref.hasBeenShown(
                ContentCoachMarkSharedPref.Key.PlayBroadcasterFaceFilter,
                any()
            )
        } returns true
    }

    fun launch() = chainable {
        activityLauncher.launch()
        delay()
    }

    fun launchLive() = chainable {
        coEvery {
            mockRepo.getChannelConfiguration(any(), any())
        } returns buildConfigurationUiModel(
            channelStatus = ChannelStatus.Pause
        )

        coEvery {
            mockGetChannelUseCase.executeOnBackground()
        } returns channelPausedResponse

        launch()
            .performDelay(1000)
            .clickDialogPrimaryCTA()
    }

    fun launchLiveWithNoDownloadedPreset() = chainable {
        coEvery {
            mockRepo.getChannelConfiguration(any(), any())
        } returns buildConfigurationUiModel(
            channelStatus = ChannelStatus.Pause,
            beautificationConfig = buildBeautificationConfig(
                assetStatus = BeautificationAssetStatus.NotDownloaded
            )
        )

        coEvery {
            mockGetChannelUseCase.executeOnBackground()
        } returns channelPausedResponse

        launch()
            .clickDialogPrimaryCTA()
    }

    fun clickBeautificationMenu() = chainable {
        clickItemRecyclerView(R.id.rv_menu, 4)
        delay(1000)
    }

    fun clickCloseBeautificationCoachMark() = chainable {
        click(com.tokopedia.coachmark.R.id.simple_ic_close) {
            inRoot(RootMatchers.isPlatformPopup())
        }
    }

    fun clickBeautificationCustomFaceTab() = chainable {
        click(context.getString(R.string.play_broadcaster_face_tab))
    }

    fun clickBeautificationPresetTab() = chainable {
        click(context.getString(R.string.play_broadcaster_makeup_tab))
    }

    fun clickCustomFace(position: Int) = chainable {
        clickItemRecyclerView(R.id.recycler_view, position)
    }

    fun clickPreset(position: Int) = chainable {
        clickItemRecyclerView(R.id.recycler_view, position)
    }

    fun clickResetFilter() = chainable {
        click(R.id.tv_bottom_sheet_action)
        delay(1000)
    }

    fun clickDialogPrimaryCTA() = chainable {
        click(com.tokopedia.dialog.R.id.dialog_btn_primary)
    }

    fun clickDialogSecondaryCTA() = chainable {
        click(com.tokopedia.dialog.R.id.dialog_btn_secondary)
    }

    fun slideBeautificationSlider(distance: Float) = chainable {
        horizontalSlide(com.tokopedia.unifycomponents.R.id.range_slider_max_button, distance)
    }

    fun clickToasterCTA() = chainable {
        click(com.tokopedia.unifycomponents.R.id.snackbar_btn)
    }

    fun performDelay(delayInMillis: Long = 500) = chainable {
        delay(delayInMillis)
    }

    fun clickBeautificationMenuOnLivePage() = chainable {
        click(R.id.ic_face_filter)
        delay(1000)
    }

    fun mock(action: () -> Unit) = chainable {
        action()
    }

    fun verifyEventAction(eventAction: String) = chainable {
        cassavaValidator.verify(eventAction)
    }

    fun verifyOpenScreen(screenName: String) = chainable {
        cassavaValidator.verifyOpenScreen(screenName)
    }

    private fun chainable(action: () -> Unit): BeautificationRobot {
        action()
        return this
    }
}
