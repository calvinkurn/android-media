package com.tokopedia.chatbot

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chatbot.data.imageupload.ChatbotUploadImagePojo
import com.tokopedia.chatbot.domain.ChatbotSendWebsocketParam
import com.tokopedia.chatbot.domain.mapper.ChatBotWebSocketMessageMapper
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListInput
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.chatbot.domain.usecase.*
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.*
import org.junit.Assert.assertEquals

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
    private lateinit var presenter: ChatbotPresenter
    private lateinit var view: ChatbotContract.View

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
                getExistingChatMapper
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
    fun `sendMessage without parent reply`() {
        mockkObject(RxWebSocket)
        mockkObject(ChatbotSendWebsocketParam)

        every {
            ChatbotSendWebsocketParam.generateParamSendMessage(any(), any(), any(), any())
        } returns mockk(relaxed = true)

        every {
            RxWebSocket.send(
                ChatbotSendWebsocketParam.generateParamSendMessage(
                    any(),
                    any(),
                    any(),
                    any()
                ), any()
            )
        } just runs

        presenter.sendMessage("", "", "", "", null) {}

        verify {
            RxWebSocket.send(
                ChatbotSendWebsocketParam.generateParamSendMessage(
                    any(),
                    any(),
                    any(),
                    any()
                ), any()
            )
        }
    }

    @Test
    fun `sendMessage with parent reply`() {
        mockkObject(RxWebSocket)
        mockkObject(ChatbotSendWebsocketParam)

        every {
            ChatbotSendWebsocketParam.generateParamSendMessageWithReplyBubble(
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        every {
            RxWebSocket.send(
                ChatbotSendWebsocketParam.generateParamSendMessageWithReplyBubble(
                    any(),
                    any(),
                    any(),
                    any()
                ), any()
            )
        } just runs

        presenter.sendMessage("", "", "", "", ParentReply()) {}

        verify {
            RxWebSocket.send(
                ChatbotSendWebsocketParam.generateParamSendMessageWithReplyBubble(
                    any(),
                    any(),
                    any(),
                    any()
                ), any()
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
            presenter.getChatRatingList(any(), any())
        }

        presenter.getBottomChat("123456", { viewModel, chatReplies -> }, {}, {})

        verify {
            presenter.getChatRatingList(chatRatingListInput, any())
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

        assertEquals(
            chatroomViewModel, getExistingChatMapper.map(expectedResponse)
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

        assertEquals(
            exception, (result as Exception)
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
            presenter.getChatRatingList(any(), any())
        }

        // When
        presenter.getTopChat("123456", { viewModel, chatReplies -> }, {}, {})

        verify {
            presenter.getChatRatingList(chatRatingListInput, any())
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
            chatroomViewModel = viewModel!!
        }, {}, {})

        assertEquals(
            chatroomViewModel, getExistingChatMapper.map(expectedResponse)
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

        assertEquals(
            exception, (result as Exception)
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
            presenter.getChatRatingList(any(), any())
        }

        // When
        presenter.getExistingChat("123456", { }, { viewModel, chatReplies -> }, {})

        verify {
            presenter.getChatRatingList(chatRatingListInput, any())
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

        assertEquals(
            exception, (result as Exception)
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

        presenter.getChatRatingList(ChipGetChatRatingListInput()) { chipGetChatRatingList ->
            expectedChatRatingList = chipGetChatRatingList!!
        }

        assertEquals(
            expectedChatRatingList, ratingListResponse.chipGetChatRatingList
        )

    }

    @Test
    fun `getChatRatingList failure`() {

        val exception = mockk<Exception>()
        coEvery {
            chipGetChatRatingListUseCase.getChatRatingList(any())
        } throws exception

        presenter.getChatRatingList(ChipGetChatRatingListInput(), {})

        verify {
            exception.printStackTrace()
        }
    }

}