package com.tokopedia.chatbot.view.presenter

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.imageupload.ChatbotUploadImagePojo
import com.tokopedia.chatbot.data.newsession.TopBotNewSessionResponse
import com.tokopedia.chatbot.data.uploadsecure.CheckUploadSecureResponse
import com.tokopedia.chatbot.domain.mapper.ChatBotWebSocketMessageMapper
import com.tokopedia.chatbot.domain.subscriber.SendRatingSubscriber
import com.tokopedia.chatbot.domain.usecase.*
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.After
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
                chatBotSecureImageUploadUseCase
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
            throw Exception()
        }

        presenter.checkUploadSecure("", Intent())

        verify{
            view.loadChatHistory()
        }

    }

    @Test
    fun `handleNewSession starts new session`(){
        //TODO should i write
    }

    @Test
    fun `handleNewSession loads history`(){
        //TODO should i write
    }

    @Test
    fun `checkForSession when new session`(){

        val response = mockk<TopBotNewSessionResponse>(relaxed = true)

        coEvery {
            getTopBotNewSessionUseCase.getTobBotUserSession(any())
        } returns response

        coEvery {
            response.topBotGetNewSession.isNewSession
        } returns true

        presenter.checkForSession("")

        verify{
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

        verify{
            view.loadChatHistory()
        }

    }

    @Test
    fun `sendRating test`(){
//        val response = mockk<CheckUploadSecureResponse>(relaxed = true)
//
//        coEvery {
//            checkUploadSecureUseCase.checkUploadSecure(any())
//        } returns response
//
//        coEvery {
//            response.topbotUploadSecureAvailability.uploadSecureAvailabilityData.isUsingUploadSecure
//        } returns false
//
//        presenter.checkUploadSecure(" ", mockk())
//
//        verify(exactly = 1) {
//            view.uploadUsingOldMechanism(any())
//        }
        val params = mapOf<String,Any>()
        mockkConstructor(SendRatingSubscriber::class)
        mockkObject(SendChatRatingUseCase)

        every {
             SendChatRatingUseCase.generateParam(
                any(), any(), any())
        } returns params

        every {
            sendChatRatingUseCase.execute(any(),any())
        } just runs


        presenter.sendRating("",1,"",{},{})

        verify {
            sendChatRatingUseCase.execute(any(),any())
        }

    }

    @Test
    fun `sendActionBubble test`(){
        mockkObject(RxWebSocket)
        mockkObject(SendChatbotWebsocketParam)

        every {
            SendChatbotWebsocketParam.generateParamSendBubbleAction(any(), any(),
                any(), any())
        } returns mockk(relaxed = true)

        every {
            RxWebSocket.send(SendChatbotWebsocketParam.generateParamSendBubbleAction(any(), any(),
                any(), any()),any())
        } just runs

        presenter.sendActionBubble("", ChatActionBubbleViewModel(),"","")

        verify {
            RxWebSocket.send(SendChatbotWebsocketParam.generateParamSendBubbleAction(any(), any(),
                any(), any()),any())
        }

    }

}