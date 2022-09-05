package com.tokopedia.chatbot.view.presenter

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.SendWebsocketParam
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.invoiceattachment.InvoiceLinkPojo
import com.tokopedia.chatbot.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.data.TickerData.TickerDataResponse
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.imageupload.ChatbotUploadImagePojo
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.data.uploadsecure.CheckUploadSecureResponse
import com.tokopedia.chatbot.domain.ChatbotSendWebsocketParam
import com.tokopedia.chatbot.domain.mapper.ChatBotWebSocketMessageMapper
import com.tokopedia.chatbot.domain.pojo.csatRating.csatInput.InputItem
import com.tokopedia.chatbot.domain.pojo.csatRating.csatResponse.SubmitCsatGqlResponse
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListInput
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatResponse
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
import com.tokopedia.chatbot.domain.usecase.SendChatbotWebsocketParam
import com.tokopedia.chatbot.domain.usecase.SendRatingReasonUseCase
import com.tokopedia.chatbot.domain.usecase.SubmitCsatRatingUseCase
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
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
    fun `submitChatCsat success`() {
        val response = mockk<ChipSubmitChatCsatResponse>(relaxed = true)

        coEvery {
            chipSubmitChatCsatUseCase.chipSubmitChatCsat(captureLambda(),any(),any())
        } coAnswers {
            firstArg<(ChipSubmitChatCsatResponse)-> Unit>().invoke(response)
        }

        presenter.submitChatCsat(ChipSubmitChatCsatInput())

        verify {
            view.onSuccessSubmitChatCsat(any())
        }
    }

    @Test
    fun `submitChatCsat failure`() {
        coEvery {
            chipSubmitChatCsatUseCase.chipSubmitChatCsat(any(),captureLambda(),any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        presenter.submitChatCsat(ChipSubmitChatCsatInput())

        verify {
            view.onError(any())
        }
    }

    //WHAT WILL I TEST HERE
    @Test
    fun `hitGqlforOptionList success`() {

    }

    @Test
    fun `hitGqlforOptionList failure`() {

    }

    @Test
    fun `checkLinkForRedirection success resoList not empty`() {

    }

    @Test
    fun `checkLinkForRedirection success resoList empty`() {

    }

    @Test
    fun `checkLinkForRedirection failure`() {

    }

    @Test
    fun `showTickerData success`() {
        val response = mockk<TickerDataResponse>(relaxed = true)

        coEvery {
            getTickerDataUseCase.getTickerData(captureLambda(),any())
        } coAnswers {
            firstArg<(TickerDataResponse) -> Unit>().invoke(response)
        }

        presenter.showTickerData()

        verify {
            view.onSuccessGetTickerData(any())
        }

    }

    @Test
    fun `showTickerData failure`() {
        coEvery {
            getTickerDataUseCase.getTickerData(any(),captureLambda())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        presenter.showTickerData()

        verify {
            view.onError(any())
        }
    }

    @Test
    fun `submitCsatRating success`() {
        val response = mockk<SubmitCsatGqlResponse>(relaxed = true)

        coEvery {
            submitCsatRatingUseCase.submitCsatRating(captureLambda(),any(),any())
        } coAnswers {
            firstArg<(SubmitCsatGqlResponse) -> Unit>().invoke(response)
        }

        presenter.submitCsatRating(InputItem(0,"","","","","",""))

        verify {
            view.onSuccessSubmitCsatRating(any())
        }

    }

    @Test
    fun `submitCsatRating failure`() {

        coEvery {
            submitCsatRatingUseCase.submitCsatRating(any(),captureLambda(),any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        presenter.submitCsatRating(InputItem(0,"","","","","",""))

        verify {
            view.onError(any())
        }

    }


    //LEaveQueueUseCase


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

        presenter.sendMessage("", "123", "", "", null) {}

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

        presenter.sendMessage("", "123", "", "", ParentReply()) {}

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

        Assert.assertEquals(
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

        Assert.assertEquals(
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

        Assert.assertEquals(
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

        Assert.assertEquals(
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

        Assert.assertEquals(
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

        Assert.assertEquals(
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

        //replace mockk
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
    fun `sendRating success`() {
        val params = mapOf<String, Any>()
        mockkConstructor(SendRatingSubscriber::class)
        mockkObject(SendChatRatingUseCase)

        every {
            SendChatRatingUseCase.generateParam(
                any(), any(), any()
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
    fun `sendReasonRating success`() {
        val params = mapOf<String, Any>()
        mockkConstructor(SendRatingReasonSubscriber::class)
        mockkObject(SendRatingReasonUseCase)

        every {
            SendRatingReasonUseCase.generateParam(
                any(), any(), any()
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
        mockkObject(RxWebSocket)
        mockkObject(SendChatbotWebsocketParam)

        every {
            SendChatbotWebsocketParam.generateParamSendBubbleAction(
                any(), any(),
                any(), any()
            )
        } returns mockk(relaxed = true)

        every {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendBubbleAction(
                    any(), any(),
                    any(), any()
                ), any()
            )
        } just runs

        presenter.sendActionBubble("", ChatActionBubbleViewModel(), "", "")

        verify {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendBubbleAction(
                    any(), any(),
                    any(), any()
                ), any()
            )
        }

    }

    @Test
    fun `sendQuickReplyInvoice success`() {
        mockkObject(RxWebSocket)
        mockkObject(SendChatbotWebsocketParam)

        every {
            SendChatbotWebsocketParam.generateParamSendQuickReplyEventArticle(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        every {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendQuickReplyEventArticle(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                ), any()
            )
        } just runs

        presenter.sendQuickReplyInvoice("123", QuickReplyViewModel("", "", ""), "", "", "", "")

        verify {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendQuickReplyEventArticle(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                ), any()
            )
        }
    }

    @Test
    fun `sendQuickReply success`() {
        mockkObject(RxWebSocket)
        mockkObject(SendChatbotWebsocketParam)

        every {
            SendChatbotWebsocketParam.generateParamSendQuickReply(
                any(),
                any(), any(), any()
            )
        } returns mockk(relaxed = true)

        every {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendQuickReply(
                    any(),
                    any(), any(), any()
                ), any()
            )
        } just runs

        presenter.sendQuickReply("123", QuickReplyViewModel("", "", ""), "", "")

        verify {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendQuickReply(
                    any(),
                    any(), any(), any()
                ), any()
            )
        }

    }

    @Test
    fun `sendInvoiceAttachment success when it is article Entry`() {
        mockkObject(RxWebSocket)
        mockkObject(SendChatbotWebsocketParam)

        every {
            SendChatbotWebsocketParam.generateParamInvoiceSendByArticle(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        every {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamInvoiceSendByArticle(
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                ), any()
            )
        } just runs

        presenter.sendInvoiceAttachment("123", InvoiceLinkPojo(), "", "", true, "")


        verify {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamInvoiceSendByArticle(
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                ), any()
            )
        }

    }

    @Test
    fun `sendInvoiceAttachment success when it is not article Entry`() {
        mockkObject(RxWebSocket)
        mockkObject(SendChatbotWebsocketParam)

        every {
            SendChatbotWebsocketParam.generateParamSendInvoice(any(), any(), any(), any())
        } returns mockk(relaxed = true)

        every {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendInvoice(
                    any(),
                    any(),
                    any(),
                    any()
                ), any()
            )
        } just runs

        presenter.sendInvoiceAttachment("123", InvoiceLinkPojo(), "", "", false, "")


        verify {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendInvoice(
                    any(),
                    any(),
                    any(),
                    any()
                ), any()
            )
        }

    }

    @Test
    fun `sendReadEvent success`() {
        mockkObject(RxWebSocket)
        mockkObject(SendChatbotWebsocketParam)

        every {
            SendChatbotWebsocketParam.getReadMessage(any())
        } returns mockk(relaxed = true)

        every {
            RxWebSocket.send(SendChatbotWebsocketParam.getReadMessage(any()), any())
        } just runs

        presenter.sendReadEvent("123")

        verify {
            RxWebSocket.send(SendChatbotWebsocketParam.getReadMessage(any()), any())
        }

    }

    @Test
    fun `sendMessageWithWebsocket success`() {
        mockkObject(RxWebSocket)
        mockkObject(SendChatbotWebsocketParam)

        every {
            SendChatbotWebsocketParam.generateParamSendMessage(any(), any(), any(), any())
        } returns mockk(relaxed = true)

        every {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendMessage(
                    any(),
                    any(),
                    any(),
                    any()
                ), any()
            )
        } just runs

        presenter.sendMessageWithWebsocket("123", "", "", "")

        verify {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendMessage(
                    any(),
                    any(),
                    any(),
                    any()
                ), any()
            )
        }

    }


}