package com.tokopedia.people.viewmodel.userprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.people.Success
import com.tokopedia.people.domains.repository.UserProfileRepository
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
class UserProfilePlayVideoViewModelTest {

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

    private val mockException = commonBuilder.buildException()
    private val mockUserId = "1"
    private val mockOwnUsername = "jonathandarwin"

    private val mockShopRecom = shopRecomBuilder.buildModelIsShown()
    private val mockOwnProfile = profileBuilder.buildProfile(userID = mockUserId)
    private val mockOwnFollow = followInfoBuilder.buildFollowInfo(userID = mockUserId, encryptedUserID = mockUserId, status = false)
    private val mockHasAcceptTnc = profileWhitelistBuilder.buildHasAcceptTnc()

    private val mockPlayVideo = playVideoBuilder.buildModel()

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

        coEvery { mockRepo.getPlayVideo(any(), any()) } returns mockPlayVideo
        coEvery { mockRepo.getShopRecom() } returns mockShopRecom

        coEvery { mockRepo.getWhitelist() } returns mockHasAcceptTnc
    }

    @Test
    fun `load profile with refresh - should emit LoadPlayVideo event`() {

        robot.start {
            recordEvent {
                submitAction(UserProfileAction.LoadProfile(isRefresh = true))
            } andThen {
                last().assertEvent(UserProfileUiEvent.LoadPlayVideo)
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

        robot.start {
            submitAction(UserProfileAction.LoadPlayVideo(""))

            val data = robot.viewModel.playPostContentLiveData.getOrAwaitValue()
            data equalTo Success(mockPlayVideo)
        }
    }

    @Test
    fun `when user failed load user play video, it will emit error`() {

        robot.start {
            coEvery { mockRepo.getPlayVideo(any(), any()) } throws mockException

            submitAction(UserProfileAction.LoadPlayVideo(""))

            val data = robot.viewModel.playPostContentLiveData.getOrNullValue()
            val throwable = robot.viewModel.userPostErrorLiveData.getOrAwaitValue()

            assertEquals(data, null)
            throwable equalTo mockException
        }
    }
}