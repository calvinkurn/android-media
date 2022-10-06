package com.tokopedia.chatbot.view.presenter

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chatbot.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.data.SocketResponse
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.imageupload.ChatbotUploadImagePojo
import com.tokopedia.chatbot.data.newsession.TopBotNewSessionResponse
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.data.uploadsecure.CheckUploadSecureResponse
import com.tokopedia.chatbot.domain.mapper.ChatBotWebSocketMessageMapper
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListInput
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.domain.socket.ChatbotSendableWebSocketParam
import com.tokopedia.chatbot.domain.subscriber.SendRatingReasonSubscriber
import com.tokopedia.chatbot.domain.subscriber.SendRatingSubscriber
import com.tokopedia.chatbot.domain.usecase.ChatBotSecureImageUploadUseCase
import com.tokopedia.chatbot.domain.usecase.CheckUploadSecureUseCase
import com.tokopedia.chatbot.domain.usecase.ChipGetChatRatingListUseCase
import com.tokopedia.chatbot.domain.usecase.ChipSubmitChatCsatUseCase
import com.tokopedia.chatbot.domain.usecase.ChipSubmitHelpfulQuestionsUseCase
import com.tokopedia.chatbot.domain.usecase.GetExistingChatUseCase
import com.tokopedia.chatbot.domain.usecase.GetResolutionLinkUseCase
import com.tokopedia.chatbot.domain.usecase.GetTickerDataUseCase
import com.tokopedia.chatbot.domain.usecase.GetTopBotNewSessionUseCase
import com.tokopedia.chatbot.domain.usecase.LeaveQueueUseCase
import com.tokopedia.chatbot.domain.usecase.SendChatRatingUseCase
import com.tokopedia.chatbot.domain.usecase.SendRatingReasonUseCase
import com.tokopedia.chatbot.domain.usecase.SubmitCsatRatingUseCase
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.chatbot.websocket.ChatbotWebSocket
import com.tokopedia.chatbot.websocket.ChatbotWebSocketAction
import com.tokopedia.chatbot.websocket.ChatbotWebSocketException
import com.tokopedia.chatbot.websocket.ChatbotWebSocketStateHandler
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ChatbotPresenterTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var testRule = CoroutineTestRule()

    private lateinit var getExistingChatUseCase: GetExistingChatUseCase
    private lateinit var userSession: UserSessionInterface
    private lateinit var chatBotWebSocketMessageMapper: ChatBotWebSocketMessageMapper
    private lateinit var tkpdAuthInterceptor: TkpdAuthInterceptor
    private lateinit var fingerprintInterceptor: FingerprintInterceptor
    private lateinit var sendChatRatingUseCase: SendChatRatingUseCase
    private lateinit var sendRatingReasonUseCase: SendRatingReasonUseCase
    private lateinit var uploadImageUseCase: UploadImageUseCase<ChatbotUploadImagePojo>
    private lateinit var submitCsatRatingUseCase: SubmitCsatRatingUseCase
    private lateinit var leaveQueueUseCase: LeaveQueueUseCase
    private lateinit var getTickerDataUseCase: GetTickerDataUseCase
    private lateinit var chipSubmitHelpfulQuestionsUseCase: ChipSubmitHelpfulQuestionsUseCase
    private lateinit var chipGetChatRatingListUseCase: ChipGetChatRatingListUseCase
    private lateinit var chipSubmitChatCsatUseCase: ChipSubmitChatCsatUseCase
    private lateinit var getResolutionLinkUseCase: GetResolutionLinkUseCase
    private lateinit var getTopBotNewSessionUseCase: GetTopBotNewSessionUseCase
    private lateinit var checkUploadSecureUseCase: CheckUploadSecureUseCase
    private lateinit var chatBotSecureImageUploadUseCase: ChatBotSecureImageUploadUseCase
    private lateinit var getExistingChatMapper: ChatbotGetExistingChatMapper
    private lateinit var chatbotWebSocket: ChatbotWebSocket
    private lateinit var chatbotWebSocketStateHandler: ChatbotWebSocketStateHandler
    private lateinit var dispatcher: CoroutineDispatchers
    private var socketJob: Job? = null

    private lateinit var presenter: ChatbotPresenter
    private lateinit var view: ChatbotContract.View

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        getExistingChatUseCase = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        chatBotWebSocketMessageMapper = mockk(relaxed = true)
        tkpdAuthInterceptor = mockk(relaxed = true)
        fingerprintInterceptor = mockk(relaxed = true)
        sendChatRatingUseCase = mockk(relaxed = true)
        sendRatingReasonUseCase = mockk(relaxed = true)
        uploadImageUseCase = mockk(relaxed = true)
        submitCsatRatingUseCase = mockk(relaxed = true)
        leaveQueueUseCase = mockk(relaxed = true)
        getTickerDataUseCase = mockk(relaxed = true)
        chipSubmitHelpfulQuestionsUseCase = mockk(relaxed = true)
        chipGetChatRatingListUseCase = mockk(relaxed = true)
        chipSubmitChatCsatUseCase = mockk(relaxed = true)
        getResolutionLinkUseCase = mockk(relaxed = true)
        getTopBotNewSessionUseCase = mockk(relaxed = true)
        checkUploadSecureUseCase = mockk(relaxed = true)
        chatBotSecureImageUploadUseCase = mockk(relaxed = true)
        getExistingChatMapper = mockk(relaxed = true)
        chatbotWebSocket = mockk(relaxed = true)
        chatbotWebSocketStateHandler = mockk(relaxed = true)
        dispatcher = testRule.dispatchers

        presenter = spyk(
            ChatbotPresenter(
                getExistingChatUseCase,
                userSession,
                chatBotWebSocketMessageMapper,
                tkpdAuthInterceptor,
                fingerprintInterceptor,
                sendChatRatingUseCase,
                sendRatingReasonUseCase,
                uploadImageUseCase,
                submitCsatRatingUseCase,
                leaveQueueUseCase,
                getTickerDataUseCase,
                chipSubmitHelpfulQuestionsUseCase,
                chipGetChatRatingListUseCase,
                chipSubmitChatCsatUseCase,
                getResolutionLinkUseCase,
                getTopBotNewSessionUseCase,
                checkUploadSecureUseCase,
                chatBotSecureImageUploadUseCase,
                getExistingChatMapper,
                chatbotWebSocket,
                chatbotWebSocketStateHandler,
                dispatcher
            )
        )

        view = mockk(relaxed = true)
        presenter.attachView(view)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `connectWebSocket success when Socket is Opened`() {
        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.SocketOpened
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        coEvery { chatbotWebSocket.connect("aaa") } just runs

        presenter.connectWebSocket("123")

        assertNotNull(socketJob)
    }

    @Test
    fun `connectWebSocket - when Socket is Closed`() {
        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.Closed(1000)
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        coEvery { chatbotWebSocket.connect("aaa") } just runs

        presenter.connectWebSocket("123")

        assertNotNull(socketJob)
    }

    @Test
    fun `connectWebSocket - when Socket is Closed with code other than 1000`() {
        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.Closed(126)
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        coEvery { chatbotWebSocket.connect("aaa") } just runs

        presenter.connectWebSocket("123")

        assertNotNull(socketJob)
    }

    @Test
    fun `connectWebSocket - when Socket connection is Failure`() {
        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.Failure(ChatbotWebSocketException(Exception()))
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        coEvery { chatbotWebSocket.connect("aaa") } just runs

        presenter.connectWebSocket("123")

        assertNotNull(socketJob)
    }

    @Test
    fun `connectWebSocket - when Socket receives new Message with code 103 `() {
        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.RESPONSE_WITH_103_REPLY_MESSAGE)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        coEvery { chatbotWebSocket.connect("aaa") } just runs

        presenter.connectWebSocket("4058088")

        assertNotNull(socketJob)
    }

    @Test
    fun `connectWebSocket - when Socket receives new Message with code 204 `() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.RESPONSE_WITH_204_END_TYPING)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                fullResponse
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        coEvery { chatbotWebSocket.connect("aaa") } just runs

        presenter.connectWebSocket("4058088")

        assertNotNull(socketJob)
    }

    @Test
    fun `handleAttachment When receiving Code 103 for Reply Message`() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.RESPONSE_WITH_103_REPLY_MESSAGE)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.RESPONSE_WITH_103_REPLY_MESSAGE)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        presenter.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
    }

    @Test
    fun `handleAttachment When receiving Code 203 for Start Typing`() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.RESPONSE_WITH_203_START_TYPING)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.RESPONSE_WITH_203_START_TYPING)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        presenter.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
    }

    @Test
    fun `handleAttachment When receiving Code 301 for Read Message`() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.RESPONSE_WITH_301_READ_MESSAGE)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.RESPONSE_WITH_301_READ_MESSAGE)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        presenter.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
    }

    @Test
    fun `handleAttachment When receiving Code 204 for End Typing`() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.RESPONSE_WITH_204_END_TYPING)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.RESPONSE_WITH_204_END_TYPING)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        presenter.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
    }

    @Test
    fun `handleAttachment When receiving attachment type 13 to open csat`() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.ATTACHMENT_13_OPEN_CSAT)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.ATTACHMENT_13_OPEN_CSAT)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        presenter.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
    }

    @Test
    fun `handleAttachment When receiving attachment type 14 to update the toolbar`() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.ATTACHMENT_14_UPDATE_TOOLBAR)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.ATTACHMENT_14_UPDATE_TOOLBAR)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        presenter.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
    }

    @Test
    fun `handleAttachment When receiving attachment type 15 with chat divider`() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.ATTACHMENT_15_CHAT_DIVIDER)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.ATTACHMENT_15_CHAT_DIVIDER)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        presenter.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
    }

    @Test
    fun `handleAttachment When receiving attachment type 31 for session Change with Mode Agent`() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.ATTACHMENT_31_SESSION_CHANGE_WITH_MODE_AGENT)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.ATTACHMENT_31_SESSION_CHANGE_WITH_MODE_AGENT)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        presenter.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
    }

    @Test
    fun `handleAttachment When receiving attachment type 31 for session Change with Mode Bot`() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.ATTACHMENT_31_SESSION_CHANGE_WITH_MODE_BOT)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.ATTACHMENT_31_SESSION_CHANGE_WITH_MODE_BOT)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        presenter.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
    }

    @Test
    fun `destroyWebSocket success`() {
        every {
            chatbotWebSocket.close()
        } just runs

        presenter.destroyWebSocket()

        verify {
            chatbotWebSocket.close()
        }
    }

    @Test
    fun `sendMessage without parent reply`() {
        mockkObject(ChatbotSendableWebSocketParam)

        every {
            ChatbotSendableWebSocketParam.generateParamSendMessage(any(), any(), any(), any())
        } returns mockk(relaxed = true)

        every {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendMessage(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        } just runs

        presenter.sendMessage("", "123", "", "", null) {}

        verify {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendMessage(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        }
    }

    @Test
    fun `sendMessage with parent reply`() {
        mockkObject(ChatbotSendableWebSocketParam)

        every {
            ChatbotSendableWebSocketParam.generateParamSendMessageWithReplyBubble(
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        every {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendMessageWithReplyBubble(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        } just runs

        presenter.sendMessage("", "123", "", "", ParentReply()) {}

        verify {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendMessageWithReplyBubble(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        }
    }

    @Test
    fun `getBottomChat success when chat ratings list is not empty`() {
        val expectedResponse = GetExistingChatPojo()
        val chatroomViewModel = getExistingChatMapper.map(expectedResponse)
        var chatRatingListInput = ChipGetChatRatingListInput(mutableListOf(mockk(relaxed = true)))

        coEvery {
            getExistingChatUseCase.getBottomChat(any())
        } returns expectedResponse

        every {
            getExistingChatMapper.map(expectedResponse)
        } returns chatroomViewModel

        every {
            presenter.getChatRatingData(any())
        } returns chatRatingListInput

        coEvery {
            presenter.getChatRatingList(any(), any(), any())
        }

        presenter.getBottomChat("123456", { viewModel, chatReplies -> }, {}, {})

        verify {
            presenter.getChatRatingList(chatRatingListInput, any(), any())
        }
    }

    @Test
    fun `getBottomChat success when chat ratings list is empty`() {
        val expectedResponse = GetExistingChatPojo()
        var chatroomViewModel = ChatroomViewModel(mockk())
        val chatRatingListInput = ChipGetChatRatingListInput(mutableListOf(mockk(relaxed = true)))

        coEvery {
            getExistingChatUseCase.getBottomChat(any())
        } returns expectedResponse

        every {
            getExistingChatMapper.map(expectedResponse)
        } returns chatroomViewModel

        every {
            presenter.getChatRatingData(any())
        } returns chatRatingListInput

        presenter.getBottomChat("123456", { viewModel, chatReplies ->
            chatroomViewModel = viewModel!!
        }, {}, {})

        Assert.assertEquals(
            chatroomViewModel,
            getExistingChatMapper.map(expectedResponse)
        )
    }

    @Test
    fun `getBottomChat failure`() {
        val exception = mockk<Exception>()
        var result: Throwable? = null

        coEvery {
            getExistingChatUseCase.getBottomChat(any())
        } throws exception

        presenter.getBottomChat("123", { chatroomViewModel, chatReplies -> }, { exception ->
            result = exception
        }, {})

        Assert.assertEquals(
            exception,
            (result as Exception)
        )
    }

    @Test
    fun `getTopChat when inputList is not empty success`() {
        val expectedResponse = GetExistingChatPojo()
        val chatroomViewModel = getExistingChatMapper.map(expectedResponse)
        var chatRatingListInput = ChipGetChatRatingListInput(mutableListOf(mockk(relaxed = true)))

        coEvery {
            getExistingChatUseCase.getTopChat(any())
        } returns expectedResponse

        every {
            getExistingChatMapper.map(expectedResponse)
        } returns chatroomViewModel

        every {
            presenter.getChatRatingData(any())
        } returns chatRatingListInput

        coEvery {
            presenter.getChatRatingList(any(), any(), any())
        }

        // When
        presenter.getTopChat("123456", { viewModel, chatReplies -> }, {}, {})

        verify {
            presenter.getChatRatingList(chatRatingListInput, any(), any())
        }
    }

    @Test
    fun `getTopChat when inputList is empty success`() {
        val expectedResponse = GetExistingChatPojo()
        var chatroomViewModel = ChatroomViewModel(mockk())
        val chatRatingListInput = ChipGetChatRatingListInput(mutableListOf(mockk(relaxed = true)))

        coEvery {
            getExistingChatUseCase.getTopChat(any())
        } returns expectedResponse

        every {
            getExistingChatMapper.map(expectedResponse)
        } returns chatroomViewModel

        every {
            presenter.getChatRatingData(any())
        } returns chatRatingListInput

        presenter.getTopChat("123456", { viewModel, chatReplies ->
            chatroomViewModel = viewModel
        }, {}, {})

        Assert.assertEquals(
            chatroomViewModel,
            getExistingChatMapper.map(expectedResponse)
        )
    }

    @Test
    fun `getTopChat throws exception - failure`() {
        val exception = mockk<Exception>()
        var result: Throwable? = null

        coEvery {
            getExistingChatUseCase.getTopChat(any())
        } throws exception

        presenter.getTopChat("123", { chatroomViewModel, chatReplies -> }, { exception ->
            result = exception
        }, {})

        Assert.assertEquals(
            exception,
            (result as Exception)
        )
    }

    @Test
    fun `getExistingChat does not get called if messageId is empty`() {
        coEvery {
            getExistingChatUseCase.getFirstPageChat(any())
        }

        presenter.getExistingChat("", {}, { chatroomViewModel, chatReplies -> }, {})

        verify(exactly = 0) {
            presenter.getChatRatingData(any())
        }
    }

    @Test
    fun `getExistingChat - when input List is empty calls getChatRatingList - Success`() {
        val expectedResponse = GetExistingChatPojo()
        val chatroomViewModel = getExistingChatMapper.map(expectedResponse)
        var chatRatingListInput = ChipGetChatRatingListInput(mutableListOf(mockk(relaxed = true)))

        coEvery {
            getExistingChatUseCase.getFirstPageChat(any())
        } returns expectedResponse

        every {
            getExistingChatMapper.map(expectedResponse)
        } returns chatroomViewModel

        every {
            presenter.getChatRatingData(any())
        } returns chatRatingListInput

        coEvery {
            presenter.getChatRatingList(any(), any(), any())
        }

        // When
        presenter.getExistingChat("123456", { }, { viewModel, chatReplies -> }, {})

        verify {
            presenter.getChatRatingList(chatRatingListInput, any(), any())
        }
    }

    @Test
    fun `getExistingChat Failure`() {
        val exception = mockk<Exception>()
        var result: Throwable? = null

        coEvery {
            getExistingChatUseCase.getFirstPageChat(any())
        } throws exception

        presenter.getExistingChat("123", { exception ->
            result = exception
        }, { chatroomViewModel, chatReplies -> }, {})

        Assert.assertEquals(
            exception,
            (result as Exception)
        )
    }

    @Test
    fun `getChatRatingList success`() {
        val response = mockk<GraphqlResponse>(relaxed = true)
        val ratingListResponse = ChipGetChatRatingListResponse(mockk())
        var expectedChatRatingList =
            mockk<ChipGetChatRatingListResponse.ChipGetChatRatingList>(relaxed = true)

        coEvery {
            chipGetChatRatingListUseCase.getChatRatingList(any())
        } returns response

        every {
            response.getData<ChipGetChatRatingListResponse>(ChipGetChatRatingListResponse::class.java)
        } returns ratingListResponse

        presenter.getChatRatingList(ChipGetChatRatingListInput(), "123456") { chipGetChatRatingList ->
            expectedChatRatingList = chipGetChatRatingList!!
        }

        Assert.assertEquals(
            expectedChatRatingList,
            ratingListResponse.chipGetChatRatingList
        )
    }

    @Test
    fun `getChatRatingList failure`() {
        val exception = mockk<Exception>()
        coEvery {
            chipGetChatRatingListUseCase.getChatRatingList(any())
        } throws exception

        presenter.getChatRatingList(ChipGetChatRatingListInput(),"123456", {})

        verify {
            exception.printStackTrace()
        }
    }

    @Test
    fun `clearGetChatUseCase success`() {
        val expectedMinReplyTime = ""

        every {
            getExistingChatUseCase.reset()
        } just runs

        presenter.clearGetChatUseCase()

        assertEquals(
            expectedMinReplyTime,
            ""
        )
    }

    @Test
    fun `setBeforeReplyTime success`() {
        val expectedBeforeReplyTime = "123"

        every {
            getExistingChatUseCase.updateMinReplyTime(any())
        } just runs

        presenter.setBeforeReplyTime("123")

        assertEquals(
            expectedBeforeReplyTime,
            "123"
        )
    }

    @Test
    fun `checkUploadSecure is true run uploadUsingSecureUpload`() {
        val response = mockk<CheckUploadSecureResponse>(relaxed = true)

        coEvery {
            checkUploadSecureUseCase.checkUploadSecure(any())
        } returns response

        coEvery {
            response.topbotUploadSecureAvailability.uploadSecureAvailabilityData.isUsingUploadSecure
        } returns true

        // replace mockk
        presenter.checkUploadSecure(" ", Intent())

        verify {
            view.uploadUsingSecureUpload(any())
        }
    }

    @Test
    fun `checkUploadSecure is false run uploadUsingOldMechanism`() {
        val response = mockk<CheckUploadSecureResponse>(relaxed = true)

        coEvery {
            checkUploadSecureUseCase.checkUploadSecure(any())
        } returns response

        coEvery {
            response.topbotUploadSecureAvailability.uploadSecureAvailabilityData.isUsingUploadSecure
        } returns false

        presenter.checkUploadSecure(" ", Intent())

        verify {
            view.uploadUsingOldMechanism(any())
        }
    }

    @Test
    fun `checkUploadSecure returns exception`() {
        coEvery {
            checkUploadSecureUseCase.checkUploadSecure(any())
        } answers {
            throw mockThrowable
        }

        presenter.checkUploadSecure("", Intent())

        verify {
            view.loadChatHistory()
        }
    }

    @Test
    fun `checkForSession when new session`() {
        val response = mockk<TopBotNewSessionResponse>(relaxed = true)

        coEvery {
            getTopBotNewSessionUseCase.getTobBotUserSession(any())
        } returns response

        coEvery {
            response.topBotGetNewSession.isNewSession
        } returns true

        presenter.checkForSession("")

        verify {
            view.startNewSession()
        }
    }

    @Test
    fun `checkForSession when existing session`() {
        val response = mockk<TopBotNewSessionResponse>(relaxed = true)

        coEvery {
            getTopBotNewSessionUseCase.getTobBotUserSession(any())
        } returns response

        coEvery {
            response.topBotGetNewSession.isNewSession
        } returns false

        presenter.checkForSession("")

        verify {
            view.loadChatHistory()
        }
    }

    @Test
    fun `sendRating success`() {
        val params = mapOf<String, Any>()
        mockkConstructor(SendRatingSubscriber::class)
        mockkObject(SendChatRatingUseCase)

        every {
            SendChatRatingUseCase.generateParam(
                any(),
                any(),
                any()
            )
        } returns params

        every {
            sendChatRatingUseCase.execute(any(), any())
        } just runs

        presenter.sendRating("", 1, "", {}, {})

        verify {
            sendChatRatingUseCase.execute(any(), any())
        }
    }

    @Test
    fun `submitChatCsat success`() {
        mockkConstructor(SendRatingReasonSubscriber::class)
        mockkObject(SendRatingReasonUseCase)

        every {
            chipSubmitChatCsatUseCase.execute(any(), any())
        } just runs

        presenter.submitChatCsat("123456", ChipSubmitChatCsatInput(), {}, {})

        verify {
            chipSubmitChatCsatUseCase.execute(any(), any())
        }
    }

    @Test
    fun `sendReasonRating success`() {
        val params = mapOf<String, Any>()
        mockkConstructor(SendRatingReasonSubscriber::class)
        mockkObject(SendRatingReasonUseCase)

        every {
            SendRatingReasonUseCase.generateParam(
                any(),
                any(),
                any()
            )
        } returns params

        every {
            sendRatingReasonUseCase.execute(any(), any())
        } just runs

        presenter.sendReasonRating("", "", "", {}, {})

        verify {
            sendRatingReasonUseCase.execute(any(), any())
        }
    }

    /******************************* Socket Related Unit Tests************************************/

    @Test
    fun `sendActionBubble success`() {
        mockkObject(ChatbotSendableWebSocketParam)

        every {
            ChatbotSendableWebSocketParam.generateParamSendBubbleAction(
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        every {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendBubbleAction(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        } just runs

        presenter.sendActionBubble("", ChatActionBubbleViewModel(), "", "")

        verify {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendBubbleAction(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        }
    }

    @Test
    fun `sendQuickReplyInvoice success`() {
        mockkObject(ChatbotSendableWebSocketParam)

        every {
            ChatbotSendableWebSocketParam.generateParamSendQuickReplyEventArticle(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        every {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendQuickReplyEventArticle(
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        } just runs

        presenter.sendQuickReplyInvoice("123", QuickReplyViewModel("", "", ""), "", "", "", "")

        verify {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendQuickReplyEventArticle(
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        }
    }

    @Test
    fun `sendQuickReply success`() {
        mockkObject(ChatbotSendableWebSocketParam)

        every {
            ChatbotSendableWebSocketParam.generateParamSendQuickReply(
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        every {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendQuickReply(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        } just runs

        presenter.sendQuickReply("123", QuickReplyViewModel("", "", ""), "", "")

        verify {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendQuickReply(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        }
    }

    @Test
    fun `sendInvoiceAttachment success when it is article Entry`() {
        mockkObject(ChatbotSendableWebSocketParam)

        every {
            ChatbotSendableWebSocketParam.generateParamInvoiceSendByArticle(
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        every {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamInvoiceSendByArticle(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        } just runs

        presenter.sendInvoiceAttachment("123", InvoiceLinkPojo(), "", "", true, "")

        verify {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamInvoiceSendByArticle(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        }
    }

    @Test
    fun `sendInvoiceAttachment success when it is not article Entry`() {
        mockkObject(ChatbotSendableWebSocketParam)

        every {
            ChatbotSendableWebSocketParam.generateParamSendInvoice(any(), any(), any(), any())
        } returns mockk(relaxed = true)

        every {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendInvoice(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        } just runs

        presenter.sendInvoiceAttachment("123", InvoiceLinkPojo(), "", "", false, "")

        verify {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendInvoice(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        }
    }

    @Test
    fun `sendReadEvent success`() {
        mockkObject(ChatbotSendableWebSocketParam)

        every {
            ChatbotSendableWebSocketParam.getReadMessage(any())
        } returns mockk(relaxed = true)

        every {
            chatbotWebSocket.send(ChatbotSendableWebSocketParam.getReadMessage(any()), any())
        } just runs

        presenter.sendReadEvent("123")

        verify {
            chatbotWebSocket.send(ChatbotSendableWebSocketParam.getReadMessage(any()), any())
        }
    }

    @Test
    fun `sendMessageWithWebsocket success`() {
        mockkObject(ChatbotSendableWebSocketParam)

        every {
            ChatbotSendableWebSocketParam.generateParamSendMessage(any(), any(), any(), any())
        } returns mockk(relaxed = true)

        every {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendMessage(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        } just runs

        presenter.sendMessageWithWebsocket("123", "", "", "")

        verify {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendMessage(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        }
    }
}
