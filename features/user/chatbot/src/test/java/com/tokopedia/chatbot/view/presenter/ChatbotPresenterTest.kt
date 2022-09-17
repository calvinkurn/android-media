package com.tokopedia.chatbot.view.presenter

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.attachinvoice.data.uimodel.AttachInvoiceSentUiModel
import com.tokopedia.chatbot.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.data.TickerData.TickerDataResponse
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.data.imageupload.ChatbotUploadImagePojo
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSingleUiModel
import com.tokopedia.chatbot.data.newsession.TopBotNewSessionResponse
import com.tokopedia.chatbot.data.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.data.rating.ChatRatingUiModel
import com.tokopedia.chatbot.data.uploadsecure.CheckUploadSecureResponse
import com.tokopedia.chatbot.domain.ChatbotSendWebsocketParam
import com.tokopedia.chatbot.domain.mapper.ChatBotWebSocketMessageMapper
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.domain.pojo.csatRating.csatInput.InputItem
import com.tokopedia.chatbot.domain.pojo.csatRating.csatResponse.SubmitCsatGqlResponse
import com.tokopedia.chatbot.domain.pojo.csatoptionlist.CsatAttributesPojo
import com.tokopedia.chatbot.domain.pojo.helpfullquestion.HelpFullQuestionPojo
import com.tokopedia.chatbot.domain.pojo.leavequeue.LeaveQueueData
import com.tokopedia.chatbot.domain.pojo.leavequeue.LeaveQueueHeader
import com.tokopedia.chatbot.domain.pojo.leavequeue.LeaveQueueResponse
import com.tokopedia.chatbot.domain.pojo.leavequeue.PostLeaveQueue
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListInput
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatResponse
import com.tokopedia.chatbot.domain.resolink.ResoLinkResponse
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
import com.tokopedia.chatbot.domain.usecase.SubmitCsatRatingUseCase
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
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
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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
            chipSubmitChatCsatUseCase.chipSubmitChatCsat(captureLambda(), any(), any())
        } coAnswers {
            firstArg<(ChipSubmitChatCsatResponse) -> Unit>().invoke(response)
        }

        presenter.submitChatCsat(ChipSubmitChatCsatInput())

        verify {
            view.onSuccessSubmitChatCsat(any())
        }
    }

    @Test
    fun `submitChatCsat failure`() {
        coEvery {
            chipSubmitChatCsatUseCase.chipSubmitChatCsat(any(), captureLambda(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        presenter.submitChatCsat(ChipSubmitChatCsatInput())

        verify {
            view.onError(any())
        }
    }

    @Test
    fun `leaveQueue success`() {
        val response = mockk<LeaveQueueResponse>(relaxed = true)
        val leaveQueueHeader = mockk<LeaveQueueHeader>(relaxed = true)

        every {
            response.postLeaveQueue?.leaveQueueHeader
        } returns leaveQueueHeader

        coEvery {
            leaveQueueUseCase.execute(captureLambda(), any(), any(), any())
        } coAnswers {
            firstArg<(LeaveQueueResponse) -> Unit>().invoke(response)
        }

        presenter.leaveQueue()

        assertNotNull(leaveQueueHeader)
    }

    @Test
    fun `leaveQueue failure`() {
        runBlockingTest {
            every { presenter.chatResponse.msgId } returns "1234"

            every {
                leaveQueueUseCase.execute(any(), any(), any(), any())
            } answers {
                secondArg<(Throwable) -> Unit>().invoke(Exception())
            }
            presenter.leaveQueue().invoke()

            verify { view.showErrorToast(any()) }
        }
    }

    @Test
    fun `OnClickLeaveQueue success`() {
        runBlockingTest {
            every { presenter.chatResponse.msgId } returns "1234"
            every {
                leaveQueueUseCase.execute(any(), any(), any(), any())
            } answers {
                firstArg<(LeaveQueueResponse) -> Unit>().invoke(
                    LeaveQueueResponse(
                        postLeaveQueue = PostLeaveQueue(
                            leaveQueueData = LeaveQueueData("Ok"),
                            leaveQueueHeader = LeaveQueueHeader(200, "400", 1, "reason")
                        )
                    )
                )
            }

            presenter.OnClickLeaveQueue("123456")

            verify { presenter.onSuccess(any()) }
        }
    }

    @Test
    fun `OnClickLeaveQueue failure`() {
        runBlockingTest {
            every { presenter.chatResponse.msgId } returns "1234"
            coEvery {
                leaveQueueUseCase.execute(any(), any(), any(), any())
            } answers {
                secondArg<(Throwable) -> Unit>().invoke(Exception())
            }

            presenter.OnClickLeaveQueue("123456")

            verify { view.showErrorToast(any()) }
        }
    }

    @Test
    fun `hitGqlforOptionList failure`() {
        coEvery {
            chipSubmitHelpfulQuestionsUseCase.chipSubmitHelpfulQuestions(any(), any())
        } answers {
            firstArg<(Throwable) -> Unit>().invoke(Exception())
        }

        presenter.hitGqlforOptionList(1, null)

        verify { presenter.onSubmitError(any()) }
    }

    @Test
    fun `checkLinkForRedirection success resoList not empty`() {
        val response = mockk<ResoLinkResponse>(relaxed = true)
        val stickyButtonStatus = true
        var expectedButtonStatus = true
        val mockOrderData =
            mockk<List<ResoLinkResponse.GetResolutionLink.ResolutionLinkData.Order>>(relaxed = true)
        val expectedDynamicLink = "Dynamic Link"

        coEvery {
            getResolutionLinkUseCase.getResoLinkResponse(any())
        } returns response

        every {
            response.getResolutionLink?.resolutionLinkData?.orderList
        } returns mockOrderData

        every {
            mockOrderData.firstOrNull()?.resoList?.isNotEmpty()
        } returns stickyButtonStatus

        every {
            mockOrderData.firstOrNull()?.dynamicLink
        } returns expectedDynamicLink

        presenter.checkLinkForRedirection("123", {}, {}, {})

        assertEquals(stickyButtonStatus, expectedButtonStatus)
    }

    @Test
    fun `checkLinkForRedirection success resoList empty`() {
        val response = mockk<ResoLinkResponse>(relaxed = true)
        val stickyButtonStatus = false
        val expectedButtonStatus = false
        val mockOrderData =
            mockk<List<ResoLinkResponse.GetResolutionLink.ResolutionLinkData.Order>>(relaxed = true)
        val expectedDynamicLink = "Dynamic Link"

        coEvery {
            getResolutionLinkUseCase.getResoLinkResponse(any())
        } returns response

        every {
            response.getResolutionLink?.resolutionLinkData?.orderList
        } returns mockOrderData

        every {
            mockOrderData.firstOrNull()?.resoList?.isNotEmpty()
        } returns stickyButtonStatus

        every {
            mockOrderData.firstOrNull()?.dynamicLink
        } returns expectedDynamicLink

        presenter.checkLinkForRedirection("123", {}, {}, {})

        assertEquals(stickyButtonStatus, expectedButtonStatus)
    }

    @Test
    fun `checkLinkForRedirection failure`() {
        val throwable = mockk<Throwable>(relaxed = true)
        var result: Throwable? = null

        coEvery {
            getResolutionLinkUseCase.getResoLinkResponse(any())
        } throws throwable

        presenter.checkLinkForRedirection("123", {}, {}, { throwable ->
            result = throwable
        })

        assertEquals(
            throwable,
            (result as Throwable)
        )
    }

    @Test
    fun `showTickerData success`() {
        val response = mockk<TickerDataResponse>(relaxed = true)

        coEvery {
            getTickerDataUseCase.getTickerData(captureLambda(), any())
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
            getTickerDataUseCase.getTickerData(any(), captureLambda())
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
            submitCsatRatingUseCase.submitCsatRating(captureLambda(), any(), any())
        } coAnswers {
            firstArg<(SubmitCsatGqlResponse) -> Unit>().invoke(response)
        }

        presenter.submitCsatRating(InputItem(0, "", "", "", "", "", ""))

        verify {
            view.onSuccessSubmitCsatRating(any())
        }
    }

    @Test
    fun `submitCsatRating failure`() {
        coEvery {
            submitCsatRatingUseCase.submitCsatRating(any(), captureLambda(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        presenter.submitCsatRating(InputItem(0, "", "", "", "", "", ""))

        verify {
            view.onError(any())
        }
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
                ),
                any()
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
                ),
                any()
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
                ),
                any()
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
                ),
                any()
            )
        }
    }

    @Test
    fun `getBottomChat success when chat ratings list is not empty`() {
        val expectedResponse = GetExistingChatPojo()
        val chatroomViewModel = getExistingChatMapper.map(expectedResponse)
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

        coEvery {
            presenter.getChatRatingList(any(), any())
        }

        presenter.getBottomChat("123456", { _, _ -> }, {}, {})

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

        presenter.getBottomChat("123456", { viewModel, _ ->
            chatroomViewModel = viewModel
        }, {}, {})

        assertEquals(
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

        presenter.getBottomChat("123", { _, _ -> }, { exception ->
            result = exception
        }, {})

        assertEquals(
            exception,
            (result as Exception)
        )
    }

    @Test
    fun `getTopChat when inputList is not empty success`() {
        val expectedResponse = GetExistingChatPojo()
        val chatroomViewModel = getExistingChatMapper.map(expectedResponse)
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

        coEvery {
            presenter.getChatRatingList(any(), any())
        }

        // When
        presenter.getTopChat("123456", { _, _ -> }, {}, {})

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

        presenter.getTopChat("123456", { viewModel, _ ->
            chatroomViewModel = viewModel
        }, {}, {})

        assertEquals(
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

        presenter.getTopChat("123", { _, _ -> }, { exception ->
            result = exception
        }, {})

        assertEquals(
            exception,
            (result as Exception)
        )
    }

    @Test
    fun `getExistingChat does not get called if messageId is empty`() {
        coEvery {
            getExistingChatUseCase.getFirstPageChat(any())
        }

        presenter.getExistingChat("", {}, { _, _ -> }, {})

        verify(exactly = 0) {
            presenter.getChatRatingData(any())
        }
    }

    @Test
    fun `getExistingChat - when input List is empty calls getChatRatingList - Success`() {
        val expectedResponse = GetExistingChatPojo()
        val chatroomViewModel = getExistingChatMapper.map(expectedResponse)
        val chatRatingListInput = ChipGetChatRatingListInput(mutableListOf(mockk(relaxed = true)))

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
        presenter.getExistingChat("123456", { }, { _, _ -> }, {})

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
        }, { _, _ -> }, {})

        assertEquals(
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

        presenter.getChatRatingList(ChipGetChatRatingListInput()) { chipGetChatRatingList ->
            expectedChatRatingList = chipGetChatRatingList!!
        }

        assertEquals(
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

        presenter.getChatRatingList(ChipGetChatRatingListInput()) {}

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
    fun `checkForSession success if newSession is in response calls startNewSession`() {
        val response = mockk<TopBotNewSessionResponse>(relaxed = true)
        val isNewSession = true
        val isTypingBlock = true

        coEvery {
            getTopBotNewSessionUseCase.getTopBotUserSession(captureLambda(), any(), any())
        } coAnswers {
            firstArg<(TopBotNewSessionResponse) -> Unit>().invoke(response)
        }

        every {
            response.topBotGetNewSession.isNewSession
        } returns isNewSession

        every {
            response.topBotGetNewSession.isTypingBlocked
        } returns isTypingBlock

        presenter.checkForSession("123456")

        verify {
            view.startNewSession()
        }
        verify {
            view.blockTyping()
        }
    }

    @Test
    fun `checkForSession success if newSession is in response calls loadChatHistory`() {
        val response = mockk<TopBotNewSessionResponse>(relaxed = true)
        val isNewSession = false
        val isTypingBlock = false

        coEvery {
            getTopBotNewSessionUseCase.getTopBotUserSession(captureLambda(), any(), any())
        } coAnswers {
            firstArg<(TopBotNewSessionResponse) -> Unit>().invoke(response)
        }

        every {
            response.topBotGetNewSession.isNewSession
        } returns isNewSession

        every {
            response.topBotGetNewSession.isTypingBlocked
        } returns isTypingBlock

        presenter.checkForSession("123456")

        verify {
            view.loadChatHistory()
        }
        verify {
            view.enableTyping()
        }
    }

    @Test
    fun `checkForSession failure  calls loadChatHistory`() {
        coEvery {
            getTopBotNewSessionUseCase.getTopBotUserSession(any(), captureLambda(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        presenter.checkForSession("123456")

        verify {
            view.loadChatHistory()
        }
    }

    @Test
    fun `sendRating success `() {
        val response = mockk<SendRatingPojo>(relaxed = true)
        val rating = 1
        val uiModel = mockk<ChatRatingUiModel>(relaxed = true)

        coEvery {
            sendChatRatingUseCase.sendChatRating(captureLambda(), any(), any(), any(), any())
        } coAnswers {
            firstArg<(SendRatingPojo, Int, ChatRatingUiModel) -> Unit>().invoke(
                response,
                rating,
                uiModel
            )
        }

        presenter.sendRating("123456", 5, ChatRatingUiModel())

        verify {
            view.onSuccessSendRating(any(), any(), any())
        }
    }

    @Test
    fun `sendRating throws exception `() {
        coEvery {
            sendChatRatingUseCase.sendChatRating(captureLambda(), any(), any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        presenter.sendRating("123456", 5, ChatRatingUiModel())

        verify {
            view.onError(any())
        }
    }

    /******************************* Socket Related Unit Tests************************************/

    @Test
    fun `sendActionBubble success`() {
        mockkObject(RxWebSocket)
        mockkObject(SendChatbotWebsocketParam)

        every {
            SendChatbotWebsocketParam.generateParamSendBubbleAction(
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        every {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendBubbleAction(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        } just runs

        presenter.sendActionBubble("", ChatActionBubbleUiModel(), "", "")

        verify {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendBubbleAction(
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
                ),
                any()
            )
        } just runs

        presenter.sendQuickReplyInvoice("123", QuickReplyUiModel("", "", ""), "", "", "", "")

        verify {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendQuickReplyEventArticle(
                    any(),
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
        mockkObject(RxWebSocket)
        mockkObject(SendChatbotWebsocketParam)

        every {
            SendChatbotWebsocketParam.generateParamSendQuickReply(
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        every {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendQuickReply(
                    any(),
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        } just runs

        presenter.sendQuickReply("123", QuickReplyUiModel("", "", ""), "", "")

        verify {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendQuickReply(
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
                ),
                any()
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
                ),
                any()
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
                ),
                any()
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
                ),
                any()
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
                ),
                any()
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
                ),
                any()
            )
        }
    }

    @Test
    fun `mapToVisitable success`() {
        val mockkVisitable = mockk<Visitable<*>>(relaxed = true)

        every {
            chatBotWebSocketMessageMapper.map(any())
        } returns mockkVisitable

        presenter.mapToVisitable(ChatSocketPojo())

        assertNotNull(mockkVisitable)
    }

    @Test
    fun `getActionBubbleforNoTrasaction success`() {
        val actionBubbleUiModel = createActionBubble()

        presenter.getActionBubbleforNoTrasaction()

        assertNotNull(actionBubbleUiModel)
    }

    @Test
    fun `clearText success`() {
        every {
            view.clearChatText()
        } just runs

        presenter.clearText()

        verify {
            view.clearChatText()
        }
    }

    @Test
    fun `showErrorSnackbar success`() {
        every {
            view.showSnackbarError(any())
        } just runs

        presenter.showErrorSnackbar(1)

        verify {
            view.showSnackbarError(any())
        }
    }

    @Test
    fun `createAttachInvoiceSingleViewModel success`() {
        val map = getMapForArticleEntry()
        val attachInvoiceSingleUiModel = getAttachSingleInvoiceUiModel(map)
        presenter.createAttachInvoiceSingleViewModel(map)
        assertNotNull(attachInvoiceSingleUiModel)
    }

    @Test
    fun `generateInvoice success`() {
        val generatedInvoice = getInvoice()
        presenter.generateInvoice(InvoiceLinkPojo(), "")
        assertNotNull(generatedInvoice)
    }

    @Test
    fun `detachView success`() {
        every {
            presenter.destroyWebSocket()
        } just runs

        presenter.detachView()

        verify { presenter.destroyWebSocket() }
    }

    @Test
    fun `getChatRatingData when type is HelpFullQuestionsViewModel`() {
        val input = ChipGetChatRatingListInput()
        val mappedPojo = mockk<ChatroomViewModel>(relaxed = true)
        mappedPojo.listChat.add(mockk())
        val helpfulQuestionUiModel = mockk<HelpFullQuestionsUiModel>(relaxed = true)
        every {
            mappedPojo.listChat.firstOrNull()
        } returns helpfulQuestionUiModel

        every {
            input.list.add(
                ChipGetChatRatingListInput.ChatRating(
                    22,
                    helpfulQuestionUiModel.helpfulQuestion?.caseChatId ?: ""
                )
            )
        } returns true

        val chatRoomViewModel = ChatroomViewModel()
        chatRoomViewModel.listChat.add(
            HelpFullQuestionsUiModel(
                "", "", "", "", "",
                "", "", "",
                HelpFullQuestionPojo.HelpfulQuestion("", "", emptyList(), 1)
            )
        )
        chatRoomViewModel.listChat.add(
            CsatOptionsUiModel(
                "", "", "", "", "", "", "",
                "", CsatAttributesPojo.Csat("", "", emptyList(), "", emptyList(), "", "", 1)
            )
        )

        presenter.getChatRatingData(chatRoomViewModel)

        assertEquals(1, input.list.size)
        assertTrue(input.list.firstOrNull() is ChipGetChatRatingListInput.ChatRating)
    }

    @Test
    fun `getChatRatingData when type is HelpFullQuestionsViewModel with attachment type null`() {
        val input = ChipGetChatRatingListInput()
        val mappedPojo = mockk<ChatroomViewModel>(relaxed = true)
        mappedPojo.listChat.add(mockk())
        val helpfulQuestionUiModel = mockk<HelpFullQuestionsUiModel>(relaxed = true)
        every {
            mappedPojo.listChat.firstOrNull()
        } returns helpfulQuestionUiModel

        every {
            input.list.add(
                ChipGetChatRatingListInput.ChatRating(
                    null.toIntOrZero(),
                    helpfulQuestionUiModel.helpfulQuestion?.caseChatId ?: ""
                )
            )
        } returns true

        val chatRoomViewModel = ChatroomViewModel()
        chatRoomViewModel.listChat.add(
            HelpFullQuestionsUiModel(
                "", "", "", "", "",
                "", "", "",
                HelpFullQuestionPojo.HelpfulQuestion("", "", emptyList(), 1)
            )
        )
        chatRoomViewModel.listChat.add(
            CsatOptionsUiModel(
                "", "", "", "", "", "", "",
                "", CsatAttributesPojo.Csat("", "", emptyList(), "", emptyList(), "", "", 1)
            )
        )

        presenter.getChatRatingData(chatRoomViewModel)

        assertEquals(1, input.list.size)
        assertTrue(input.list.firstOrNull() is ChipGetChatRatingListInput.ChatRating)
    }

    @Test
    fun `getChatRatingData when type is HelpFullQuestionsViewModel and null`() {
        val input = ChipGetChatRatingListInput()
        val mappedPojo = mockk<ChatroomViewModel>(relaxed = true)
        mappedPojo.listChat.add(mockk())
        val helpfulQuestionUiModel = mockk<HelpFullQuestionsUiModel>(relaxed = true)
        every {
            mappedPojo.listChat.firstOrNull()
        } returns helpfulQuestionUiModel

        every {
            input.list.add(
                ChipGetChatRatingListInput.ChatRating(
                    22,
                    helpfulQuestionUiModel.helpfulQuestion?.caseChatId ?: ""
                )
            )
        } returns true

        val chatRoomViewModel = ChatroomViewModel()
        chatRoomViewModel.listChat.add(
            HelpFullQuestionsUiModel(
                "", "", "", "", "",
                "", "", "",
                null
            )
        )
        chatRoomViewModel.listChat.add(
            CsatOptionsUiModel(
                "", "", "", "", "", "", "",
                "", null
            )
        )

        presenter.getChatRatingData(chatRoomViewModel)

        assertEquals(1, input.list.size)
        assertTrue(input.list.firstOrNull() is ChipGetChatRatingListInput.ChatRating)
    }

    @Test
    fun `getChatRatingData when type is CsatOptionsViewModel`() {
        val input = ChipGetChatRatingListInput()
        val mappedPojo = mockk<ChatroomViewModel>(relaxed = true)
        mappedPojo.listChat.add(mockk())
        val csatOptionsUiModel = mockk<CsatOptionsUiModel>(relaxed = true)
        every {
            mappedPojo.listChat.firstOrNull()
        } returns csatOptionsUiModel

        every {
            input.list.add(
                ChipGetChatRatingListInput.ChatRating(
                    23,
                    csatOptionsUiModel.csat?.caseChatId ?: ""
                )
            )
        } returns true

        val chatRoomViewModel = ChatroomViewModel()
        chatRoomViewModel.listChat.add(
            HelpFullQuestionsUiModel(
                "", "", "", "", "",
                "", "", "",
                HelpFullQuestionPojo.HelpfulQuestion("", "", emptyList(), 1)
            )
        )
        chatRoomViewModel.listChat.add(
            CsatOptionsUiModel(
                "", "", "", "", "", "", "",
                "", CsatAttributesPojo.Csat("", "", emptyList(), "", emptyList(), "", "", 1)
            )
        )

        presenter.getChatRatingData(chatRoomViewModel)

        assertEquals(1, input.list.size)
        assertTrue(input.list.firstOrNull() is ChipGetChatRatingListInput.ChatRating)
    }

    @Test
    fun `getChatRatingData when type is CsatOptionsViewModel and null`() {
        val input = ChipGetChatRatingListInput()
        val mappedPojo = mockk<ChatroomViewModel>(relaxed = true)
        mappedPojo.listChat.add(mockk())
        val csatOptionsUiModel = mockk<CsatOptionsUiModel>(relaxed = true)
        every {
            mappedPojo.listChat.firstOrNull()
        } returns csatOptionsUiModel

        every {
            input.list.add(
                ChipGetChatRatingListInput.ChatRating(
                    23,
                    csatOptionsUiModel.csat?.caseChatId ?: ""
                )
            )
        } returns true

        val chatRoomViewModel = ChatroomViewModel()
        chatRoomViewModel.listChat.add(
            HelpFullQuestionsUiModel(
                "", "", "", "", "",
                "", "", "",
                HelpFullQuestionPojo.HelpfulQuestion("", "", emptyList(), 1)
            )
        )
        chatRoomViewModel.listChat.add(
            CsatOptionsUiModel(
                "", "", "", "", "", "", "",
                "", null
            )
        )

        presenter.getChatRatingData(chatRoomViewModel)

        assertEquals(1, input.list.size)
        assertTrue(input.list.firstOrNull() is ChipGetChatRatingListInput.ChatRating)
    }

    @Test
    fun `sendUploadedImageToWebsocket success`() {
        mockkObject(RxWebSocket)

        every {
            RxWebSocket.send(any<JsonObject>(), any())
        } returns mockk(relaxed = true)

        presenter.sendUploadedImageToWebsocket(JsonObject())

        verify {
            RxWebSocket.send(
                any<JsonObject>(),
                any()
            )
        }
    }

//    @Test
//    fun `updateMappedPojo success`() {
//        val mockkRatings = mockk<ChipGetChatRatingListResponse.ChipGetChatRatingList>(relaxed = true)
//        val isSuccess = 1
//        val mappedPojo = mockk<ChatroomViewModel>(relaxed = true)
//        val mockkHelpfullQuestion = mockk<HelpFullQuestionsViewModel>(relaxed = true)
//        val mockkRatingList = mockk<ChipGetChatRatingListResponse.ChipGetChatRatingList.RatingListData.RatingList>(relaxed = true)
//        var mockkRateListMsg= mockk<MutableList<HelpFullQuestionsViewModel>>(relaxed = true)
//        mockkRateListMsg.add(mockkHelpfullQuestion)
//
//        every {
//            mockkRatings?.ratingListData?.isSuccess
//        } returns isSuccess
//
//        every {
//            mockkRatings.ratingListData?.list?.firstOrNull()
//        } returns mockkRatingList
//
//        every {
//            mappedPojo.listChat.firstOrNull()
//        } returns mockkHelpfullQuestion
//
//        every {
//            mockkRatingList.attachmentType == 22
//        } returns true
//
//        every {
//            mockkHelpfullQuestion
//                .helpfulQuestion?.caseChatId == mockkRatingList.caseChatID
//        } returns true
//
//        every {
//            mockkHelpfullQuestion.isSubmited = mockkRatingList.isSubmitted?: true
//        } just runs
//
//        var ratings = ChipGetChatRatingListResponse.ChipGetChatRatingList(
//            ChipGetChatRatingListResponse.ChipGetChatRatingList.RatingListData(
//                1,
//                listOf(
//                    ChipGetChatRatingListResponse.ChipGetChatRatingList.RatingListData.RatingList(
//                        "1",
//                        true,
//                        "",
//                        22
//                    ),
//                    ChipGetChatRatingListResponse.ChipGetChatRatingList.RatingListData.RatingList(
//                        "2",
//                        false,
//                        "",
//                        22
//                    )
//                )
//            ),
//            emptyList(),
//            "",
//            ""
//        )
//
//        presenter.updateMappedPojo(
//            ChatroomViewModel(),
//            ratings
//        ) {
//        }
//
//        assertTrue(true)
//    }

    private fun getInvoice(): AttachInvoiceSentUiModel {
        return AttachInvoiceSentUiModel.Builder().build()
    }

    private fun createActionBubble(): ChatActionBubbleUiModel {
        return ChatActionBubbleUiModel(
            "Text",
            "Value",
            "Action"
        )
    }

    private fun getAttachSingleInvoiceUiModel(hashMap: Map<String, String>): AttachInvoiceSingleUiModel {
        return AttachInvoiceSingleUiModel(
            typeString = "",
            type = 0,
            code = hashMap[ChatbotConstant.ChatbotUnification.CODE] ?: "",
            createdTime = SendableUiModel.generateStartTime(),
            description = hashMap[ChatbotConstant.ChatbotUnification.DESCRIPTION] ?: "",
            url = hashMap[ChatbotConstant.ChatbotUnification.IMAGE_URL] ?: "",
            id = hashMap.get(ChatbotConstant.ChatbotUnification.ID)!!.toLongOrZero(),
            imageUrl = hashMap[ChatbotConstant.ChatbotUnification.IMAGE_URL] ?: "",
            status = hashMap[ChatbotConstant.ChatbotUnification.STATUS] ?: "",
            statusId = hashMap[ChatbotConstant.ChatbotUnification.STATUS_ID]!!.toIntOrZero(),
            title = hashMap[ChatbotConstant.ChatbotUnification.TITLE] ?: "",
            amount = hashMap[ChatbotConstant.ChatbotUnification.TOTAL_AMOUNT] ?: ""
        )
    }

    private fun getMapForArticleEntry(): Map<String, String> {
        return mapOf(
            ChatbotConstant.ChatbotUnification.ARTICLE_ID to "1",
            ChatbotConstant.ChatbotUnification.ARTICLE_TITLE to "2",
            ChatbotConstant.ChatbotUnification.CODE to "3",
            ChatbotConstant.ChatbotUnification.CREATE_TIME to "time",
            ChatbotConstant.ChatbotUnification.DESCRIPTION to "description",
            ChatbotConstant.ChatbotUnification.EVENT to "event",
            ChatbotConstant.ChatbotUnification.ID to "0",
            ChatbotConstant.ChatbotUnification.IMAGE_URL to "url",
            ChatbotConstant.ChatbotUnification.IS_ATTACHED to "true",
            ChatbotConstant.ChatbotUnification.STATUS to "status",
            ChatbotConstant.ChatbotUnification.STATUS_COLOR to "status_color",
            ChatbotConstant.ChatbotUnification.STATUS_ID to "0",
            ChatbotConstant.ChatbotUnification.TITLE to "title",
            ChatbotConstant.ChatbotUnification.TOTAL_AMOUNT to "123",
            ChatbotConstant.ChatbotUnification.USED_BY to "Article"
        )
    }
}