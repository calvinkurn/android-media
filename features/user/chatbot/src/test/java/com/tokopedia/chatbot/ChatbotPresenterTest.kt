package com.tokopedia.chatbot

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.chatbot.data.TickerData.TickerDataResponse
import com.tokopedia.chatbot.data.imageupload.ChatbotUploadImagePojo
import com.tokopedia.chatbot.domain.mapper.ChatBotWebSocketMessageMapper
import com.tokopedia.chatbot.domain.pojo.csatRating.csatInput.InputItem
import com.tokopedia.chatbot.domain.pojo.csatRating.csatResponse.SubmitCsatGqlResponse
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatResponse
import com.tokopedia.chatbot.domain.usecase.*
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
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

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

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

}