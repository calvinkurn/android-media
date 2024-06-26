package com.tokopedia.play.viewmodel

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.helper.NoValueException
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.PlayResponseBuilder
import com.tokopedia.play.robot.parent.andThen
import com.tokopedia.play.robot.parent.andWhen
import com.tokopedia.play.robot.parent.givenParentViewModelRobot
import com.tokopedia.play.robot.parent.thenVerify
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.throwsException
import com.tokopedia.play.view.storage.PagingChannel
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 10/02/21
 */
class PlayParentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val responseBuilder = PlayResponseBuilder()
    private val classBuilder = ClassBuilder()
    private val mapper = classBuilder.getPlayChannelDetailsRecomMapper()

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    @Test
    fun `given there are available channels, when first init, then channels can be retrieved`() {
        val response = responseBuilder.buildChannelDetailsWithRecomResponse()
        coEvery { repo.getChannels(any(), any()) } returns PagingChannel(
            channelList = mapper.map(response, classBuilder.getMapperExtraParams()),
            cursor = response.channelDetails.meta.cursor
        )

        givenParentViewModelRobot(
            repo = repo
        ) thenVerify {
            channelIdResult
                .isSuccess()
                .isNotEmpty()
        }
    }

    @Test
    fun `given retrieving channel is error, when first init, then channels can not be retrieved`() {
        coEvery { repo.getChannels(any(), any()) } throws IllegalStateException("Channel is error")

        givenParentViewModelRobot(
            repo = repo
        ) thenVerify {
            channelIdResult
                .isFailure()
                .isEmpty()
        }
    }

    @Test
    fun `given valid user id, when retrieved, then it should be correct`() {
        val validUserId = "123"

        val mockUserSession: UserSessionInterface = mockk(relaxed = true)
        every { mockUserSession.userId } returns validUserId

        givenParentViewModelRobot(
            userSession = mockUserSession
        ) thenVerify {
            userIdResult.shouldBe(validUserId)
        }
    }

    @Test
    fun `given channel data is already stored, when retrieved existing channel id data, then it should return the correct data`() {
        val response = responseBuilder.buildChannelDetailsWithRecomResponse()
        val channelData = mapper.map(response, classBuilder.getMapperExtraParams())
        coEvery { repo.getChannels(any(), any()) } returns PagingChannel(
            channelList = channelData,
            cursor = response.channelDetails.meta.cursor
        )

        givenParentViewModelRobot(
            repo = repo
        ) thenVerify {
            channelDataResult(channelData.first().id)
                .isAvailable()
        }
    }

    @Test
    fun `given channel data is already stored, when retrieved invalid channel id data, then it should return error`() {
        val response = responseBuilder.buildChannelDetailsWithRecomResponse()
        coEvery { repo.getChannels(any(), any()) } returns PagingChannel(
            channelList = mapper.map(response, classBuilder.getMapperExtraParams()),
            cursor = response.channelDetails.meta.cursor
        )

        givenParentViewModelRobot(
            repo = repo
        ) thenVerify {
            channelDataResult("invalid channel id")
                .isError()
        }
    }

    @Test
    fun `given channel data is already stored, when data is overridden, then it should return newest data`() {
        val response = responseBuilder.buildChannelDetailsWithRecomResponse()
        val mappedData = mapper.map(response, classBuilder.getMapperExtraParams())
        coEvery { repo.getChannels(any(), any()) } returns PagingChannel(
            channelList = mappedData,
            cursor = response.channelDetails.meta.cursor
        )

        val oldData = mappedData.first()

        val newData: PlayChannelData = mockk(relaxed = true)

        givenParentViewModelRobot(
            repo = repo
        ) thenVerify {
//            channelDataResult(oldData.id)
//                    .isDataEqualTo(oldData)
            // TODO("need to find out why different even though it is identical")
        } andWhen {
            setChannelData(oldData.id, newData)
        } thenVerify {
            channelDataResult(oldData.id)
                .isDataNotEqualTo(oldData)
                .isDataEqualTo(newData)
        }
    }

    @Test
    fun `when app wants to refresh channel, it should hit get channel gql again`() {
        givenParentViewModelRobot(
            repo = repo
        ) andWhen {
            viewModel.refreshChannel()

            coVerify(exactly = 2) { repo.getChannels(any(), any()) }
        }
    }

    @Test
    fun `when app wants to set new channel param, it should load new channels if its not from pip and channelId is not empty`() {
        val mockBundle = mockk<Bundle>(relaxed = true)

        coEvery { mockBundle.get("channelId") } returns "123"
        coEvery { mockBundle.get("is_from_pip") } returns false

        givenParentViewModelRobot(
            repo = repo
        ) andWhen {
            viewModel.setNewChannelParams(mockBundle)

            coVerify(exactly = 2) { repo.getChannels(any(), any()) }
        }
    }

    private val preference: PlayPreference = mockk(relaxed = true)
    private val remoteConfig: RemoteConfig = mockk(relaxed = true)
    private val dispatchers = CoroutineTestDispatchers

    @Test
    fun `when it's first page and user allowed to see onboarding and hasnt shown onboarding before, should show onboarding`() {
        val isFirstPage = true

        coEvery { remoteConfig.getBoolean(any(), any()) } returns true
        coEvery { preference.isOnBoardingHasBeenShown() } returns false

        givenParentViewModelRobot(
            dispatchers = dispatchers,
            preference = preference,
            remoteConfig = remoteConfig
        ) andWhen {
            viewModel.setupOnBoarding(isFirstPage)
        } andThen {
            dispatchers.coroutineDispatcher.advanceTimeBy(2000)
            viewModel.observableOnBoarding.getOrAwaitValue().peekContent().assertEqualTo(Unit)
        }
    }

    @Test
    fun `when it's first page and user allowed to see onboarding and has shown onboarding before, should not show onboarding`() {
        val isFirstPage = true

        coEvery { remoteConfig.getBoolean(any(), any()) } returns true
        coEvery { preference.isOnBoardingHasBeenShown() } returns true

        givenParentViewModelRobot(
            preference = preference,
            remoteConfig = remoteConfig
        ) andWhen {
            viewModel.setupOnBoarding(isFirstPage)
        } andThen {
            throwsException<NoValueException> {
                viewModel.observableOnBoarding.getOrAwaitValue()
            }
        }
    }

    @Test
    fun `when it's first page and user not allowed to see onboarding, should not show onboarding`() {
        val isFirstPage = true

        coEvery { preference.isOnBoardingHasBeenShown() } returns true
        coEvery { remoteConfig.getBoolean(any(), any()) } returns false

        givenParentViewModelRobot(
            preference = preference,
            remoteConfig = remoteConfig
        ) andWhen {
            viewModel.setupOnBoarding(isFirstPage)
        } andThen {
            throwsException<NoValueException> {
                viewModel.observableOnBoarding.getOrAwaitValue()
            }
        }
    }

    @Test
    fun `when it isn't first page and user allowed to see onboarding then should not show onboarding`() {
        val isFirstPage = false

        coEvery { remoteConfig.getBoolean(any(), any()) } returns true

        givenParentViewModelRobot(
            remoteConfig = remoteConfig
        ) andWhen {
            viewModel.setupOnBoarding(isFirstPage)
        } andThen {
            throwsException<NoValueException> {
                viewModel.observableOnBoarding.getOrAwaitValue()
            }
        }
    }

    @Test
    fun `when it isn't first page and user not allowed to see onboarding then should not show onboarding`() {
        val isFirstPage = false

        coEvery { remoteConfig.getBoolean(any(), any()) } returns false

        givenParentViewModelRobot(
            remoteConfig = remoteConfig
        ) andWhen {
            viewModel.setupOnBoarding(isFirstPage)
        } andThen {
            throwsException<NoValueException> {
                viewModel.observableOnBoarding.getOrAwaitValue()
            }
        }
    }
}
