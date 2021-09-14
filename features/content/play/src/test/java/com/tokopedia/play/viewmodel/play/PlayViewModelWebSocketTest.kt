package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.play.data.websocket.PlayChannelWebSocket
import com.tokopedia.play.fake.FakePlayWebSocket
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.andThen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.withState
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.isEqualTo
import com.tokopedia.play.view.uimodel.recom.PinnedMessageUiModel
import com.tokopedia.play.websocket.response.PlayChatSocketResponse
import com.tokopedia.play.websocket.response.PlayPinnedMessageSocketResponse
import com.tokopedia.play.websocket.response.PlayTotalViewSocketResponse
import com.tokopedia.play.websocket.response.PlayTotalLikeSocketResponse
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
class PlayViewModelWebSocketTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    private val channelDataModelBuilder = PlayChannelDataModelBuilder()
    private val channelData = channelDataModelBuilder.buildChannelData()
    private val mapperBuilder = PlayMapperBuilder()
    private lateinit var playChannelWebSocket: PlayChannelWebSocket
    private lateinit var fakePlayWebSocket: FakePlayWebSocket

    private val testDispatcher = CoroutineTestDispatchers

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)

        fakePlayWebSocket = FakePlayWebSocket(testDispatcher)
        playChannelWebSocket = PlayChannelWebSocket(fakePlayWebSocket, mockk(relaxed = true))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when get total like data from web socket, then it should update the data`() {
        givenPlayViewModelRobot(
            playChannelWebSocket = playChannelWebSocket,
            dispatchers = testDispatcher,
            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
        ) {
            createPage(channelData)
            focusPage(channelData)
        } andThen {
            fakePlayWebSocket.fakeReceivedMessage(PlayTotalLikeSocketResponse.response)
        } thenVerify {
            withState {
                like.totalLike.isEqualTo(PlayTotalLikeSocketResponse.totalLikeFormatted)
            }
        }
    }

    @Test
    fun `when get total view data from web socket, then it should update the data`() {
        givenPlayViewModelRobot(
            playChannelWebSocket = playChannelWebSocket,
            dispatchers = testDispatcher,
            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
        ) {
            createPage(channelData)
            focusPage(channelData)
        } andThen {
            fakePlayWebSocket.fakeReceivedMessage(PlayTotalViewSocketResponse.response)
        } thenVerify {
            withState {
                totalView.isEqualTo(PlayTotalViewSocketResponse.totalViewFormatted)
            }
        }
    }

    @Test
    fun `when get message from web socket, then it should update the data`() {
        val chatModelBuilder = PlayChatModelBuilder()
        val chatListObserver: Observer<List<PlayChatUiModel>> = mockk(relaxed = true)
        every { chatListObserver.onChanged(any()) }.just(Runs)

        val mockResponse = mutableListOf<PlayChatUiModel>()
        mockResponse.add(
            chatModelBuilder.build(
                messageId = "",
                userId = PlayChatSocketResponse.userId.toString(),
                name = PlayChatSocketResponse.userName,
                message = PlayChatSocketResponse.message,
                isSelfMessage = false
            )
        )

        givenPlayViewModelRobot(
            playChannelWebSocket = playChannelWebSocket,
            dispatchers = testDispatcher,
            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
        ) {
            viewModel.observableChatList.observeForever(chatListObserver)

            createPage(channelData)
            focusPage(channelData)
        } andThen {
            fakePlayWebSocket.fakeReceivedMessage(PlayChatSocketResponse.response)
        } thenVerify {
            verify {
                chatListObserver.onChanged(mockResponse)
            }
        }
    }

    @Test
    fun `when get pinned message data from web socket, then it should update the data`() {
        val pinnedMessageModelBuilder = PlayPinnedModelBuilder()

        val pinnedMessageObserver: Observer<PinnedMessageUiModel> = mockk(relaxed = true)
        every { pinnedMessageObserver.onChanged(any()) }.just(Runs)

        val mockResponse = pinnedMessageModelBuilder.buildPinnedMessage(
            id = PlayPinnedMessageSocketResponse.pinnedMessageId.toString(),
            applink = PlayPinnedMessageSocketResponse.redirectUrl,
            title = PlayPinnedMessageSocketResponse.title
        )

        givenPlayViewModelRobot(
            playChannelWebSocket = playChannelWebSocket,
            dispatchers = testDispatcher,
            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
        ) {
            viewModel.observablePinnedMessage.observeForever(pinnedMessageObserver)

            createPage(channelData)
            focusPage(channelData)
        } andThen {
            fakePlayWebSocket.fakeReceivedMessage(PlayPinnedMessageSocketResponse.response)
        } thenVerify {
            verify {
                pinnedMessageObserver.onChanged(mockResponse)
            }
        }
    }
}