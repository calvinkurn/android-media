package com.tokopedia.chatbot.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.chatbot2.data.csatoptionlist.CsatAttributesPojo
import com.tokopedia.chatbot.chatbot2.data.helpfullquestion.HelpFullQuestionPojo
import com.tokopedia.chatbot.chatbot2.data.inboxTicketList.InboxTicketListResponse
import com.tokopedia.chatbot.chatbot2.data.newsession.TopBotNewSessionResponse
import com.tokopedia.chatbot.chatbot2.data.quickreply.QuickReplyAttachmentAttributes
import com.tokopedia.chatbot.chatbot2.data.quickreply.QuickReplyPojo
import com.tokopedia.chatbot.chatbot2.data.ratinglist.ChipGetChatRatingListInput
import com.tokopedia.chatbot.chatbot2.data.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.chatbot.chatbot2.data.resolink.ResoLinkResponse
import com.tokopedia.chatbot.chatbot2.data.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.chatbot2.data.submitchatcsat.ChipSubmitChatCsatResponse
import com.tokopedia.chatbot.chatbot2.data.uploadEligibility.ChatbotUploadVideoEligibilityResponse
import com.tokopedia.chatbot.chatbot2.data.uploadsecure.CheckUploadSecureResponse
import com.tokopedia.chatbot.chatbot2.data.uploadsecure.UploadSecureResponse
import com.tokopedia.chatbot.chatbot2.domain.mapper.ChatbotGetExistingChatMapper
import com.tokopedia.chatbot.chatbot2.domain.pojo.replyBox.DynamicAttachment
import com.tokopedia.chatbot.chatbot2.domain.socket.ChatbotSendableWebSocketParam
import com.tokopedia.chatbot.chatbot2.domain.usecase.ChatBotSecureImageUploadUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.ChatbotCheckUploadSecureUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.ChatbotUploadVideoEligibilityUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.ChipGetChatRatingListUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.ChipSubmitChatCsatUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.ChipSubmitHelpfulQuestionsUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.GetExistingChatUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.GetResoLinkUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.GetTickerDataUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.GetTopBotNewSessionUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.SendChatRatingUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.SubmitCsatRatingUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.TicketListContactUsUsecase
import com.tokopedia.chatbot.chatbot2.domain.video.ChatbotVideoUploadResult
import com.tokopedia.chatbot.chatbot2.domain.video.VideoUploadData
import com.tokopedia.chatbot.chatbot2.util.ChatbotNewRelicLogger
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.rating.ChatRatingUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.videoupload.VideoUploadUiModel
import com.tokopedia.chatbot.chatbot2.view.util.helper.getEnvResoLink
import com.tokopedia.chatbot.chatbot2.view.viewmodel.MediaUploadJobMap
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.BigReplyBoxState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatDataState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatRatingListState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotArticleEntryState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotChatSeparatorState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotImageUploadFailureState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotOpenCsatState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSendChatRatingState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSocketErrorState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSocketReceiveEvent
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSubmitChatCsatState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSubmitCsatRatingState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotUpdateToolbarState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotVideoUploadEligibilityState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotVideoUploadFailureState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.CheckUploadSecureState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.GetTickerDataState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.TopBotNewSessionState
import com.tokopedia.chatbot.chatbot2.websocket.ChatbotWebSocket
import com.tokopedia.chatbot.chatbot2.websocket.ChatbotWebSocketAction
import com.tokopedia.chatbot.chatbot2.websocket.ChatbotWebSocketException
import com.tokopedia.chatbot.chatbot2.websocket.ChatbotWebSocketStateHandler
import com.tokopedia.chatbot.data.SocketResponse
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import rx.Subscriber
import timber.log.Timber
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ChatbotViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var testRule = CoroutineTestRule()

    private lateinit var ticketListContactUsUsecase: TicketListContactUsUsecase
    private lateinit var getTopBotNewSessionUseCase: GetTopBotNewSessionUseCase
    private lateinit var checkUploadSecureUseCase: ChatbotCheckUploadSecureUseCase
    private lateinit var chatbotVideoUploadVideoEligibilityUseCase: ChatbotUploadVideoEligibilityUseCase
    private lateinit var getTickerDataUseCase: GetTickerDataUseCase
    private lateinit var getResoLinkUseCase: GetResoLinkUseCase
    private lateinit var submitCsatRatingUseCase: SubmitCsatRatingUseCase
    private lateinit var chipSubmitHelpfulQuestionsUseCase: ChipSubmitHelpfulQuestionsUseCase
    private lateinit var chipSubmitChatCsatUseCase: ChipSubmitChatCsatUseCase
    private lateinit var sendChatRatingUseCase: SendChatRatingUseCase
    private lateinit var getExistingChatUseCase: GetExistingChatUseCase
    private lateinit var userSession: UserSessionInterface
    private lateinit var chatBotWebSocketMessageMapper: com.tokopedia.chatbot.chatbot2.domain.mapper.ChatBotWebSocketMessageMapper
    private lateinit var tkpdAuthInterceptor: TkpdAuthInterceptor
    private lateinit var fingerprintInterceptor: FingerprintInterceptor
    private lateinit var getExistingChatMapper: ChatbotGetExistingChatMapper
    private lateinit var uploaderUseCase: UploaderUseCase
    private lateinit var chipGetChatRatingListUseCase: ChipGetChatRatingListUseCase
    private lateinit var chatbotWebSocket: ChatbotWebSocket
    private lateinit var chatbotWebSocketStateHandler: ChatbotWebSocketStateHandler
    private lateinit var dispatcher: CoroutineDispatchers
    private var socketJob: Job? = null
    private lateinit var chatResponse: ChatSocketPojo
    private lateinit var chatBotSecureImageUploadUseCase: ChatBotSecureImageUploadUseCase

    private lateinit var viewModel: com.tokopedia.chatbot.chatbot2.view.viewmodel.ChatbotViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        ticketListContactUsUsecase = mockk(relaxed = true)
        getExistingChatUseCase = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        chatBotWebSocketMessageMapper = mockk(relaxed = true)
        tkpdAuthInterceptor = mockk(relaxed = true)
        fingerprintInterceptor = mockk(relaxed = true)
        sendChatRatingUseCase = mockk(relaxed = true)
        submitCsatRatingUseCase = mockk(relaxed = true)
        getTickerDataUseCase = mockk(relaxed = true)
        chipSubmitHelpfulQuestionsUseCase = mockk(relaxed = true)
        chipGetChatRatingListUseCase = mockk(relaxed = true)
        chipSubmitChatCsatUseCase = mockk(relaxed = true)
        getTopBotNewSessionUseCase = mockk(relaxed = true)
        checkUploadSecureUseCase = mockk(relaxed = true)
        getExistingChatMapper = mockk(relaxed = true)
        uploaderUseCase = mockk(relaxed = true)
        chatbotVideoUploadVideoEligibilityUseCase = mockk(relaxed = true)
        getResoLinkUseCase = mockk(relaxed = true)
        chatbotWebSocket = mockk(relaxed = true)
        chatbotWebSocketStateHandler = mockk(relaxed = true)
        dispatcher = testRule.dispatchers
        uploaderUseCase = mockk(relaxed = true)
        chatbotVideoUploadVideoEligibilityUseCase = mockk(relaxed = true)
        chatResponse = mockk(relaxed = true)
        chatBotSecureImageUploadUseCase = mockk(relaxed = true)

        viewModel =
            com.tokopedia.chatbot.chatbot2.view.viewmodel.ChatbotViewModel(
                ticketListContactUsUsecase,
                getTopBotNewSessionUseCase,
                checkUploadSecureUseCase,
                chatbotVideoUploadVideoEligibilityUseCase,
                getTickerDataUseCase,
                getResoLinkUseCase,
                submitCsatRatingUseCase,
                chipSubmitHelpfulQuestionsUseCase,
                chipSubmitChatCsatUseCase,
                sendChatRatingUseCase,
                getExistingChatUseCase,
                userSession,
                chatBotWebSocketMessageMapper,
                tkpdAuthInterceptor,
                fingerprintInterceptor,
                getExistingChatMapper,
                uploaderUseCase,
                chipGetChatRatingListUseCase,
                chatbotWebSocket,
                chatbotWebSocketStateHandler,
                chatBotSecureImageUploadUseCase,
                testRule.dispatchers
            )
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * GQL call Unit Tests
     * */
    @Test
    fun `handleReplyBoxWSToggle if render=android, content_code=100,is_active=true`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_CODE_100_IS_ACTIVE_TRUE_RENDER_ANDROID)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val replyBoxAttribute =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute

        viewModel.handleDynamicAttachment34(chatResponse)

        assertEquals(replyBoxAttribute?.contentCode, 100)
        assertTrue(viewModel.bigReplyBoxState.value is BigReplyBoxState.BigReplyBoxContents)
    }

    @Test
    fun `handleReplyBoxWSToggle if render=android, content_code=100, is_active=false`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_CODE_100_IS_ACTIVE_FALSE_RENDER_ANDROID)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val replyBoxAttribute =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute

        viewModel.handleDynamicAttachment34(chatResponse)

        assertEquals(replyBoxAttribute?.contentCode, 100)
    }

    @Test
    fun `handleReplyBoxWSToggle if render=ios, content_code=100, is_active=false`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_CODE_100_IS_ACTIVE_FALSE_RENDER_IOS)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val replyBoxAttribute =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute

        viewModel.handleDynamicAttachment34(chatResponse)

        assertEquals(replyBoxAttribute?.contentCode, 100)
    }

    @Test
    fun `handleReplyBoxWSToggle if attachment is null`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_ATTACHMENT_NULL)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        viewModel.handleDynamicAttachment34(chatResponse)

        assertEquals(dynamicAttachmentContents, null)
    }

    @Test
    fun `handleReplyBoxWSToggle if dynamic attachment is null`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_DYNAMIC_ATTACHMENT_IS_NULL)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val dynamicAttachmentAttribute =
            dynamicAttachmentContents?.dynamicAttachmentAttribute

        viewModel.handleDynamicAttachment34(chatResponse)

        assertEquals(dynamicAttachmentAttribute, null)
    }

    @Test
    fun `handleReplyBoxWSToggle if dynamic attachment-attribute is null`() {
        val fullResponse =
            SocketResponse.getResponse(
                SocketResponse.DYNAMIC_ATTACHMENT_DYNAMIC_ATTACHMENT_ATTRIBUTE_DYNAMIC_CONTENT_IS_NULL
            )
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val dynamicAttachmentAttribute =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute?.dynamicContent

        viewModel.handleDynamicAttachment34(chatResponse)

        assertEquals(dynamicAttachmentAttribute, null)
    }

    @Test
    fun `handleReplyBoxWSToggle if replyBoxAttribute is null`() {
        val fullResponse =
            SocketResponse.getResponse(
                SocketResponse.DYNAMIC_ATTACHMENT_DYNAMIC_ATTACHMENT_ATTRIBUTE_DYNAMIC_CONTENT_IS_NULL
            )
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val dynamicContent =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute?.dynamicContent

        viewModel.handleDynamicAttachment34(chatResponse)

        assertEquals(dynamicContent, null)
    }

    @Test
    fun `handleReplyBoxWSToggle if content_code=101, render= android, hidden=true`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_CODE_101_RENDER_ANDROID_HIDDEN_TRUE)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val dynamicContentCode =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute?.contentCode

        viewModel.handleDynamicAttachment34(chatResponse)

        assertEquals(dynamicContentCode, 101)
        assertTrue(viewModel.smallReplyBoxDisabled.value == true)
    }

    @Test
    fun `handleReplyBoxWSToggle if content_code=101, render= android, hidden=false`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_CODE_101_RENDER_ANDROID_HIDDEN_FALSE)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val dynamicContentCode =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute?.contentCode

        viewModel.handleDynamicAttachment34(chatResponse)

        assertEquals(dynamicContentCode, 101)
        assertTrue(viewModel.smallReplyBoxDisabled.value == false)
    }

    @Test
    fun `handleReplyBoxWSToggle if content_code=101, render= ios`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_CODE_101_RENDER_IOS)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val dynamicContentCode =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute?.contentCode

        viewModel.handleDynamicAttachment34(chatResponse)

        assertEquals(dynamicContentCode, 101)
    }

    @Test
    fun `handleReplyBoxWSToggle if content_code=101, dynamic content is null `() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_CODE_101_DYNAMIC_CONTENT_NULL)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val dynamicContent =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute?.dynamicContent

        viewModel.handleDynamicAttachment34(chatResponse)

        assertEquals(dynamicContent, null)
    }

    @Test
    fun `validateHistoryForAttachment34 if content_code=101, render=android, hidden=false`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_CODE_101_RENDER_ANDROID_HIDDEN_FALSE)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val replyBoxAttribute =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute

        val dynamicContentCode = replyBoxAttribute?.contentCode

        viewModel.validateHistoryForAttachment34(replyBoxAttribute)

        assertEquals(dynamicContentCode, 101)
    }

    @Test
    fun `validateHistoryForAttachment34 if render=android, content_code=100, is_active=false`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_CODE_100_IS_ACTIVE_FALSE_RENDER_ANDROID)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val replyBoxAttribute =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute

        viewModel.validateHistoryForAttachment34(replyBoxAttribute)

        assertEquals(replyBoxAttribute?.contentCode, 100)
    }

    @Test
    fun `validateHistoryForAttachment34 attachment is null`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_DYNAMIC_ATTACHMENT_ATTRIBUTE_IS_NULL)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val replyBoxAttribute =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute

        viewModel.validateHistoryForAttachment34(replyBoxAttribute)

        assertEquals(replyBoxAttribute, null)
    }

    @Test
    fun `validateHistoryForAttachment34 if content_code=102, does not process`() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_CODE_102)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val replyBoxAttribute =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute

        viewModel.validateHistoryForAttachment34(replyBoxAttribute)

        assertEquals(replyBoxAttribute?.contentCode, 102)
    }

    @Test
    fun `handleReplyBoxWSToggle if content_code=102 , goes to mapToVisitable`() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_CODE_102)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val dynamicContentCode =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute?.contentCode

        viewModel.handleDynamicAttachment34(chatResponse)

        assertEquals(dynamicContentCode, 102)
    }

    @Test
    fun `getTicketList success if isActive is true`() {
        val actual = mockk<InboxTicketListResponse.Ticket.Data.NoticeItem>(relaxed = true)
        val response = mockk<InboxTicketListResponse>(relaxed = true)

        every {
            actual.isActive
        } returns true

        every {
            response.ticket?.TicketData?.notice
        } returns actual

        coEvery {
            ticketListContactUsUsecase.getTicketList(captureLambda(), any())
        } coAnswers {
            firstArg<(InboxTicketListResponse) -> Unit>().invoke(response)
        }

        viewModel.getTicketList()

        assertEquals(
            (viewModel.ticketList.value as com.tokopedia.chatbot.chatbot2.view.viewmodel.state.TicketListState.BottomSheetData).noticeData,
            actual
        )
    }

    @Test
    fun `getTicketList success if isActive is false`() {
        val actual = mockk<InboxTicketListResponse.Ticket.Data.NoticeItem>(relaxed = true)
        val response = mockk<InboxTicketListResponse>(relaxed = true)

        every {
            actual.isActive
        } returns false

        every {
            response.ticket?.TicketData?.notice
        } returns actual

        coEvery {
            ticketListContactUsUsecase.getTicketList(captureLambda(), any())
        } coAnswers {
            firstArg<(InboxTicketListResponse) -> Unit>().invoke(response)
        }

        viewModel.getTicketList()

        assertTrue(
            (viewModel.ticketList.value is com.tokopedia.chatbot.chatbot2.view.viewmodel.state.TicketListState.ShowContactUs)
        )
    }

    @Test
    fun `getTicketList success if noticeData is null`() {
        val actual = mockk<InboxTicketListResponse.Ticket.Data.NoticeItem>(relaxed = true)
        val response = mockk<InboxTicketListResponse>(relaxed = true)

        every {
            actual.isActive
        } returns false

        every {
            response.ticket?.TicketData?.notice
        } returns null

        coEvery {
            ticketListContactUsUsecase.getTicketList(captureLambda(), any())
        } coAnswers {
            firstArg<(InboxTicketListResponse) -> Unit>().invoke(response)
        }

        viewModel.getTicketList()

        assertTrue(
            (viewModel.ticketList.value is com.tokopedia.chatbot.chatbot2.view.viewmodel.state.TicketListState.ShowContactUs)
        )
    }

    @Test
    fun `getTicketList success if ticket data is null`() {
        val response = mockk<InboxTicketListResponse>(relaxed = true)

        every {
            response.ticket?.TicketData
        } returns null

        coEvery {
            ticketListContactUsUsecase.getTicketList(captureLambda(), any())
        } coAnswers {
            firstArg<(InboxTicketListResponse) -> Unit>().invoke(response)
        }

        viewModel.getTicketList()

        assertTrue(
            (viewModel.ticketList.value is com.tokopedia.chatbot.chatbot2.view.viewmodel.state.TicketListState.ShowContactUs)
        )
    }

    @Test
    fun `getTicketList success if ticket is null`() {
        val response = mockk<InboxTicketListResponse>(relaxed = true)

        every {
            response.ticket
        } returns null

        coEvery {
            ticketListContactUsUsecase.getTicketList(captureLambda(), any())
        } coAnswers {
            firstArg<(InboxTicketListResponse) -> Unit>().invoke(response)
        }

        viewModel.getTicketList()

        assertTrue(
            (viewModel.ticketList.value is com.tokopedia.chatbot.chatbot2.view.viewmodel.state.TicketListState.ShowContactUs)
        )
    }

    @Test
    fun `getTicketList failure`() {
        coEvery {
            ticketListContactUsUsecase.getTicketList(any(), captureLambda())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.getTicketList()

        assertTrue(
            (viewModel.ticketList.value is com.tokopedia.chatbot.chatbot2.view.viewmodel.state.TicketListState.ShowContactUs)
        )
    }

    @Test
    fun `checkUploadSecure is true run uploadUsingSecureUpload`() {
        val response = mockk<CheckUploadSecureResponse>(relaxed = true)

        coEvery {
            checkUploadSecureUseCase.checkUploadSecure(
                captureLambda(),
                any(),
                any()
            )
        } coAnswers {
            firstArg<(CheckUploadSecureResponse) -> Unit>().invoke(response)
        }

        // replace mockk
        viewModel.checkUploadSecure("123")

        assertTrue(
            viewModel.checkUploadSecure.value
            is CheckUploadSecureState.SuccessCheckUploadSecure
        )
    }

    @Test
    fun `checkUploadSecure returns exception`() {
        coEvery {
            checkUploadSecureUseCase.checkUploadSecure(
                captureLambda(),
                any(),
                any()
            )
        } coAnswers {
            secondArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        // replace mockk
        viewModel.checkUploadSecure("123")

        assertTrue(
            viewModel.checkUploadSecure.value
            is CheckUploadSecureState.HandleFailureCheckUploadSecure
        )
    }

    @Test
    fun `checkForSession success`() {
        val response = mockk<TopBotNewSessionResponse>(relaxed = true)

        coEvery {
            getTopBotNewSessionUseCase.getTopBotUserSession(
                captureLambda(),
                any(),
                any()
            )
        } coAnswers {
            firstArg<(TopBotNewSessionResponse) -> Unit>().invoke(response)
        }

        // replace mockk
        viewModel.checkForSession("123")

        assertTrue(
            viewModel.topBotNewSession.value
            is TopBotNewSessionState.SuccessTopBotNewSession
        )
    }

    @Test
    fun `checkForSession failure`() {
        coEvery {
            getTopBotNewSessionUseCase.getTopBotUserSession(any(), captureLambda(), any())
        } coAnswers {
            secondArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        viewModel.checkForSession("123456")

        assertTrue(
            viewModel.topBotNewSession.value
            is TopBotNewSessionState.HandleFailureNewSession
        )
    }

    @Test
    fun `sendRating success`() {
        val response = mockk<com.tokopedia.chatbot.chatbot2.data.chatrating.SendRatingPojo>(relaxed = true)

        coEvery {
            sendChatRatingUseCase.sendChatRating(captureLambda(), any(), any(), any(), any())
        } coAnswers {
            firstArg<(com.tokopedia.chatbot.chatbot2.data.chatrating.SendRatingPojo) -> Unit>().invoke(
                response
            )
        }

        viewModel.sendRating("123456", 5, ChatRatingUiModel())

        assertTrue(
            viewModel.sendChatRating.value
            is ChatbotSendChatRatingState.HandleSuccessChatbotSendChatRating
        )
    }

    @Test
    fun `sendRating throws exception `() {
        coEvery {
            sendChatRatingUseCase.sendChatRating(captureLambda(), any(), any(), any(), any())
        } coAnswers {
            secondArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        viewModel.sendRating("123456", 5, ChatRatingUiModel())

        assertTrue(
            viewModel.sendChatRating.value
            is ChatbotSendChatRatingState.HandleErrorSendChatRating
        )
    }

    @Test
    fun `checkLinkForRedirectionForStickyActionClick failure`() {
        coEvery {
            getResoLinkUseCase.getResolutionLink(
                any(),
                captureLambda(),
                any(),
                any()
            )
        } coAnswers {
            secondArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        viewModel.checkLinkForRedirectionForStickyActionClick("123", "123")

        assertTrue(viewModel.resoLinkError.value == true)
    }

    @Test
    fun `checkLinkForRedirectionForStickyActionClick success when stickyActionClick is false`() {
        val response = mockk<ResoLinkResponse>(relaxed = true)

        coEvery {
            getResoLinkUseCase.getResolutionLink(
                captureLambda(),
                any(),
                any(),
                any()
            )
        } coAnswers {
            firstArg<(ResoLinkResponse) -> Unit>().invoke(response)
        }

        val mockOrderData =
            mockk<List<ResoLinkResponse.GetResolutionLink.ResolutionLinkData.Order>>(relaxed = true)

        val resoList = mutableListOf<ResoLinkResponse.GetResolutionLink.ResolutionLinkData.Order.Reso>()

        val dynamicLink = ""

        every {
            response.getResolutionLink?.resolutionLinkData?.orderList
        } returns mockOrderData

        every {
            mockOrderData.get(0).resoList
        } returns resoList

        every {
            mockOrderData.get(0).dynamicLink
        } returns dynamicLink

        viewModel.checkLinkForRedirectionForStickyActionClick("123", "123")

        assertTrue(viewModel.stickyActionClick.value == false)
    }

    @Test
    fun `checkLinkForRedirectionForStickyActionClick success when stickyActionClick is true`() {
        val response = mockk<ResoLinkResponse>(relaxed = true)

        coEvery {
            getResoLinkUseCase.getResolutionLink(
                captureLambda(),
                any(),
                any(),
                any()
            )
        } coAnswers {
            firstArg<(ResoLinkResponse) -> Unit>().invoke(response)
        }

        val mockOrderData =
            mockk<List<ResoLinkResponse.GetResolutionLink.ResolutionLinkData.Order>>(relaxed = true)

        val resoList = mutableListOf(ResoLinkResponse.GetResolutionLink.ResolutionLinkData.Order.Reso("123"))

        val dynamicLink = "/123"

        every {
            response.getResolutionLink?.resolutionLinkData?.orderList
        } returns mockOrderData

        every {
            mockOrderData.get(0).resoList
        } returns resoList

        every {
            mockOrderData.get(0).dynamicLink
        } returns dynamicLink

        viewModel.checkLinkForRedirectionForStickyActionClick("123", "123")

        assertTrue(viewModel.stickyActionClick.value == true)
        assertTrue(viewModel.stickyActionGoToWebView.value == getEnvResoLink(dynamicLink))
    }

    @Test
    fun `checkToSendMessageForResoLink failure`() {
        coEvery {
            getResoLinkUseCase.getResolutionLink(
                any(),
                captureLambda(),
                any(),
                any()
            )
        } coAnswers {
            secondArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        viewModel.checkToSendMessageForResoLink("123", "456")

        assertTrue(viewModel.resoLinkError.value == true)
    }

    @Test
    fun `checkToSendMessageForResoLink success when resoList is empty, don't send message`() {
        val response = mockk<ResoLinkResponse>(relaxed = true)

        coEvery {
            getResoLinkUseCase.getResolutionLink(
                captureLambda(),
                any(),
                any(),
                any()
            )
        } coAnswers {
            firstArg<(ResoLinkResponse) -> Unit>().invoke(response)
        }

        val mockOrderData =
            mockk<List<ResoLinkResponse.GetResolutionLink.ResolutionLinkData.Order>>(relaxed = true)

        val resoList = mutableListOf<ResoLinkResponse.GetResolutionLink.ResolutionLinkData.Order.Reso>()

        every {
            response.getResolutionLink?.resolutionLinkData?.orderList
        } returns mockOrderData

        every {
            mockOrderData.get(0).resoList
        } returns resoList

        viewModel.checkToSendMessageForResoLink("123", "456")

        assertTrue(viewModel.toSendTextMessageForResoComponent.value == false)
    }

    @Test
    fun `checkToSendMessageForResoLink success when resoList is not empty, send message`() {
        val response = mockk<ResoLinkResponse>(relaxed = true)

        coEvery {
            getResoLinkUseCase.getResolutionLink(
                captureLambda(),
                any(),
                any(),
                any()
            )
        } coAnswers {
            firstArg<(ResoLinkResponse) -> Unit>().invoke(response)
        }

        val mockOrderData =
            mockk<List<ResoLinkResponse.GetResolutionLink.ResolutionLinkData.Order>>(relaxed = true)

        val resoList = mutableListOf(ResoLinkResponse.GetResolutionLink.ResolutionLinkData.Order.Reso("123"))

        every {
            response.getResolutionLink?.resolutionLinkData?.orderList
        } returns mockOrderData

        every {
            mockOrderData.get(0).resoList
        } returns resoList

        viewModel.checkToSendMessageForResoLink("123", "456")

        assertTrue(viewModel.toSendTextMessageForResoComponent.value == true)
    }

    @Test
    fun `hitGqlforOptionList failure`() {
        coEvery {
            chipSubmitHelpfulQuestionsUseCase.chipSubmitHelpfulQuestions(captureLambda(), any(), any())
        } answers {
            firstArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        viewModel.hitGqlforOptionList("123", 1, null)

        assertTrue(viewModel.optionsListError.value == true)
    }

    @Test
    fun `hitGqlforOptionList failure with model not null`() {
        coEvery {
            chipSubmitHelpfulQuestionsUseCase.chipSubmitHelpfulQuestions(captureLambda(), any(), any())
        } answers {
            firstArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        viewModel.hitGqlforOptionList(
            "123",
            1,
            HelpFullQuestionsUiModel(
                "", "", "", "", "", "", "",
                "", HelpFullQuestionPojo.HelpfulQuestion("", "", emptyList(), 1)
            )
        )

        assertTrue(viewModel.optionsListError.value == true)
    }

    @Test
    fun `hitGqlforOptionList failure with HelpFullQuestionsUiModel helpfulQuestion caseChatId  null`() {
        coEvery {
            chipSubmitHelpfulQuestionsUseCase.chipSubmitHelpfulQuestions(captureLambda(), any(), any())
        } answers {
            firstArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        viewModel.hitGqlforOptionList(
            "1234",
            1,
            HelpFullQuestionsUiModel(
                "", "", "", "", "", "", "",
                "", HelpFullQuestionPojo.HelpfulQuestion(null, "", emptyList(), 1)
            )
        )

        assertTrue(viewModel.optionsListError.value == true)
    }

    @Test
    fun `hitGqlforOptionList failure with HelpFullQuestionsUiModel helpfulQuestion caseId  null`() {
        coEvery {
            chipSubmitHelpfulQuestionsUseCase.chipSubmitHelpfulQuestions(captureLambda(), any(), any())
        } answers {
            firstArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        viewModel.hitGqlforOptionList(
            "123",
            1,
            HelpFullQuestionsUiModel(
                "", "", "", "", "", "", "",
                "", HelpFullQuestionPojo.HelpfulQuestion("", null, emptyList(), 1)
            )
        )
        assertTrue(viewModel.optionsListError.value == true)
    }

    @Test
    fun `hitGqlforOptionList failure with HelpFullQuestionsUiModel messageId  null`() {
        coEvery {
            chipSubmitHelpfulQuestionsUseCase.chipSubmitHelpfulQuestions(captureLambda(), any(), any())
        } answers {
            firstArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        viewModel.hitGqlforOptionList(
            "123",
            1,
            HelpFullQuestionsUiModel(
                "", "", "", "", "", "", "",
                "", HelpFullQuestionPojo.HelpfulQuestion("", "", emptyList(), 1)
            )
        )

        assertTrue(viewModel.optionsListError.value == true)
    }

    @Test
    fun `hitGqlforOptionList failure with HelpFullQuestionsUiModel is  null`() {
        coEvery {
            chipSubmitHelpfulQuestionsUseCase.chipSubmitHelpfulQuestions(captureLambda(), any(), any())
        } answers {
            firstArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        viewModel.hitGqlforOptionList(
            "123",
            1,
            HelpFullQuestionsUiModel(
                "", "", "", "", "", "", "",
                "", null, ""
            )
        )

        assertTrue(viewModel.optionsListError.value == true)
    }

    @Test
    fun `submitChatCsat success`() {
        val response = mockk<ChipSubmitChatCsatResponse>(relaxed = true)

        coEvery {
            chipSubmitChatCsatUseCase.chipSubmitChatCsat(captureLambda(), any(), any(), any())
        } coAnswers {
            firstArg<(ChipSubmitChatCsatResponse) -> Unit>().invoke(response)
        }

        viewModel.submitChatCsat("123", ChipSubmitChatCsatInput())

        assertTrue(viewModel.submitChatCsat.value is ChatbotSubmitChatCsatState.HandleChatbotSubmitChatCsatSuccess)
    }

    @Test
    fun `submitChatCsat success when toaster Message is null`() {
        val response = ChipSubmitChatCsatResponse(
            ChipSubmitChatCsatResponse.ChipSubmitChatCSAT(
                ChipSubmitChatCsatResponse.ChipSubmitChatCSAT.CsatSubmitData(
                    1,
                    null
                ),
                emptyList(),
                "",
                ""
            )
        )

        coEvery {
            chipSubmitChatCsatUseCase.chipSubmitChatCsat(captureLambda(), any(), any(), any())
        } coAnswers {
            firstArg<(ChipSubmitChatCsatResponse) -> Unit>().invoke(response)
        }

        viewModel.submitChatCsat("123", ChipSubmitChatCsatInput())

        assertTrue(viewModel.submitChatCsat.value is ChatbotSubmitChatCsatState.HandleChatbotSubmitChatCsatSuccess)
    }

    @Test
    fun `submitChatCsat success when csatSubmitData is null`() {
        val response = ChipSubmitChatCsatResponse(
            ChipSubmitChatCsatResponse.ChipSubmitChatCSAT(
                null,
                emptyList(),
                "",
                ""
            )
        )

        coEvery {
            chipSubmitChatCsatUseCase.chipSubmitChatCsat(captureLambda(), any(), any(), any())
        } coAnswers {
            firstArg<(ChipSubmitChatCsatResponse) -> Unit>().invoke(response)
        }

        viewModel.submitChatCsat("123", ChipSubmitChatCsatInput())

        assertTrue(viewModel.submitChatCsat.value is ChatbotSubmitChatCsatState.HandleChatbotSubmitChatCsatSuccess)
    }

    @Test
    fun `submitChatCsat success when ChipSubmitChatCsat is null`() {
        val response = ChipSubmitChatCsatResponse(
            null
        )

        coEvery {
            chipSubmitChatCsatUseCase.chipSubmitChatCsat(captureLambda(), any(), any(), any())
        } coAnswers {
            firstArg<(ChipSubmitChatCsatResponse) -> Unit>().invoke(response)
        }

        viewModel.submitChatCsat("123", ChipSubmitChatCsatInput())

        assertTrue(viewModel.submitChatCsat.value is ChatbotSubmitChatCsatState.HandleChatbotSubmitChatCsatSuccess)
    }

    @Test
    fun `submitChatCsat failure`() {
        coEvery {
            chipSubmitChatCsatUseCase.chipSubmitChatCsat(any(), captureLambda(), any(), any())
        } coAnswers {
            secondArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        viewModel.submitChatCsat("123", ChipSubmitChatCsatInput())

        assertTrue(viewModel.submitChatCsat.value is ChatbotSubmitChatCsatState.FailureChatbotSubmitChatCsat)
    }

    @Test
    fun `showTickerData success`() {
        val response = mockk<com.tokopedia.chatbot.chatbot2.data.TickerData.TickerDataResponse>(relaxed = true)

        coEvery {
            getTickerDataUseCase.getTickerData(captureLambda(), any(), any())
        } coAnswers {
            firstArg<(com.tokopedia.chatbot.chatbot2.data.TickerData.TickerDataResponse) -> Unit>().invoke(response)
        }

        viewModel.showTickerData("123")

        assertTrue(viewModel.tickerData.value is GetTickerDataState.SuccessTickerData)
    }

    @Test
    fun `showTickerData success with null tickerdata`() {
        val response = com.tokopedia.chatbot.chatbot2.data.TickerData.TickerDataResponse(
            com.tokopedia.chatbot.chatbot2.data.TickerData.ChipGetActiveTickerV4(
                "",
                null
            )
        )

        coEvery {
            getTickerDataUseCase.getTickerData(captureLambda(), any(), any())
        } coAnswers {
            firstArg<(com.tokopedia.chatbot.chatbot2.data.TickerData.TickerDataResponse) -> Unit>().invoke(response)
        }

        viewModel.showTickerData("123")

        assertTrue(viewModel.tickerData.value is GetTickerDataState.HandleTickerDataFailure)
    }

    @Test
    fun `showTickerData success with null ChipGetActiveTickerV4`() {
        val response = com.tokopedia.chatbot.chatbot2.data.TickerData.TickerDataResponse(
            null
        )

        coEvery {
            getTickerDataUseCase.getTickerData(captureLambda(), any(), any())
        } coAnswers {
            firstArg<(com.tokopedia.chatbot.chatbot2.data.TickerData.TickerDataResponse) -> Unit>().invoke(response)
        }

        viewModel.showTickerData("123")

        assertTrue(viewModel.tickerData.value is GetTickerDataState.HandleTickerDataFailure)
    }

    @Test
    fun `showTickerData failure`() {
        coEvery {
            getTickerDataUseCase.getTickerData(any(), captureLambda(), any())
        } coAnswers {
            secondArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        viewModel.showTickerData("123")

        assertTrue(viewModel.tickerData.value is GetTickerDataState.HandleTickerDataFailure)
    }

    @Test
    fun `submitCsatRating success`() {
        val response = mockk<com.tokopedia.chatbot.chatbot2.data.csatRating.csatResponse.SubmitCsatGqlResponse>(relaxed = true)

        coEvery {
            submitCsatRatingUseCase.submitCsatRating(captureLambda(), any(), any(), any())
        } coAnswers {
            firstArg<(com.tokopedia.chatbot.chatbot2.data.csatRating.csatResponse.SubmitCsatGqlResponse) -> Unit>().invoke(response)
        }

        viewModel.submitCsatRating(
            "123",
            com.tokopedia.chatbot.chatbot2.data.csatRating.csatInput.InputItem(
                0,
                "",
                "",
                "",
                "",
                "",
                ""
            )
        )

        assertTrue(viewModel.submitCsatRating.value is ChatbotSubmitCsatRatingState.SuccessChatbotSubmtiCsatRating)
    }

    @Test
    fun `submitCsatRating failure`() {
        coEvery {
            submitCsatRatingUseCase.submitCsatRating(any(), captureLambda(), any(), any())
        } coAnswers {
            secondArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        viewModel.submitCsatRating(
            "123",
            com.tokopedia.chatbot.chatbot2.data.csatRating.csatInput.InputItem(
                0,
                "",
                "",
                "",
                "",
                "",
                ""
            )
        )

        assertTrue(viewModel.submitCsatRating.value is ChatbotSubmitCsatRatingState.HandleFailureChatbotSubmitCsatRating)
    }

    @Test
    fun `checkUploadVideoEligibility success`() {
        val response = mockk<ChatbotUploadVideoEligibilityResponse>(relaxed = true)

        coEvery {
            chatbotVideoUploadVideoEligibilityUseCase.getVideoUploadEligibility(
                captureLambda(),
                any(),
                any()
            )
        } coAnswers {
            firstArg<(ChatbotUploadVideoEligibilityResponse) -> Unit>().invoke(response)
        }

        viewModel.checkUploadVideoEligibility("1234")

        assertTrue(
            viewModel.videoUploadEligibility.value
            is ChatbotVideoUploadEligibilityState.SuccessVideoUploadEligibility
        )
    }

    @Test
    fun `checkUploadVideoEligibility failure`() {
        coEvery {
            chatbotVideoUploadVideoEligibilityUseCase.getVideoUploadEligibility(
                captureLambda(),
                any(),
                any()
            )
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.checkUploadVideoEligibility("1234")

        assertTrue(
            viewModel.videoUploadEligibility.value
            is ChatbotVideoUploadEligibilityState.FailureVideoUploadEligibility
        )
    }

    /**
     * Video Upload UT
     * */

    @Test
    fun `startNewUploadMediaJob success when uploaderUseCase returns success`() {
        val response = mockk<UploadResult.Success>(relaxed = true)
        var result: ChatbotVideoUploadResult? = null

        coEvery {
            uploaderUseCase.invoke(any())
        } returns response

        result = ChatbotVideoUploadResult.Success("123", "456")

        viewModel.startNewUploadMediaJob(
            "https://vod-tokopedia.com/abc",
            "123456",
            "1234",
            generateChatUiModelWithVideo("abc", 111)
        )

        assertTrue(result is ChatbotVideoUploadResult.Success)
    }

    @Test
    fun `startNewUploadMediaJob failure when uploaderUseCase returns failure`() {
        val response = mockk<UploadResult.Error>(relaxed = true)
        var result: ChatbotVideoUploadResult? = null

        coEvery {
            uploaderUseCase.invoke(any())
        } returns response

        result = ChatbotVideoUploadResult.Error("123")

        viewModel.startNewUploadMediaJob(
            "https://vod-tokopedia.com/abc",
            "123456",
            "123",
            generateChatUiModelWithVideo("abc", 111)
        )

        assertTrue(result is ChatbotVideoUploadResult.Error)
        assertTrue(viewModel.videoUploadFailure.value is ChatbotVideoUploadFailureState.ChatbotVideoUploadFailure)
    }

    @Test
    fun `cancelVideoUpload success`() {
        var mediaUploadJobs = MutableStateFlow<MediaUploadJobMap>(mapOf())

        coEvery {
            uploaderUseCase.abortUpload("ABC", "123")
        } just runs

        viewModel.cancelVideoUpload("123", "ABC")

        assertNull(mediaUploadJobs.value.get("123"))
    }

    @Test
    fun `tryUploadMedia success when shouldResetFailedUpload is true`() {
        runBlockingTest {
            val data: Pair<Boolean, List<VideoUploadData>> = Pair(
                true,
                listOf(
                    VideoUploadData(
                        "123",
                        "111",
                        "456",
                        generateChatUiModelWithVideo("abc", 111)
                    )
                )
            )
            viewModel.mediaUploadResults.value = mutableMapOf<String, ChatbotVideoUploadResult>().apply {
                put("123", ChatbotVideoUploadResult.Success("444", "555"))
            }

            viewModel.tryUploadMedia(data)

            assertTrue(!viewModel.shouldResetFailedUploadStatus.value)
        }
    }

    @Test
    fun `tryUploadMedia success when shouldResetFailedUpload is false`() {
        runBlockingTest {
            val data: Pair<Boolean, List<VideoUploadData>> = Pair(
                false,
                listOf(
                    VideoUploadData(
                        "123",
                        "111",
                        "456",
                        generateChatUiModelWithVideo("abc", 111)
                    )
                )
            )
            viewModel.tryUploadMedia(data)

            assertTrue(data.second.isNotEmpty())
        }
    }

    @Test
    fun `tryUploadMedia success when shouldResetFailedUpload is false with null data`() {
        runBlockingTest {
            val data: Pair<Boolean, List<VideoUploadData>> = Pair(
                false,
                listOf(
                    VideoUploadData(
                        null,
                        "111",
                        "456",
                        generateChatUiModelWithVideo("", 120)
                    )
                )
            )
            viewModel.tryUploadMedia(data)

            assertTrue(data.second.isNotEmpty())
        }
    }

    @Test
    fun `updateMediaUris success`() {
        val data = mutableListOf<VideoUploadData>()
        data.add(VideoUploadData("123", "456", "789", generateChatUiModelWithVideo("abc", 111)))
        viewModel.updateMediaUris(data)

        assertTrue(
            viewModel.mediaUris.value.isNotEmpty()
        )
    }

    @Test
    fun `filterMediaUploadJobs success`() {
        viewModel.mediaUploadJobs.value = mutableMapOf<String, Job>().apply {
            putAll(viewModel.mediaUploadJobs.value)
        }

        val data = mutableListOf<String>()
        data.add("1234")
        data.add("5678")
        viewModel.filterMediaUploadJobs(data)

        assertTrue(viewModel.mediaUploadJobs.value.isEmpty())
    }

    /**
     * Socket Connection Related Unit Tests
     * */
    @Test
    fun `connectWebSocket success when Socket is Opened`() {
        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.SocketOpened
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        coEvery { chatbotWebSocket.connect("aaa") } just runs

        viewModel.connectWebSocket("123")

        assertNotNull(socketJob)
        assertTrue(viewModel.socketConnectionError.value is ChatbotSocketErrorState.SocketConnectionSuccessful)
        assertTrue(viewModel.isArticleEntryToSendData.value is ChatbotArticleEntryState.ToSendArticleUponEntry)
    }

    @Test
    fun `connectWebSocket - when Socket is Closed`() {
        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.Closed(1000)
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        coEvery { chatbotWebSocket.connect("aaa") } just runs

        viewModel.connectWebSocket("123")

        assertNotNull(socketJob)
    }

    @Test
    fun `connectWebSocket - when Socket is Closed with code other than 1000`() {
        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.Closed(126)
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        coEvery { chatbotWebSocket.connect("aaa") } just runs

        viewModel.connectWebSocket("123")

        assertNotNull(socketJob)
    }

    @Test
    fun `connectWebSocket - when Socket connection is Failure`() {
        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.Failure(ChatbotWebSocketException(Exception()))
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        coEvery { chatbotWebSocket.connect("123") } just runs

        viewModel.connectWebSocket("123")

        assertNotNull(socketJob)
    }

    @Test
    fun `connectWebSocket - when Socket connection Exception`() {
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } throws mockThrowable

        coEvery { chatbotWebSocket.connect("aaa") } throws mockThrowable

        viewModel.connectWebSocket("123")

        assertNull(socketJob)
    }

    @Test
    fun `connectWebSocket - when Socket receives new Message with code 103 `() {
        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.RESPONSE_WITH_103_REPLY_MESSAGE)
            )
        )

        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true

        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        coEvery { chatbotWebSocket.connect("aaa") } just runs

        viewModel.connectWebSocket("4058088")

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

        viewModel.connectWebSocket("4058088")

        assertNotNull(socketJob)
    }

    @Test
    fun `handleAttachment When receiving Code 103 for Reply Message`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.RESPONSE_WITH_103_REPLY_MESSAGE)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.RESPONSE_WITH_103_REPLY_MESSAGE)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        viewModel.handleAttachmentTypes(fullResponse, "4058088")

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

        viewModel.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
        assertTrue(viewModel.socketReceiveMessageEvent.value is ChatbotSocketReceiveEvent.StartTypingEvent)
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

        viewModel.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
    }

    @Test
    fun `handleAttachment When receiving Code NOT HANDLED`() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.RESPONSE_WITH_CODE_NOT_HANDLED)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.RESPONSE_WITH_CODE_NOT_HANDLED)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        viewModel.handleAttachmentTypes(fullResponse, "4058088")

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

        viewModel.handleAttachmentTypes(fullResponse, "4058088")

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

        viewModel.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
        assertTrue(viewModel.openCsat.value is ChatbotOpenCsatState.ShowCsat)
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

        viewModel.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
        assertTrue(viewModel.updateToolbar.value is ChatbotUpdateToolbarState.UpdateToolbar)
    }

    @Test
    fun `handleAttachment When receiving attachment type 14 to update the toolbar - attachment is null`() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.ATTACHMENT_14_UPDATE_TOOLBAR_ATTACHMENT_NULL)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.ATTACHMENT_14_UPDATE_TOOLBAR_ATTACHMENT_NULL)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        viewModel.handleAttachmentTypes(fullResponse, "4058088")

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

        viewModel.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
        assertTrue(viewModel.chatSeparator.value is ChatbotChatSeparatorState.ChatSeparator)
    }

    @Test
    fun `handleAttachment When receiving attachment type 31 for session Change with Mode Agent`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.ATTACHMENT_31_SESSION_CHANGE_WITH_MODE_AGENT)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.ATTACHMENT_31_SESSION_CHANGE_WITH_MODE_AGENT)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        viewModel.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
        assertTrue(viewModel.receiverModeAgent.value == true)
    }

    @Test
    fun `handleAttachment When receiving attachment type 31 for session Change with Mode Unknown`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.ATTACHMENT_31_SESSION_CHANGE_WITH_MODE_UNKNOWN)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.ATTACHMENT_31_SESSION_CHANGE_WITH_MODE_UNKNOWN)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        viewModel.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
    }

    @Test
    fun `handleAttachment When receiving attachment type 31 for session Change with Mode Bot`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.ATTACHMENT_31_SESSION_CHANGE_WITH_MODE_BOT)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.ATTACHMENT_31_SESSION_CHANGE_WITH_MODE_BOT)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        viewModel.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
        assertTrue(viewModel.receiverModeAgent.value == false)
    }

    @Test
    fun `cancelImageUpload success`() {
        viewModel.cancelImageUpload()

        verify {
            chatBotSecureImageUploadUseCase.unsubscribe()
        }
    }

    @Test
    fun `handleAttachment When receiving attachment type 34 for dynamic attachment`() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_CODE_100_IS_ACTIVE_TRUE_RENDER_ANDROID)

        val socketJob = MutableStateFlow<ChatbotWebSocketAction>(
            ChatbotWebSocketAction.NewMessage(
                SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_CODE_100_IS_ACTIVE_TRUE_RENDER_ANDROID)
            )
        )
        coEvery { chatbotWebSocket.getDataFromSocketAsFlow() } returns socketJob

        viewModel.handleAttachmentTypes(fullResponse, "4058088")

        assertNotNull(socketJob)
    }

    @Test
    fun `handleAttachment When receiving attachment type not available`() {
        val fullResponse = SocketResponse.getResponse(SocketResponse.ATTACHMENT_NOT_AVAILABLE_RIGHT_NOW_SHOW_FALLBACK)

        val socketJob = mockThrowable

        viewModel.handleAttachmentTypes(fullResponse, "4058088")

        assertEquals(socketJob, mockThrowable)
    }

    /**
     * Socket Send Message Unit Tests
     * */

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

        viewModel.sendMessage("", "123", "", "", null) {}

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
    fun `sendMessage without parent reply and with empty message`() {
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

        viewModel.sendMessage("", "", "", "", null) {}

        verify(exactly = 0) {
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

        viewModel.sendMessage("", "123", "", "", ParentReply()) {}

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

        viewModel.sendActionBubble("", ChatActionBubbleUiModel(), "", "")

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

        viewModel.sendQuickReplyInvoice("123", QuickReplyUiModel("", "", ""), "", "", "", "")

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

        viewModel.sendQuickReply("123", QuickReplyUiModel("", "", ""), "", "")

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

        viewModel.sendInvoiceAttachment("123", InvoiceLinkPojo(), "", "", true, "")

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

        viewModel.sendInvoiceAttachment("123", InvoiceLinkPojo(), "", "", false, "")

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
    fun `sendVideoAttachment message via socket success`() {
        mockkObject(ChatbotSendableWebSocketParam)

        every {
            ChatbotSendableWebSocketParam.generateParamSendVideoAttachment(
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        every {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendVideoAttachment(
                    any(),
                    any(),
                    any()
                ),
                any()
            )
        } just runs

        viewModel.sendVideoAttachment("", "", "")

        verify {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendVideoAttachment(
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
            ChatbotSendableWebSocketParam.getReadMessageWebSocket(any())
        } returns mockk(relaxed = true)

        every {
            chatbotWebSocket.send(ChatbotSendableWebSocketParam.getReadMessageWebSocket(any()), any())
        } just runs

        viewModel.sendReadEventWebSocket("123")

        verify {
            chatbotWebSocket.send(ChatbotSendableWebSocketParam.getReadMessageWebSocket(any()), any())
        }
    }

    @Test
    fun `sendUploadedImageToWebsocket success`() {
        every {
            chatbotWebSocket.send(any(), any())
        } returns mockk(relaxed = true)

        viewModel.sendUploadedImageToWebsocket(JsonObject())

        verify {
            chatbotWebSocket.send(
                any(),
                any()
            )
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

        viewModel.sendMessageWithWebsocket("123", "", "", "")

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

    /**
     * Get Chat Unit Tests
     * */

    @Test
    fun `getBottomChat failure`() {
        val exception = mockk<Exception>()

        coEvery {
            getExistingChatUseCase.getBottomChat(any())
        } throws exception

        viewModel.getBottomChat("123")

        assertTrue(viewModel.bottomChatData.value is ChatDataState.HandleFailureChatData)
    }

    @Test
    fun `getTopChat throws exception - failure`() {
        val exception = mockk<Exception>()

        coEvery {
            getExistingChatUseCase.getTopChat(any())
        } throws exception

        viewModel.getTopChat("123")

        assertTrue(viewModel.topChatData.value is ChatDataState.HandleFailureChatData)
    }

    @Test
    fun `getExistingChat Failure`() {
        val exception = mockk<Exception>()

        coEvery {
            getExistingChatUseCase.getFirstPageChat(any())
        } throws exception

        viewModel.getExistingChat("123")

        assertTrue(
            viewModel.existingChatData.value is ChatDataState.HandleFailureChatData
        )
    }

    @Test
    fun `getChatRatingList failure`() {
        val exception = mockk<Exception>()
        coEvery {
            chipGetChatRatingListUseCase.getChatRatingList(any())
        } throws exception

        viewModel.getChatRatingList(
            ChipGetChatRatingListInput(),
            "123",
            ChatReplies(),
            ChatroomViewModel(),
            mockk()
        )

        assertTrue(true)
    }

    @Test
    fun `getBottomChat success when chat ratings list is empty`() {
        val expectedResponse = GetExistingChatPojo()
        val chatRoomViewModel = ChatroomViewModel()
        val chatReplies = expectedResponse.chatReplies
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

        var chatRatingListInput = ChipGetChatRatingListInput(mutableListOf(mockk(relaxed = true)))

        coEvery {
            getExistingChatUseCase.getBottomChat(any())
        } returns expectedResponse

        every {
            getExistingChatMapper.map(expectedResponse)
        } returns chatRoomViewModel

        coEvery {
            viewModel.getChatRatingList(chatRatingListInput, "123456", chatReplies, chatRoomViewModel, mockk())
        } just runs

        viewModel.getBottomChat("123456")

        verify {
            getExistingChatMapper.map(expectedResponse)
        }
    }

    @Test
    fun `getBottomChat success when chat ratings list is empty 2`() {
        val expectedResponse = GetExistingChatPojo()
        val chatRoomViewModel = ChatroomViewModel()
        coEvery {
            getExistingChatUseCase.getBottomChat(any())
        } returns expectedResponse

        every {
            getExistingChatMapper.map(expectedResponse)
        } returns chatRoomViewModel

        viewModel.getBottomChat("123456")

        verify {
            getExistingChatMapper.map(expectedResponse)
        }
    }

    @Test
    fun `getTopChat success when chat ratings list is not empty`() {
        mockkObject(ChatbotNewRelicLogger::class)
        val expectedResponse = GetExistingChatPojo()
        val chatRoomViewModel = ChatroomViewModel()
        val chatReplies = expectedResponse.chatReplies
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

        var chatRatingListInput = ChipGetChatRatingListInput(mutableListOf(mockk(relaxed = true)))

        coEvery {
            getExistingChatUseCase.getTopChat(any())
        } returns expectedResponse

        every {
            getExistingChatMapper.map(expectedResponse)
        } returns chatRoomViewModel

        coEvery {
            viewModel.getChatRatingList(chatRatingListInput, "123456", chatReplies, chatRoomViewModel, mockk())
        } just runs

        viewModel.getTopChat("123456")

        verify {
            getExistingChatMapper.map(expectedResponse)
        }
    }

    @Test
    fun `getTopChat success when chat ratings list is empty`() {
        mockkObject(ChatbotNewRelicLogger::class)
        val expectedResponse = GetExistingChatPojo()
        val chatRoomViewModel = ChatroomViewModel()

        coEvery {
            getExistingChatUseCase.getTopChat(any())
        } returns expectedResponse

        every {
            getExistingChatMapper.map(expectedResponse)
        } returns chatRoomViewModel

        viewModel.getTopChat("123456")

        verify {
            getExistingChatMapper.map(expectedResponse)
        }
    }

    @Test
    fun `getExistingChat success when chat ratings list is empty `() {
        mockkObject(ChatbotNewRelicLogger::class)
        val expectedResponse = GetExistingChatPojo()
        val chatRoomViewModel = ChatroomViewModel()

        coEvery {
            getExistingChatUseCase.getFirstPageChat(any())
        } returns expectedResponse

        every {
            getExistingChatMapper.map(expectedResponse)
        } returns chatRoomViewModel

        viewModel.getExistingChat("123456")

        verify {
            getExistingChatMapper.map(expectedResponse)
        }
    }

    @Test
    fun `getExistingChat success when chat ratings list is not empty`() {
        mockkObject(ChatbotNewRelicLogger::class)
        val expectedResponse = GetExistingChatPojo()
        val chatRoomViewModel = ChatroomViewModel()
        val chatReplies = expectedResponse.chatReplies
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

        coEvery {
            getExistingChatUseCase.getFirstPageChat(any())
        } returns expectedResponse

        every {
            getExistingChatMapper.map(expectedResponse)
        } returns chatRoomViewModel

        viewModel.getExistingChat("123456")

        verify {
            getExistingChatMapper.map(expectedResponse)
        }
    }

    @Test
    fun `getBottomChat MessageId is empty`() {
        coEvery {
            getExistingChatUseCase.getBottomChat(any())
        }

        viewModel.getBottomChat("")

        verify(exactly = 0) {
            getExistingChatMapper.map(any())
        }
    }

    @Test
    fun `getTopChat MessageId is empty`() {
        coEvery {
            getExistingChatUseCase.getTopChat(any())
        }

        viewModel.getTopChat("")

        verify(exactly = 0) {
            getExistingChatMapper.map(any())
        }
    }

    @Test
    fun `getExistingChat MessageId is empty`() {
        coEvery {
            getExistingChatUseCase.getFirstPageChat(any())
        }

        viewModel.getExistingChat("")

        verify(exactly = 0) {
            getExistingChatMapper.map(any())
        }
    }

    @Test
    fun `getChatRatingList success`() {
        val response = mockk<GraphqlResponse>(relaxed = true)
        val ratingListResponse = ChipGetChatRatingListResponse(
            ChipGetChatRatingListResponse.ChipGetChatRatingList(
                ChipGetChatRatingListResponse.ChipGetChatRatingList.RatingListData(
                    1,
                    emptyList()
                ),
                listOf("Some random Error occured"),
                "123",
                "DONE"
            )
        )

        coEvery {
            chipGetChatRatingListUseCase.getChatRatingList(any())
        } returns response

        every {
            response.getData<ChipGetChatRatingListResponse>(ChipGetChatRatingListResponse::class.java)
        } returns ratingListResponse

        viewModel.updateMappedPojo(ChatroomViewModel(), ratingListResponse.chipGetChatRatingList)

        viewModel.getChatRatingList(
            ChipGetChatRatingListInput(),
            "123",
            ChatReplies(),
            ChatroomViewModel(),
            mockk()
        )

        assertTrue(ratingListResponse.chipGetChatRatingList?.ratingListData?.isSuccess == 1L)
    }

    @Test
    fun `uploadImageSecureUpload - onError`() {
        every {
            chatBotSecureImageUploadUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onError(
                Throwable("error")
            )
        }

        viewModel.uploadImageSecureUpload(
            ImageUploadUiModel(ImageUploadUiModel.Builder()),
            "ADC",
            "1234"
        )

        assertTrue(
            viewModel.imageUploadFailureData.value is ChatbotImageUploadFailureState.ImageUploadErrorBody
        )
    }

    @Test
    fun `uploadImageSecureUpload - onCompleted`() {
        mockkStatic(Timber::class)

        every {
            chatBotSecureImageUploadUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onCompleted()
        }

        viewModel.uploadImageSecureUpload(
            ImageUploadUiModel(ImageUploadUiModel.Builder()),
            "ADC",
            "1234"
        )

        verify {
            Timber.d(any() as String)
        }
    }

    @Test
    fun `uploadImageSecureUpload - onNext`() {
        val restResponse = RestResponse(UploadSecureResponse(UploadSecureResponse.UploadSecureData("123"), "123", 123.0, "123", 1L), 200, false)
        val uploadSecureResponse = mapOf<Type, RestResponse>(
            UploadSecureResponse::class.java to restResponse
        )

        every {
            chatBotSecureImageUploadUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                uploadSecureResponse
            )
        }

        viewModel.uploadImageSecureUpload(
            ImageUploadUiModel(ImageUploadUiModel.Builder()),
            "ADC",
            "1234"
        )

        assertTrue(true)
        verify {
            viewModel.sendUploadedImageToWebsocket(
                any()
            )
        }
    }

    /**
     * Utility method Unit Tests
     * */

    @Test
    fun `updateMappedPojo if ratingListData isSuccess is not 1`() {
        val ratings = ChipGetChatRatingListResponse.ChipGetChatRatingList(
            ChipGetChatRatingListResponse.ChipGetChatRatingList.RatingListData(
                0,
                emptyList()
            ),
            listOf("Some random Error occured"),
            "123",
            "DONE"
        )

        viewModel.updateMappedPojo(ChatroomViewModel(), ratings)

        assertTrue(
            viewModel.ratingListMessageError.value is ChatRatingListState.HandleFailureChatRatingList
        )
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

        viewModel.getChatRatingData(chatRoomViewModel)

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

        viewModel.getChatRatingData(chatRoomViewModel)

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

        viewModel.getChatRatingData(chatRoomViewModel)

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

        viewModel.getChatRatingData(chatRoomViewModel)

        assertEquals(1, input.list.size)
        assertTrue(input.list.firstOrNull() is ChipGetChatRatingListInput.ChatRating)
    }

    @Test
    fun `updateMappedPojo Success with Rating 1 and 2`() {
        val ratings = ChipGetChatRatingListResponse.ChipGetChatRatingList(
            ChipGetChatRatingListResponse.ChipGetChatRatingList.RatingListData(
                1,
                listOf(
                    ChipGetChatRatingListResponse.ChipGetChatRatingList.RatingListData.RatingList(
                        "1",
                        true,
                        "",
                        22
                    ),
                    ChipGetChatRatingListResponse.ChipGetChatRatingList.RatingListData.RatingList(
                        "2",
                        false,
                        "",
                        22
                    )
                )
            ),
            listOf("Some random Error occured"),
            "123",
            "DONE"
        )

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

        chatRoomViewModel.listChat.add(
            ChatRatingUiModel()
        )

        viewModel.updateMappedPojo(chatRoomViewModel, ratings)

        assertTrue(true)
    }

    @Test
    fun `updateMappedPojo Success with Rating 1 and 2 - 2`() {
        val ratings = ChipGetChatRatingListResponse.ChipGetChatRatingList(
            ChipGetChatRatingListResponse.ChipGetChatRatingList.RatingListData(
                1,
                listOf(
                    ChipGetChatRatingListResponse.ChipGetChatRatingList.RatingListData.RatingList(
                        "1",
                        true,
                        "",
                        22
                    ),
                    ChipGetChatRatingListResponse.ChipGetChatRatingList.RatingListData.RatingList(
                        "2",
                        false,
                        "",
                        22
                    )
                )
            ),
            listOf("Some random Error occured"),
            "123",
            "DONE"
        )

        val chatRoomViewModel = ChatroomViewModel()
        chatRoomViewModel.listChat.add(
            HelpFullQuestionsUiModel(
                "", "", "", "", "",
                "", "", "",
                HelpFullQuestionPojo.HelpfulQuestion("1", "", emptyList(), 1)
            )
        )
        chatRoomViewModel.listChat.add(
            CsatOptionsUiModel(
                "", "", "", "", "", "", "",
                "", CsatAttributesPojo.Csat("1", "", emptyList(), "", listOf(), "", "", 123)
            )
        )

        chatRoomViewModel.listChat.add(
            ChatRatingUiModel()
        )

        viewModel.updateMappedPojo(chatRoomViewModel, ratings)

        assertTrue(true)
    }

    @Test
    fun `updateMappedPojo Success with Rating 1 and 2 - 3`() {
        val ratings = ChipGetChatRatingListResponse.ChipGetChatRatingList(
            ChipGetChatRatingListResponse.ChipGetChatRatingList.RatingListData(
                1,
                listOf(
                    ChipGetChatRatingListResponse.ChipGetChatRatingList.RatingListData.RatingList(
                        "1",
                        true,
                        "",
                        22
                    ),
                    ChipGetChatRatingListResponse.ChipGetChatRatingList.RatingListData.RatingList(
                        "2",
                        true,
                        "",
                        23
                    )
                )
            ),
            listOf("Some random Error occured"),
            "123",
            "DONE"
        )

        val chatRoomViewModel = ChatroomViewModel()
        chatRoomViewModel.listChat.add(
            HelpFullQuestionsUiModel(
                "", "", "", "", "",
                "", "", "",
                HelpFullQuestionPojo.HelpfulQuestion("5", "", emptyList(), 1)
            )
        )
        chatRoomViewModel.listChat.add(
            CsatOptionsUiModel(
                "", "", "", "", "", "", "",
                "", CsatAttributesPojo.Csat("2", "", emptyList(), "", listOf(), "", "", 123), "", true
            )
        )

        chatRoomViewModel.listChat.add(
            ChatRatingUiModel()
        )

        viewModel.updateMappedPojo(chatRoomViewModel, ratings)

        assertTrue(true)
    }

    @Test
    fun `mapToVisitable success`() {
        val mockkVisitable = mockk<Visitable<*>>(relaxed = true)

        every {
            chatBotWebSocketMessageMapper.map(any())
        } returns mockkVisitable

        viewModel.mapToVisitable(ChatSocketPojo())

        assertNotNull(mockkVisitable)
    }

    @Test
    fun `clearGetChatUseCase success`() {
        val expectedMinReplyTime = ""

        every {
            getExistingChatUseCase.reset()
        } just runs

        viewModel.clearGetChatUseCase()

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

        viewModel.setBeforeReplyTime("123")

        assertEquals(
            expectedBeforeReplyTime,
            "123"
        )
    }

    @Test
    fun `getLiveChatQuickReply success`() {
        val list = QuickReplyAttachmentAttributes(
            quickReplies = listOf(
                QuickReplyPojo("1", "2", "3"),
                QuickReplyPojo("4", "5", "6")
            )
        )

        val ans = mutableListOf<QuickReplyUiModel>()

        if (list.quickReplies.get(0).text.isNotEmpty()) {
            val curr = list.quickReplies.get(0)
            ans.add(QuickReplyUiModel(curr.text, curr.value, curr.action))
        }

        viewModel.getLiveChatQuickReply()

        assertTrue(ans.size == 1)
    }

    @Test
    fun `liveQuickChatReply`() {
        val method =
            viewModel.javaClass.getDeclaredMethod("addItemsToQuickReplyList", List::class.java)

        method.isAccessible = true
        val result = method.invoke(
            viewModel,
            listOf(
                QuickReplyPojo("1", "2", "3"),
                QuickReplyPojo("4", "5", "6")
            )
        ) as List<QuickReplyUiModel>

        assertEquals(2, result.size)
    }

    @Test
    fun `onCleared success`() {
        every {
            ticketListContactUsUsecase.cancelJobs()
        } just runs

        val method = viewModel::class.java.getDeclaredMethod("onCleared")
        method.isAccessible = true
        method.invoke(viewModel)

        verify { ticketListContactUsUsecase.cancelJobs() }
    }

    private fun generateChatUiModelWithVideo(video: String, totalLength: Long): VideoUploadUiModel {
        return VideoUploadUiModel.Builder().withMsgId("123")
            .withFromUid("456")
            .withAttachmentId((System.currentTimeMillis() / ChatbotConstant.ONE_SECOND_IN_MILLISECONDS).toString())
            .withAttachmentType(AttachmentType.Companion.TYPE_IMAGE_UPLOAD)
            .withReplyTime(SendableUiModel.SENDING_TEXT)
            .withStartTime(SendableUiModel.generateStartTime())
            .withVideoUrl(video)
            .withIsDummy(true)
            .withLength(totalLength)
            .build()
    }
}
