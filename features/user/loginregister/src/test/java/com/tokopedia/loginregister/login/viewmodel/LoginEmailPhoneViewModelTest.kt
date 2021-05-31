package com.tokopedia.loginregister.login.viewmodel

import android.util.Base64
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserPojo
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.common.view.banner.data.DynamicBannerDataModel
import com.tokopedia.loginregister.common.view.banner.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.common.view.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.common.view.ticker.domain.usecase.TickerInfoUseCase
import com.tokopedia.loginregister.discover.data.DiscoverItemDataModel
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.login.domain.RegisterCheckUseCase
import com.tokopedia.loginregister.login.domain.StatusPinUseCase
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.login.domain.pojo.StatusPinData
import com.tokopedia.loginregister.login.domain.pojo.StatusPinPojo
import com.tokopedia.loginregister.login.view.model.DiscoverDataModel
import com.tokopedia.loginregister.login.view.viewmodel.LoginEmailPhoneViewModel
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialUseCase
import com.tokopedia.loginregister.loginthirdparty.facebook.data.FacebookCredentialData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.*
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenFacebookSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.*
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber

class LoginEmailPhoneViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val registerCheckUseCase = mockk<RegisterCheckUseCase>(relaxed = true)
    val discoverUseCase = mockk<DiscoverUseCase>(relaxed = true)
    val activateUserUseCase = mockk<ActivateUserUseCase>(relaxed = true)
    val getFacebookCredentialUseCase = mockk<GetFacebookCredentialUseCase>(relaxed = true)
    val loginTokenUseCase = mockk<LoginTokenUseCase>(relaxed = true)
    val getProfileUseCase = mockk<GetProfileUseCase>(relaxed = true)
    val tickerInfoUseCase = mockk<TickerInfoUseCase>(relaxed = true)
    val statusPinUseCase = mockk<StatusPinUseCase>(relaxed = true)
    val dynamicBannerUseCase = mockk<DynamicBannerUseCase>(relaxed = true)
    val userSession = mockk<UserSessionInterface>(relaxed = true)

    lateinit var viewModel: LoginEmailPhoneViewModel


    private var registerCheckObserver = mockk<Observer<Result<RegisterCheckData>>>(relaxed = true)
    private var activateUserObserver = mockk<Observer<Result<ActivateUserData>>>(relaxed = true)
    private var discoverObserver = mockk<Observer<Result<DiscoverDataModel>>>(relaxed = true)
    private var getFacebookObserver = mockk<Observer<Result<FacebookCredentialData>>>(relaxed = true)
    private var showPopupErrorObserver = mockk<Observer<PopupError>>(relaxed = true)
    private var goToSecurityQuestionObserver = mockk<Observer<String>>(relaxed = true)
    private var loginToken = mockk<Observer<Result<LoginTokenPojo>>>(relaxed = true)
    private var loginTokenV2 = mockk<Observer<Result<LoginTokenPojoV2>>>(relaxed = true)

    private var loginTokenFacebookPhoneObserver = mockk<Observer<Result<LoginTokenPojo>>>(relaxed = true)
    private var loginTokenFacebookObserver = mockk<Observer<Result<LoginToken>>>(relaxed = true)
    private var loginTokenGoogleObserver = mockk<Observer<Result<LoginTokenPojo>>>(relaxed = true)
    private var getUserInfoObserver = mockk<Observer<Result<ProfilePojo>>>(relaxed = true)
    private var getTickerInfoObserver = mockk<Observer<Result<List<TickerInfoPojo>>>>(relaxed = true)
    private var getDynamicBannerObserver = mockk<Observer<Result<DynamicBannerDataModel>>>(relaxed = true)
    private var reloginAfterSqObserver = mockk<Observer<Result<LoginTokenPojo>>>(relaxed = true)
    private var goToActivationPageAfterReloginObserver = mockk<Observer<MessageErrorException>>(relaxed = true)
    private var goToSecurityAfterReloginQuestionObserver = mockk<Observer<String>>(relaxed = true)
    private var goToActivationPage = mockk<Observer<String>>(relaxed = true)
    private var statusPinObserver = mockk<Observer<Result<StatusPinData>>>(relaxed = true)

    private var loginTokenV2UseCase = mockk<LoginTokenV2UseCase>(relaxed = true)
    private var getAdminTypeUseCase = mockk<GetAdminTypeUseCase>(relaxed = true)
    private var generatePublicKeyUseCase = mockk<GeneratePublicKeyUseCase>(relaxed = true)

    private val mockFragment = mockk<Fragment>(relaxed = true)
    private val mockCallbackManager = mockk<CallbackManager>(relaxed = true)
    private val messageException = MessageErrorException("error bro")
    private val stringError = "Error bro"
    private val accessToken = mockk<AccessToken>(relaxed = true)
    private val phone = "0822424112312"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(RsaUtils)
        mockkObject(EncoderDecoder())

        viewModel = LoginEmailPhoneViewModel(
            registerCheckUseCase,
            discoverUseCase,
            activateUserUseCase,
            getFacebookCredentialUseCase,
            loginTokenUseCase,
            getProfileUseCase,
            tickerInfoUseCase,
            statusPinUseCase,
            getAdminTypeUseCase,
            loginTokenV2UseCase,
            generatePublicKeyUseCase,
            dynamicBannerUseCase,
            userSession,
            CoroutineTestDispatchersProvider
        )

        viewModel.registerCheckResponse.observeForever(registerCheckObserver)
        viewModel.activateResponse.observeForever(activateUserObserver)
        viewModel.discoverResponse.observeForever(discoverObserver)
        viewModel.getFacebookCredentialResponse.observeForever(getFacebookObserver)
        viewModel.loginTokenResponse.observeForever(loginToken)
        viewModel.loginTokenV2Response.observeForever(loginTokenV2)
        viewModel.showPopup.observeForever(showPopupErrorObserver)
        viewModel.goToSecurityQuestion.observeForever(goToSecurityQuestionObserver)
        viewModel.loginTokenFacebookPhoneResponse.observeForever(loginTokenFacebookPhoneObserver)
        viewModel.loginTokenFacebookResponse.observeForever(loginTokenFacebookObserver)
        viewModel.loginTokenGoogleResponse.observeForever(loginTokenGoogleObserver)
        viewModel.profileResponse.observeForever(getUserInfoObserver)
        viewModel.getTickerInfoResponse.observeForever(getTickerInfoObserver)
        viewModel.dynamicBannerResponse.observeForever(getDynamicBannerObserver)
        viewModel.loginTokenAfterSQResponse.observeForever(reloginAfterSqObserver)
        viewModel.goToActivationPage.observeForever(goToActivationPage)
        viewModel.goToActivationPageAfterRelogin.observeForever(goToActivationPageAfterReloginObserver)
        viewModel.goToSecurityQuestionAfterRelogin.observeForever(goToSecurityAfterReloginQuestionObserver)
        viewModel.getStatusPinResponse.observeForever(statusPinObserver)
    }

    private val throwable = Throwable("Error")
    private val email = "yoris.prayogo@tokopedia.com"
    private val password = "abc123456"

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
        MatcherAssert.assertThat(viewModel.registerCheckResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        assertEquals((viewModel.registerCheckResponse.value as Fail).throwable.message, throwable.message)
    }

    @Test
    fun `on Register check has Errors`() {
        /* When */
        val errors = arrayListOf("Error 1")
        val responseData = RegisterCheckData(errors = errors)
        val response = RegisterCheckPojo(data = responseData)

        val testId = "123456"

        every { registerCheckUseCase.execute(any(), any()) } answers {
            firstArg<(RegisterCheckPojo) -> Unit>().invoke(response)
        }

        viewModel.registerCheck(testId)

        /* Then */
        MatcherAssert.assertThat(viewModel.registerCheckResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        MatcherAssert.assertThat((viewModel.registerCheckResponse.value as Fail).throwable, CoreMatchers.instanceOf(MessageErrorException::class.java))
    }

    @Test
    fun `on Success Activate User`() {
        /* When */
        val responseData = ActivateUserData(isSuccess = 1, accessToken = "asd", refreshToken = "fffaa", tokenType = "Bearer")
        val response = ActivateUserPojo(data = responseData)

        coEvery { activateUserUseCase.executeOnBackground() } returns response

        viewModel.activateUser(email, "asd")

        /* Then */
        verify { activateUserObserver.onChanged(Success(responseData)) }
    }

    @Test
    fun `on Exception Throw during Activate User`() {
        coEvery { activateUserUseCase.executeOnBackground() } throws throwable

        viewModel.activateUser("", "")

        /* Then */
        verify {
            activateUserObserver.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `on Success Discover`() {
        /* When */
        val discoverViewModel = DiscoverDataModel(arrayListOf(
                DiscoverItemDataModel("123", "", "", "", "")
        ), "")

        every { discoverUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<DiscoverDataModel>>().onNext(discoverViewModel)
        }

        viewModel.discoverLogin()

        /* Then */
        verify { discoverObserver.onChanged(Success(discoverViewModel)) }
    }

    @Test
    fun `on Providers Empty Error`() {
        /* When */
        every { discoverUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<DiscoverDataModel>>().onError(throwable)
        }

        viewModel.discoverLogin()

        /* Then */
        MatcherAssert.assertThat(viewModel.discoverResponse.value, CoreMatchers.instanceOf(Fail::class.java))
    }

    @Test
    fun `on Failed Discover`() {
        /* When */
        every { discoverUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<DiscoverDataModel>>().onError(throwable)
        }

        viewModel.discoverLogin()

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
        MatcherAssert.assertThat(viewModel.getFacebookCredentialResponse.value, CoreMatchers.instanceOf(Success::class.java))
    }

    @Test
    fun `on Success Get Phone Credential Facebook`() {
        /* When */
        val responseEmail = "yoris.prayogo@tokopedia.com"
        val responseToken = mockk<AccessToken>(relaxed = true)

        every { userSession.loginMethod } returns "facebook"
        every { getFacebookCredentialUseCase.execute(any(), any()) } answers {
            secondArg<GetFacebookCredentialSubscriber>().onSuccessPhone(responseToken, responseEmail)
        }

        viewModel.getFacebookCredential(mockFragment, mockCallbackManager)

        /* Then */
        MatcherAssert.assertThat(viewModel.getFacebookCredentialResponse.value, CoreMatchers.instanceOf(Success::class.java))
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
        MatcherAssert.assertThat(viewModel.getFacebookCredentialResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        verify {
            getFacebookObserver.onChanged(Fail(errorResponse))
        }
    }

    @Test
    fun `on Login Email Success`() {

        /* When */
        val responseToken = mockk<LoginTokenPojo>(relaxed = true)

        every { loginTokenUseCase.executeLoginEmailWithPassword(any(), any()) } answers {
            (secondArg() as LoginTokenSubscriber).onSuccessLoginToken(responseToken)
        }

        viewModel.loginEmail(email, password)

        /* Then */
        verify { loginToken.onChanged(Success(responseToken)) }
    }

    @Test
    fun `on Login Email V2 Success`() {

        /* When */
        val loginToken = LoginToken(accessToken = "abc123")
        val responseToken = LoginTokenPojoV2(loginToken = loginToken)

        val keyData = KeyData(key = "cGFkZGluZw==", hash = "zzzz")
        val generateKeyPojo = GenerateKeyPojo(keyData = keyData)

        mockkStatic("android.util.Base64")
        mockkStatic(EncoderDecoder::class)

        every { EncoderDecoder.Encrypt(any(), any()) } returns "ok"
        every { Base64.decode(keyData.key, any()) } returns ByteArray(10)

        coEvery { RsaUtils.encrypt(any(), any(), true) } returns "qwerty"
        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyPojo
        coEvery { loginTokenV2UseCase.executeOnBackground() } returns responseToken

        viewModel.loginEmailV2(email, password, useHash = true)

        /* Then */
        verify {
            RsaUtils.encrypt(any(), any(), true)
            loginTokenV2UseCase.setParams(any(), any(), any())
            loginTokenV2.onChanged(Success(responseToken))
        }
    }

    @Test
    fun `on Login Email V2 Empty key`() {
        val keyData = KeyData(key = "", hash = "")
        val generateKeyPojo = GenerateKeyPojo(keyData = keyData)

        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns generateKeyPojo

        viewModel.loginEmailV2(email, password, useHash = true)

        /* Then */
        MatcherAssert.assertThat(viewModel.loginTokenV2Response.value, CoreMatchers.instanceOf(Fail::class.java))
    }

    @Test
    fun `on Login Email V2 Error`() {
        coEvery { generatePublicKeyUseCase.executeOnBackground() } throws throwable

        viewModel.loginEmailV2(email, password, useHash = true)

        /* Then */
        verify {
            loginTokenV2.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `on Login Email smart lock`() {
        /* When */
        val responseToken = mockk<LoginTokenPojo>(relaxed = true)

        every { loginTokenUseCase.executeLoginEmailWithPassword(any(), any()) } answers {
            (secondArg() as LoginTokenSubscriber).onSuccessLoginToken(responseToken)
        }

        viewModel.loginEmail(email, password, true)

        /* Then */
        verify {
            userSession.loginMethod = UserSessionInterface.LOGIN_METHOD_EMAIL_SMART_LOCK
        }
    }

    @Test
    fun `on Login Email Failed`() {

        /* When */
        every { loginTokenUseCase.executeLoginEmailWithPassword(any(), any()) } answers {
            (secondArg() as LoginTokenSubscriber).onErrorLoginToken(throwable)
        }

        viewModel.loginEmail(email, password)

        /* Then */
        verify { loginToken.onChanged(Fail(throwable)) }
    }

    @Test
    fun `on Show popup login Email`() {
        val popupError = PopupError("header")
        val loginTokenData = LoginToken(popupError = popupError)
        val responseToken = LoginTokenPojo(loginTokenData)

        /* When */
        every { loginTokenUseCase.executeLoginEmailWithPassword(any(), any()) } answers {
            (secondArg() as LoginTokenSubscriber).onShowPopupError(responseToken)
        }

        viewModel.loginEmail(email, password)

        /* Then */
        verify { showPopupErrorObserver.onChanged(popupError) }
    }

    @Test
    fun `on go To Activation Page after Login Email`() {

        /* When */
        every { loginTokenUseCase.executeLoginEmailWithPassword(any(), any()) } answers {
            (secondArg() as LoginTokenSubscriber).onGoToActivationPage(messageException)
        }

        viewModel.loginEmail(email, password)

        /* Then */
        verify {
            goToActivationPage.onChanged(email)
        }
    }

    @Test
    fun `on go To Security Question Page after Login Email`() {
        /* When */
        every { loginTokenUseCase.executeLoginEmailWithPassword(any(), any()) } answers {
            (secondArg() as LoginTokenSubscriber).onGoToSecurityQuestion()
        }

        viewModel.loginEmail(email, password)

        /* Then */
        verify { goToSecurityQuestionObserver.onChanged(email) }
    }

    @Test
    fun `on Success Login Facebook`() {
        /* When */
        val responseToken = mockk<LoginTokenPojo>(relaxed = true)

        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onSuccessLoginToken(responseToken)
        }

        viewModel.loginFacebook(accessToken, email)

        /* Then */
        MatcherAssert.assertThat(viewModel.loginTokenFacebookResponse.value, CoreMatchers.instanceOf(Success::class.java))
    }

    @Test
    fun `on Failed Login Token Facebook`() {
        /* When */
        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onErrorLoginToken(throwable)
        }

        viewModel.loginFacebook(accessToken, email)

        /* Then */
        MatcherAssert.assertThat(viewModel.loginTokenFacebookResponse.value, CoreMatchers.instanceOf(Fail::class.java))
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

        viewModel.loginFacebook(accessToken, email)

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

        viewModel.loginFacebook(accessToken, email)

        /* Then */
        verify {
            goToActivationPage.onChanged(email)
        }
    }

    @Test
    fun `on go to security questions Token Facebook`() {
        /* When */
        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onGoToSecurityQuestion()
        }

        viewModel.loginFacebook(accessToken, email)

        /* Then */
        verify {
            goToSecurityQuestionObserver.onChanged(email)
        }
    }

    @Test
    fun `on Success Login Facebook Phone`() {
        /* When */
        val responseToken = mockk<LoginTokenPojo>(relaxed = true)

        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenFacebookSubscriber>().onSuccessLoginToken(responseToken)
        }

        viewModel.loginFacebookPhone(accessToken, phone)

        /* Then */
        MatcherAssert.assertThat(viewModel.loginTokenFacebookPhoneResponse.value, CoreMatchers.instanceOf(Success::class.java))
    }

    @Test
    fun `on Failed Login Token Facebook Phone`() {
        /* When */
        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenFacebookSubscriber>().onErrorLoginToken(throwable)
        }

        viewModel.loginFacebookPhone(accessToken, phone)

        /* Then */
        MatcherAssert.assertThat(viewModel.loginTokenFacebookPhoneResponse.value, CoreMatchers.instanceOf(Fail::class.java))
    }

    @Test
    fun `on show popup error Login Token Facebook Phone`() {
        val popupError = PopupError("header")

        /* When */
        val loginToken = LoginToken(popupError = popupError)
        val responseToken = LoginTokenPojo(loginToken)

        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenFacebookSubscriber>().onShowPopupError(responseToken)
        }

        viewModel.loginFacebookPhone(accessToken, phone)

        /* Then */
        verify {
            showPopupErrorObserver.onChanged(popupError)
        }
    }

    @Test
    fun `on go to security questions Token Facebook Phone`() {
        /* When */
        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenFacebookSubscriber>().onGoToSecurityQuestion()
        }

        viewModel.loginFacebookPhone(accessToken, phone)

        /* Then */
        verify {
            goToSecurityQuestionObserver.onChanged("")
        }
    }

    @Test
    fun `on Success Login Google`() {
        /* When */
        val responseToken = mockk<LoginTokenPojo>(relaxed = true)

        every { userSession.loginMethod } returns "google"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onSuccessLoginToken(responseToken)
        }

        viewModel.loginGoogle("", email)

        /* Then */
        MatcherAssert.assertThat(viewModel.loginTokenGoogleResponse.value, CoreMatchers.instanceOf(Success::class.java))
    }

    @Test
    fun `on Failed Login Token Google`() {
        /* When */
        every { userSession.loginMethod } returns "google"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onErrorLoginToken(throwable)
        }

        viewModel.loginGoogle("", email)

        /* Then */
        MatcherAssert.assertThat(viewModel.loginTokenGoogleResponse.value, CoreMatchers.instanceOf(Fail::class.java))
    }

    @Test
    fun `on show popup error Login Google`() {
        val popupError = PopupError("header")

        /* When */
        val loginToken = LoginToken(popupError = popupError)
        val responseToken = LoginTokenPojo(loginToken)

        every { userSession.loginMethod } returns "google"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onShowPopupError(responseToken)
        }

        viewModel.loginGoogle("", email)

        /* Then */
        verify {
            showPopupErrorObserver.onChanged(popupError)
        }
    }

    @Test
    fun `on go to activation page Google`() {
        /* When */
        every { userSession.loginMethod } returns "google"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onGoToActivationPage(messageException)
        }

        viewModel.loginGoogle("", email)

        /* Then */
        verify {
            goToActivationPage.onChanged(email)
        }
    }

    @Test
    fun `on go to security questions Google`() {
        /* When */
        every { userSession.loginMethod } returns "google"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onGoToSecurityQuestion()
        }

        viewModel.loginGoogle("", email)

        /* Then */
        verify {
            goToSecurityQuestionObserver.onChanged(email)
        }
    }

    @Test
    fun `on relogin after SQ success`() {
        val loginToken = LoginToken("", "asd", "qqqq")
        val responseToken = LoginTokenPojo(loginToken = loginToken)

        /* When */
        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginAfterSQ(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onSuccessLoginToken(responseToken)
        }

        viewModel.reloginAfterSQ("asd")

        /* Then */
        verify {
            reloginAfterSqObserver.onChanged(Success(responseToken))
        }
    }

    @Test
    fun `on relogin after SQ Fail`() {
        val validateToken = "asdf123"

        /* When */
        every { userSession.loginMethod } returns "facebook"
        every { loginTokenUseCase.executeLoginAfterSQ(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onErrorLoginToken(throwable)
        }

        viewModel.reloginAfterSQ(validateToken)

        /* Then */
        verify {
            reloginAfterSqObserver.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `on Show Popup after SQ`() {
        val popupError = PopupError("header")

        /* When */
        val loginToken = LoginToken(popupError = popupError)
        val responseToken = LoginTokenPojo(loginToken)

        /* When */
        every { loginTokenUseCase.executeLoginAfterSQ(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onShowPopupError(responseToken)
        }

        viewModel.reloginAfterSQ("")

        /* Then */
        verify {
            showPopupErrorObserver.onChanged(popupError)
        }
    }

    @Test
    fun `on go to activation page after SQ`() {
        /* When */
        every { loginTokenUseCase.executeLoginAfterSQ(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onGoToActivationPage(messageException)
        }

        viewModel.reloginAfterSQ("")

        /* Then */
        verify {
            goToActivationPageAfterReloginObserver.onChanged(messageException)
        }
    }

    @Test
    fun `on go to security questions after SQ`() {
        /* When */
        every { loginTokenUseCase.executeLoginAfterSQ(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onGoToSecurityQuestion()
        }

        viewModel.reloginAfterSQ("")

        /* Then */
        verify {
            goToSecurityAfterReloginQuestionObserver.onChanged("")
        }
    }

    @Test
    fun `on Success get ticker`() {
        /* When */
        val tickerInfo = TickerInfoPojo("test", "test message", "")
        val tickerList = listOf(tickerInfo)

        coEvery { tickerInfoUseCase.createObservable(any()).toBlocking().single() } returns tickerList

        viewModel.getTickerInfo()

        /* Then */
        verify {
            getTickerInfoObserver.onChanged(Success(tickerList))
        }
    }

    @Test
    fun `on Failed get ticker`() {
        /* When */
        coEvery { tickerInfoUseCase.createObservable(any()).toBlocking().single() } throws throwable

        viewModel.getTickerInfo()

        /* Then */
        verify {
            getTickerInfoObserver.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `on Success get dynamic banner`() {
        /* When */
        val banner = DynamicBannerDataModel.GetBanner(message = "test")
        val response = DynamicBannerDataModel(banner = banner)

        coEvery { dynamicBannerUseCase.executeOnBackground() } returns response

        viewModel.getDynamicBannerData("1")

        /* Then */
        verify {
            getDynamicBannerObserver.onChanged(Success(response))
        }
    }

    @Test
    fun `on Failed get dynamic banner`() {
        /* When */
        coEvery { dynamicBannerUseCase.executeOnBackground() } throws throwable

        viewModel.getDynamicBannerData("1")

        /* Then */
        verify {
            getDynamicBannerObserver.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `on Success get user info`() {
        /* When */
        val profileInfo = ProfileInfo(firstName = "yoris")
        val response = ProfilePojo(profileInfo = profileInfo)

        every { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().onSuccessGetProfile(response)
        }

        viewModel.getUserInfo()

        /* Then */
        verify {
            getUserInfoObserver.onChanged(Success(response))
        }
    }

    @Test
    fun `on Failed get user info`() {
        /* When */
        every { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().onErrorGetProfile(throwable)
        }

        viewModel.getUserInfo()

        /* Then */
        verify {
            getUserInfoObserver.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `on Success check status pin`() {
        /* When */
        val statusPinData = StatusPinData()
        val response = StatusPinPojo(data = statusPinData)

        coEvery { statusPinUseCase.executeOnBackground() } returns response

        viewModel.checkStatusPin()

        /* Then */
        verify {
            statusPinObserver.onChanged(Success(response.data))
        }
    }

    @Test
    fun `on Failed check status pin`() {
        /* When */
        coEvery { statusPinUseCase.executeOnBackground() } throws throwable

        viewModel.checkStatusPin()

        /* Then */
        verify {
            statusPinObserver.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `clear task`() {
        viewModel.clearBackgroundTask()
        verify {
            tickerInfoUseCase.unsubscribe()
            discoverUseCase.unsubscribe()
            loginTokenUseCase.unsubscribe()
            getProfileUseCase.unsubscribe()
        }
    }
}