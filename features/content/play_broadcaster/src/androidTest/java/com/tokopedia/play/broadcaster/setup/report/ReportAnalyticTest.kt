package com.tokopedia.play.broadcaster.setup.report

import android.content.Intent
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.content.test.util.click
import com.tokopedia.content.test.util.pressBack
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.PlayBroadcastInjector
import com.tokopedia.play.broadcaster.domain.model.GetAddedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.model.GetRecommendedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.GetSellerLeaderboardSlotResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.quiz.GetInteractiveSummaryLivestreamResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveStatisticsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetRecommendedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetInteractiveSummaryLivestreamUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetSellerLeaderboardUseCase
import com.tokopedia.play.broadcaster.fake.FakeBroadcastManager
import com.tokopedia.play.broadcaster.helper.PlayBroadcastCassavaValidator
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimer
import com.tokopedia.play.broadcaster.setup.accountListResponse
import com.tokopedia.play.broadcaster.setup.buildBroadcastingConfigUiModel
import com.tokopedia.play.broadcaster.setup.buildConfigurationUiModel
import com.tokopedia.play.broadcaster.setup.buildLiveReportSummary
import com.tokopedia.play.broadcaster.setup.buildProductReportSummary
import com.tokopedia.play.broadcaster.setup.channelPausedResponse
import com.tokopedia.play.broadcaster.setup.di.DaggerPlayBroadcastTestComponent
import com.tokopedia.play.broadcaster.setup.di.PlayBroadcastRepositoryTestModule
import com.tokopedia.play.broadcaster.setup.di.PlayBroadcastTestModule
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.wrapper.PlayBroadcastValueWrapper
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.compose.createAndroidIntentComposeRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.tokopedia.coachmark.R as coachmarkR
import com.tokopedia.dialog.R as dialogR

/**
 * Created by Jonathan Darwin on 22 March 2024
 */
@CassavaTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ReportAnalyticTest {

    @get:Rule
    val permissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.RECORD_AUDIO
    )

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    @get:Rule
    val composeActivityTestRule = createAndroidIntentComposeRule<PlayBroadcastActivity> { context ->
        Intent(context, PlayBroadcastActivity::class.java)
    }

    private val context = InstrumentationRegistry.getInstrumentation().context

    private val cassavaValidator = PlayBroadcastCassavaValidator.buildForReport(cassavaTestRule)

    private val mockContentCoachMarkSharedPref: ContentCoachMarkSharedPref = mockk(relaxed = true)
    private val mockHydraSharedPreferences: HydraSharedPreferences = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val fakeBroadcaster = FakeBroadcastManager()
    private val mockGetChannelUseCase: GetChannelUseCase = mockk(relaxed = true)
    private val mockGetAddedTagUseCase: GetAddedChannelTagsUseCase = mockk(relaxed = true)
    private val mockBroadcastTimer: PlayBroadcastTimer = mockk(relaxed = true)
    private val mockValueWrapper: PlayBroadcastValueWrapper = mockk(relaxed = true)
    private val mockGetLiveStatisticsUseCase: GetLiveStatisticsUseCase = mockk(relaxed = true)
    private val mockGetInteractiveSummaryLivestreamUseCase: GetInteractiveSummaryLivestreamUseCase = mockk(relaxed = true)
    private val mockGetSellerLeaderboardUseCase: GetSellerLeaderboardUseCase = mockk(relaxed = true)
    private val mockGetRecommendedChannelTagsUseCase: GetRecommendedChannelTagsUseCase = mockk(relaxed = true)

    private val playBroadcastTestModule = PlayBroadcastTestModule(
        activityContext = context,
        mockUserSession = mockUserSession,
        mockBroadcaster = fakeBroadcaster,
        mockCoachMarkSharedPref = mockContentCoachMarkSharedPref,
        mockPermissionSharedPreferences = mockk(relaxed = true),
        mockHydraSharedPreferences = mockHydraSharedPreferences,
        mockBroadcastTimer = mockBroadcastTimer,
        mockGetChannelUseCase = mockGetChannelUseCase,
        mockGetAddedTagUseCase = mockGetAddedTagUseCase,
        mockValueWrapper = mockValueWrapper,
        mockGetLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
        mockGetInteractiveSummaryLivestreamUseCase = mockGetInteractiveSummaryLivestreamUseCase,
        mockGetSellerLeaderboardUseCase = mockGetSellerLeaderboardUseCase,
        mockGetRecommendedChannelTagsUseCase = mockGetRecommendedChannelTagsUseCase,
        mockBroadcasterErrorLogger = mockk(relaxed = true),
    )

    private val playBroadcastRepositoryTestModule = PlayBroadcastRepositoryTestModule(
        mockRepo = mockRepo,
    )

    init {
        setUp()
        init()
    }

    private fun setUp() {
        /** Mock General Config */
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockBroadcastTimer.isPastPauseDuration } returns false
        coEvery { mockHydraSharedPreferences.isFirstStatisticIconShown(any()) } returns true

        /** Mock Broadcaster Configuration */
        coEvery { mockRepo.getAccountList() } returns accountListResponse()
        coEvery {
            mockRepo.getChannelConfiguration(any(), any())
        } returns buildConfigurationUiModel(
            channelStatus = ChannelStatus.Pause
        )
        coEvery { mockRepo.getBroadcastingConfig(any(), any()) } returns buildBroadcastingConfigUiModel()
        coEvery { mockRepo.getProductTagSummarySection(any()) } returns emptyList()

        coEvery { mockGetAddedTagUseCase.executeOnBackground() } returns GetAddedChannelTagsResponse()

        coEvery {
            mockGetChannelUseCase.executeOnBackground()
        } returns channelPausedResponse

        /** Mock Start Broadcaster */
        coEvery { mockRepo.getInteractiveConfig(any(), any()) } returns InteractiveConfigUiModel.empty()
        coEvery { mockRepo.getReportProductSummary(any()) } returns buildProductReportSummary()
        coEvery { mockRepo.getActivePinnedMessage(any()) } returns null

        /** Mock Live Summary Page */
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } returns buildLiveReportSummary()
        coEvery { mockGetInteractiveSummaryLivestreamUseCase.executeOnBackground() } returns GetInteractiveSummaryLivestreamResponse()
        coEvery { mockGetSellerLeaderboardUseCase.executeOnBackground() } returns GetSellerLeaderboardSlotResponse()
        coEvery { mockGetRecommendedChannelTagsUseCase.executeOnBackground() } returns GetRecommendedChannelTagsResponse()
    }

    private fun init() {
        PlayBroadcastInjector.set(
            DaggerPlayBroadcastTestComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .playBroadcastTestModule(playBroadcastTestModule)
                .playBroadcastRepositoryTestModule(playBroadcastRepositoryTestModule)
                .build()
        )
    }

    @Test
    fun testReport_liveRoom() {
        composeActivityTestRule.apply {

            /** Continue Livestream */
            clickDialogPrimaryCTA()
            performDelay()

            /** Report CoachMark */
            verify("view - estimasi pendapatan coachmark")
            closeCoachMark()

            /** Open Product Report Summary Bottom Sheet */
            click(R.id.ic_statistic)
            verify("click - estimasi pendapatan icon")
            performDelay()
            verify("view - estimasi pendapatan bottomsheet")

            /** Open Estimated Income Info Bottom Sheet */
            clickEstimatedIncomeInfoIcon()
            verify("click - estimasi pendapatan info icon bottomsheet")
            performDelay()
            verify("view - estimasi pendapatan explanation bottomsheet")

            /** Close Bottom Sheets */
            pressBack()
            performDelay()
            pressBack()

            /** Open Live Report Summary Bottom Sheet */
            clickLiveStatsView()
            verify("click - top area")
            performDelay()
            verify("view - information bottomsheet")

            /** Product Report Summary Negative Case */
            coEvery { mockRepo.getReportProductSummary(any()) } throws Exception()
            clickEstimatedIncomeCard()
            verify("click - estimasi pendapatan card bottomsheet")
            performDelay()
            verify("view - estimasi pendapatan error bottomsheet")

            /** Close Bottom Sheets */
            pressBack()
            performDelay()
            pressBack()

            /** End Livestream */
            click(R.id.ic_bro_end_stream)
            click(dialogR.id.dialog_btn_secondary)
            performDelay()

            clickEstimatedIncomeCard()
            verify("click - estimasi pendapatan icon card")
        }
    }

    private fun closeCoachMark() {
        click(coachmarkR.id.simple_ic_close) {
            inRoot(RootMatchers.isPlatformPopup())
        }
    }

    private fun clickDialogPrimaryCTA() {
        click(dialogR.id.dialog_btn_primary)
    }

    private fun performDelay(delayInMillis: Long = 1000) {
        delay(delayInMillis)
    }

    private fun verify(eventAction: String) {
        cassavaValidator.verify(eventAction)
    }

    private fun AndroidComposeTestRule<ActivityScenarioRule<PlayBroadcastActivity>, PlayBroadcastActivity>.clickEstimatedIncomeInfoIcon() {
        val text = context.getString(R.string.play_broadcaster_live_stats_estimated_income_label)
        onNodeWithTag("${text}_icon").performClick()
    }

    private fun AndroidComposeTestRule<ActivityScenarioRule<PlayBroadcastActivity>, PlayBroadcastActivity>.clickLiveStatsView() {
        onNodeWithTag("live_stats_view").performClick()
    }

    private fun AndroidComposeTestRule<ActivityScenarioRule<PlayBroadcastActivity>, PlayBroadcastActivity>.clickEstimatedIncomeCard() {
        val text = context.getString(R.string.play_broadcaster_live_stats_estimated_income_label)
        onNodeWithTag(text).performClick()
    }
}
