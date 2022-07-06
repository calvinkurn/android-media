package com.tokopedia.people.viewmodel.userprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.people.Success
import com.tokopedia.people.domains.repository.UserProfileRepository
import com.tokopedia.people.model.CommonModelBuilder
import com.tokopedia.people.model.userprofile.PlayVideoModelBuilder
import com.tokopedia.people.robot.UserProfileViewModelRobot
import com.tokopedia.people.util.equalTo
import com.tokopedia.people.util.getOrAwaitValue
import com.tokopedia.people.util.getOrNullValue
import com.tokopedia.people.views.uimodel.action.UserProfileAction
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
    private val playVideoBuilder = PlayVideoModelBuilder()

    private val mockOwnUsername = "jonathandarwin"
    private val mockException = commonBuilder.buildException()

    private val mockPlayVideo = playVideoBuilder.buildModel()

    private val robot = UserProfileViewModelRobot(
        username = mockOwnUsername,
        repo = mockRepo,
        dispatcher = testDispatcher,
        userSession = mockUserSession,
    )

    @Before
    fun setUp() {
        coEvery { mockRepo.getPlayVideo(any(), any()) } returns mockPlayVideo
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