package com.tokopedia.people.viewmodel.followerfollowing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.people.Success
import com.tokopedia.people.data.UserFollowRepository
import com.tokopedia.people.model.CommonModelBuilder
import com.tokopedia.people.model.followerfollowing.FollowerListModelBuilder
import com.tokopedia.people.model.followerfollowing.FollowingListModelBuilder
import com.tokopedia.people.model.userprofile.MutationUiModelBuilder
import com.tokopedia.people.robot.FollowerFollowingViewModelRobot
import com.tokopedia.people.util.equalTo
import com.tokopedia.people.util.getOrAwaitValue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
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
    private val mutationBuilder = MutationUiModelBuilder()

    private val mockUserIdTarget = "2"

    private val mockException = commonBuilder.buildException()

    private val mockSuccess = mutationBuilder.buildSuccess()
    private val mockError = mutationBuilder.buildError()
    private val mockErrorWithoutMessage = mutationBuilder.buildError("")
    private val repo: UserFollowRepository = mockk(relaxed = true)

    private val robot = FollowerFollowingViewModelRobot(
        repo = repo,
    )

    @Test
    fun `when user load follower list successfully, it should emit the data`() {
        val expectedValue = FollowerListModelBuilder().build("", 0)
        coEvery { repo.getMyFollowers(any(), any(), any()) } returns expectedValue

        robot.start {
            this.getFollowers()

            val result = robot.viewModel.profileFollowersListLiveData.getOrAwaitValue()
            result equalTo Success(expectedValue.first, expectedValue.second)
        }
    }

    @Test
    fun `when user failed load follower list, it should emit the error data`() {
        coEvery { repo.getMyFollowers(any(), any(), any()) } throws mockException
        robot.start {
            getFollowers()

            val throwable = robot.viewModel.followersErrorLiveData.getOrAwaitValue()
            throwable equalTo mockException
        }
    }

    @Test
    fun `when user load follower list but the list is empty & has next cursor, it should re-hit the gql`() {
        val nextCursor = "nextCursor"
        val expectedValue = FollowerListModelBuilder().build("", 2)

        coEvery { repo.getMyFollowers(any(), "", any()) } returns FollowerListModelBuilder().build(nextCursor, 0)
        coEvery { repo.getMyFollowers(any(), nextCursor, any()) } returns expectedValue
        robot.start {
            getFollowers()

            val result = robot.viewModel.profileFollowersListLiveData.getOrAwaitValue()
            result equalTo Success(expectedValue.first, expectedValue.second)
        }
    }

    @Test
    fun `when user load follower list, the list is not empty  & has next cursor, it should not re-hit the gql`() {
        val nextCursor = "nextCursor"
        val expectedValue = FollowerListModelBuilder().build(nextCursor, 2)

        coEvery { repo.getMyFollowers(any(), "", any()) } returns expectedValue
        coEvery { repo.getMyFollowers(any(), nextCursor, any()) } returns FollowingListModelBuilder().build("", 2)

        robot.start {
            getFollowers()

            val result = robot.viewModel.profileFollowersListLiveData.getOrAwaitValue()
            result equalTo Success(expectedValue.first, expectedValue.second)
        }
    }

    @Test
    fun `when user load following list successfully, it should emit the data`() {
        val expectedValue = FollowingListModelBuilder().build("", 0)

        coEvery { repo.getMyFollowing(any(), "", any()) } returns expectedValue
        robot.start {
            getFollowings()

            val result = robot.viewModel.profileFollowingsListLiveData.getOrAwaitValue()
            result equalTo Success(expectedValue.first, expectedValue.second)
        }
    }

    @Test
    fun `when user failed load following list, it should emit the error data`() {
        coEvery { repo.getMyFollowing(any(), any(), any()) } throws mockException

        robot.start {
            getFollowings()

            val throwable = robot.viewModel.followersErrorLiveData.getOrAwaitValue()
            throwable equalTo mockException
        }
    }

    @Test
    fun `when user load following list but the list is empty & has next cursor, it should re-hit the gql`() {
        val nextCursor = "nextCursor"
        val expectedValue = FollowingListModelBuilder().build("", 2)

        coEvery { repo.getMyFollowing(any(), "", any()) } returns FollowingListModelBuilder().build(nextCursor, 0)
        coEvery { repo.getMyFollowing(any(), nextCursor, any()) } returns expectedValue

        robot.start {
            getFollowings()

            val result = robot.viewModel.profileFollowingsListLiveData.getOrAwaitValue()
            result equalTo Success(expectedValue.first, expectedValue.second)
        }
    }

    @Test
    fun `when user load following list, the list is not empty & has next cursor, it should not re-hit the gql`() {
        val nextCursor = "nextCursor"
        val expectedValue = FollowingListModelBuilder().build(nextCursor, 2)

        coEvery { repo.getMyFollowing(any(), "", any()) } returns expectedValue
        coEvery { repo.getMyFollowing(any(), nextCursor, any()) } returns FollowingListModelBuilder().build("", 2)

        robot.start {
            getFollowings()

            val result = robot.viewModel.profileFollowingsListLiveData.getOrAwaitValue()
            result equalTo Success(expectedValue.first, expectedValue.second)
        }
    }

    @Test
    fun `when user successfully follow the account, it should emit the success data`() {
        val expectedResult = mockSuccess

        coEvery { repo.followUser(mockUserIdTarget, true) } returns expectedResult
        robot.start {
            viewModel.followUser(mockUserIdTarget, false)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo expectedResult
        }
    }

    @Test
    fun `when user failed follow the account, it should emit error state`() {
        coEvery { repo.followUser(mockUserIdTarget, true) } throws mockException

        robot.start {
            viewModel.followUser(mockUserIdTarget, false)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo mockErrorWithoutMessage
        }
    }

    @Test
    fun `when user failed follow the account because of BE issue, it should emit error state`() {
        coEvery { repo.followUser(mockUserIdTarget, true) } returns mockError

        robot.start {
            viewModel.followUser(mockUserIdTarget, false)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo mockError
        }
    }

    @Test
    fun `when user successfully unfollow the account, it should emit the success data`() {
        val expectedResult = mockSuccess

        coEvery { repo.followUser(mockUserIdTarget, false) } returns expectedResult

        robot.start {
            viewModel.followUser(mockUserIdTarget, true)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo expectedResult
        }
    }

    @Test
    fun `when user failed unfollow the account, it should emit error state`() {
        coEvery { repo.followUser(mockUserIdTarget, false) } throws mockException

        robot.start {
            viewModel.followUser(mockUserIdTarget, true)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo mockErrorWithoutMessage
        }
    }

    @Test
    fun `when user failed unfollow the account because of BE issue, it should emit error state`() {
        coEvery { repo.followUser(mockUserIdTarget, false) } returns mockError

        robot.start {
            viewModel.followUser(mockUserIdTarget, true)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo mockError
        }
    }
}
