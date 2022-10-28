package com.tokopedia.loginregister.login.viewmodel

import android.util.Base64
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserPojo
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.common.view.banner.data.DynamicBannerDataModel
import com.tokopedia.loginregister.common.view.banner.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.common.view.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.common.view.ticker.domain.usecase.TickerInfoUseCase
import com.tokopedia.loginregister.discover.pojo.DiscoverData
import com.tokopedia.loginregister.discover.pojo.DiscoverPojo
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessHelper
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessPreference
import com.tokopedia.loginregister.goto_seamless.model.GojekProfileData
import com.tokopedia.loginregister.goto_seamless.model.TempKeyData
import com.tokopedia.loginregister.goto_seamless.model.TempKeyResponse
import com.tokopedia.loginregister.goto_seamless.usecase.GetTemporaryKeyUseCase
import com.tokopedia.loginregister.login.domain.RegisterCheckFingerprintUseCase
import com.tokopedia.loginregister.login.domain.RegisterCheckUseCase
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckFingerprint
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckFingerprintResult
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.login.view.viewmodel.LoginEmailPhoneViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.*
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
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

class LoginEmailPhoneViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val registerCheckUseCase = mockk<RegisterCheckUseCase>(relaxed = true)
    val discoverUseCase = mockk<DiscoverUseCase>(relaxed = true)
    val activateUserUseCase = mockk<ActivateUserUseCase>(relaxed = true)
    val loginTokenUseCase = mockk<LoginTokenUseCase>(relaxed = true)
    val getProfileUseCase = mockk<GetProfileUseCase>(relaxed = true)
    val tickerInfoUseCase = mockk<TickerInfoUseCase>(relaxed = true)
    val dynamicBannerUseCase = mockk<DynamicBannerUseCase>(relaxed = true)
    val userSession = mockk<UserSessionInterface>(relaxed = true)
    val registerCheckFingerprintUseCase = mockk<RegisterCheckFingerprintUseCase>(relaxed = true)
    val loginFingerprintUseCase = mockk<LoginFingerprintUseCase>(relaxed = true)

    lateinit var viewModel: LoginEmailPhoneViewModel

    private var registerCheckObserver = mockk<Observer<Result<RegisterCheckData>>>(relaxed = true)
    private var registerCheckFingerprintObserver = mockk<Observer<Result<RegisterCheckFingerprint>>>(relaxed = true)
    private var activateUserObserver = mockk<Observer<Result<ActivateUserData>>>(relaxed = true)
    private var discoverObserver = mockk<Observer<Result<DiscoverData>>>(relaxed = true)
    private var showPopupErrorObserver = mockk<Observer<PopupError>>(relaxed = true)
    private var goToSecurityQuestionObserver = mockk<Observer<String>>(relaxed = true)
    private var loginToken = mockk<Observer<Result<LoginTokenPojo>>>(relaxed = true)
    private var loginTokenV2 = mockk<Observer<Result<LoginToken>>>(relaxed = true)
    private var loginFingerprint = mockk<Observer<Result<LoginToken>>>(relaxed = true)
    private var navigateGojekSeamlessObserver = mockk<Observer<Boolean>>(relaxed = true)

    private var loginTokenGoogleObserver = mockk<Observer<Result<LoginTokenPojo>>>(relaxed = true)
    private var getUserInfoObserver = mockk<Observer<Result<ProfilePojo>>>(relaxed = true)
    private var getTickerInfoObserver = mockk<Observer<Result<List<TickerInfoPojo>>>>(relaxed = true)
    private var getDynamicBannerObserver = mockk<Observer<Result<DynamicBannerDataModel>>>(relaxed = true)
    private var reloginAfterSqObserver = mockk<Observer<Result<LoginTokenPojo>>>(relaxed = true)
    private var goToActivationPageAfterReloginObserver = mockk<Observer<MessageErrorException>>(relaxed = true)
    private var goToSecurityAfterReloginQuestionObserver = mockk<Observer<String>>(relaxed = true)
    private var goToActivationPage = mockk<Observer<String>>(relaxed = true)
    private var getTemporaryKeyObserver = mockk<Observer<Boolean>>(relaxed = true)

    private var showLocationAdminPopUp = mockk<Observer<Result<Boolean>>>(relaxed = true)

    private var adminRedirection = mockk<Observer<Result<Boolean>>>(relaxed = true)

    private var loginTokenV2UseCase = mockk<LoginTokenV2UseCase>(relaxed = true)
    private var getAdminTypeUseCase = mockk<GetAdminTypeUseCase>(relaxed = true)
    private var generatePublicKeyUseCase = mockk<GeneratePublicKeyUseCase>(relaxed = true)

    private var getTemporaryKeyUseCase = mockk<GetTemporaryKeyUseCase>(relaxed = true)

    private var gotoSeamlessHelper = mockk<GotoSeamlessHelper>(relaxed = true)
    private var gotoSeamlessPreference = mockk<GotoSeamlessPreference>(relaxed = true)

    private val messageException = MessageErrorException("error bro")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(RsaUtils)
        mockkObject(EncoderDecoder())

        viewModel = LoginEmailPhoneViewModel(
            registerCheckUseCase,
            discoverUseCase,
            activateUserUseCase,
            loginTokenUseCase,
            getProfileUseCase,
            tickerInfoUseCase,
            getAdminTypeUseCase,
            loginTokenV2UseCase,
            generatePublicKeyUseCase,
            dynamicBannerUseCase,
            registerCheckFingerprintUseCase,
            loginFingerprintUseCase,
            getTemporaryKeyUseCase,
            gotoSeamlessHelper,
            gotoSeamlessPreference,
            userSession,
            CoroutineTestDispatchersProvider
        )

        viewModel.registerCheckResponse.observeForever(registerCheckObserver)
        viewModel.activateResponse.observeForever(activateUserObserver)
        viewModel.discoverResponse.observeForever(discoverObserver)
        viewModel.loginTokenResponse.observeForever(loginToken)
        viewModel.loginTokenV2Response.observeForever(loginTokenV2)
        viewModel.showPopup.observeForever(showPopupErrorObserver)
        viewModel.goToSecurityQuestion.observeForever(goToSecurityQuestionObserver)
        viewModel.loginTokenGoogleResponse.observeForever(loginTokenGoogleObserver)
        viewModel.profileResponse.observeForever(getUserInfoObserver)
        viewModel.getTickerInfoResponse.observeForever(getTickerInfoObserver)
        viewModel.dynamicBannerResponse.observeForever(getDynamicBannerObserver)
        viewModel.loginTokenAfterSQResponse.observeForever(reloginAfterSqObserver)
        viewModel.goToActivationPage.observeForever(goToActivationPage)
        viewModel.goToActivationPageAfterRelogin.observeForever(goToActivationPageAfterReloginObserver)
        viewModel.goToSecurityQuestionAfterRelogin.observeForever(goToSecurityAfterReloginQuestionObserver)
        viewModel.registerCheckFingerprint.observeForever(registerCheckFingerprintObserver)
        viewModel.loginBiometricResponse.observeForever(loginFingerprint)
        viewModel.showLocationAdminPopUp.observeForever(showLocationAdminPopUp)
        viewModel.getTemporaryKeyResponse.observeForever(getTemporaryKeyObserver)
        viewModel.navigateToGojekSeamless.observeForever(navigateGojekSeamlessObserver)
        viewModel.adminRedirection.observeForever(adminRedirection)
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

        coEvery { registerCheckUseCase.executeOnBackground() } returns response

        viewModel.registerCheck(testId)

        /* Then */
        verify { registerCheckObserver.onChanged(Success(responseData)) }
    }

    @Test
    fun `on Failed Register Check`() {
        val testId = "123456"

        coEvery { registerCheckUseCase.executeOnBackground() } throws throwable

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

        coEvery { registerCheckUseCase.executeOnBackground() } returns response

        viewModel.registerCheck(testId)

        /* Then */
        MatcherAssert.assertThat(viewModel.registerCheckResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        MatcherAssert.assertThat((viewModel.registerCheckResponse.value as Fail).throwable, CoreMatchers.instanceOf(MessageErrorException::class.java))
    }

    @Test
    fun `on Register check has other Errors`() {
        /* When */
        val errors = arrayListOf("")
        val responseData = RegisterCheckData(errors = errors)
        val response = RegisterCheckPojo(data = responseData)

        val testId = "123456"

        coEvery { registerCheckUseCase.executeOnBackground() } returns response

        viewModel.registerCheck(testId)

        /* Then */
        MatcherAssert.assertThat(viewModel.registerCheckResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        MatcherAssert.assertThat((viewModel.registerCheckResponse.value as Fail).throwable, CoreMatchers.instanceOf(RuntimeException::class.java))
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
        val discoverPojo = DiscoverPojo()

        coEvery { discoverUseCase(any()) } returns discoverPojo

        viewModel.discoverLogin()

        /* Then */
        verify { discoverObserver.onChanged(Success(discoverPojo.data)) }
    }

    @Test
    fun `on Failed Discover`() {
        /* When */
        coEvery { discoverUseCase(any()) } throws throwable

        viewModel.discoverLogin()

        /* Then */
        MatcherAssert.assertThat(viewModel.discoverResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        verify { discoverObserver.onChanged(Fail(throwable)) }
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
            loginTokenV2.onChanged(Success(responseToken.loginToken))
        }
    }

    @Test
    fun `on Login Email V2 Success - has errors`() {
        /* When */
        val loginToken = LoginToken(accessToken = "abc123", errors = arrayListOf(Error("msg", "error")))
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
            loginTokenV2.onChanged(any<Fail>())
        }
    }

    @Test
    fun `on Login Email V2 Success - popup error`() {
        /* When */
        val popupError = PopupError("header", body = "body", action = "action")
        val loginToken = LoginToken(accessToken = "", popupError = popupError)
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
            showPopupErrorObserver.onChanged(popupError)
        }
    }

    @Test
    fun `on Login Email V2 Success - activation error`() {
        /* When */
        val loginToken = LoginToken(accessToken = "", errors = arrayListOf(Error(message = "belum diaktivasi")))
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
            goToActivationPage.onChanged(email)
        }
    }

    @Test
    fun `on Login Email V2 Success - go to security question`() {
        /* When */
        val loginToken = LoginToken(accessToken = "abc123", sqCheck = true)
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
        verify { goToSecurityQuestionObserver.onChanged(email) }
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
        every { userSession.loginMethod } returns "phone"
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
        every { userSession.loginMethod } returns "phone"
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
        MatcherAssert.assertThat(viewModel.getTickerInfoResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        assertEquals((viewModel.getTickerInfoResponse.value as Fail).throwable.message, throwable.message)
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
    fun `on Success Register Check Fingerprint`() {
        /* When */
        val responseData = RegisterCheckFingerprintResult(isRegistered = true)
        val response = RegisterCheckFingerprint(data = responseData)

        every { registerCheckFingerprintUseCase.checkRegisteredFingerprint(any(), any()) } answers {
            firstArg<(RegisterCheckFingerprint) -> Unit>().invoke(response)
        }

        viewModel.registerCheckFingerprint()

        /* Then */
        verify { registerCheckFingerprintObserver.onChanged(Success(response)) }
    }

    @Test
    fun `on Failed Register Check Fingerprint`() {

        every { registerCheckFingerprintUseCase.checkRegisteredFingerprint(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        viewModel.registerCheckFingerprint()

        /* Then */
        MatcherAssert.assertThat(viewModel.registerCheckFingerprint.value, CoreMatchers.instanceOf(Fail::class.java))
        assertEquals((viewModel.registerCheckFingerprint.value as Fail).throwable.message, throwable.message)
    }

    @Test
    fun `on Register check Fingerprint has Errors`() {
        /* When */
        val responseData = RegisterCheckFingerprintResult(isRegistered = false, errorMessage = "error")
        val response = RegisterCheckFingerprint(data = responseData)

        every { registerCheckFingerprintUseCase.checkRegisteredFingerprint(any(), any()) } answers {
            firstArg<(RegisterCheckFingerprint) -> Unit>().invoke(response)
        }

        viewModel.registerCheckFingerprint()

        /* Then */
        MatcherAssert.assertThat(viewModel.registerCheckFingerprint.value, CoreMatchers.instanceOf(Fail::class.java))
        MatcherAssert.assertThat((viewModel.registerCheckFingerprint.value as Fail).throwable, CoreMatchers.instanceOf(MessageErrorException::class.java))
    }

    @Test
    fun `on Success Login Fingerprint`() {
        /* When */
        val responseToken = LoginToken(accessToken = "abc123", refreshToken = "azzz", tokenType = "12")

        every { loginFingerprintUseCase.loginBiometric(any(), any(), any(), any(), any(), any(), any()) } answers {
            arg<(LoginToken) -> Unit>(2).invoke(responseToken)
        }

        viewModel.loginTokenBiometric("test", "1234")

        /* Then */
        verify { loginFingerprint.onChanged(Success(responseToken)) }
    }

    @Test
    fun `on Success Login Fingerprint has Errors`() {
        val errorMsg = "message"
        /* When */
        val responseToken = LoginToken(errors = arrayListOf(Error("error", errorMsg)))

        every { loginFingerprintUseCase.loginBiometric(any(), any(), any(), any(), any(), any(), any()) } answers {
            arg<(LoginToken) -> Unit>(2).invoke(responseToken)
        }

        viewModel.loginTokenBiometric("test", "1234")

        /* Then */
        verify { loginFingerprint.onChanged(any<Fail>()) }
        val result = viewModel.loginBiometricResponse.value as Fail
        assert(result.throwable.message == errorMsg)
    }

    @Test
    fun `on Success Login Fingerprint has empty Errors`() {
        val errorMsg = ""
        /* When */
        val responseToken = LoginToken(errors = arrayListOf(Error("error", errorMsg)))

        every { loginFingerprintUseCase.loginBiometric(any(), any(), any(), any(), any(), any(), any()) } answers {
            arg<(LoginToken) -> Unit>(2).invoke(responseToken)
        }

        viewModel.loginTokenBiometric("test", "1234")

        /* Then */
        verify { loginFingerprint.onChanged(any<Fail>()) }
        MatcherAssert.assertThat((viewModel.loginBiometricResponse.value as Fail).throwable, CoreMatchers.instanceOf(RuntimeException::class.java))
    }

    @Test
    fun `on Failed Login Fingerprint`() {

        every { loginFingerprintUseCase.loginBiometric(any(), any(), any(), any(), any(), any(), any()) } answers {
            arg<(Throwable) -> Unit>(3).invoke(throwable)
        }

        viewModel.loginTokenBiometric("test", "1234")

        /* Then */
        MatcherAssert.assertThat(viewModel.loginBiometricResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        assertEquals((viewModel.loginBiometricResponse.value as Fail).throwable.message, throwable.message)
    }

    @Test
    fun `on Failed Login Fingerprint Show Popup Error`() {
        /* When */
        val popupError = mockk<PopupError>(relaxed = true)
        val responseToken = LoginToken(accessToken = "abc123", refreshToken = "azzz", tokenType = "12", popupError = popupError)

        every { loginFingerprintUseCase.loginBiometric(any(), any(), any(), any(), any(), any(), any()) } answers {
            arg<(LoginToken) -> Unit>(4).invoke(responseToken)
        }

        viewModel.loginTokenBiometric("test", "1234")

        /* Then */
        verify {
            showPopupErrorObserver.onChanged(popupError)
        }
    }

    @Test
    fun `on Failed Login Fingerprint onGoToActivationPage`() {
        /* When */
        val messageErrorException = mockk<MessageErrorException>(relaxed = true)

        every { loginFingerprintUseCase.loginBiometric(any(), any(), any(), any(), any(), any(), any()) } answers {
            arg<(MessageErrorException) -> Unit>(5).invoke(messageErrorException)
        }

        viewModel.loginTokenBiometric("test", "1234")

        /* Then */
        verify {
            goToActivationPage.onChanged("test")
        }
    }

    @Test
    fun `on Failed Login Fingerprint onGoToSecurityQuestion`() {
        every { loginFingerprintUseCase.loginBiometric(any(), any(), any(), any(), any(), any(), any()) } answers {
            arg<() -> Unit>(6).invoke()
        }

        viewModel.loginTokenBiometric("test", "1234")

        /* Then */
        verify {
            goToSecurityQuestionObserver.onChanged("test")
        }
    }

    @Test
    fun `on Show Location Admin Popup`() {

        every { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().showLocationAdminPopUp?.invoke()
        }

        viewModel.getUserInfo()

        /* Then */
        verify {
            showLocationAdminPopUp.onChanged(Success(true))
        }
    }

    @Test
    fun `on Admin Redirection`() {

        every { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().onLocationAdminRedirection?.invoke()
        }

        viewModel.getUserInfo()

        /* Then */
        verify {
            adminRedirection.onChanged(Success(true))
        }
    }

    @Test
    fun `on Show Location Admin Popup Error`() {

        every { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().showErrorGetAdminType?.invoke(throwable)
        }

        viewModel.getUserInfo()

        /* Then */
        verify {
            showLocationAdminPopUp.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `getTemporaryKeyForSDK success`() {
        val token = "abc"
        val data = TempKeyData(token)
        val resp = TempKeyResponse(data)
        coEvery { getTemporaryKeyUseCase(any()) } returns resp

        viewModel.getTemporaryKeyForSDK(ProfilePojo())

        coVerify {
            gotoSeamlessPreference.storeTemporaryToken(token)
            gotoSeamlessHelper.saveUserProfileToSDK(any())
            getTemporaryKeyObserver.onChanged(true)
        }
    }

    @Test
    fun `getTemporaryKeyForSDK throw Error`() {
        coEvery { getTemporaryKeyUseCase(any()) } throws throwable

        viewModel.getTemporaryKeyForSDK(ProfilePojo())

        coVerify {
            getTemporaryKeyObserver.onChanged(false)
        }
    }

    @Test
    fun `check seamless eligibility - success`() {
        val gojekProfileData = GojekProfileData(authCode = "abc")

        coEvery { gotoSeamlessHelper.getGojekProfile() } returns gojekProfileData

        viewModel.checkSeamlessEligiblity()

        verify {
            navigateGojekSeamlessObserver.onChanged(true)
        }
    }

    @Test
    fun `check seamless eligibility - failed`() {
        val exception = Exception("error")
        coEvery { gotoSeamlessHelper.getGojekProfile() } throws exception

        viewModel.checkSeamlessEligiblity()

        verify {
            navigateGojekSeamlessObserver.onChanged(false)
        }
    }

    @Test
    fun `clear task`() {
        viewModel.clearBackgroundTask()
        verify {
            tickerInfoUseCase.unsubscribe()
            loginTokenUseCase.unsubscribe()
            getProfileUseCase.unsubscribe()
        }
    }
}
