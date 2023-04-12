package com.tokopedia.play.broadcaster.setup.beautification

import androidx.test.espresso.matcher.RootMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.content.test.util.click
import com.tokopedia.content.test.util.clickItemRecyclerView
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.di.PlayBroadcastInjector
import com.tokopedia.play.broadcaster.domain.model.GetAddedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.helper.PlayBroadcastActivityLauncher
import com.tokopedia.play.broadcaster.helper.PlayBroadcastCassavaValidator
import com.tokopedia.play.broadcaster.setup.accountListResponse
import com.tokopedia.play.broadcaster.setup.buildBroadcastingConfigUiModel
import com.tokopedia.play.broadcaster.setup.buildConfigurationUiModel
import com.tokopedia.play.broadcaster.setup.channelResponse
import com.tokopedia.play.broadcaster.setup.di.DaggerPlayBroadcastTestComponent
import com.tokopedia.play.broadcaster.setup.di.PlayBroadcastRepositoryTestModule
import com.tokopedia.play.broadcaster.setup.di.PlayBroadcastTestModule
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

    private val playBroadcastTestModule = PlayBroadcastTestModule(
        activityContext = context,
        mockUserSession = mockUserSession,
        mockBroadcaster = mockBroadcaster,
        mockCoachMarkSharedPref = mockContentCoachMarkSharedPref,
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

        coEvery { mockRepo.getAccountList() } returns accountListResponse()
        coEvery { mockRepo.getBroadcastingConfig(any(), any()) } returns buildBroadcastingConfigUiModel()
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns buildConfigurationUiModel()
        coEvery { mockRepo.getProductTagSummarySection(any()) } returns emptyList()
        coEvery { mockRepo.downloadLicense(any()) } returns true
        coEvery { mockRepo.downloadModel(any()) } returns true
        coEvery { mockRepo.downloadCustomFace(any()) } returns true
        coEvery { mockRepo.downloadPresetAsset(any(), any()) } returns true
        coEvery { mockRepo.saveBeautificationConfig(any(), any(), any()) } returns true

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns channelResponse
        coEvery { mockGetAddedTagUseCase.executeOnBackground() } returns GetAddedChannelTagsResponse()

        coEvery { mockBroadcaster.setFaceFilter(any(), any()) } returns true
        coEvery { mockBroadcaster.setPreset(any(), any()) } returns true

        coEvery {
            mockContentCoachMarkSharedPref.hasBeenShown(
                ContentCoachMarkSharedPref.Key.PlayBroadcasterFaceFilter,
                any(),
            )
        } returns true
    }

    fun launch() = chainable {
        activityLauncher.launch()
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

    fun performDelay(delayInMillis: Long = 500) = chainable {
        delay(delayInMillis)
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
