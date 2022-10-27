package com.tokopedia.play.viewmodel.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.fake.FakePlayWebSocket
import com.tokopedia.play.fake.chat.FakeChatManager
import com.tokopedia.play.fake.chat.FakeChatStreams
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.chat.ChatManager
import com.tokopedia.play.util.chat.ChatStreams
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.websocket.response.PlayChatSocketResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on October 20, 2022
 */
class PlayChatHistoryTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    /** Model Builder */
    private val chatModelBuilder = PlayChatModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val videoBuilder = PlayVideoModelBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val commonBuilder = PlayCommonBuilder()

    /** Mock */
    private val mockDispatchers = coroutineTestRule.dispatchers
    private val mockRepo: PlayViewerRepository = mockk(relaxed = true)
    private val mockWebsocket = FakePlayWebSocket(mockDispatchers)
    private val mockChatStreams = FakeChatStreams(
        CoroutineScope(mockDispatchers.main),
        dispatchers = mockDispatchers
    )
    private val mockChatManager = FakeChatManager(mockChatStreams)
    private val mockChatStreamsFactory = object : ChatStreams.Factory {
        override fun create(scope: CoroutineScope): ChatStreams {
            return mockChatStreams
        }
    }

    private val mockChatManagerFactory = object : ChatManager.Factory {
        override fun create(chatStreams: ChatStreams): ChatManager {
            return mockChatManager
        }
    }

    /** Mock Response */
    private val mockLiveChannelData = channelDataBuilder.buildChannelData(
        channelDetail = channelInfoBuilder.buildChannelDetail(
            channelInfo = channelInfoBuilder.buildChannelInfo(channelType = PlayChannelType.Live),
        ),
        videoMetaInfo = videoBuilder.buildVideoMeta(
            videoPlayer = videoBuilder.buildCompleteGeneralVideoPlayer(),
        )
    )
    private val mockVODChannelData = channelDataBuilder.buildChannelData(
        channelDetail = channelInfoBuilder.buildChannelDetail(
            channelInfo = channelInfoBuilder.buildChannelInfo(channelType = PlayChannelType.VOD),
        ),
        videoMetaInfo = videoBuilder.buildVideoMeta(
            videoPlayer = videoBuilder.buildCompleteGeneralVideoPlayer(),
        )
    )
    private val mockChatHistoryResponse = chatModelBuilder.buildHistory()
    private val mockChatHistoryEmptyResponse = chatModelBuilder.buildHistory(size = 0)
    private val mockChatMessage1 = "pokemon"
    private val mockChatMessage2 = "digimon"
    private val mockSendChat1 = PlayChatSocketResponse.generateResponse(mockChatMessage1)
    private val mockSendChat2 = PlayChatSocketResponse.generateResponse(mockChatMessage2)
    private val mockException = commonBuilder.buildException()

    @Before
    fun setUp() {
        coEvery { mockRepo.getChatHistory(any()) } returns mockChatHistoryResponse
    }

    @Test
    fun `playChatHistory_channelNotLive_notHitChatHistory`() {
        createPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = mockDispatchers,
            chatManagerFactory = mockChatManagerFactory,
            chatStreamsFactory = mockChatStreamsFactory,
        ).use {
            it.createPage(mockVODChannelData)
            it.focusPage(mockVODChannelData)

            val chats = it.recordChatState { }

            chats.assertEqualTo(emptyList())
        }
    }

    @Test
    fun `playChatHistory_noNewChat_applyChatHistory`() {
        createPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = mockDispatchers,
            chatManagerFactory = mockChatManagerFactory,
            chatStreamsFactory = mockChatStreamsFactory,
        ).use {
            it.createPage(mockLiveChannelData)
            it.focusPage(mockLiveChannelData)

            val chats = it.recordChatState { }

            chats.assertEqualTo(mockChatHistoryResponse.chatList.reversed())
        }
    }

    @Test
    fun `playChatHistory_newChatBeforeHistory_dontApplyHistory`() {
        mockChatManager.interceptWaitingForHistory {
            mockWebsocket.fakeReceivedMessage(mockSendChat1)
        }

        createPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = mockDispatchers,
            chatManagerFactory = mockChatManagerFactory,
            chatStreamsFactory = mockChatStreamsFactory,
            playChannelWebSocket = mockWebsocket,
        ).use {
            it.createPage(mockLiveChannelData)
            it.focusPage(mockLiveChannelData)

            val chats = it.recordChatState { }

            chats.size.assertEqualTo(1)
            chats[0].message.assertEqualTo(mockChatMessage1)
        }
    }

    @Test
    fun `playChatHistory_hasPrevChat_replaceOldChatWithHistory`() {
        createPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = mockDispatchers,
            chatManagerFactory = mockChatManagerFactory,
            chatStreamsFactory = mockChatStreamsFactory,
            playChannelWebSocket = mockWebsocket,
        ).use {
            /**
             * Enter room for the first time
             */
            coEvery { mockRepo.getChatHistory(any()) } returns mockChatHistoryEmptyResponse

            it.createPage(mockLiveChannelData)
            it.focusPage(mockLiveChannelData)

            mockWebsocket.fakeReceivedMessage(mockSendChat1)
            mockWebsocket.fakeReceivedMessage(mockSendChat2)

            val chatsBefore = it.recordChatState { }

            chatsBefore.size.assertEqualTo(2)
            chatsBefore[0].message.assertEqualTo(mockChatMessage1)
            chatsBefore[1].message.assertEqualTo(mockChatMessage2)

            /**
             * Leave and re-enter room
             */
            coEvery { mockRepo.getChatHistory(any()) } returns mockChatHistoryResponse

            it.focusPage(mockLiveChannelData)

            val chatsAfter = it.recordChatState { }

            chatsAfter.assertEqualTo(mockChatHistoryResponse.chatList.reversed())
        }
    }

    @Test
    fun `playChatHistory_hasPrevChat_errorGetHistory_chatsRemain`() {
        createPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = mockDispatchers,
            chatManagerFactory = mockChatManagerFactory,
            chatStreamsFactory = mockChatStreamsFactory,
            playChannelWebSocket = mockWebsocket,
        ).use {
            /**
             * Enter room for the first time
             */
            coEvery { mockRepo.getChatHistory(any()) } returns mockChatHistoryEmptyResponse

            it.createPage(mockLiveChannelData)
            it.focusPage(mockLiveChannelData)

            mockWebsocket.fakeReceivedMessage(mockSendChat1)
            mockWebsocket.fakeReceivedMessage(mockSendChat2)

            val chatsBefore = it.recordChatState { }

            chatsBefore.size.assertEqualTo(2)
            chatsBefore[0].message.assertEqualTo(mockChatMessage1)
            chatsBefore[1].message.assertEqualTo(mockChatMessage2)

            /**
             * Leave and re-enter room
             */
            coEvery { mockRepo.getChatHistory(any()) } throws mockException

            it.focusPage(mockLiveChannelData)

            val chatsAfter = it.recordChatState { }

            chatsAfter.size.assertEqualTo(2)
            chatsAfter[0].message.assertEqualTo(mockChatMessage1)
            chatsAfter[1].message.assertEqualTo(mockChatMessage2)
        }
    }
}
