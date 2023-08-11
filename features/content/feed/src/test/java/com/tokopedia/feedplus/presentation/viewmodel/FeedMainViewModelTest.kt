package com.tokopedia.feedplus.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.content.common.producttag.view.uimodel.NetworkResult
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.createpost.common.domain.usecase.cache.DeleteMediaPostCacheUseCase
import com.tokopedia.feedplus.data.FeedTabsModelBuilder
import com.tokopedia.feedplus.domain.repository.FeedRepository
import com.tokopedia.feedplus.presentation.model.CreateContentType
import com.tokopedia.feedplus.presentation.model.CreatorType
import com.tokopedia.feedplus.presentation.model.FeedMainEvent
import com.tokopedia.feedplus.presentation.onboarding.OnboardingPreferences
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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

    private val repository: FeedRepository = mockk()
    private val deletePostCacheUseCase: DeleteMediaPostCacheUseCase = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = coroutineTestRule.dispatchers

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scope = TestScope(testDispatcher.io)
    private val onBoardingPreferences: OnboardingPreferences = mockk()
    private val userSession: UserSessionInterface = mockk()
    private val uiEventManager: UiEventManager<FeedMainEvent> = mockk()

    private lateinit var viewModel: FeedMainViewModel

    private val tabsModelBuilder = FeedTabsModelBuilder()

    @Before
    fun setUp() {
        coEvery {
            userSession.isLoggedIn
        } returns true
        coEvery {
            onBoardingPreferences.hasShownSwipeOnboarding()
        } returns false

        viewModel = FeedMainViewModel(
            repository,
            deletePostCacheUseCase,
            onBoardingPreferences,
            userSession,
            uiEventManager
        )
    }

    @Test
    fun onGetDisplayName_shoudReturnUserName() {
        // given
        val name = "Muhammad Furqan"
        coEvery { userSession.name } returns name
        coEvery { userSession.isLoggedIn } returns true
        coEvery { onBoardingPreferences.setHasShownSwipeOnboarding() } coAnswers {}
        coEvery { uiEventManager.emitEvent(any()) } coAnswers {}

        // when
        val mViewModel = FeedMainViewModel(
            repository,
            deletePostCacheUseCase,
            onBoardingPreferences,
            userSession,
            uiEventManager
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
    fun onFetchFeedTabs_whenSuccess_shouldChangeFeedTabsValue() {
        // given
        val expectedValue = tabsModelBuilder.buildUiModel()

        coEvery { repository.getTabs() } returns expectedValue

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

        coEvery { repository.getTabs() } throws expectedValue

        // when
        viewModel.fetchFeedTabs()

        // then
        val feedTabsData = viewModel.feedTabs.value
        assert(feedTabsData is NetworkResult.Error)

        val failedTabsData = feedTabsData as NetworkResult.Error

        assert(failedTabsData.error is MessageErrorException)
        assert(failedTabsData.error.message == expectedValue.message)
    }

    @Test
    fun onChangeCurrentTabByType_whenTabsSuccess_shouldChangeCurrentTabsIndex() {
        // given
        val mockValue = tabsModelBuilder.buildUiModel(
            data = tabsModelBuilder.buildDefaultTabsModel()
        )
        coEvery { repository.getTabs() } returns mockValue
        viewModel.fetchFeedTabs()

        // when 1
        viewModel.changeCurrentTabByType("foryou")
        // then 1
        assert(viewModel.currentTabIndex.value == 0)

        // when 2
        viewModel.changeCurrentTabByType("following")
        // then 2
        assert(viewModel.currentTabIndex.value == 1)
    }

    @Test
    fun onChangeCurrentTabByIndex_whenTabsSuccess_shouldChangeCurrentTabsIndex() {
        // given

        // when 1
        viewModel.changeCurrentTabByIndex(0)
        // then 1
        assert(viewModel.currentTabIndex.value == 0)

        // when 2
        viewModel.changeCurrentTabByIndex(1)
        // then 2
        assert(viewModel.currentTabIndex.value == 1)
    }

    @Test
    fun onChangeCurrentTabByType_whenTabsFail_shouldNotChangeCurrentTabsIndex() {
        // given
        val currentTabIndex = viewModel.currentTabIndex.value

        coEvery { repository.getTabs() } throws Exception()

        // when
        viewModel.changeCurrentTabByType("foryou")
        // then
        assert(viewModel.currentTabIndex.value == currentTabIndex)
    }

    @Test
    fun onScrollCurrentTabToTop_whenTabIsNotSuccess_shouldNotEmitEvent() {
        // given
        coEvery { repository.getTabs() } throws Exception()
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
        coEvery { repository.getTabs() } returns mockValue
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
        coEvery { repository.getTabs() } returns mockValue
        viewModel.fetchFeedTabs()

        coEvery { uiEventManager.emitEvent(any()) } coAnswers {}

        // when
        viewModel.scrollCurrentTabToTop()

        // then
        coVerify(exactly = 1) { uiEventManager.emitEvent(FeedMainEvent.ScrollToTop("following")) }
        coVerify(exactly = 1) { uiEventManager.emitEvent(FeedMainEvent.ScrollToTop("foryou")) }
    }

    @Test
    fun onGetCurrentType_whenSuccess_shouldReturnCorrectType() {
        var currentTabType = viewModel.getCurrentTabType()
        assert(currentTabType.isEmpty())
        var currentTabByIndex = viewModel.getTabType(0)
        assert(currentTabByIndex.isEmpty())

        // prepare feed tabs data
        val mockValue = tabsModelBuilder.buildUiModel(
            data = tabsModelBuilder.buildDefaultTabsModel()
        )
        coEvery { repository.getTabs() } returns mockValue
        viewModel.fetchFeedTabs()

        // get current tab type
        viewModel.changeCurrentTabByIndex(0)
        currentTabType = viewModel.getCurrentTabType()
        assert(currentTabType == "foryou")

        // should be empty if index more than size
        currentTabByIndex = viewModel.getTabType(10)
        assert(currentTabByIndex.isEmpty())

        // change and assert new tab type
        viewModel.changeCurrentTabByType("following")
        currentTabType = viewModel.getCurrentTabType()
        assert(currentTabType == "following")

        // get tab by index
        currentTabByIndex = viewModel.getTabType(1)
        assert(currentTabByIndex == "following")
    }

    @Test
    fun onGetCurrentType_whenFailed_shouldReturnEmpty() {
        // given
        coEvery { repository.getTabs() } throws Exception()
        viewModel.fetchFeedTabs()

        // when 2
        val currentTabType = viewModel.getCurrentTabType()
        assert(currentTabType == "")
    }

    @Test
    fun onFetchMetaData_whenSuccess_shouldChangeValueToSuccess() {
        // given
        val expectedValue = tabsModelBuilder.buildUiModel().meta
        coEvery { repository.getMeta() } returns expectedValue

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
        coEvery { repository.getMeta() } returns expectedValue

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
    fun onDeletePostCache() {
        viewModel.deletePostCache()

        coVerify(exactly = 1) { deletePostCacheUseCase(Unit) }
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
        val isLoaded = true

        viewModel.onPostDataLoaded(isLoaded)
        coVerify(exactly = 0) { uiEventManager.emitEvent(FeedMainEvent.ShowSwipeOnboarding) }

        viewModel.setReadyToShowOnboarding()
        coVerify(exactly = 1) { uiEventManager.emitEvent(FeedMainEvent.ShowSwipeOnboarding) }
    }
}
