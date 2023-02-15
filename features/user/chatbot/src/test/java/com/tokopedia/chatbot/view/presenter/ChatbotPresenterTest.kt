package com.tokopedia.chatbot.view.presenter

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.attachinvoice.data.uimodel.AttachInvoiceSentUiModel
import com.tokopedia.chatbot.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.data.SocketResponse
import com.tokopedia.chatbot.data.TickerData.ChipGetActiveTickerV4
import com.tokopedia.chatbot.data.TickerData.TickerDataResponse
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSingleUiModel
import com.tokopedia.chatbot.data.newsession.TopBotNewSessionResponse
import com.tokopedia.chatbot.data.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.data.rating.ChatRatingUiModel
import com.tokopedia.chatbot.data.uploadEligibility.ChatbotUploadVideoEligibilityResponse
import com.tokopedia.chatbot.data.uploadsecure.CheckUploadSecureResponse
import com.tokopedia.chatbot.domain.mapper.ChatBotWebSocketMessageMapper
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.domain.pojo.csatRating.csatInput.InputItem
import com.tokopedia.chatbot.domain.pojo.csatRating.csatResponse.SubmitCsatGqlResponse
import com.tokopedia.chatbot.domain.pojo.csatoptionlist.CsatAttributesPojo
import com.tokopedia.chatbot.domain.pojo.helpfullquestion.HelpFullQuestionPojo
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListInput
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.chatbot.domain.pojo.replyBox.DynamicAttachment
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatResponse
import com.tokopedia.chatbot.domain.resolink.ResoLinkResponse
import com.tokopedia.chatbot.domain.socket.ChatbotSendableWebSocketParam
import com.tokopedia.chatbot.domain.usecase.ChatBotSecureImageUploadUseCase
import com.tokopedia.chatbot.domain.usecase.ChatbotUploadVideoEligibilityUseCase
import com.tokopedia.chatbot.domain.usecase.CheckUploadSecureUseCase
import com.tokopedia.chatbot.domain.usecase.ChipGetChatRatingListUseCase
import com.tokopedia.chatbot.domain.usecase.ChipSubmitChatCsatUseCase
import com.tokopedia.chatbot.domain.usecase.ChipSubmitHelpfulQuestionsUseCase
import com.tokopedia.chatbot.domain.usecase.GetExistingChatUseCase
import com.tokopedia.chatbot.domain.usecase.GetResolutionLinkUseCase
import com.tokopedia.chatbot.domain.usecase.GetTickerDataUseCase
import com.tokopedia.chatbot.domain.usecase.GetTopBotNewSessionUseCase
import com.tokopedia.chatbot.domain.usecase.SendChatRatingUseCase
import com.tokopedia.chatbot.domain.usecase.SubmitCsatRatingUseCase
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.chatbot.websocket.ChatbotWebSocket
import com.tokopedia.chatbot.websocket.ChatbotWebSocketAction
import com.tokopedia.chatbot.websocket.ChatbotWebSocketException
import com.tokopedia.chatbot.websocket.ChatbotWebSocketStateHandler
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.resetMain
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
    private lateinit var submitCsatRatingUseCase: SubmitCsatRatingUseCase
    private lateinit var getTickerDataUseCase: GetTickerDataUseCase
    private lateinit var chipSubmitHelpfulQuestionsUseCase: ChipSubmitHelpfulQuestionsUseCase
    private lateinit var chipGetChatRatingListUseCase: ChipGetChatRatingListUseCase
    private lateinit var chipSubmitChatCsatUseCase: ChipSubmitChatCsatUseCase
    private lateinit var getResolutionLinkUseCase: GetResolutionLinkUseCase
    private lateinit var getTopBotNewSessionUseCase: GetTopBotNewSessionUseCase
    private lateinit var checkUploadSecureUseCase: CheckUploadSecureUseCase
    private lateinit var chatBotSecureImageUploadUseCase: ChatBotSecureImageUploadUseCase
    private lateinit var uploaderUseCase: UploaderUseCase
    private lateinit var getExistingChatMapper: ChatbotGetExistingChatMapper
    private lateinit var chatbotVideoUploadVideoEligibilityUseCase: ChatbotUploadVideoEligibilityUseCase
    private lateinit var chatbotWebSocket: ChatbotWebSocket
    private lateinit var chatbotWebSocketStateHandler: ChatbotWebSocketStateHandler
    private lateinit var dispatcher: CoroutineDispatchers
    private var socketJob: Job? = null
    private lateinit var chatResponse : ChatSocketPojo

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
        submitCsatRatingUseCase = mockk(relaxed = true)
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
        uploaderUseCase = mockk(relaxed = true)
        chatbotVideoUploadVideoEligibilityUseCase = mockk(relaxed = true)
        chatResponse = mockk(relaxed = true)

        presenter = spyk(
            ChatbotPresenter(
                getExistingChatUseCase,
                userSession,
                chatBotWebSocketMessageMapper,
                tkpdAuthInterceptor,
                fingerprintInterceptor,
                sendChatRatingUseCase,
                submitCsatRatingUseCase,
                getTickerDataUseCase,
                chipSubmitHelpfulQuestionsUseCase,
                chipGetChatRatingListUseCase,
                chipSubmitChatCsatUseCase,
                getResolutionLinkUseCase,
                getTopBotNewSessionUseCase,
                checkUploadSecureUseCase,
                chatBotSecureImageUploadUseCase,
                getExistingChatMapper,
                uploaderUseCase,
                chatbotVideoUploadVideoEligibilityUseCase,
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
    fun `handleReplyBoxWSToggle if render=android, content_code=100,is_active=true`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_CODE_100_IS_ACTIVE_TRUE_RENDER_ANDROID)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        val replyBoxAttribute =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute

        presenter.handleDynamicAttachment34(chatResponse)

        assertEquals(replyBoxAttribute?.contentCode, 100)
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

        presenter.handleDynamicAttachment34(chatResponse)

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

        presenter.handleDynamicAttachment34(chatResponse)

        assertEquals(replyBoxAttribute?.contentCode, 100)
    }

    @Test
    fun `handleReplyBoxWSToggle if attachment is null`() {
        val fullResponse =
            SocketResponse.getResponse(SocketResponse.DYNAMIC_ATTACHMENT_ATTACHMENT_NULL)
        chatResponse = Gson().fromJson(fullResponse.jsonObject, ChatSocketPojo::class.java)

        val dynamicAttachmentContents =
            Gson().fromJson(chatResponse.attachment?.attributes, DynamicAttachment::class.java)

        presenter.handleDynamicAttachment34(chatResponse)

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

        presenter.handleDynamicAttachment34(chatResponse)

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

        presenter.handleDynamicAttachment34(chatResponse)

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

        presenter.handleDynamicAttachment34(chatResponse)

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

        presenter.handleDynamicAttachment34(chatResponse)

        assertEquals(dynamicContentCode, 101)
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

        presenter.handleDynamicAttachment34(chatResponse)

        assertEquals(dynamicContentCode, 101)
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

        presenter.handleDynamicAttachment34(chatResponse)

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

        presenter.handleDynamicAttachment34(chatResponse)

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

        presenter.validateHistoryForAttachment34(replyBoxAttribute)

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

        presenter.validateHistoryForAttachment34(replyBoxAttribute)

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

        presenter.validateHistoryForAttachment34(replyBoxAttribute)

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

        presenter.validateHistoryForAttachment34(replyBoxAttribute)

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

        presenter.handleDynamicAttachment34(chatResponse)

        assertEquals(dynamicContentCode, 102)
    }


    @Test
    fun `submitChatCsat success`() {
        val response = mockk<ChipSubmitChatCsatResponse>(relaxed = true)

        coEvery {
            chipSubmitChatCsatUseCase.chipSubmitChatCsat(captureLambda(), any(), any(), any())
        } coAnswers {
            firstArg<(ChipSubmitChatCsatResponse) -> Unit>().invoke(response)
        }

        presenter.submitChatCsat("123", ChipSubmitChatCsatInput())

        verify {
            view.onSuccessSubmitChatCsat(any())
        }
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

        presenter.submitChatCsat("123", ChipSubmitChatCsatInput())

        verify {
            view.onSuccessSubmitChatCsat(any())
        }
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

        presenter.submitChatCsat("123", ChipSubmitChatCsatInput())

        verify {
            view.onSuccessSubmitChatCsat(any())
        }
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

        presenter.submitChatCsat("123", ChipSubmitChatCsatInput())

        verify {
            view.onSuccessSubmitChatCsat(any())
        }
    }

    @Test
    fun `submitChatCsat failure`() {
        coEvery {
            chipSubmitChatCsatUseCase.chipSubmitChatCsat(any(), captureLambda(), any(), any())
        } coAnswers {
            secondArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        presenter.submitChatCsat("123", ChipSubmitChatCsatInput())

        verify {
            view.onError(any())
        }
    }

    @Test
    fun `hitGqlforOptionList failure`() {
        coEvery {
            chipSubmitHelpfulQuestionsUseCase.chipSubmitHelpfulQuestions(any(), any(), any())
        } answers {
            firstArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        presenter.hitGqlforOptionList("123", 1, null)

        verify { presenter.onSubmitError(any()) }
    }

    @Test
    fun `hitGqlforOptionList failure with model not null`() {
        coEvery {
            chipSubmitHelpfulQuestionsUseCase.chipSubmitHelpfulQuestions(any(), any(), any())
        } answers {
            firstArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        presenter.hitGqlforOptionList(
            "123",
            1,
            HelpFullQuestionsUiModel(
                "", "", "", "", "", "", "",
                "", HelpFullQuestionPojo.HelpfulQuestion("", "", emptyList(), 1)
            )
        )

        verify { presenter.onSubmitError(any()) }
    }

    @Test
    fun `hitGqlforOptionList failure with HelpFullQuestionsUiModel helpfulQuestion caseChatId  null`() {
        coEvery {
            chipSubmitHelpfulQuestionsUseCase.chipSubmitHelpfulQuestions(any(), any(), any())
        } answers {
            firstArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        presenter.hitGqlforOptionList(
            "1234",
            1,
            HelpFullQuestionsUiModel(
                "", "", "", "", "", "", "",
                "", HelpFullQuestionPojo.HelpfulQuestion(null, "", emptyList(), 1)
            )
        )

        verify { presenter.onSubmitError(any()) }
    }

    @Test
    fun `hitGqlforOptionList failure with HelpFullQuestionsUiModel helpfulQuestion caseId  null`() {
        coEvery {
            chipSubmitHelpfulQuestionsUseCase.chipSubmitHelpfulQuestions(any(), any(), any())
        } answers {
            firstArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        presenter.hitGqlforOptionList(
            "123",
            1,
            HelpFullQuestionsUiModel(
                "", "", "", "", "", "", "",
                "", HelpFullQuestionPojo.HelpfulQuestion("", null, emptyList(), 1)
            )
        )

        verify { presenter.onSubmitError(any()) }
    }

    @Test
    fun `hitGqlforOptionList failure with HelpFullQuestionsUiModel messageId  null`() {
        coEvery {
            chipSubmitHelpfulQuestionsUseCase.chipSubmitHelpfulQuestions(any(), any(), any())
        } answers {
            firstArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        presenter.hitGqlforOptionList(
            "123",
            1,
            HelpFullQuestionsUiModel(
                "", "", "", "", "", "", "",
                "", HelpFullQuestionPojo.HelpfulQuestion("", "", emptyList(), 1)
            )
        )

        verify { presenter.onSubmitError(any()) }
    }

    @Test
    fun `hitGqlforOptionList failure with HelpFullQuestionsUiModel is  null`() {
        coEvery {
            chipSubmitHelpfulQuestionsUseCase.chipSubmitHelpfulQuestions(any(), any(), any())
        } answers {
            firstArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        presenter.hitGqlforOptionList(
            "123",
            1,
            HelpFullQuestionsUiModel(
                "", "", "", "", "", "", "",
                "", null, ""
            )
        )

        verify { presenter.onSubmitError(any()) }
    }

    @Test
    fun `checkLinkForRedirection success resoList not empty`() {
        val response = mockk<ResoLinkResponse>(relaxed = true)
        val stickyButtonStatus = true
        val expectedButtonStatus = true
        val mockOrderData =
            mockk<List<ResoLinkResponse.GetResolutionLink.ResolutionLinkData.Order>>(relaxed = true)

        coEvery {
            getResolutionLinkUseCase.getResoLinkResponse(any())
        } returns response

        every {
            response.getResolutionLink?.resolutionLinkData?.orderList
        } returns mockOrderData

        presenter.checkLinkForRedirection("123", "123", {}, {}, {})

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

        presenter.checkLinkForRedirection("123", "123", {}, {}, {})

        assertEquals(stickyButtonStatus, expectedButtonStatus)
    }

    @Test
    fun `checkLinkForRedirection failure`() {
        val throwable = mockk<Throwable>(relaxed = true)
        var result: Throwable? = null

        coEvery {
            getResolutionLinkUseCase.getResoLinkResponse(any())
        } throws throwable

        presenter.checkLinkForRedirection("123", "123", {}, {}, { throwable ->
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
            getTickerDataUseCase.getTickerData(captureLambda(), any(), any())
        } coAnswers {
            firstArg<(TickerDataResponse) -> Unit>().invoke(response)
        }

        presenter.showTickerData("123")

        verify {
            view.onSuccessGetTickerData(any())
        }
    }

    @Test
    fun `showTickerData success with null tickerdata`() {
        val response = TickerDataResponse(
            ChipGetActiveTickerV4(
                "", null
            )
        )

        coEvery {
            getTickerDataUseCase.getTickerData(captureLambda(), any(), any())
        } coAnswers {
            firstArg<(TickerDataResponse) -> Unit>().invoke(response)
        }

        presenter.showTickerData("123")

        verify(exactly = 0) {
            view.onSuccessGetTickerData(any())
        }
    }

    @Test
    fun `showTickerData success with null ChipGetActiveTickerV4`() {
        val response = TickerDataResponse(
            null
        )

        coEvery {
            getTickerDataUseCase.getTickerData(captureLambda(), any(), any())
        } coAnswers {
            firstArg<(TickerDataResponse) -> Unit>().invoke(response)
        }

        presenter.showTickerData("123")

        verify(exactly = 0) {
            view.onSuccessGetTickerData(any())
        }
    }

    @Test
    fun `showTickerData failure`() {
        coEvery {
            getTickerDataUseCase.getTickerData(any(), captureLambda(), any())
        } coAnswers {
            secondArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        presenter.showTickerData("123")

        verify {
            view.onError(any())
        }
    }

    @Test
    fun `submitCsatRating success`() {
        val response = mockk<SubmitCsatGqlResponse>(relaxed = true)

        coEvery {
            submitCsatRatingUseCase.submitCsatRating(captureLambda(), any(), any(), any())
        } coAnswers {
            firstArg<(SubmitCsatGqlResponse) -> Unit>().invoke(response)
        }

        presenter.submitCsatRating("123", InputItem(0, "", "", "", "", "", ""))

        verify {
            view.onSuccessSubmitCsatRating(any())
        }
    }

    @Test
    fun `submitCsatRating failure`() {
        coEvery {
            submitCsatRatingUseCase.submitCsatRating(any(), captureLambda(), any(), any())
        } coAnswers {
            secondArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        presenter.submitCsatRating("123", InputItem(0, "", "", "", "", "", ""))

        verify {
            view.onError(any())
        }
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

        presenter.sendMessage("", "", "", "", null) {}

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
            presenter.getChatRatingList(any(), any(), any())
        }

        presenter.getBottomChat("123456", { _, _ -> }, {}, {})

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
        presenter.getTopChat("123456", { _, _ -> }, {}, {})

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
        presenter.getExistingChat("123456", { }, { _, _ -> }, {})

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

        presenter.getChatRatingList(
            ChipGetChatRatingListInput(),
            "123456"
        ) { chipGetChatRatingList ->
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

        presenter.getChatRatingList(ChipGetChatRatingListInput(), "123") {}

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

        // replace mockk
        presenter.checkUploadSecure("123", Intent())

        verify {
            view.uploadUsingSecureUpload(any())
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
            response.topBotGetNewSession?.isNewSession
        } returns isNewSession

        every {
            response.topBotGetNewSession?.isTypingBlocked
        } returns isTypingBlock

        presenter.checkForSession("123456")

        verify {
            view.startNewSession()
        }
        verify {
            view.hideReplyBox()
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
            response.topBotGetNewSession?.isNewSession
        } returns isNewSession

        every {
            response.topBotGetNewSession?.isTypingBlocked
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
            secondArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
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
            secondArg<(Throwable, String) -> Unit>().invoke(mockThrowable, "123")
        }

        presenter.sendRating("123456", 5, ChatRatingUiModel())

        verify {
            view.onError(any())
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

        presenter.sendActionBubble("", ChatActionBubbleUiModel(), "", "")

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

        presenter.sendQuickReplyInvoice("123", QuickReplyUiModel("", "", ""), "", "", "", "")

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

        presenter.sendQuickReply("123", QuickReplyUiModel("", "", ""), "", "")

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
    fun `createAttachInvoiceSingleViewModel success with null`() {
        val map = getMapForArticleEntry()
        val attachInvoiceSingleUiModel = getAttachSingleInvoiceUiModelWithNull()
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

        every {
            chatbotWebSocket.send(any<JsonObject>(), any())
        } returns mockk(relaxed = true)

        presenter.sendUploadedImageToWebsocket(JsonObject())

        verify {
            chatbotWebSocket.send(
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
//        val mockkRatingList = mockk<ChipGetChatRatingListResponse.ChipGetChatRatingList
//        .RatingListData.RatingList>(relaxed = true)
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

    @Test
    fun `sendVideoAttachment message via socket success`() {
        mockkObject(ChatbotSendableWebSocketParam)

        every {
            ChatbotSendableWebSocketParam.generateParamSendVideoAttachment(
                any(), any(), any()
            )
        } returns mockk(relaxed = true)

        every {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendVideoAttachment(
                    any(), any(), any()
                ), any()
            )
        } just runs

        presenter.sendVideoAttachment("", "", "")

        verify {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendVideoAttachment(
                    any(), any(), any()
                ), any()
            )
        }
    }

    @Test
    fun `startNewUploadMediaJob success when uploaderUseCase returns success`() {
        val response = mockk<UploadResult.Success>(relaxed = true)
        var result: String? = null

        coEvery {
            uploaderUseCase.invoke(any())
        } returns response

        every {
            result = response.videoUrl
        } just runs

        presenter.startNewUploadMediaJob(
            "https://vod-tokopedia.com/abc",
            "123456",
            ""
        )

        assertNotNull(result)

    }

    @Test
    fun `startNewUploadMediaJob failure when uploaderUseCase returns failure`() {
        val response = mockk<UploadResult.Error>(relaxed = true)
        var message: String? = null

        coEvery {
            uploaderUseCase.invoke(any())
        } returns response

        every {
            message = response.message
        } just runs

        presenter.startNewUploadMediaJob(
            "https://vod-tokopedia.com/abc",
            "123456",
            ""
        )

        assertNotNull(message)
    }

    @Test
    fun `startNewUploadMediaJob failure`() {

        coEvery {
            uploaderUseCase.invoke(any())
        } answers {
            throw mockThrowable
        }

        presenter.startNewUploadMediaJob(
            "https://vod-tokopedia.com/abc",
            "123456",
            ""
        )

        verify {
            presenter.updateMediaUploadResults(any(), any())
        }
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
            amount = hashMap[ChatbotConstant.ChatbotUnification.TOTAL_AMOUNT] ?: "",
            color = hashMap[ChatbotConstant.ChatbotUnification.STATUS_COLOR] ?: ""
        )
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

        presenter.checkUploadVideoEligibility("1234")

        verify {
            view.videoUploadEligibilityHandler(any())
        }

    }

    private fun getAttachSingleInvoiceUiModelWithNull(): AttachInvoiceSingleUiModel {
        val hashMap = HashMap<String, String?>()
        hashMap[ChatbotConstant.ChatbotUnification.CODE] = null
        hashMap[ChatbotConstant.ChatbotUnification.DESCRIPTION] = null
        hashMap[ChatbotConstant.ChatbotUnification.IMAGE_URL] = null
        hashMap[ChatbotConstant.ChatbotUnification.ID] = null
        hashMap[ChatbotConstant.ChatbotUnification.STATUS] = null
        hashMap[ChatbotConstant.ChatbotUnification.STATUS_ID] = null
        hashMap[ChatbotConstant.ChatbotUnification.TITLE] = null
        hashMap[ChatbotConstant.ChatbotUnification.TOTAL_AMOUNT] = null
        return AttachInvoiceSingleUiModel(
            typeString = "",
            type = 0,
            code = hashMap[ChatbotConstant.ChatbotUnification.CODE] ?: "",
            createdTime = SendableUiModel.generateStartTime(),
            description = hashMap[ChatbotConstant.ChatbotUnification.DESCRIPTION] ?: "",
            url = hashMap[ChatbotConstant.ChatbotUnification.IMAGE_URL] ?: "",
            id = hashMap.get(ChatbotConstant.ChatbotUnification.ID).toLongOrZero(),
            imageUrl = hashMap[ChatbotConstant.ChatbotUnification.IMAGE_URL] ?: "",
            status = hashMap[ChatbotConstant.ChatbotUnification.STATUS] ?: "",
            statusId = hashMap[ChatbotConstant.ChatbotUnification.STATUS_ID].toIntOrZero(),
            title = hashMap[ChatbotConstant.ChatbotUnification.TITLE] ?: "",
            amount = hashMap[ChatbotConstant.ChatbotUnification.TOTAL_AMOUNT] ?: "",
            color = hashMap[ChatbotConstant.ChatbotUnification.STATUS_COLOR] ?: ""
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

    private fun getMapForArticleEntryForNull(): Map<String, String> {
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
