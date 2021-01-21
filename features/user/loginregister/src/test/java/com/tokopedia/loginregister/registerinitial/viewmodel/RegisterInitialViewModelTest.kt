package com.tokopedia.loginregister.registerinitial.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.loginregister.common.DispatcherProvider
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserPojo
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.common.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.discover.data.DiscoverItemViewModel
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.login.view.model.DiscoverViewModel
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialUseCase
import com.tokopedia.loginregister.loginthirdparty.facebook.data.FacebookCredentialData
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialQueryConstant
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestPojo
import com.tokopedia.loginregister.ticker.domain.usecase.TickerInfoUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenFacebookSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber
import java.util.ArrayList

class RegisterInitialViewModelTest {

    val testDispatcher = object: DispatcherProvider {
        override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
        override fun ui(): CoroutineDispatcher = Dispatchers.Unconfined
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val registerCheckUseCase = mockk<GraphqlUseCase<RegisterCheckPojo>>(relaxed = true)
    val registerRequestUseCase = mockk<GraphqlUseCase<RegisterRequestPojo>>(relaxed = true)
    val activateUserUseCase = mockk<ActivateUserUseCase>(relaxed = true)
    val discoverUseCase = mockk<DiscoverUseCase>(relaxed = true)
    val getFacebookCredentialUseCase = mockk<GetFacebookCredentialUseCase>(relaxed = true)
    val loginTokenUseCase = mockk<LoginTokenUseCase>(relaxed = true)
    val getProfileUseCase = mockk<GetProfileUseCase>(relaxed = true)
    val tickerInfoUseCase = mockk<TickerInfoUseCase>(relaxed = true)
    val dynamicBannerUseCase = mockk<DynamicBannerUseCase>(relaxed = true)

    val userSession = mockk<UserSessionInterface>(relaxed = true)
    val rawQueries = mapOf(
            RegisterInitialQueryConstant.MUTATION_REGISTER_CHECK to "test",
            RegisterInitialQueryConstant.MUTATION_REGISTER_REQUEST to "test"
    )

    private var registerCheckObserver = mockk<Observer<Result<RegisterCheckData>>>(relaxed = true)
    private var registerRequestObserver = mockk<Observer<Result<RegisterRequestData>>>(relaxed = true)
    private var activateUserObserver = mockk<Observer<Result<ActivateUserData>>>(relaxed = true)
    private var discoverObserver = mockk<Observer<Result<ArrayList<DiscoverItemViewModel>>>>(relaxed = true)
    private var getFacebookObserver = mockk<Observer<Result<FacebookCredentialData>>>(relaxed = true)
    private var showPopupErrorObserver = mockk<Observer<PopupError>>(relaxed = true)
    private var goToActivationPageObserver = mockk<Observer<MessageErrorException>>(relaxed = true)
    private var goToSecurityQuestionObserver = mockk<Observer<String>>(relaxed = true)
    private var loginTokenFacebookPhoneObserver = mockk<Observer<Result<LoginTokenPojo>>>(relaxed = true)
    private var loginTokenGoogleObserver = mockk<Observer<Result<LoginTokenPojo>>>(relaxed = true)

    lateinit var viewModel: RegisterInitialViewModel

    val throwable = Throwable(message = "Error")
    val messageException = MessageErrorException("error bro")

    val mockFragment = mockk<Fragment>(relaxed = true)
    val mockCallbackManager = mockk<CallbackManager>(relaxed = true)


    @Before
    fun setUp() {
        viewModel = RegisterInitialViewModel(
                registerCheckUseCase,
                registerRequestUseCase,
                activateUserUseCase,
                discoverUseCase,
                getFacebookCredentialUseCase,
                loginTokenUseCase,
                getProfileUseCase,
                tickerInfoUseCase,
                dynamicBannerUseCase,
                userSession,
                rawQueries,
                testDispatcher
        )
        viewModel.idlingResourceProvider = null
        viewModel.registerCheckResponse.observeForever(registerCheckObserver)
        viewModel.registerRequestResponse.observeForever(registerRequestObserver)
        viewModel.activateUserResponse.observeForever(activateUserObserver)
        viewModel.getProviderResponse.observeForever(discoverObserver)
        viewModel.getFacebookCredentialResponse.observeForever(getFacebookObserver)
        viewModel.showPopup.observeForever(showPopupErrorObserver)
        viewModel.goToActivationPage.observeForever(goToActivationPageObserver)
        viewModel.goToSecurityQuestion.observeForever(goToSecurityQuestionObserver)
        viewModel.loginTokenFacebookPhoneResponse.observeForever(loginTokenFacebookPhoneObserver)
        viewModel.loginTokenGoogleResponse.observeForever(loginTokenGoogleObserver)
    }

    @Test
    fun `on Success Register Check`() {
        val testId = "123456"

        /* When */
        val responseData = RegisterCheckData()
        val response = RegisterCheckPojo(data = responseData)

        every { registerCheckUseCase.execute(any(), any()) } answers {
            firstArg<(RegisterCheckPojo) -> Unit>().invoke(response)
        }

        viewModel.registerCheck(testId)

        /* Then */
        verify { registerCheckObserver.onChanged(Success(responseData)) }
    }

    @Test
    fun `on Failed Register Check`() {
        val testId = "123456"

        every { registerCheckUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        viewModel.registerCheck(testId)

        /* Then */
        verify { registerCheckObserver.onChanged(Fail(throwable)) }
    }

    @Test
    fun `on Success Register Request`() {
        /* When */
        val responseData = RegisterRequestData(accessToken = "asda", refreshToken = "asdasd", tokenType = "kfkfk")
        val response = RegisterRequestPojo(data = responseData)

        every { registerRequestUseCase.execute(any(), any()) } answers {
            firstArg<(RegisterRequestPojo) -> Unit>().invoke(response)
        }

        viewModel.registerRequest("", "", "", "")

        /* Then */
        verify { registerRequestObserver.onChanged(Success(responseData)) }
    }

    @Test
    fun `on Failed Register Request`() {
        every { registerRequestUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        viewModel.registerRequest("", "", "", "")

        /* Then */
        verify { registerRequestObserver.onChanged(Fail(throwable)) }
    }

    @Test
    fun `on Success Activate User`() {
        val params = mapOf("email" to "asd")

        /* When */
        val responseData = ActivateUserData(isSuccess = 1, accessToken = "asd", refreshToken = "fffaa", tokenType = "Bearer")
        val response = ActivateUserPojo(data = responseData)

        coEvery { activateUserUseCase.getParams(any(), any()) } returns params
        coEvery { activateUserUseCase.getData(any()) } returns response

        viewModel.activateUser("", "")

        /* Then */
        verify { activateUserObserver.onChanged(Success(responseData)) }
    }

    @Test
    fun `on Exception Throw during Activate User`() {
        coEvery { activateUserUseCase.getData(any()) } answers {
            throw throwable
        }

        viewModel.activateUser("", "")

        /* Then */
        verify {
            activateUserObserver.onChanged(Fail(throwable))
            userSession.clearToken()
        }
    }

    @Test
    fun `on Message is not empty during Activate User`() {
        val params = mapOf("email" to "asd")

        /* When */
        val responseData = ActivateUserData(message = "error happen!")
        val response = ActivateUserPojo(data = responseData)

        coEvery { activateUserUseCase.getParams(any(), any()) } returns params
        coEvery { activateUserUseCase.getData(any()) } returns response

        viewModel.activateUser("", "")

        /* Then */
        verify {
            assertThat(viewModel.activateUserResponse.value, instanceOf(Fail::class.java))
            assertEquals(responseData.message, (viewModel.activateUserResponse.value as Fail).throwable.message)
            userSession.clearToken()
        }
    }

    @Test
    fun `on Success Discover`() {
        /* When */
        val discoverViewModel = DiscoverViewModel(arrayListOf(
                DiscoverItemViewModel("123", "", "", "", "")
        ), "")

        every { discoverUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<DiscoverViewModel>>().onNext(discoverViewModel)
        }

        viewModel.getProvider()

        /* Then */
        verify { discoverObserver.onChanged(Success(discoverViewModel.providers)) }
    }

    @Test
    fun `on Providers Empty Error`() {
        /* When */
        val discoverViewModel = DiscoverViewModel(arrayListOf(), "")

        every { discoverUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<DiscoverViewModel>>().onError(throwable)
        }

        viewModel.getProvider()

        /* Then */
        assertThat(viewModel.getProviderResponse.value, instanceOf(Fail::class.java))
    }

    @Test
    fun `on Failed Discover`() {
        /* When */
        every { discoverUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<DiscoverViewModel>>().onError(throwable)
        }

        viewModel.getProvider()

        /* Then */
        verify { discoverObserver.onChanged(Fail(throwable)) }
    }

    @Test
    fun `on Success Get Credential Facebook`() {
        /* When */
        val responseEmail = "yoris.prayogo@tokopedia.com"
        val responseToken = mockk<AccessToken>(relaxed = true)

        every { userSession.loginMethod } returns "facebook"
        every { getFacebookCredentialUseCase.execute(any(), any()) } answers {
            secondArg<GetFacebookCredentialSubscriber>().onSuccessEmail(responseToken, responseEmail)
        }

        viewModel.getFacebookCredential(mockFragment, mockCallbackManager)

        /* Then */
        assertThat(viewModel.getFacebookCredentialResponse.value, instanceOf(Success::class.java))
    }

    @Test
    fun `on Fail Facebook`() {
        /* When */
        val errorResponse = Exception()

        every { userSession.loginMethod } returns "facebook"
        every { getFacebookCredentialUseCase.execute(any(), any()) } answers {
            secondArg<GetFacebookCredentialSubscriber>().onError(errorResponse)
        }

        viewModel.getFacebookCredential(mockFragment, mockCallbackManager)

        /* Then */
        assertThat(viewModel.getFacebookCredentialResponse.value, instanceOf(Fail::class.java))
        verify {
            getFacebookObserver.onChanged(Fail(errorResponse))
        }
    }

    @Test
    fun `on Success Register Facebook`() {
        /* When */
        val responseToken = mockk<LoginTokenPojo>(relaxed = true)

        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onSuccessLoginToken(responseToken)
        }

        viewModel.registerFacebook("", "")

        /* Then */
        assertThat(viewModel.loginTokenFacebookResponse.value, instanceOf(Success::class.java))
    }

    @Test
    fun `on Failed Login Token Facebook`() {
        /* When */
        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onErrorLoginToken(throwable)
        }

        viewModel.registerFacebook("", "")

        /* Then */
        assertThat(viewModel.loginTokenFacebookResponse.value, instanceOf(Fail::class.java))
    }

    @Test
    fun `on show popup error Login Token Facebook`() {
        val popupError = PopupError("header")

        /* When */
        val loginToken = LoginToken(popupError = popupError)
        val responseToken = LoginTokenPojo(loginToken)

        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onShowPopupError(responseToken)
        }

        viewModel.registerFacebook("", "")

        /* Then */
        verify {
            showPopupErrorObserver.onChanged(popupError)
        }
    }

    @Test
    fun `on go to activation page Login Token Facebook`() {
        /* When */
        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onGoToActivationPage(messageException)
        }

        viewModel.registerFacebook("", "")

        /* Then */
        verify {
            goToActivationPageObserver.onChanged(messageException)
        }
    }

    @Test
    fun `on go to security questions Token Facebook`() {
        val email = "yoris.prayogo@tokopedia.com"

        /* When */
        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onGoToSecurityQuestion()
        }

        viewModel.registerFacebook("", email)

        /* Then */
        verify {
            goToSecurityQuestionObserver.onChanged(email)
        }
    }

    @Test
    fun `on Success Register Facebook Phone`() {
        /* When */
        val responseToken = mockk<LoginTokenPojo>(relaxed = true)

        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenFacebookSubscriber>().onSuccessLoginToken(responseToken)
        }

        viewModel.registerFacebookPhone("", "")

        /* Then */
        verify {
            loginTokenFacebookPhoneObserver.onChanged(Success(responseToken))
        }
    }

    @Test
    fun `on Failed Register Facebook Phone`() {
        /* When */
        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenFacebookSubscriber>().onErrorLoginToken(throwable)
        }

        viewModel.registerFacebookPhone("", "")

        /* Then */
        verify {
            loginTokenFacebookPhoneObserver.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `on Show Popup Register Facebook Phone`() {
        val popupError = PopupError("header")

        /* When */
        val loginToken = LoginToken(popupError = popupError)
        val responseToken = LoginTokenPojo(loginToken)

        /* When */
        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenFacebookSubscriber>().onShowPopupError(responseToken)
        }

        viewModel.registerFacebookPhone("", "")

        /* Then */
        verify {
            showPopupErrorObserver.onChanged(popupError)
        }
    }

    @Test
    fun `on go to security questions Register Facebook Phone`() {
        /* When */
        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenFacebookSubscriber>().onGoToSecurityQuestion()
        }

        viewModel.registerFacebookPhone("", "")

        /* Then */
        verify {
            goToSecurityQuestionObserver.onChanged("")
        }
    }

    @Test
    fun `on Success Register Google Phone`() {
        /* When */
        val responseToken = mockk<LoginTokenPojo>(relaxed = true)

        every { userSession.loginMethod } returns "google"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onSuccessLoginToken(responseToken)
        }

        viewModel.registerGoogle("", "")

        /* Then */
        verify {
            loginTokenGoogleObserver.onChanged(Success(responseToken))
        }
    }

    @Test
    fun `on Failed Register Google Phone`() {
        /* When */
        every { userSession.loginMethod } returns "google"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onErrorLoginToken(throwable)
        }

        viewModel.registerGoogle("", "")

        /* Then */
        verify {
            loginTokenGoogleObserver.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `on Show Popup Register Google Phone`() {
        val popupError = PopupError("header")

        /* When */
        val loginToken = LoginToken(popupError = popupError)
        val responseToken = LoginTokenPojo(loginToken)

        /* When */
        every { userSession.loginMethod } returns "google"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onShowPopupError(responseToken)
        }

        viewModel.registerGoogle("", "")

        /* Then */
        verify {
            showPopupErrorObserver.onChanged(popupError)
        }
    }

    @Test
    fun `on go to security questions Register Google Phone`() {
        /* When */
        every { userSession.loginMethod } returns "google"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onGoToSecurityQuestion()
        }

        viewModel.registerGoogle("", "")

        /* Then */
        verify {
            goToSecurityQuestionObserver.onChanged("")
        }
    }

}