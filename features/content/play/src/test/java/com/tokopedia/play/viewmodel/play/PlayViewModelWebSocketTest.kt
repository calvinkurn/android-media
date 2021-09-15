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
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play.websocket.response.*
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.fail

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

    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
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
    fun `when get multiple new chat from web socket, then the latest chat should be emitted`() {
        val message1 = "Message 1"
        val message2 = "Message 2"

        givenPlayViewModelRobot(
            playChannelWebSocket = playChannelWebSocket,
            dispatchers = testDispatcher,
            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
        ) {
            createPage(channelData)
            focusPage(channelData)
        } andThen {
            fakePlayWebSocket.fakeReceivedMessage(PlayChatSocketResponse.generateResponse(message1))
            fakePlayWebSocket.fakeReceivedMessage(PlayChatSocketResponse.generateResponse(message2))
        } thenVerify {
            viewModel.observableNewChat.value?.peekContent()?.message?.isEqualTo(message2)
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
            verifySequence {
                pinnedMessageObserver.onChanged(channelData.pinnedInfo.pinnedMessage)
                pinnedMessageObserver.onChanged(mockResponse)
            }
        }
    }

    @Test
    fun `when get quick reply data from web socket, then it should update the data`() {
        val quickReplyModelBuilder = PlayQuickReplyModelBuilder()
        every { mockUserSession.userId } returns "1"
        val quickReplyObserver: Observer<PlayQuickReplyInfoUiModel> = mockk(relaxed = true)
        every { quickReplyObserver.onChanged(any()) }.just(Runs)

        val mockResponse = quickReplyModelBuilder.build(
            quickReplyList = PlayQuickReplySocketResponse.quickReplyList
        )

        givenPlayViewModelRobot(
            playChannelWebSocket = playChannelWebSocket,
            dispatchers = testDispatcher,
            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
        ) {
            viewModel.observableQuickReply.observeForever(quickReplyObserver)

            createPage(channelData)
            focusPage(channelData)
        } andThen {
            fakePlayWebSocket.fakeReceivedMessage(PlayQuickReplySocketResponse.response)
        } thenVerify {
            verifySequence {
                quickReplyObserver.onChanged(channelData.quickReplyInfo)
                quickReplyObserver.onChanged(mockResponse)
            }
        }
    }

    @Test
    fun `when get banned status from web socket, then it should update the data`() {
        val statusInfoModelBuilder = PlayStatusInfoModelBuilder()

        val statusInfoObserver: Observer<PlayStatusInfoUiModel> = mockk(relaxed = true)
        every { statusInfoObserver.onChanged(any()) }.just(Runs)

        every { mockUserSession.userId } returns "1"

        val mockResponse = statusInfoModelBuilder.build(
            statusType = PlayStatusType.Banned
        )

        givenPlayViewModelRobot(
            playChannelWebSocket = playChannelWebSocket,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
        ) {
            viewModel.observableStatusInfo.observeForever(statusInfoObserver)

            createPage(channelData)
            focusPage(channelData)
        } andThen {
            fakePlayWebSocket.fakeReceivedMessage(PlayBannedSocketResponse.generateResponse())
        } thenVerify {
            verifySequence {
                statusInfoObserver.onChanged(channelData.statusInfo)
                statusInfoObserver.onChanged(mockResponse)
            }
        }
    }

    @Test
    fun `when get banned status from web socket with different channelId than current channelId, then no new data should be emitted`() {
        val statusInfoObserver: Observer<PlayStatusInfoUiModel> = mockk(relaxed = true)
        every { statusInfoObserver.onChanged(any()) }.just(Runs)

        every { mockUserSession.userId } returns "1"

        givenPlayViewModelRobot(
            playChannelWebSocket = playChannelWebSocket,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
        ) {
            viewModel.observableStatusInfo.observeForever(statusInfoObserver)

            createPage(channelData)
            focusPage(channelData)
        } andThen {
            fakePlayWebSocket.fakeReceivedMessage(PlayBannedSocketResponse.generateResponse(channelId = "2"))
        } thenVerify {
            verifySequence {
                statusInfoObserver.onChanged(channelData.statusInfo)
            }
        }
    }

    @Test
    fun `when get banned status from web socket with different userId than current userId, then it should not update the data to banned`() {
        val statusInfoObserver: Observer<PlayStatusInfoUiModel> = mockk(relaxed = true)
        every { statusInfoObserver.onChanged(any()) }.just(Runs)

        every { mockUserSession.userId } returns "1"

        givenPlayViewModelRobot(
            playChannelWebSocket = playChannelWebSocket,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
        ) {
            viewModel.observableStatusInfo.observeForever(statusInfoObserver)

            createPage(channelData)
            focusPage(channelData)
        } andThen {
            fakePlayWebSocket.fakeReceivedMessage(PlayBannedSocketResponse.generateResponse(userId = "2"))
        } thenVerify {
            verifySequence {
                statusInfoObserver.onChanged(channelData.statusInfo)
                statusInfoObserver.onChanged(channelData.statusInfo)
            }
        }
    }

    @Test
    fun `when get product tag from web socket, then it should update the current pinned product`() {
        val pinnedModelBuilder = PlayPinnedModelBuilder()
        val productTagModelBuilder = PlayProductTagsModelBuilder()

        val pinnedProductObserver: Observer<PinnedProductUiModel> = mockk(relaxed = true)
        every { pinnedProductObserver.onChanged(any()) }.just(Runs)

        val mockName = "Product Name Test"
        val mockSize = 3

        val mockProduct = productTagModelBuilder.buildCompleteData(
            productList = List(mockSize) { productTagModelBuilder.buildProductLine(title = mockName) },
        )
        val mockData = pinnedModelBuilder.buildPinnedProduct(
            productTags = mockProduct
        )

        givenPlayViewModelRobot(
            playChannelWebSocket = playChannelWebSocket,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
        ) {
            viewModel.observablePinnedProduct.observeForever(pinnedProductObserver)

            createPage(channelData)
            focusPage(channelData)
        } andThen {
            fakePlayWebSocket.fakeReceivedMessage(PlayProductTagSocketResponse.generateResponse(size = 3, title = mockName))
        } thenVerify {
            when(val productTag = viewModel.observablePinnedProduct.value?.productTags) {
                is PlayProductTagsUiModel.Complete -> {
                    val mockSize = (mockData.productTags as PlayProductTagsUiModel.Complete).productList.size
                    productTag.productList.size.isEqualTo(mockSize)

                    for(i in 0 until mockSize) {
                        val counter = i+1
                        (productTag.productList[i] as PlayProductUiModel.Product).title.isEqualTo("$mockName $counter")
                        (productTag.productList[i] as PlayProductUiModel.Product).id.isEqualTo("$counter")
                    }
                }
                else -> {
                    fail(Exception("Model should be PlayProductTagsUiModel.Complete"))
                }
            }
        }
    }
}