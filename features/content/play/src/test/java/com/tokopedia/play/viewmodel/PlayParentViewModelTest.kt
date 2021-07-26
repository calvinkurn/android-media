package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.GetChannelDetailsWithRecomUseCase
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.model.PlayResponseBuilder
import com.tokopedia.play.robot.parent.andWhen
import com.tokopedia.play.robot.parent.givenParentViewModelRobot
import com.tokopedia.play.robot.parent.thenVerify
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import java.lang.IllegalStateException

/**
 * Created by jegul on 10/02/21
 */
class PlayParentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val responseBuilder = PlayResponseBuilder()
    private val classBuilder = ClassBuilder()
    private val mapper = classBuilder.getPlayChannelDetailsRecomMapper()

    @Test
    fun `given there are available channels, when first init, then channels can be retrieved`() {
        val mockUseCase: GetChannelDetailsWithRecomUseCase = mockk(relaxed = true)
        coEvery { mockUseCase.executeOnBackground() } returns responseBuilder.buildChannelDetailsWithRecomResponse()

        givenParentViewModelRobot(
                getChannelDetailsWithRecomUseCase = mockUseCase,
        ) thenVerify {
            channelIdResult
                    .isSuccess()
                    .isNotEmpty()
        }
    }

    @Test
    fun `given retrieving channel is error, when first init, then channels can not be retrieved`() {
        val mockUseCase: GetChannelDetailsWithRecomUseCase = mockk(relaxed = true)
        coEvery { mockUseCase.executeOnBackground() } throws IllegalStateException("Channel is error")

        givenParentViewModelRobot(
                getChannelDetailsWithRecomUseCase = mockUseCase,
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
        val mockResponse = responseBuilder.buildChannelDetailsWithRecomResponse()

        val mockUseCase: GetChannelDetailsWithRecomUseCase = mockk(relaxed = true)
        coEvery { mockUseCase.executeOnBackground() } returns mockResponse

        val mappedData = mapper.map(mockResponse, classBuilder.getMapperExtraParams())

        givenParentViewModelRobot(
                getChannelDetailsWithRecomUseCase = mockUseCase
        ) thenVerify {
            channelDataResult(mappedData.first().id)
                    .isAvailable()
        }
    }

    @Test
    fun `given channel data is already stored, when retrieved invalid channel id data, then it should return error`() {
        val mockResponse = responseBuilder.buildChannelDetailsWithRecomResponse()

        val mockUseCase: GetChannelDetailsWithRecomUseCase = mockk(relaxed = true)
        coEvery { mockUseCase.executeOnBackground() } returns mockResponse

        givenParentViewModelRobot(
                getChannelDetailsWithRecomUseCase = mockUseCase
        ) thenVerify {
            channelDataResult("invalid channel id")
                    .isError()
        }
    }

    @Test
    fun `given channel data is already stored, when data is overridden, then it should return newest data`() {
        val mockResponse = responseBuilder.buildChannelDetailsWithRecomResponse()

        val mockUseCase: GetChannelDetailsWithRecomUseCase = mockk(relaxed = true)
        coEvery { mockUseCase.executeOnBackground() } returns mockResponse

        val mappedData = mapper.map(mockResponse, classBuilder.getMapperExtraParams())
        val oldData = mappedData.first()

        val newData: PlayChannelData = mockk(relaxed = true)

        givenParentViewModelRobot(
                getChannelDetailsWithRecomUseCase = mockUseCase
        ) thenVerify {
            channelDataResult(oldData.id)
                    .isDataEqualTo(oldData)
        } andWhen {
            setChannelData(oldData.id, newData)
        } thenVerify {
            channelDataResult(oldData.id)
                    .isDataNotEqualTo(oldData)
                    .isDataEqualTo(newData)
        }
    }
}