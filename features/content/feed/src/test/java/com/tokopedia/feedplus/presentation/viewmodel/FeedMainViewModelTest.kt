package com.tokopedia.feedplus.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.content.common.model.FeedComplaintSubmitReportResponse
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.createpost.common.domain.usecase.cache.DeleteMediaPostCacheUseCase
import com.tokopedia.feedplus.data.AuthorItem
import com.tokopedia.feedplus.data.Authors
import com.tokopedia.feedplus.data.Creation
import com.tokopedia.feedplus.data.FeedXHeader
import com.tokopedia.feedplus.data.FeedXHeaderData
import com.tokopedia.feedplus.data.FeedXHeaderResponse
import com.tokopedia.feedplus.data.Items
import com.tokopedia.feedplus.data.Live
import com.tokopedia.feedplus.data.MetaData
import com.tokopedia.feedplus.data.Tab
import com.tokopedia.feedplus.data.UserProfile
import com.tokopedia.feedplus.domain.usecase.FeedXHeaderUseCase
import com.tokopedia.feedplus.presentation.model.CreateContentType
import com.tokopedia.feedplus.presentation.model.FeedMainEvent
import com.tokopedia.feedplus.presentation.onboarding.OnboardingPreferences
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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

    private val feedXHeaderUseCase: FeedXHeaderUseCase = mockk()
    private val submitReportUseCase: FeedComplaintSubmitReportUseCase = mockk()
    private val deletePostCacheUseCase: DeleteMediaPostCacheUseCase = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = coroutineTestRule.dispatchers

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scope = TestScope(testDispatcher.io)
    private val onBoardingPreferences: OnboardingPreferences = mockk()
    private val userSession: UserSessionInterface = mockk()
    private val uiEventManager: UiEventManager<FeedMainEvent> = mockk()

    private lateinit var viewModel: FeedMainViewModel

    @Before
    fun setUp() {
        coEvery {
            userSession.isLoggedIn
        } returns true
        coEvery {
            onBoardingPreferences.hasShownSwipeOnboarding()
        } returns false

        viewModel = FeedMainViewModel(
            feedXHeaderUseCase,
            submitReportUseCase,
            deletePostCacheUseCase,
            testDispatcher,
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
            feedXHeaderUseCase,
            submitReportUseCase,
            deletePostCacheUseCase,
            testDispatcher,
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
        val dummyResponse = getDummyFeedXHeaderData()

        coEvery { feedXHeaderUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { feedXHeaderUseCase.executeOnBackground() } returns dummyResponse

        // when
        viewModel.fetchFeedTabs()

        // then
        val feedTabs = viewModel.feedTabs.value
        assert(feedTabs is Success)

        val successFeedTabsData = (feedTabs as Success).data
        assert(successFeedTabsData.size == dummyResponse.feedXHeaderData.data.tab.items.size)

        dummyResponse.feedXHeaderData.data.tab.items.forEachIndexed { index, items ->
            assert(successFeedTabsData[index].isActive == items.isActive)
            assert(successFeedTabsData[index].title == items.title)
            assert(successFeedTabsData[index].key == items.key)
            assert(successFeedTabsData[index].type == items.type)
            assert(successFeedTabsData[index].position == items.position)
        }
    }

    @Test
    fun onFetchFeedTabs_whenFail_shouldChangeFeedTabsValue() {
        // given
        coEvery { feedXHeaderUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { feedXHeaderUseCase.executeOnBackground() } throws MessageErrorException("Failed to fetch")

        // when
        viewModel.fetchFeedTabs()

        // then
        val feedTabsData = viewModel.feedTabs.value
        assert(feedTabsData is Fail)

        val failedTabsData = feedTabsData as Fail
        assert(failedTabsData.throwable is MessageErrorException)
        assert(failedTabsData.throwable.message == "Failed to fetch")
    }

    @Test
    fun onChangeCurrentTabByType_whenTabsSuccess_shouldChangeCurrentTabsIndex() {
        // given
        val dummyResponse = getDummyFeedXHeaderData()
        coEvery { feedXHeaderUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { feedXHeaderUseCase.executeOnBackground() } returns dummyResponse
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

        coEvery { feedXHeaderUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { feedXHeaderUseCase.executeOnBackground() } throws MessageErrorException("")
        viewModel.fetchFeedTabs()

        // when
        viewModel.changeCurrentTabByType("foryou")
        // then
        assert(viewModel.currentTabIndex.value == currentTabIndex)
    }

    @Test
    fun onScrollCurrentTabToTop_whenTabIsNotSuccess_shouldNotEmitEvent() {
        // given
        coEvery { feedXHeaderUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { feedXHeaderUseCase.executeOnBackground() } throws MessageErrorException("")
        viewModel.fetchFeedTabs()

        // when
        viewModel.scrollCurrentTabToTop()

        // then
        coVerify(exactly = 0) { uiEventManager.emitEvent(any()) }
    }

    @Test
    fun onScrollCurrentTabToTop_whenTabIsSuccessWithOneActiveTab_shouldEmitEventOnce() {
        // given
        val dummyResponse = FeedXHeaderResponse(
            feedXHeaderData = FeedXHeader(
                data = FeedXHeaderData(
                    tab = Tab(
                        isActive = true,
                        items = listOf(
                            Items(
                                isActive = false,
                                position = 1,
                                type = "foryou",
                                title = "Untuk Kamu",
                                key = "foryou"
                            ),
                            Items(
                                isActive = true,
                                position = 2,
                                type = "following",
                                title = "Following",
                                key = "following"
                            )
                        ),
                        meta = MetaData(
                            selectedIndex = 0
                        )
                    )
                )
            )
        )
        coEvery { feedXHeaderUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { feedXHeaderUseCase.executeOnBackground() } returns dummyResponse
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
        val dummyResponse = getDummyFeedXHeaderData()
        coEvery { feedXHeaderUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { feedXHeaderUseCase.executeOnBackground() } returns dummyResponse
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
        val dummyResponse = getDummyFeedXHeaderData()
        coEvery { feedXHeaderUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { feedXHeaderUseCase.executeOnBackground() } returns dummyResponse
        viewModel.fetchFeedTabs()

        // get current tab type
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
        coEvery { feedXHeaderUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { feedXHeaderUseCase.executeOnBackground() } throws MessageErrorException("")
        viewModel.fetchFeedTabs()

        // when 2
        val currentTabType = viewModel.getCurrentTabType()
        assert(currentTabType == "")
    }

    @Test
    fun onFetchMetaData_whenFailed_shouldChangeValueToFail() {
        // given
        coEvery { feedXHeaderUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { feedXHeaderUseCase.executeOnBackground() } throws MessageErrorException("Failed")

        // when
        viewModel.fetchFeedMetaData()

        assert(viewModel.metaData.value is Fail)
        val errorMetaData = viewModel.metaData.value as Fail
        assert(errorMetaData.throwable is MessageErrorException)
        assert(errorMetaData.throwable.message == "Failed")

        assert(viewModel.feedCreateContentBottomSheetData.value is Fail)
        val errorBottomSheetData = viewModel.feedCreateContentBottomSheetData.value as Fail
        assert(errorBottomSheetData.throwable is MessageErrorException)
        assert(errorBottomSheetData.throwable.message == "Failed")
    }

    @Test
    fun onFetchMetaData_whenSuccess_shouldChangeValueToSuccess() {
        // given
        val dummyData = getDummyFeedXHeaderData()
        coEvery { feedXHeaderUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { feedXHeaderUseCase.executeOnBackground() } returns dummyData

        // when
        viewModel.fetchFeedMetaData()

        // then
        assert(viewModel.metaData.value is Success)
        val successData = (viewModel.metaData.value as Success).data
        assert(successData.selectedIndex == 0)
        assert(successData.profileApplink == dummyData.feedXHeaderData.data.userProfile.applink)
        assert(successData.profilePhotoUrl == dummyData.feedXHeaderData.data.userProfile.image)
        assert(successData.showMyProfile == dummyData.feedXHeaderData.data.userProfile.isShown)
        assert(successData.isCreationActive == dummyData.feedXHeaderData.data.creation.isActive)
        assert(successData.showLive == dummyData.feedXHeaderData.data.live.isActive)
        assert(successData.liveApplink == dummyData.feedXHeaderData.data.live.applink)

        val creationData = (viewModel.feedCreateContentBottomSheetData.value as Success).data
        assert(creationData[0].type == CreateContentType.CREATE_SHORT_VIDEO)
        assert(creationData[1].type == CreateContentType.CREATE_POST)
        assert(creationData[2].type == CreateContentType.CREATE_LIVE)
    }

    @Test
    fun onFetchMetaData_whenSuccessWithCreationAsUser_shouldChangeValueToSuccess() {
        // given
        val dummyData = FeedXHeaderResponse(
            feedXHeaderData = FeedXHeader(
                data = FeedXHeaderData(
                    creation = Creation(
                        isActive = true,
                        image = "",
                        authors = listOf(
                            Authors(
                                id = "123",
                                name = "Nama Toko",
                                type = "content-user",
                                image = "https://images.tokopedia.com/...",
                                hasUsername = false,
                                hasAcceptTnC = false,
                                items = listOf(
                                    AuthorItem(
                                        isActive = true,
                                        type = "post",
                                        title = "Buat Post",
                                        image = "https://images.tokopedia.com/...",
                                        weblink = "https://tokopedia.com/...",
                                        applink = "tokopedia://content/..."
                                    ),
                                    AuthorItem(
                                        isActive = true,
                                        type = "livestream",
                                        title = "Buat Livestream",
                                        image = "https://images.tokopedia.com/...",
                                        weblink = "https://tokopedia.com/...",
                                        applink = "tokopedia://content/..."
                                    ),
                                    AuthorItem(
                                        isActive = true,
                                        type = "shortvideo",
                                        title = "Buat Video",
                                        image = "https://images.tokopedia.com/...",
                                        weblink = "https://tokopedia.com/...",
                                        applink = "tokopedia://content/..."
                                    )
                                )
                            )
                        )
                    ),
                    tab = Tab(
                        isActive = true,
                        items = listOf(
                            Items(
                                isActive = true,
                                position = 1,
                                type = "foryou",
                                title = "Untuk Kamu",
                                key = "foryou"
                            ),
                            Items(
                                isActive = true,
                                position = 2,
                                type = "following",
                                title = "Following",
                                key = "following"
                            )
                        ),
                        meta = MetaData(
                            selectedIndex = 0
                        )
                    ),
                    live = Live(
                        isActive = true,
                        title = "Following",
                        image = "https://images.tokopedia.com/...",
                        weblink = "https://tokopedia.com/...",
                        applink = "tokopedia://content/..."
                    ),
                    userProfile = UserProfile(
                        applink = "applink://dummy",
                        image = "dummyimage",
                        isShown = true
                    )
                )
            )
        )
        coEvery { feedXHeaderUseCase.setRequestParams(any()) } coAnswers {}
        coEvery { feedXHeaderUseCase.executeOnBackground() } returns dummyData

        // when
        viewModel.fetchFeedMetaData()

        // then
        assert(viewModel.metaData.value is Success)
        val successData = (viewModel.metaData.value as Success).data
        assert(successData.selectedIndex == 0)
        assert(successData.profileApplink == dummyData.feedXHeaderData.data.userProfile.applink)
        assert(successData.profilePhotoUrl == dummyData.feedXHeaderData.data.userProfile.image)
        assert(successData.showMyProfile == dummyData.feedXHeaderData.data.userProfile.isShown)
        assert(successData.isCreationActive == dummyData.feedXHeaderData.data.creation.isActive)
        assert(successData.showLive == dummyData.feedXHeaderData.data.live.isActive)
        assert(successData.liveApplink == dummyData.feedXHeaderData.data.live.applink)

        val creationData = (viewModel.feedCreateContentBottomSheetData.value as Success).data
        assert(creationData[0].type == CreateContentType.CREATE_SHORT_VIDEO)
        assert(creationData[1].type == CreateContentType.CREATE_POST)
        assert(creationData[2].type == CreateContentType.CREATE_LIVE)
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

    @Test
    fun onReportContent_whenFailUsecase_shouldChangeValueToFail() {
        // given
        coEvery { submitReportUseCase(any()) } throws MessageErrorException("Failed to fetch")

        // when
        viewModel.reportContent(FeedComplaintSubmitReportUseCase.Param("", "", "", ""))

        // then
        val response = viewModel.reportResponse.value
        assert(response is Fail)
        assert((response as Fail).throwable is MessageErrorException)
        assert(response.throwable.message == "Failed to fetch")
    }

    @Test
    fun onReportContent_whenNotSuccess_shouldChangeValueToFail() {
        // given
        val dummyResponse = FeedComplaintSubmitReportResponse(
            data = FeedComplaintSubmitReportResponse.FeedComplaintSubmitReport(
                success = false
            )
        )
        coEvery { submitReportUseCase(any()) } returns dummyResponse

        // when
        viewModel.reportContent(FeedComplaintSubmitReportUseCase.Param("", "", "", ""))

        // then
        val response = viewModel.reportResponse.value
        assert(response is Fail)
        assert((response as Fail).throwable is MessageErrorException)
        assert(response.throwable.message == "Error in Reporting")
    }

    @Test
    fun onReportContent_whenSuccess_shouldChangeValueToSuccess() {
        // given
        val dummyResponse = FeedComplaintSubmitReportResponse(
            data = FeedComplaintSubmitReportResponse.FeedComplaintSubmitReport(
                success = true
            )
        )
        coEvery { submitReportUseCase(any()) } returns dummyResponse

        // when
        viewModel.reportContent(FeedComplaintSubmitReportUseCase.Param("", "", "", ""))

        // then
        val response = viewModel.reportResponse.value
        assert(response is Success)
        assert((response as Success).data == dummyResponse)
    }

    private fun getDummyFeedXHeaderData() = FeedXHeaderResponse(
        feedXHeaderData = FeedXHeader(
            data = FeedXHeaderData(
                creation = Creation(
                    isActive = true,
                    image = "",
                    authors = listOf(
                        Authors(
                            id = "123",
                            name = "Nama Toko",
                            type = "content-shop",
                            image = "https://images.tokopedia.com/...",
                            hasUsername = false,
                            hasAcceptTnC = false,
                            items = listOf(
                                AuthorItem(
                                    isActive = true,
                                    type = "post",
                                    title = "Buat Post",
                                    image = "https://images.tokopedia.com/...",
                                    weblink = "https://tokopedia.com/...",
                                    applink = "tokopedia://content/..."
                                ),
                                AuthorItem(
                                    isActive = true,
                                    type = "livestream",
                                    title = "Buat Livestream",
                                    image = "https://images.tokopedia.com/...",
                                    weblink = "https://tokopedia.com/...",
                                    applink = "tokopedia://content/..."
                                ),
                                AuthorItem(
                                    isActive = true,
                                    type = "shortvideo",
                                    title = "Buat Video",
                                    image = "https://images.tokopedia.com/...",
                                    weblink = "https://tokopedia.com/...",
                                    applink = "tokopedia://content/..."
                                )
                            )
                        )
                    )
                ),
                tab = Tab(
                    isActive = true,
                    items = listOf(
                        Items(
                            isActive = true,
                            position = 1,
                            type = "foryou",
                            title = "Untuk Kamu",
                            key = "foryou"
                        ),
                        Items(
                            isActive = true,
                            position = 2,
                            type = "following",
                            title = "Following",
                            key = "following"
                        )
                    ),
                    meta = MetaData(
                        selectedIndex = 0
                    )
                ),
                live = Live(
                    isActive = true,
                    title = "Following",
                    image = "https://images.tokopedia.com/...",
                    weblink = "https://tokopedia.com/...",
                    applink = "tokopedia://content/..."
                ),
                userProfile = UserProfile(
                    applink = "applink://dummy",
                    image = "dummyimage",
                    isShown = true
                )
            )
        )
    )
}
