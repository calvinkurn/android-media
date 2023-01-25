package com.tokopedia.people.viewmodel.userprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.people.Success
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.model.CommonModelBuilder
import com.tokopedia.people.model.shoprecom.ShopRecomModelBuilder
import com.tokopedia.people.model.userprofile.*
import com.tokopedia.people.robot.UserProfileViewModelRobot
import com.tokopedia.people.util.*
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class UserProfileContentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: UserProfileRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)

    private val commonBuilder = CommonModelBuilder()
    private val profileBuilder = ProfileUiModelBuilder()
    private val followInfoBuilder = FollowInfoUiModelBuilder()
    private val playVideoBuilder = PlayVideoModelBuilder()
    private val profileWhitelistBuilder = ProfileWhitelistUiModelBuilder()
    private val shopRecomBuilder = ShopRecomModelBuilder()
    private val feedsModelBuilder = FeedsModelBuilder()
    private val profileTabBuilder = TabModelBuilder()

    private val mockException = commonBuilder.buildException()
    private val mockUserId = "1"
    private val mockOwnUsername = "jonathandarwin"

    private val mockShopRecom = shopRecomBuilder.buildModelIsShown()
    private val mockOwnProfile = profileBuilder.buildProfile(userID = mockUserId)
    private val mockOwnFollow = followInfoBuilder.buildFollowInfo(userID = mockUserId, encryptedUserID = mockUserId, status = false)
    private val mockHasAcceptTnc = profileWhitelistBuilder.buildHasAcceptTnc()
    private val mockProfileTabShown = profileTabBuilder.mockProfileTab()
    private val mockProfileTabNotShown = profileTabBuilder.mockProfileTab(showTabs = false)
    private val mockFeed = feedsModelBuilder.mockFeedsPost()
    private val mockFeedPagination = feedsModelBuilder.mockFeedsPost(id = "3324")
    private val mockFeedEmpty = feedsModelBuilder.mockFeedsPost(isEmpty = true)

    private val mockPlayVideo = playVideoBuilder.buildModel()
    private val mockPlayVideoEmpty = playVideoBuilder.buildModel(isEmpty = true)

    private val robot = UserProfileViewModelRobot(
        username = mockOwnUsername,
        repo = mockRepo,
        dispatcher = testDispatcher,
        userSession = mockUserSession,
    )

    @Before
    fun setUp() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.userId } returns mockUserId

        coEvery { mockRepo.getProfile(mockOwnUsername) } returns mockOwnProfile

        coEvery { mockRepo.getFollowInfo(listOf(mockUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getUserProfileTab(mockOwnProfile.userID) } returns mockProfileTabShown
        coEvery { mockRepo.getPlayVideo(any(), any()) } returns mockPlayVideo
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecom

        coEvery { mockRepo.getWhitelist() } returns mockHasAcceptTnc
    }

    @Test
    fun `when user success load profile tab and emit data`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getUserProfileTab(mockOwnProfile.userID) } returns mockProfileTabShown
            }
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                profileTab equalTo mockProfileTabShown
            }
        }
    }

    @Test
    fun `when user success load profile tab and emit event`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getUserProfileTab(mockOwnProfile.userID) } returns mockProfileTabShown
            }
            it.recordEvent {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                last().assertEvent(UserProfileUiEvent.SuccessLoadTabs(false))
            }
        }
    }

    @Test
    fun `when user success load profile tab and emit event but empty data`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getUserProfileTab(mockOwnProfile.userID) } returns mockProfileTabNotShown
            }
            it.recordEvent {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                last().assertEvent(UserProfileUiEvent.SuccessLoadTabs(false))
            }
        }
    }

    @Test
    fun `when user fail load profile tab`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getUserProfileTab(mockOwnProfile.userID) } throws mockException
            }
            it.recordEvent {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                last().assertEvent(UserProfileUiEvent.ErrorGetProfileTab(Throwable()))
            }
        }
    }

    @Test
    fun `when user reload profile tab`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getUserProfileTab(mockOwnProfile.userID) } returns mockProfileTabShown
            }
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                profileTab equalTo mockProfileTabShown
            }
        }
    }

    @Test
    fun `when user success load feed post`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getFeedPosts(mockOwnProfile.userID, "", 10) } returns mockFeed
            }
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
                submitAction(UserProfileAction.LoadFeedPosts(""))
            } andThen {
                feedPostsContent equalTo mockFeed
            }
        }
    }

    @Test
    fun `when user success refresh load feed post`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getFeedPosts(mockOwnProfile.userID, "", 10) } returns mockFeed
            }
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
                submitAction(UserProfileAction.LoadFeedPosts("", true))
            } andThen {
                feedPostsContent equalTo mockFeed
            }
        }
    }

    @Test
    fun `when user success load feed post pagination`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getFeedPosts(mockOwnProfile.userID, "", 10) } returns mockFeed
                coEvery { mockRepo.getFeedPosts(mockOwnProfile.userID, "123", 10) } returns mockFeedPagination
            }
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
                submitAction(UserProfileAction.LoadFeedPosts(""))
                submitAction(UserProfileAction.LoadFeedPosts("123"))
            } andThen {
                feedPostsContent.posts.size equalTo mockFeed.posts.size * 2
            }
        }
    }

    @Test
    fun `when user success load feed post pagination and do refresh`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getFeedPosts(mockOwnProfile.userID, "", 10) } returns mockFeed
                coEvery { mockRepo.getFeedPosts(mockOwnProfile.userID, "123", 10) } returns mockFeedPagination
                coEvery { mockRepo.getFeedPosts(mockOwnProfile.userID, "", mockFeed.posts.size * 2) } returns mockFeedPagination
            }
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
                submitAction(UserProfileAction.LoadFeedPosts(""))
                submitAction(UserProfileAction.LoadFeedPosts("123"))
                submitAction(UserProfileAction.LoadFeedPosts("123", true))
            } andThen {
                feedPostsContent.posts.size equalTo mockFeed.posts.size
            }
        }
    }

    @Test
    fun `when user success load feed post but empty`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getFeedPosts(mockOwnProfile.userID, "", 10) } returns mockFeedEmpty
            }
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
                submitAction(UserProfileAction.LoadFeedPosts(""))
            } andThen {
                feedPostsContent equalTo mockFeedEmpty
            }
        }
    }

    @Test
    fun `when user fail load feed post`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getFeedPosts(mockOwnProfile.userID, "", 10) } throws mockException
            }
            it.recordEvent {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
                submitAction(UserProfileAction.LoadFeedPosts(""))
            } andThen {
                last().assertEvent(UserProfileUiEvent.ErrorFeedPosts(mockException))
            }
        }}

    @Test
    fun `load profile with no refresh - should not emit LoadPlayVideo event`() {
        robot.start {
            recordEvent {
                submitAction(UserProfileAction.LoadProfile(isRefresh = false))
            } andThen {
                this.assertEmpty()
            }
        }
    }

    @Test
    fun `when user successfully load user play video, it will emit the data`() {
        robot.recordState {
            submitAction(UserProfileAction.LoadPlayVideo())
        } andThen {
            videoPostsContent equalTo mockPlayVideo
        }
    }

    @Test
    fun `when user successfully load user play video pagination, it will emit the data`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getPlayVideo(any(), "123") } returns mockPlayVideo
            }
            it.recordState {
                submitAction(UserProfileAction.LoadPlayVideo())
                submitAction(UserProfileAction.LoadPlayVideo("123"))
            } andThen {
                videoPostsContent.playGetContentSlot.data.size equalTo mockPlayVideo.playGetContentSlot.data.size * 2
            }
        }
    }

    @Test
    fun `when user successfully load user play video but empty, it will emit the event`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getPlayVideo(any(), any()) } returns mockPlayVideoEmpty
            }
            it.recordState {
                submitAction(UserProfileAction.LoadPlayVideo())
            } andThen {
                videoPostsContent equalTo mockPlayVideoEmpty
            }
        }
    }

    @Test
    fun `when user failed load user play video, it will emit error`() {
        robot.recordEvent {
            coEvery { mockRepo.getPlayVideo(any(), any()) } throws mockException
            submitAction(UserProfileAction.LoadPlayVideo())
        } andThen {
            last().assertEvent(UserProfileUiEvent.ErrorVideoPosts(mockException))
        }
    }
}
