package com.tokopedia.people.viewmodel.followerfollowing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.people.Success
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.model.CommonModelBuilder
import com.tokopedia.people.model.followerfollowing.FollowerListModelBuilder
import com.tokopedia.people.model.followerfollowing.FollowingListModelBuilder
import com.tokopedia.people.model.userprofile.MutationUiModelBuilder
import com.tokopedia.people.robot.FollowerFollowingViewModelRobot
import com.tokopedia.people.util.equalTo
import com.tokopedia.people.util.getOrAwaitValue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class FollowerFollowingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val commonBuilder = CommonModelBuilder()
    private val followerListBuilder = FollowerListModelBuilder()
    private val followingListBuilder = FollowingListModelBuilder()
    private val mutationBuilder = MutationUiModelBuilder()

    private val mockUserIdTarget = "2"

    private val mockException = commonBuilder.buildException()
    private val mockFollowerList = followerListBuilder.buildModel()
    private val mockFollowingList = followingListBuilder.buildModel()
    private val mockSuccess = mutationBuilder.buildSuccess()
    private val mockError = mutationBuilder.buildError()
    private val mockErrorWithoutMessage = mutationBuilder.buildError("")
    private val repo: UserProfileRepository = mockk(relaxed = true)

    private val robot = FollowerFollowingViewModelRobot(
        repo = repo,
    )

    @Before
    fun setUp() {
        coEvery { repo.getFollowerList(any(), any(), any()) } returns mockFollowerList
        coEvery { repo.getFollowingList(any(), any(), any()) } returns mockFollowingList
        coEvery { repo.followProfile(any()) } returns mockSuccess
        coEvery { repo.unFollowProfile(any()) } returns mockSuccess
    }

    @Test
    fun `when user load follower list successfully, it should emit the data`() {
        robot.start {
            getFollowers()

            val result = robot.viewModel.profileFollowersListLiveData.getOrAwaitValue()
            result equalTo Success(mockFollowerList)
        }
    }

    @Test
    fun `when user failed load follower list, it should emit the error data`() {
        robot.start {
            coEvery { repo.getFollowerList(any(), any(), any()) } throws mockException
            getFollowers()

            val throwable = robot.viewModel.followersErrorLiveData.getOrAwaitValue()
            throwable equalTo mockException
        }
    }

    @Test
    fun `when user load following list successfully, it should emit the data`() {
        robot.start {
            getFollowings()

            val result = robot.viewModel.profileFollowingsListLiveData.getOrAwaitValue()
            result equalTo Success(mockFollowingList)
        }
    }

    @Test
    fun `when user failed load following list, it should emit the error data`() {
        robot.start {
            coEvery { repo.getFollowingList(any(), any(), any()) } throws mockException
            getFollowings()

            val throwable = robot.viewModel.followersErrorLiveData.getOrAwaitValue()
            throwable equalTo mockException
        }
    }

    @Test
    fun `when user successfully follow the account, it should emit the success data`() {
        robot.start {
            viewModel.doFollow(mockUserIdTarget)

            val data = robot.viewModel.profileDoFollowLiveData.getOrAwaitValue()
            data equalTo mockSuccess
        }
    }

    @Test
    fun `when user failed follow the account, it should emit error state`() {
        robot.start {
            coEvery { repo.followProfile(any()) } throws mockException
            viewModel.doFollow(mockUserIdTarget)

            val data = robot.viewModel.profileDoFollowLiveData.getOrAwaitValue()
            data equalTo mockErrorWithoutMessage
        }
    }

    @Test
    fun `when user failed follow the account because of BE issue, it should emit error state`() {
        robot.start {
            coEvery { repo.followProfile(any()) } returns mockError
            viewModel.doFollow(mockUserIdTarget)

            val data = robot.viewModel.profileDoFollowLiveData.getOrAwaitValue()
            data equalTo mockError
        }
    }

    @Test
    fun `when user successfully unfollow the account, it should emit the success data`() {
        robot.start {
            viewModel.doUnFollow(mockUserIdTarget)

            val data = robot.viewModel.profileDoUnFollowLiveData.getOrAwaitValue()
            data equalTo mockSuccess
        }
    }

    @Test
    fun `when user failed unfollow the account, it should emit error state`() {
        robot.start {
            coEvery { repo.unFollowProfile(any()) } throws mockException
            viewModel.doUnFollow(mockUserIdTarget)

            val data = robot.viewModel.profileDoUnFollowLiveData.getOrAwaitValue()
            data equalTo mockErrorWithoutMessage
        }
    }

    @Test
    fun `when user failed unfollow the account because of BE issue, it should emit error state`() {
        robot.start {
            coEvery { repo.unFollowProfile(any()) } returns mockError
            viewModel.doUnFollow(mockUserIdTarget)

            val data = robot.viewModel.profileDoUnFollowLiveData.getOrAwaitValue()
            data equalTo mockError
        }
    }
}
