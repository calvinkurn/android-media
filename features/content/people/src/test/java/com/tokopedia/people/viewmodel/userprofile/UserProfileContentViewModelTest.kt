package com.tokopedia.people.viewmodel.userprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.model.CommonModelBuilder
import com.tokopedia.people.model.review.UserReviewModelBuilder
import com.tokopedia.people.model.shoprecom.ShopRecomModelBuilder
import com.tokopedia.people.model.userprofile.FeedsModelBuilder
import com.tokopedia.people.model.userprofile.FollowInfoUiModelBuilder
import com.tokopedia.people.model.userprofile.PlayVideoModelBuilder
import com.tokopedia.people.model.userprofile.ProfileUiModelBuilder
import com.tokopedia.people.model.userprofile.ProfileWhitelistUiModelBuilder
import com.tokopedia.people.model.userprofile.TabModelBuilder
import com.tokopedia.people.robot.UserProfileViewModelRobot
import com.tokopedia.people.util.andThen
import com.tokopedia.people.util.assertEmpty
import com.tokopedia.people.util.assertEvent
import com.tokopedia.people.util.equalTo
import com.tokopedia.people.utils.UserProfileSharedPref
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.content.UserPlayVideoUiModel
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.people.views.uimodel.profile.ProfileTabState
import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions
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
    private val mockUserProfileSharedPref: UserProfileSharedPref = mockk(relaxed = true)

    private val commonBuilder = CommonModelBuilder()
    private val profileBuilder = ProfileUiModelBuilder()
    private val followInfoBuilder = FollowInfoUiModelBuilder()
    private val playVideoBuilder = PlayVideoModelBuilder()
    private val profileWhitelistBuilder = ProfileWhitelistUiModelBuilder()
    private val shopRecomBuilder = ShopRecomModelBuilder()
    private val feedsModelBuilder = FeedsModelBuilder()
    private val profileTabBuilder = TabModelBuilder()
    private val reviewModelBuilder = UserReviewModelBuilder()

    private val mockException = commonBuilder.buildException()
    private val mockUserId = "1"
    private val mockOwnUsername = "jonathandarwin"

    private val mockShopRecom = shopRecomBuilder.buildModelIsShown()
    private val mockOwnProfile = profileBuilder.buildProfile(userID = mockUserId)
    private val mockOwnFollow = followInfoBuilder.buildFollowInfo(userID = mockUserId, encryptedUserID = mockUserId, status = false)
    private val mockCreationInfo = profileWhitelistBuilder.buildCreationInfoModel()
    private val mockProfileTabShown = profileTabBuilder.mockProfileTab(newestTab = ProfileTabUiModel.Key.Review)
    private val mockProfileTabNotShown = profileTabBuilder.mockProfileTab(showTabs = false)
    private val mockFeed = feedsModelBuilder.mockFeedsPost()
    private val mockFeedPagination = feedsModelBuilder.mockFeedsPost(id = "3324")
    private val mockFeedEmpty = feedsModelBuilder.mockFeedsPost(isEmpty = true)
    private val mockReviewSettingsEnabled = reviewModelBuilder.buildReviewSetting(isEnabled = true)

    private val mockPlayVideo = playVideoBuilder.buildModel(nextCursor = "asdf")
    private val mockPlayVideoEmpty = playVideoBuilder.buildModel(size = 0)
    private val mockPlayVideoUpcoming = playVideoBuilder.buildModel(channelType = PlayWidgetChannelType.Upcoming, reminderType = PlayWidgetReminderType.NotReminded)
    private val mockPlayVideoChannel = mockPlayVideo.items.first()

    private val robot by lazy {
        UserProfileViewModelRobot(
            username = mockOwnUsername,
            repo = mockRepo,
            dispatcher = testDispatcher,
            userSession = mockUserSession,
            userProfileSharedPref = mockUserProfileSharedPref,
            dispatchers = testDispatcher,
        )
    }

    @Before
    fun setUp() {
        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockUserSession.userId } returns mockUserId

        coEvery { mockRepo.getProfile(mockOwnUsername) } returns mockOwnProfile

        coEvery { mockRepo.getFollowInfo(listOf(mockUserId)) } returns mockOwnFollow
        coEvery { mockRepo.getUserProfileTab(mockOwnProfile.userID) } returns mockProfileTabShown
        coEvery { mockRepo.getPlayVideo(any(), any(), any()) } returns mockPlayVideo
        coEvery { mockRepo.getShopRecom("") } returns mockShopRecom
        coEvery { mockRepo.getProfileSettings(any()) } returns mockReviewSettingsEnabled

        coEvery { mockRepo.getCreationInfo() } returns mockCreationInfo

        coEvery { mockUserProfileSharedPref.hasBeenShown(UserProfileSharedPref.Key.ReviewOnboarding) } returns true
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
                assert(profileTab is ProfileTabState.Success)
                it.viewModel.profileTab equalTo mockProfileTabShown
            }
        }
    }

    @Test
    fun `when user success load profile tab and this is the first time review tab is shown`() {
        robot.use {
            it.setup {
                coEvery { mockUserProfileSharedPref.hasBeenShown(UserProfileSharedPref.Key.ReviewOnboarding) } returns false
            }
            it.recordEvent {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                last().assertEvent(UserProfileUiEvent.ShowReviewOnboarding)
            }
        }
    }

    @Test
    fun `when user fail load profile tab`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getUserProfileTab(mockOwnProfile.userID) } throws mockException
            }
            it.recordState {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                assert(profileTab is ProfileTabState.Error)
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
        }
    }

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
            submitAction(UserProfileAction.LoadPlayVideo(isRefresh = true))
        } andThen {
            videoPostsContent equalTo mockPlayVideo
        }
    }

    @Test
    fun `when user successfully load user play video pagination, it will emit the data`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getPlayVideo(any(), "123", any()) } returns mockPlayVideo
            }
            it.recordState {
                submitAction(UserProfileAction.LoadPlayVideo(isRefresh = true))
                submitAction(UserProfileAction.LoadPlayVideo())
            } andThen {
                videoPostsContent.items.size equalTo mockPlayVideo.items.size * 2
            }
        }
    }

    @Test
    fun `when user successfully load user play video next page but empty, it will emit the data`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getPlayVideo(any(), "123", any()) } returns mockPlayVideo
            }
            it.recordState {
                submitAction(UserProfileAction.LoadPlayVideo(isRefresh = true))
                coEvery { mockRepo.getPlayVideo(any(), any(), any()) } returns mockPlayVideoEmpty
                submitAction(UserProfileAction.LoadPlayVideo())
                submitAction(UserProfileAction.LoadPlayVideo())
            } andThen {
                videoPostsContent.items.size equalTo mockPlayVideo.items.size
            }
        }
    }

    @Test
    fun `when user successfully load user play video but empty, it will emit the event`() {
        robot.use {
            it.setup {
                coEvery { mockRepo.getPlayVideo(any(), any(), any()) } returns mockPlayVideoEmpty
            }
            it.recordState {
                submitAction(UserProfileAction.LoadPlayVideo(isRefresh = true))
            } andThen {
                videoPostsContent equalTo mockPlayVideoEmpty
            }
        }
    }

    @Test
    fun `when user failed load user play video, it will emit error`() {
        robot.recordEvent {
            coEvery { mockRepo.getPlayVideo(any(), any(), any()) } throws mockException
            submitAction(UserProfileAction.LoadPlayVideo(isRefresh = true))
        } andThen {
            last().assertEvent(UserProfileUiEvent.ErrorVideoPosts(mockException))
        }
    }

    @Test
    fun `when user delete channel and success, it should emit success delete channel event`() {
        val deletedChannelId = "1"
        val afterDeleteVideoList = mockPlayVideo.copy(
            items = mockPlayVideo.items.filter { it.channelId != deletedChannelId }
        )

        coEvery { mockRepo.deletePlayChannel(any(), any()) } returns deletedChannelId

        robot.recordStateAndEvent {
            submitAction(UserProfileAction.LoadPlayVideo(isRefresh = true))
            submitAction(UserProfileAction.DeletePlayChannel(deletedChannelId))
        } andThen { state, events ->
            state.videoPostsContent.status equalTo UserPlayVideoUiModel.Status.Success
            state.videoPostsContent equalTo afterDeleteVideoList

            events.last().assertEvent(UserProfileUiEvent.SuccessDeleteChannel)
        }
    }

    @Test
    fun `when user delete channel and failed, it should emit error delete channel event`() {
        val deletedChannelId = "1"
        coEvery { mockRepo.deletePlayChannel(any(), any()) } throws mockException

        robot.recordStateAndEvent {
            submitAction(UserProfileAction.LoadPlayVideo(isRefresh = true))
            submitAction(UserProfileAction.DeletePlayChannel(deletedChannelId))
        } andThen { state, events ->
            state.videoPostsContent equalTo mockPlayVideo

            events.last().assertEvent(UserProfileUiEvent.ErrorDeleteChannel(mockException))
        }
    }

    @Test
    fun `when user comeback from play room and totalview is updated, it should update play channel info`() {

        val channelId = "1"
        val updatedTotalView = "123k"
        val updatedIsReminderSet = false

        robot.recordState {
            submitAction(UserProfileAction.LoadPlayVideo(isRefresh = true))
            submitAction(UserProfileAction.UpdatePlayChannelInfo(channelId, updatedTotalView, updatedIsReminderSet))
        } andThen {
            videoPostsContent.items.first { it.channelId == channelId }.totalView.totalViewFmt equalTo updatedTotalView
        }
    }

    @Test
    fun `when user comeback from play room and reminder is updated, it should update play channel info`() {

        val channelId = "1"
        val updatedTotalView = mockPlayVideoUpcoming.items.first { it.channelId == channelId }.totalView.totalViewFmt
        val updatedIsReminderSet = true

        coEvery { mockRepo.getPlayVideo(any(), any(), any()) } returns mockPlayVideoUpcoming

        robot.recordState {
            submitAction(UserProfileAction.LoadPlayVideo(isRefresh = true))
            submitAction(UserProfileAction.UpdatePlayChannelInfo(channelId, updatedTotalView, updatedIsReminderSet))
        } andThen {
            videoPostsContent.items.first { it.channelId == channelId }.reminderType equalTo PlayWidgetReminderType.Reminded
        }
    }

    @Test
    fun `when user clicks play video menu action, it should emit event to open play video action menu`() {
        robot.recordEvent {
            submitAction(UserProfileAction.ClickPlayVideoMenuAction(mockPlayVideoChannel))
        } andThen {
            last().assertEvent(UserProfileUiEvent.OpenPlayVideoActionMenu(mockPlayVideoChannel))
        }
    }

    @Test
    fun `when user clicks copy link play channel, it should emit event to copy link play video`() {
        robot.recordEvent {
            submitAction(UserProfileAction.ClickCopyLinkPlayChannel(mockPlayVideoChannel))
        } andThen {
            last().assertEvent(UserProfileUiEvent.CopyLinkPlayVideo(mockPlayVideoChannel.share.fullShareContent))
        }
    }

    @Test
    fun `when user clicks see performance play channel, it should emit event to open performance channel`() {
        robot.recordEvent {
            submitAction(UserProfileAction.ClickSeePerformancePlayChannel(mockPlayVideoChannel))
        } andThen {
            last().assertEvent(UserProfileUiEvent.OpenPerformancePlayChannel(mockPlayVideoChannel.performanceSummaryLink))
        }
    }

    @Test
    fun `when user clicks delete channel, it should emit event to show confirmation dialog to delete channel`() {
        robot.recordEvent {
            submitAction(UserProfileAction.ClickDeletePlayChannel(mockPlayVideoChannel))
        } andThen {
            last().assertEvent(UserProfileUiEvent.ShowDeletePlayVideoConfirmationDialog(mockPlayVideoChannel))
        }
    }
}
