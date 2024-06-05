package com.tokopedia.feedplus.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.creation.common.upload.domain.usecase.post.DeleteMediaPostCacheUseCase
import com.tokopedia.feedplus.data.FeedTabsModelBuilder
import com.tokopedia.feedplus.domain.FeedRepository
import com.tokopedia.feedplus.presentation.model.ActiveTabSource
import com.tokopedia.feedplus.presentation.model.CreateContentType
import com.tokopedia.feedplus.presentation.model.CreatorType
import com.tokopedia.feedplus.presentation.model.FeedMainEvent
import com.tokopedia.feedplus.presentation.model.FeedTooltipEvent
import com.tokopedia.feedplus.presentation.onboarding.OnBoardingPreferences
import com.tokopedia.feedplus.presentation.tooltip.FeedSearchTooltipCategory
import com.tokopedia.feedplus.presentation.tooltip.FeedTooltipManager
import com.tokopedia.feedplus.presentation.util.FeedContentManager
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.fail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Muhammad Furqan on 12/05/23
 */
class FeedMainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private val activeTabSource = ActiveTabSource(null, 0)

    private val repository: FeedRepository = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = coroutineTestRule.dispatchers

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scope = TestScope(testDispatcher.io)
    private val onBoardingPreferences: OnBoardingPreferences = mockk()
    private val userSession: UserSessionInterface = mockk()
    private val uiEventManager: UiEventManager<FeedMainEvent> = mockk()
    private val tooltipManager: FeedTooltipManager = mockk()

    private lateinit var viewModel: FeedMainViewModel

    private val tabsModelBuilder = FeedTabsModelBuilder()

    @Before
    fun setUp() {
        coEvery {
            userSession.isLoggedIn
        } returns true
        coEvery {
            onBoardingPreferences.hasShownSwipeOnBoarding()
        } returns false

        viewModel = FeedMainViewModel(
            activeTabSource,
            repository,
            onBoardingPreferences,
            userSession,
            uiEventManager,
            tooltipManager,
        )
    }

    @Test
    fun onGetDisplayName_shoudReturnUserName() {
        // given
        val name = "Fulan"
        coEvery { userSession.name } returns name
        coEvery { userSession.isLoggedIn } returns true
        coEvery { onBoardingPreferences.setHasShownSwipeOnBoarding() } coAnswers {}
        coEvery { uiEventManager.emitEvent(any()) } coAnswers {}

        // when
        val mViewModel = FeedMainViewModel(
            activeTabSource,
            repository,
            onBoardingPreferences,
            userSession,
            uiEventManager,
            tooltipManager,
        )
        val displayName = mViewModel.displayName

        // then
        assert(displayName == name)
    }

    @Test
    fun onResumePage_shouldChangeIsPageResumedToTrue() {
        // given

        // when
        viewModel.resumePage()

        // then
        assert(viewModel.isPageResumed.value == true)
    }

    @Test
    fun onPausePage_shouldChangeIsPageResumedToFalse() {
        // given

        // when
        viewModel.pausePage()

        // then
        assert(viewModel.isPageResumed.value == false)
    }

    @Test
    fun onMuteSound_shouldChangeIsMutedToTrue() {
        // when
        viewModel.muteSound()

        // then
        assert(FeedContentManager.muteState.value == true)
    }

    @Test
    fun onUnmuteSound_shouldChangeIsMutedToFalse() {
        // when
        viewModel.unmuteSound()

        // then
        assert(FeedContentManager.muteState.value == false)
    }

    @Test
    fun onFetchFeedTabs_whenSuccess_shouldChangeFeedTabsValue() {
        // given
        val expectedValue = tabsModelBuilder.buildUiModel()

        coEvery { repository.getTabs(activeTabSource) } returns expectedValue

        // when
        viewModel.fetchFeedTabs()

        // then
        val feedTabs = viewModel.feedTabs.value
        assert(feedTabs is NetworkResult.Success)
    }

    @Test
    fun onFetchFeedTabs_whenFail_shouldChangeFeedTabsValue() {
        // given
        val expectedValue = MessageErrorException("Failed to fetch")

        coEvery { repository.getTabs(activeTabSource) } throws expectedValue

        // when
        viewModel.fetchFeedTabs()

        // then
        val feedTabsData = viewModel.feedTabs.value
        assert(feedTabsData is NetworkResult.Fail)

        val failedTabsData = feedTabsData as NetworkResult.Fail

        assert(failedTabsData.error is MessageErrorException)
        assert(failedTabsData.error.message == expectedValue.message)
    }

    @Test
    fun onScrollCurrentTabToTop_whenTabIsNotSuccess_shouldNotEmitEvent() {
        // given
        coEvery { repository.getTabs(activeTabSource) } throws Exception()
        viewModel.fetchFeedTabs()

        // when
        viewModel.scrollCurrentTabToTop()

        // then
        coVerify(exactly = 0) { uiEventManager.emitEvent(any()) }
    }

    @Test
    fun onScrollCurrentTabToTop_whenTabIsSuccessWithOneActiveTab_shouldEmitEventOnce() {
        // given
        val mockValue = tabsModelBuilder.buildUiModel(
            data = tabsModelBuilder.buildCustomTabsModel()
        )
        coEvery { repository.getTabs(activeTabSource) } returns mockValue
        viewModel.fetchFeedTabs()

        // when
        viewModel.scrollCurrentTabToTop()

        // then
        coVerify(exactly = 1) { uiEventManager.emitEvent(FeedMainEvent.ScrollToTop("following")) }
        coVerify(exactly = 0) { uiEventManager.emitEvent(FeedMainEvent.ScrollToTop("foryou")) }
    }

    @Test
    fun onScrollCurrentTabToTop_whenTabIsSuccessWithTwoActiveTabs_shouldEmitEventTwice() {
        // given
        val mockValue = tabsModelBuilder.buildUiModel(
            data = tabsModelBuilder.buildDefaultTabsModel()
        )
        coEvery { repository.getTabs(activeTabSource) } returns mockValue
        viewModel.fetchFeedTabs()

        coEvery { uiEventManager.emitEvent(any()) } coAnswers {}

        // when
        viewModel.scrollCurrentTabToTop()

        // then
        coVerify(exactly = 1) { uiEventManager.emitEvent(FeedMainEvent.ScrollToTop("following")) }
        coVerify(exactly = 1) { uiEventManager.emitEvent(FeedMainEvent.ScrollToTop("foryou")) }
    }

    @Test
    fun onFetchMetaData_whenSuccess_shouldChangeValueToSuccess() {
        // given
        val expectedValue = tabsModelBuilder.buildUiModel().meta
        coEvery { repository.getMeta(activeTabSource) } returns expectedValue

        // when
        viewModel.fetchFeedMetaData()

        // then
        assert(viewModel.metaData.value == expectedValue)
        assert(!viewModel.isShortEntryPointShowed) // no eligible creation entry points
    }

    @Test
    fun onFetchMetaData_whenSuccessWithShortsCreation_isShortEntryPointShowedShouldBeTrue() {
        // given
        val expectedValue = tabsModelBuilder.buildUiModel(
            tabsModelBuilder.buildDefaultMetaModel(
                eligibleCreationEntryPoints = listOf(
                    tabsModelBuilder.buildCreationEntryPointUiModel(
                        authorType = CreatorType.USER,
                        creationType = CreateContentType.ShortVideo
                    )
                )
            )
        ).meta
        coEvery { repository.getMeta(activeTabSource) } returns expectedValue

        // when
        viewModel.fetchFeedMetaData()

        // then
        assert(viewModel.metaData.value == expectedValue)
        assert(viewModel.isShortEntryPointShowed)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onConsumeEvent_shouldClearEvent() {
        // given
        val event = FeedMainEvent.HasJustLoggedIn

        // when
        viewModel.consumeEvent(event)

        // then
        coVerify(exactly = 1) { uiEventManager.clearEvent(event.id) }
        scope.launch {
            viewModel.uiEvent.collect {
                assert(it == null || it.id != event.id)
            }
        }
    }

    @Test
    fun onUpdateUserInfo_whenUserNotLoggedIn_shouldEmitEvent() {
        // given
        coEvery { userSession.isLoggedIn } returns false

        // when
        viewModel.updateUserInfo()

        // then
        coVerify(exactly = 0) { uiEventManager.emitEvent(FeedMainEvent.HasJustLoggedIn) }
    }

    @Test
    fun onUpdateUserInfo_whenUserLoggedIn_shouldEmitEvent() {
        // given
        coEvery { uiEventManager.emitEvent(any()) } coAnswers {}
        coEvery { userSession.isLoggedIn } returns false
        viewModel.updateUserInfo()
        coEvery { userSession.isLoggedIn } returns true

        // when
        viewModel.updateUserInfo()

        // then
        coVerify(exactly = 1) { uiEventManager.emitEvent(FeedMainEvent.HasJustLoggedIn) }
    }

    @Test
    fun onReadyToShowOnBoarding() {
        viewModel.setDataEligibleForOnboarding(true)
        coVerify(exactly = 0) { uiEventManager.emitEvent(FeedMainEvent.ShowSwipeOnboarding) }

        viewModel.setReadyToShowOnboarding()
        coVerify(exactly = 1) { uiEventManager.emitEvent(FeedMainEvent.ShowSwipeOnboarding) }
    }

    @Test
    fun onSetForYouTabByPosition_thenItShouldEmitSelectTabEvent() {
        // given
        val mockValue = tabsModelBuilder.buildUiModel(
            data = tabsModelBuilder.buildDefaultTabsModel()
        )
        coEvery { repository.getTabs(activeTabSource) } returns mockValue
        viewModel.fetchFeedTabs()

        coEvery { uiEventManager.emitEvent(any()) } coAnswers {}

        viewModel.setActiveTab(0)

        val feedTabs = viewModel.feedTabs.value
        if (feedTabs is NetworkResult.Success) {
            assert(feedTabs.data.data[0].isSelected)
            assert(!feedTabs.data.data[1].isSelected)

            assert(viewModel.selectedTab?.type == feedTabs.data.data[0].type)
        } else {
            fail("Feed tabs should be NetworkResult.Success")
        }
    }

    @Test
    fun onSetFollowingTabByPosition_thenItShouldEmitSelectTabEvent() {
        // given
        val mockValue = tabsModelBuilder.buildUiModel(
            data = tabsModelBuilder.buildDefaultTabsModel()
        )
        coEvery { repository.getTabs(activeTabSource) } returns mockValue
        viewModel.fetchFeedTabs()

        coEvery { uiEventManager.emitEvent(any()) } coAnswers {}

        viewModel.setActiveTab(1)

        val feedTabs = viewModel.feedTabs.value
        if (feedTabs is NetworkResult.Success) {
            assert(!feedTabs.data.data[0].isSelected)
            assert(feedTabs.data.data[1].isSelected)
            assert(viewModel.selectedTab?.type == feedTabs.data.data[1].type)
        } else {
            fail("Feed tabs should be NetworkResult.Success")
        }
    }

    @Test
    fun onSetUnknownTabByPosition_thenItShouldNotEmitSelectTabEvent() {
        // given
        val mockValue = tabsModelBuilder.buildUiModel(
            data = tabsModelBuilder.buildDefaultTabsModel()
        )
        coEvery { repository.getTabs(activeTabSource) } returns mockValue
        viewModel.fetchFeedTabs()

        coEvery { uiEventManager.emitEvent(any()) } coAnswers {}

        viewModel.setActiveTab(2)

        val feedTabs = viewModel.feedTabs.value
        if (feedTabs is NetworkResult.Success) {
            assert(!feedTabs.data.data[0].isSelected)
            assert(!feedTabs.data.data[1].isSelected)
            assert(viewModel.selectedTab == null)
        } else {
            fail("Feed tabs should be NetworkResult.Success")
        }
    }

    @Test
    fun onSetForYouTabByType_thenItShouldEmitSelectTabEvent() {
        // given
        val mockValue = tabsModelBuilder.buildUiModel(
            data = tabsModelBuilder.buildDefaultTabsModel()
        )
        coEvery { repository.getTabs(activeTabSource) } returns mockValue
        viewModel.fetchFeedTabs()

        coEvery { uiEventManager.emitEvent(any()) } coAnswers {}

        val expectedValue = mockValue.tab.data[0]

        viewModel.setActiveTab(expectedValue.type)

        val feedTabs = viewModel.feedTabs.value
        if (feedTabs is NetworkResult.Success) {
            assert(feedTabs.data.data[0].isSelected)
            assert(!feedTabs.data.data[1].isSelected)

            assert(viewModel.selectedTab?.type == feedTabs.data.data[0].type)
        } else {
            fail("Feed tabs should be NetworkResult.Success")
        }
    }

    @Test
    fun onSetFollowingTabByType_thenItShouldEmitSelectTabEvent() {
        // given
        val mockValue = tabsModelBuilder.buildUiModel(
            data = tabsModelBuilder.buildDefaultTabsModel()
        )
        coEvery { repository.getTabs(activeTabSource) } returns mockValue
        viewModel.fetchFeedTabs()

        coEvery { uiEventManager.emitEvent(any()) } coAnswers {}

        val expectedValue = mockValue.tab.data[1]

        viewModel.setActiveTab(expectedValue.type)

        val feedTabs = viewModel.feedTabs.value
        if (feedTabs is NetworkResult.Success) {
            assert(!feedTabs.data.data[0].isSelected)
            assert(feedTabs.data.data[1].isSelected)

            assert(viewModel.selectedTab?.type == feedTabs.data.data[1].type)
        } else {
            fail("Feed tabs should be NetworkResult.Success")
        }
    }

    @Test
    fun onSetUnknownTabByType_thenItShouldEmitSelectTabEvent() {
        // given
        val mockValue = tabsModelBuilder.buildUiModel(
            data = tabsModelBuilder.buildDefaultTabsModel()
        )
        coEvery { repository.getTabs(activeTabSource) } returns mockValue
        viewModel.fetchFeedTabs()

        coEvery { uiEventManager.emitEvent(any()) } coAnswers {}

        viewModel.setActiveTab("unknown")
        val feedTabs = viewModel.feedTabs.value
        if (feedTabs is NetworkResult.Success) {
            assert(!feedTabs.data.data[0].isSelected)
            assert(!feedTabs.data.data[1].isSelected)
            assert(viewModel.selectedTab == null)
        } else {
            fail("Feed tabs should be NetworkResult.Success")
        }
    }

    @Test
    fun onProvideFactory() {
        // given
        val factory: FeedMainViewModel.Factory = mockk()
        val mActiveTabSource = ActiveTabSource("foryou", 0)

        coEvery { factory.create(mActiveTabSource) } returns FeedMainViewModel(
            mActiveTabSource,
            repository,
            onBoardingPreferences,
            userSession,
            uiEventManager,
            tooltipManager
        )

        val mViewModel = FeedMainViewModel.provideFactory(factory, mActiveTabSource)
            .create(FeedMainViewModel::class.java)

        assert(mViewModel.activeTabSource.tabName == mActiveTabSource.tabName)
        assert(mViewModel.activeTabSource.index == mActiveTabSource.index)
    }

    /** Tooltip */
    @Test
    fun consumeEventTooltip() {
        val event = FeedTooltipEvent.ShowTooltip(FeedSearchTooltipCategory.Promo)

        coEvery { tooltipManager.clearTooltipEvent(event.id) } returns Unit

        viewModel.consumeEvent(event)

        coVerify(exactly = 1) { tooltipManager.clearTooltipEvent(event.id) }
    }

    @Test
    fun setHasShownTooltip() {
        coEvery { tooltipManager.setHasShownTooltip() } returns Unit

        viewModel.setHasShownTooltip()

        coVerify(exactly = 1) { tooltipManager.setHasShownTooltip() }
    }
}
