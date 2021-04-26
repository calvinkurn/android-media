package com.tokopedia.loginregister.registerinitial.viewmodel

import android.util.Base64
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserPojo
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.common.view.banner.data.DynamicBannerDataModel
import com.tokopedia.loginregister.common.view.banner.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.common.view.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.common.view.ticker.domain.usecase.TickerInfoUseCase
import com.tokopedia.loginregister.discover.data.DiscoverItemDataModel
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.external_register.ovo.data.CheckOvoResponse
import com.tokopedia.loginregister.external_register.ovo.domain.usecase.CheckHasOvoAccUseCase
import com.tokopedia.loginregister.login.view.model.DiscoverDataModel
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialUseCase
import com.tokopedia.loginregister.loginthirdparty.facebook.data.FacebookCredentialData
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialQueryConstant
import com.tokopedia.loginregister.registerinitial.domain.data.ProfileInfoData
import com.tokopedia.loginregister.registerinitial.domain.pojo.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.*
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenFacebookSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber
import java.util.*

class RegisterInitialViewModelTest {

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
    val checkHasOvoUseCase = mockk<CheckHasOvoAccUseCase>(relaxed = true)

    val userSession = mockk<UserSessionInterface>(relaxed = true)
    val rawQueries = mapOf(
            RegisterInitialQueryConstant.MUTATION_REGISTER_CHECK to "test",
            RegisterInitialQueryConstant.MUTATION_REGISTER_REQUEST to "test"
    )

    private var registerCheckObserver = mockk<Observer<Result<RegisterCheckData>>>(relaxed = true)
    private var registerRequestObserver = mockk<Observer<Result<RegisterRequestData>>>(relaxed = true)
    private var activateUserObserver = mockk<Observer<Result<ActivateUserData>>>(relaxed = true)
    private var discoverObserver = mockk<Observer<Result<ArrayList<DiscoverItemDataModel>>>>(relaxed = true)
    private var getFacebookObserver = mockk<Observer<Result<FacebookCredentialData>>>(relaxed = true)
    private var showPopupErrorObserver = mockk<Observer<PopupError>>(relaxed = true)
    private var goToActivationPageObserver = mockk<Observer<MessageErrorException>>(relaxed = true)
    private var goToSecurityQuestionObserver = mockk<Observer<String>>(relaxed = true)
    private var loginTokenFacebookPhoneObserver = mockk<Observer<Result<LoginTokenPojo>>>(relaxed = true)
    private var loginTokenGoogleObserver = mockk<Observer<Result<LoginTokenPojo>>>(relaxed = true)
    private var getUserInfoObserver = mockk<Observer<Result<ProfileInfoData>>>(relaxed = true)
    private var getUserInfoAddPinObserver = mockk<Observer<Result<ProfileInfoData>>>(relaxed = true)
    private var getTickerInfoObserver = mockk<Observer<Result<List<TickerInfoPojo>>>>(relaxed = true)
    private var getDynamicBannerObserver = mockk<Observer<Result<DynamicBannerDataModel>>>(relaxed = true)
    private var reloginAfterSqObserver = mockk<Observer<Result<LoginTokenPojo>>>(relaxed = true)
    private var validateTokenObserver = mockk<Observer<String>>(relaxed = true)
    private var goToActivationPageAfterReloginObserver = mockk<Observer<MessageErrorException>>(relaxed = true)
    private var goToSecurityAfterReloginQuestionObserver = mockk<Observer<String>>(relaxed = true)
    private var hasOvoObserver = mockk<Observer<Result<CheckOvoResponse>>>(relaxed = true)

    lateinit var viewModel: RegisterInitialViewModel

    val throwable = Throwable(message = "Error")
    val messageException = MessageErrorException("error bro")

    val mockFragment = mockk<Fragment>(relaxed = true)
    val mockCallbackManager = mockk<CallbackManager>(relaxed = true)

    private var generatePublicKeyUseCase = mockk<GeneratePublicKeyUseCase>(relaxed = true)
    private var registerV2UseCase = mockk<GraphqlUseCase<RegisterRequestV2>>(relaxed = true)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(RsaUtils)
        mockkObject(EncoderDecoder())

        viewModel = RegisterInitialViewModel(
                registerCheckUseCase,
                registerRequestUseCase,
                registerV2UseCase,
                activateUserUseCase,
                discoverUseCase,
                getFacebookCredentialUseCase,
                loginTokenUseCase,
                getProfileUseCase,
                tickerInfoUseCase,
                dynamicBannerUseCase,
                checkHasOvoUseCase,
                generatePublicKeyUseCase,
                userSession,
                rawQueries,
                CoroutineTestDispatchersProvider
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
        viewModel.getUserInfoResponse.observeForever(getUserInfoObserver)
        viewModel.getUserInfoAfterAddPinResponse.observeForever(getUserInfoAddPinObserver)
        viewModel.getTickerInfoResponse.observeForever(getTickerInfoObserver)
        viewModel.dynamicBannerResponse.observeForever(getDynamicBannerObserver)
        viewModel.loginTokenAfterSQResponse.observeForever(reloginAfterSqObserver)
        viewModel.validateToken.observeForever(validateTokenObserver)
        viewModel.goToActivationPageAfterRelogin.observeForever(goToActivationPageAfterReloginObserver)
        viewModel.goToSecurityQuestionAfterRelogin.observeForever(goToSecurityAfterReloginQuestionObserver)
        viewModel.checkOvoResponse.observeForever(hasOvoObserver)

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
    fun `on Error Register Request had errors`() {
        /* When */
        val errors = arrayListOf(RegisterRequestErrorData(name = "errors", message = "error happen"))
        val responseData = RegisterRequestData(accessToken = "", refreshToken = "", tokenType = "", errors = errors)
        val response = RegisterRequestPojo(data = responseData)

        every { registerRequestUseCase.execute(any(), any()) } answers {
            firstArg<(RegisterRequestPojo) -> Unit>().invoke(response)
        }

        viewModel.registerRequest("", "", "", "")

        /* Then */
        assertThat(viewModel.registerRequestResponse.value, instanceOf(Fail::class.java))
    }

    @Test
    fun `on Error Register Request had another errors`() {
        /* When */
        val responseData = RegisterRequestData(accessToken = "", refreshToken = "", tokenType = "")
        val response = RegisterRequestPojo(data = responseData)

        every { registerRequestUseCase.execute(any(), any()) } answers {
            firstArg<(RegisterRequestPojo) -> Unit>().invoke(response)
        }

        viewModel.registerRequest("", "", "", "")

        /* Then */
        assertThat(viewModel.registerRequestResponse.value, instanceOf(Fail::class.java))
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
    fun `on Success Register Request v2`() {
        /* When */
        val responseData = RegisterRequestData(accessToken = "abc123", refreshToken = "abc123", tokenType = "12")
        val response = RegisterRequestV2(data = responseData)
        val keyData = KeyData(key = "testkey", hash = "123")
        val keyPojo = GenerateKeyPojo(keyData)

        mockkStatic("android.util.Base64")
        mockkStatic(EncoderDecoder::class)

        every { EncoderDecoder.Encrypt(any(), any()) } returns "ok"
        every { Base64.decode(keyData.key, any()) } returns ByteArray(10)

        coEvery { RsaUtils.encrypt(any(), any(), true) } returns "qwerty"

        coEvery { generatePublicKeyUseCase.executeOnBackground() } returns keyPojo
        coEvery { registerV2UseCase.executeOnBackground() } returns response

        viewModel.registerRequestV2("yoris.prayogo@tokopedia.com", "123456", "Yoris", "asd")

        /* Then */
        coVerify {
            generatePublicKeyUseCase.executeOnBackground()
            RsaUtils.encrypt(any(), any(), true)
            userSession.setToken(any(), any())
            registerRequestObserver.onChanged(Success(responseData))
        }
    }

    @Test
    fun `on Failed Register Request v2`() {
        coEvery { generatePublicKeyUseCase.executeOnBackground() } throws throwable

        viewModel.registerRequestV2("yoris.prayogo@tokopedia.com", "123456", "Yoris", "asd")

        /* Then */
        coVerify {
            userSession.clearToken()
            registerRequestObserver.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `on Success Activate User`() {
        val params = mapOf("email" to "asd")

        /* When */
        val responseData = ActivateUserData(isSuccess = 1, accessToken = "asd", refreshToken = "fffaa", tokenType = "Bearer")
        val response = ActivateUserPojo(data = responseData)

        coEvery { activateUserUseCase.executeOnBackground() } returns response

        viewModel.activateUser("", "")

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
            userSession.clearToken()
        }
    }

    @Test
    fun `on Message is not empty during Activate User`() {
        val params = mapOf("email" to "asd")

        /* When */
        val responseData = ActivateUserData(message = "error happen!")
        val response = ActivateUserPojo(data = responseData)

        coEvery { activateUserUseCase.executeOnBackground() } returns response

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
        val discoverViewModel = DiscoverDataModel(arrayListOf(
                DiscoverItemDataModel("123", "", "", "", "")
        ), "")

        every { discoverUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<DiscoverDataModel>>().onNext(discoverViewModel)
        }

        viewModel.getProvider()

        /* Then */
        verify { discoverObserver.onChanged(Success(discoverViewModel.providers)) }
    }

    @Test
    fun `on Providers Empty Error`() {
        /* When */
        every { discoverUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<DiscoverDataModel>>().onError(throwable)
        }

        viewModel.getProvider()

        /* Then */
        assertThat(viewModel.getProviderResponse.value, instanceOf(Fail::class.java))
    }

    @Test
    fun `on Failed Discover`() {
        /* When */
        every { discoverUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<DiscoverDataModel>>().onError(throwable)
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
            getUserInfoObserver.onChanged(Success(ProfileInfoData(response.profileInfo)))
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
    fun `on Success get user info after add pin`() {
        /* When */
        val profileInfo = ProfileInfo(firstName = "yoris")
        val response = ProfilePojo(profileInfo = profileInfo)

        every { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().onSuccessGetProfile(response)
        }

        viewModel.getUserInfoAfterAddPin()

        /* Then */
        verify {
            getUserInfoAddPinObserver.onChanged(Success(ProfileInfoData(response.profileInfo)))
        }
    }

    @Test
    fun `on Failed get user info after add pin`() {
        /* When */
        every { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().onErrorGetProfile(throwable)
        }

        viewModel.getUserInfoAfterAddPin()

        /* Then */
        verify {
            getUserInfoAddPinObserver.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `on Success get ticker`() {
        /* When */
        val tickerInfo = TickerInfoPojo("test", "test message", "")
        val tickerList = listOf(tickerInfo)

        every { tickerInfoUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<List<TickerInfoPojo>>>().onNext(tickerList)
        }

        viewModel.getTickerInfo()

        /* Then */
        verify {
            getTickerInfoObserver.onChanged(Success(tickerList))
        }
    }

    @Test
    fun `on Failed get ticker`() {
        /* When */
        every { tickerInfoUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<List<TickerInfoPojo>>>().onError(throwable)
        }

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
            validateTokenObserver.onChanged(validateToken)
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
    fun `has ovo check`() {
        val response = CheckOvoResponse()
        val phone = "082242454511"

        checkHasOvoUseCase.setParams(phone)
        coEvery { checkHasOvoUseCase.executeOnBackground() } returns response

        viewModel.checkHasOvoAccount(phone)

        verify { hasOvoObserver.onChanged(Success(response)) }
    }

    @Test
    fun `clear task`() {
        viewModel.clearBackgroundTask()
        verify {
            registerRequestUseCase.cancelJobs()
            registerCheckUseCase.cancelJobs()
            tickerInfoUseCase.unsubscribe()
            discoverUseCase.unsubscribe()
            loginTokenUseCase.unsubscribe()
            getProfileUseCase.unsubscribe()
        }
    }
}