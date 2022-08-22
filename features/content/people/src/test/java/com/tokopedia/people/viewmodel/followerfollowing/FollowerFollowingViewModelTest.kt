package com.tokopedia.people.viewmodel.followerfollowing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.people.Success
import com.tokopedia.people.domains.FollowerFollowingListingUseCase
import com.tokopedia.people.domains.ProfileFollowUseCase
import com.tokopedia.people.domains.ProfileUnfollowedUseCase
import com.tokopedia.people.model.CommonModelBuilder
import com.tokopedia.people.model.followerfollowing.FollowModelBuilder
import com.tokopedia.people.model.followerfollowing.FollowerListModelBuilder
import com.tokopedia.people.model.followerfollowing.FollowingListModelBuilder
import com.tokopedia.people.model.followerfollowing.UnFollowModelBuilder
import com.tokopedia.people.robot.FollowerFollowingViewModelRobot
import com.tokopedia.people.util.equalTo
import com.tokopedia.people.util.getOrAwaitValue
import com.tokopedia.people.util.getOrNullValue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import junit.framework.Assert.assertEquals
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
    private val followBuilder = FollowModelBuilder()
    private val unFollowBuilder = UnFollowModelBuilder()

    private val mockUserIdSource = "1"
    private val mockUserIdTarget = "2"

    private val mockException = commonBuilder.buildException()
    private val mockFollowerList = followerListBuilder.buildModel()
    private val mockFollowingList = followingListBuilder.buildModel()
    private val mockFollow = followBuilder.buildModel(mockUserIdSource, mockUserIdTarget)
    private val mockUnfollow = unFollowBuilder.buildModel(mockUserIdSource, mockUserIdTarget)

    private val mockFollowerFollowingUseCase: FollowerFollowingListingUseCase = mockk(relaxed = true)
    private val mockDoFollowUseCase: ProfileFollowUseCase = mockk(relaxed = true)
    private val mockDoUnFollowUseCase: ProfileUnfollowedUseCase = mockk(relaxed = true)

    private val robot = FollowerFollowingViewModelRobot(
        useCaseFollowersFollowingsList = mockFollowerFollowingUseCase,
        useCaseDoFollow = mockDoFollowUseCase,
        useCaseDoUnFollow = mockDoUnFollowUseCase,
    )

    @Before
    fun setUp() {
        coEvery { mockFollowerFollowingUseCase.getProfileFollowerList(any(), any(), any()) } returns mockFollowerList
        coEvery { mockFollowerFollowingUseCase.getProfileFollowingList(any(), any(), any()) } returns mockFollowingList
        coEvery { mockDoFollowUseCase.doFollow(any()) } returns mockFollow
        coEvery { mockDoUnFollowUseCase.doUnfollow(any()) } returns mockUnfollow
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
            coEvery { mockFollowerFollowingUseCase.getProfileFollowerList(any(), any(), any()) } throws mockException
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
            coEvery { mockFollowerFollowingUseCase.getProfileFollowingList(any(), any(), any()) } throws mockException
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
            data equalTo Success(mockFollow)
        }
    }

    @Test
    fun `when user failed follow the account, it should not emit any data`() {
        robot.start {
            coEvery { mockDoFollowUseCase.doFollow(any()) } throws mockException
            viewModel.doFollow(mockUserIdTarget)

            val data = robot.viewModel.profileDoFollowLiveData.getOrNullValue()
            assertEquals(data, null)
        }
    }

    @Test
    fun `when user successfully unfollow the account, it should emit the success data`() {
        robot.start {
            viewModel.doUnFollow(mockUserIdTarget)

            val data = robot.viewModel.profileDoUnFollowLiveData.getOrAwaitValue()
            data equalTo Success(mockUnfollow)
        }
    }

    @Test
    fun `when user failed unfollow the account, it should not emit any data`() {
        robot.start {
            coEvery { mockDoUnFollowUseCase.doUnfollow(any()) } throws mockException
            viewModel.doUnFollow(mockUserIdTarget)

            val data = robot.viewModel.profileDoUnFollowLiveData.getOrNullValue()
            assertEquals(data, null)
        }
    }
}