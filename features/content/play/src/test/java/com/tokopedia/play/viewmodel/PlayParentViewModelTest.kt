package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.GetChannelDetailsWithRecomUseCase
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.PlayResponseBuilder
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.robot.givenParentViewModelRobot
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
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

    private val dispatcher = TestCoroutineDispatchersProvider

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher.main)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given there are available channels, when first init, then channels can be retrieved`() {
        val mockUseCase: GetChannelDetailsWithRecomUseCase = mockk(relaxed = true)
        coEvery { mockUseCase.executeOnBackground() } returns responseBuilder.buildChannelDetailsWithRecomResponse()

        givenParentViewModelRobot(
                getChannelDetailsWithRecomUseCase = mockUseCase,
        ) {
        } thenVerify {
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
        ) {
        } thenVerify {
            channelIdResult
                    .isFailure()
                    .isEmpty()
        }
    }
}