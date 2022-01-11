package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.TrackProductTagBroadcasterUseCase
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayMapperBuilder
import com.tokopedia.play.model.PlaySocketResponseBuilder
import com.tokopedia.play.robot.andThen
import com.tokopedia.play.robot.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.*
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 11/02/21
 */
class PlayViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val channelDataModelBuilder = PlayChannelDataModelBuilder()

    private val socketResponseBuilder = PlaySocketResponseBuilder()
    private val mapperBuilder = PlayMapperBuilder()

    private val testDispatcher = CoroutineTestDispatchers

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given video player instance is created, when retrieved, it should return the correct video player instance`() {
        val mockPlayerBuilder: PlayVideoWrapper.Builder = mockk(relaxed = true)
        val mockPlayer: PlayVideoWrapper = mockk(relaxed = true)
        every { mockPlayerBuilder.build() } returns mockPlayer

        givenPlayViewModelRobot(
                playVideoBuilder = mockPlayerBuilder
        ) andWhen {
            getVideoPlayer()
        } thenVerify { result ->
            result.isEqualTo(mockPlayer)
        }
    }

    @Test
    fun `given channel data is set, when retrieved, it should return same data`() {
        val channelData = channelDataModelBuilder.buildChannelData()

        givenPlayViewModelRobot {
            createPage(channelData)
        } thenVerify {
            viewModel.latestCompleteChannelData.isEqualTo(channelData)
        }
    }

    @Test
    fun `when get new product, track product should be called`() {
        val trackProductUseCase: TrackProductTagBroadcasterUseCase = mockk(relaxed = true)
        val mockSocket: PlayWebSocket = mockk(relaxed = true)
        val socketFlow = MutableStateFlow<WebSocketAction?>(null)

        var isCalled = false

        val channelData = channelDataModelBuilder.buildChannelData()

        every { mockSocket.listenAsFlow() } returns socketFlow.filterNotNull()
        coEvery { trackProductUseCase.executeOnBackground() } answers {
            isCalled = true
            true
        }

        givenPlayViewModelRobot(
                trackProductTagBroadcasterUseCase = trackProductUseCase,
                playChannelWebSocket = mockSocket,
                dispatchers = testDispatcher,
                playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
        ) {
            createPage(channelData)
            focusPage(channelData)
        } thenVerify {
            isCalled.assertFalse()
        } andThen {
            runBlockingTest(testDispatcher.coroutineDispatcher) {
                socketFlow.emit(
                        WebSocketAction.NewMessage(socketResponseBuilder.buildProductTagResponse())
                )
            }
        } thenVerify {
            isCalled.assertTrue()
        }
    }
}