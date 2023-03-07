package com.tokopedia.people.viewmodel.followerfollowing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction
import com.tokopedia.people.Success
import com.tokopedia.people.data.UserFollowRepository
import com.tokopedia.people.model.CommonModelBuilder
import com.tokopedia.people.model.followerfollowing.FollowerListModelBuilder
import com.tokopedia.people.model.followerfollowing.FollowingListModelBuilder
import com.tokopedia.people.model.userprofile.MutationUiModelBuilder
import com.tokopedia.people.robot.FollowerFollowingViewModelRobot
import com.tokopedia.people.util.equalTo
import com.tokopedia.people.util.getOrAwaitValue
import com.tokopedia.people.views.uimodel.FollowResultUiModel
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

    private val mockError = mutationBuilder.buildError()
    private val repo: UserFollowRepository = mockk(relaxed = true)

    private val robot = FollowerFollowingViewModelRobot(
        repo = repo
    )

    @Test
    fun `when user load follower list successfully, it should emit the data`() {
        val expectedValue = FollowerListModelBuilder().build("", 0)
        coEvery { repo.getMyFollowers(any(), any(), any()) } returns expectedValue

        robot.start {
            this.getFollowers()

            val result = robot.viewModel.profileFollowersListLiveData.getOrAwaitValue()
            result equalTo Success(expectedValue)
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
            result equalTo Success(expectedValue)
        }
    }

    @Test
    fun `when user load follower list, the list is not empty  & has next cursor, it should not re-hit the gql`() {
        val nextCursor = "nextCursor"
        val expectedValue = FollowerListModelBuilder().build(nextCursor, 2)

        coEvery { repo.getMyFollowers(any(), "", any()) } returns expectedValue
        coEvery { repo.getMyFollowers(any(), nextCursor, any()) } returns FollowerListModelBuilder().build("", 2)

        robot.start {
            getFollowers()

            val result = robot.viewModel.profileFollowersListLiveData.getOrAwaitValue()
            result equalTo Success(expectedValue)
        }
    }

    @Test
    fun `when user load following list successfully, it should emit the data`() {
        val expectedValue = FollowingListModelBuilder().build("", 0)

        coEvery { repo.getMyFollowing(any(), "", any()) } returns expectedValue
        robot.start {
            getFollowings()

            val result = robot.viewModel.profileFollowingsListLiveData.getOrAwaitValue()
            result equalTo Success(expectedValue)
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
            result equalTo Success(expectedValue)
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
            result equalTo Success(expectedValue)
        }
    }

    @Test
    fun `when user successfully follow the account, it should emit the success data`() {
        val position = 0
        val follow = false
        val expectedResponse = MutationUiModelBuilder().buildSuccess("yay")
        val expectedResult = FollowResultUiModel.Success(expectedResponse.message)

        coEvery { repo.followUser(mockUserIdTarget, !follow) } returns expectedResponse
        robot.start {
            viewModel.followUser(mockUserIdTarget, follow, position)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo expectedResult
        }
    }

    @Test
    fun `when user successfully follow shop, it should emit the success data`() {
        val expectedResponse = MutationUiModelBuilder().buildSuccess("yay")
        val expectedResult = FollowResultUiModel.Success(expectedResponse.message)

        coEvery { repo.followShop("1", ShopFollowAction.Follow) } returns expectedResponse
        robot.start {
            viewModel.followShop("1", false, 1)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo expectedResult
        }
    }

    @Test
    fun `when user failed follow the account, it should emit error state`() {
        val follow = false
        val position = 0
        val expectedResult = FollowResultUiModel.Fail(
            mockException.localizedMessage,
            follow,
            position
        )
        coEvery { repo.followUser(mockUserIdTarget, !follow) } throws mockException

        robot.start {
            viewModel.followUser(mockUserIdTarget, follow, position)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo expectedResult
        }
    }

    @Test
    fun `when user failed follow shop, it should emit error state`() {
        val follow = false
        val position = 0
        val expectedResult = FollowResultUiModel.Fail(
            mockException.localizedMessage,
            follow,
            position
        )
        coEvery { repo.followShop("1", ShopFollowAction.Follow) } throws mockException

        robot.start {
            viewModel.followShop("1", follow, position)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo expectedResult
        }
    }

    @Test
    fun `when user failed follow the account because of BE issue, it should emit error state`() {
        val follow = false
        val position = 0
        val expectedResult = FollowResultUiModel.Fail(
            mockError.message,
            follow,
            position
        )
        coEvery { repo.followUser(mockUserIdTarget, !follow) } returns mockError

        robot.start {
            viewModel.followUser(mockUserIdTarget, follow, position)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo expectedResult
        }
    }

    @Test
    fun `when user failed follow shop because of BE issue, it should emit error state`() {
        val follow = false
        val position = 0
        val expectedResult = FollowResultUiModel.Fail(
            mockError.message,
            follow,
            position
        )
        coEvery { repo.followShop("1", ShopFollowAction.Follow) } returns mockError

        robot.start {
            viewModel.followShop("1", follow, position)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo expectedResult
        }
    }

    @Test
    fun `when user successfully unfollow the account, it should emit the success data`() {
        val position = 0
        val unFollow = true
        val expectedResponse = MutationUiModelBuilder().buildSuccess("yay")
        val expectedResult = FollowResultUiModel.Success(expectedResponse.message)

        coEvery { repo.followUser(mockUserIdTarget, !unFollow) } returns expectedResponse

        robot.start {
            viewModel.followUser(mockUserIdTarget, unFollow, position)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo expectedResult
        }
    }

    @Test
    fun `when user successfully unfollow shop, it should emit the success data`() {
        val position = 0
        val unFollow = true
        val expectedResponse = MutationUiModelBuilder().buildSuccess("yay")
        val expectedResult = FollowResultUiModel.Success(expectedResponse.message)

        coEvery { repo.followShop("1", ShopFollowAction.UnFollow) } returns expectedResponse

        robot.start {
            viewModel.followShop("1", unFollow, position)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo expectedResult
        }
    }

    @Test
    fun `when user failed unfollow the account, it should emit error state`() {
        val unfollow = true
        val position = 0
        val expectedResult = FollowResultUiModel.Fail(
            mockException.localizedMessage,
            unfollow,
            position
        )
        coEvery { repo.followUser(mockUserIdTarget, !unfollow) } throws mockException

        robot.start {
            viewModel.followUser(mockUserIdTarget, unfollow, position)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo expectedResult
        }
    }

    @Test
    fun `when user failed unfollow shop, it should emit error state`() {
        val unfollow = true
        val position = 0
        val expectedResult = FollowResultUiModel.Fail(
            mockException.localizedMessage,
            unfollow,
            position
        )
        coEvery { repo.followShop("1", ShopFollowAction.UnFollow) } throws mockException

        robot.start {
            viewModel.followShop("1", unfollow, position)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo expectedResult
        }
    }

    @Test
    fun `when user failed unfollow the account because of BE issue, it should emit error state`() {
        val unfollow = true
        val position = 0
        val expectedResult = FollowResultUiModel.Fail(
            mockError.message,
            unfollow,
            position
        )
        coEvery { repo.followUser(mockUserIdTarget, !unfollow) } returns mockError

        robot.start {
            viewModel.followUser(mockUserIdTarget, unfollow, position)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo expectedResult
        }
    }

    @Test
    fun `when user failed unfollow shop because of BE issue, it should emit error state`() {
        val unfollow = true
        val position = 0
        val expectedResult = FollowResultUiModel.Fail(
            mockError.message,
            unfollow,
            position
        )
        coEvery { repo.followShop("1", ShopFollowAction.UnFollow) } returns mockError

        robot.start {
            viewModel.followShop("1", unfollow, position)

            val data = robot.viewModel.followResult.getOrAwaitValue()
            data equalTo expectedResult
        }
    }

    @Test
    fun `when developer set username from everywhere, the username value should be same`() {
        val expectedResult = "username"
        robot.start {
            viewModel.username = expectedResult

            viewModel.username equalTo expectedResult
        }
    }
}
