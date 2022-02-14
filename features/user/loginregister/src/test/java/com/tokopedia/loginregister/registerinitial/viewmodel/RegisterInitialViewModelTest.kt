package com.tokopedia.loginregister.registerinitial.viewmodel

import android.util.Base64
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
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
import com.tokopedia.loginregister.external_register.ovo.data.CheckOvoResponse
import com.tokopedia.loginregister.external_register.ovo.domain.usecase.CheckHasOvoAccUseCase
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialQueryConstant
import com.tokopedia.loginregister.registerinitial.domain.data.ProfileInfoData
import com.tokopedia.loginregister.registerinitial.domain.pojo.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.*
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
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
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber

class RegisterInitialViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val registerCheckUseCase = mockk<GraphqlUseCase<RegisterCheckPojo>>(relaxed = true)
    val registerRequestUseCase = mockk<GraphqlUseCase<RegisterRequestPojo>>(relaxed = true)
    val activateUserUseCase = mockk<ActivateUserUseCase>(relaxed = true)
    val discoverUseCase = mockk<DiscoverUseCase>(relaxed = true)
    val loginTokenUseCase = mockk<LoginTokenUseCase>(relaxed = true)
    val getProfileUseCase = mockk<GetProfileUseCase>(relaxed = true)
    val tickerInfoUseCase = mockk<TickerInfoUseCase>(relaxed = true)
    val dynamicBannerUseCase = mockk<DynamicBannerUseCase>(relaxed = true)
    val checkHasOvoUseCase = mockk<CheckHasOvoAccUseCase>(relaxed = true)

    val userSession = mockk<UserSessionInterface>(relaxed = true)
    val rawQueries = mapOf(
            RegisterInitialQueryConstant.MUTATION_REGISTER_CHECK to "test2",
            RegisterInitialQueryConstant.MUTATION_REGISTER_REQUEST to "test"
    )

    private var registerCheckObserver = mockk<Observer<Result<RegisterCheckData>>>(relaxed = true)
    private var registerRequestObserver = mockk<Observer<Result<RegisterRequestData>>>(relaxed = true)
    private var activateUserObserver = mockk<Observer<Result<ActivateUserData>>>(relaxed = true)
    private var discoverObserver = mockk<Observer<Result<DiscoverData>>>(relaxed = true)
    private var showPopupErrorObserver = mockk<Observer<PopupError>>(relaxed = true)
    private var goToActivationPageObserver = mockk<Observer<MessageErrorException>>(relaxed = true)
    private var goToSecurityQuestionObserver = mockk<Observer<String>>(relaxed = true)
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
        viewModel.showPopup.observeForever(showPopupErrorObserver)
        viewModel.goToActivationPage.observeForever(goToActivationPageObserver)
        viewModel.goToSecurityQuestion.observeForever(goToSecurityQuestionObserver)
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

        coEvery { registerCheckUseCase.executeOnBackground() } returns response

        viewModel.registerCheck(testId)

        /* Then */
        verify { registerCheckObserver.onChanged(Success(responseData)) }
    }

    @Test
    fun `on Success Register Check has errors`() {
        val testId = "123456"

        val errors = arrayListOf("error 1")
        /* When */
        val responseData = RegisterCheckData(errors = errors)
        val response = RegisterCheckPojo(data = responseData)

        coEvery { registerCheckUseCase.executeOnBackground() } returns response

        viewModel.registerCheck(testId)

        /* Then */
        verify { registerCheckObserver.onChanged(any<Fail>()) }
        assertThat((viewModel.registerCheckResponse.value as Fail).throwable, CoreMatchers.instanceOf(MessageErrorException::class.java))
    }

    @Test
    fun `on Success Register Check has errors but empty`() {
        val testId = "123456"

        val errors = arrayListOf("")
        /* When */
        val responseData = RegisterCheckData(errors = errors)
        val response = RegisterCheckPojo(data = responseData)

        coEvery { registerCheckUseCase.executeOnBackground() } returns response

        viewModel.registerCheck(testId)

        /* Then */
        verify { registerCheckObserver.onChanged(any<Fail>()) }
        assertThat((viewModel.registerCheckResponse.value as Fail).throwable, instanceOf(RuntimeException::class.java))
    }


    @Test
    fun `on Failed Register Check`() {
        val testId = "123456"

        coEvery { registerCheckUseCase.executeOnBackground() } throws throwable

        viewModel.registerCheck(testId)

        /* Then */
        verify { registerCheckObserver.onChanged(Fail(throwable)) }
    }

    @Test
    fun `on Success Register Request`() {
        /* When */
        val responseData = RegisterRequestData(accessToken = "asda", refreshToken = "asdasd", tokenType = "kfkfk")
        val response = RegisterRequestPojo(data = responseData)

        coEvery { registerRequestUseCase.executeOnBackground() } returns response

        viewModel.registerRequest("", "", "", "")

        /* Then */
        verify { registerRequestObserver.onChanged(Success(responseData)) }
    }

    @Test
    fun `on Error Register Request has errors`() {
        /* When */
        val errors = arrayListOf(RegisterRequestErrorData(name = "errors", message = "error happen"))
        val responseData = RegisterRequestData(accessToken = "", refreshToken = "", tokenType = "", errors = errors)
        val response = RegisterRequestPojo(data = responseData)

        coEvery { registerRequestUseCase.executeOnBackground() } returns response

        viewModel.registerRequest("", "", "", "")

        /* Then */
        assertThat(viewModel.registerRequestResponse.value, instanceOf(Fail::class.java))
        assertThat((viewModel.registerRequestResponse.value as Fail).throwable, instanceOf(com.tokopedia.network.exception.MessageErrorException::class.java))
    }

    @Test
    fun `on Error Register Request has errors but empty`() {
        /* When */
        val errors = arrayListOf(RegisterRequestErrorData(name = "", message = ""))
        val responseData = RegisterRequestData(accessToken = "", refreshToken = "", tokenType = "", errors = errors)
        val response = RegisterRequestPojo(data = responseData)

        every { registerRequestUseCase.execute(any(), any()) } answers {
            firstArg<(RegisterRequestPojo) -> Unit>().invoke(response)
        }

        viewModel.registerRequest("", "", "", "")

        /* Then */
        assertThat(viewModel.registerRequestResponse.value, instanceOf(Fail::class.java))
        assertThat((viewModel.registerRequestResponse.value as Fail).throwable, instanceOf(RuntimeException::class.java))
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

        coEvery { registerRequestUseCase.executeOnBackground() } throws throwable

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
        /* When */
        val responseData = ActivateUserData(isSuccess = 1, accessToken = "asd", refreshToken = "fffaa", tokenType = "Bearer")
        val response = ActivateUserPojo(data = responseData)

        coEvery { activateUserUseCase.executeOnBackground() } returns response

        viewModel.activateUser("", "")

        /* Then */
        verify { activateUserObserver.onChanged(Success(responseData)) }
    }

    @Test
    fun `on Success Activate User has errors`() {
        /* When */
        val msg = "message"
        val responseData = ActivateUserData(isSuccess = 1, accessToken = "", refreshToken = "", tokenType = "", message = msg)
        val response = ActivateUserPojo(data = responseData)

        coEvery { activateUserUseCase.executeOnBackground() } returns response

        viewModel.activateUser("", "")

        /* Then */
        verify { activateUserObserver.onChanged(any<Fail>()) }
        assertEquals((viewModel.activateUserResponse.value as Fail).throwable.message, msg)
        assertThat((viewModel.activateUserResponse.value as Fail).throwable, instanceOf(MessageErrorException::class.java))
    }

    @Test
    fun `on Success Activate User has other errors`() {
        /* When */
        val responseData = ActivateUserData(isSuccess = 1, accessToken = "", refreshToken = "", tokenType = "", message = "")
        val response = ActivateUserPojo(data = responseData)

        coEvery { activateUserUseCase.executeOnBackground() } returns response

        viewModel.activateUser("", "")

        /* Then */
        verify { activateUserObserver.onChanged(any<Fail>()) }
        assertThat((viewModel.activateUserResponse.value as Fail).throwable, instanceOf(RuntimeException::class.java))
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
        val discoverPojo = DiscoverPojo()

        coEvery { discoverUseCase(any()) } returns discoverPojo

        viewModel.getProvider()

        /* Then */
        verify { discoverObserver.onChanged(Success(discoverPojo.data)) }
    }

    @Test
    fun `on Failed Discover`() {
        /* When */
        coEvery { discoverUseCase(any()) } throws throwable

        viewModel.getProvider()

        /* Then */
        MatcherAssert.assertThat(viewModel.getProviderResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        verify { discoverObserver.onChanged(Fail(throwable)) }
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
    fun `on Success Register Google Phone Go to activation`() {
        /* When */
        val exception = mockk<MessageErrorException>(relaxed = true)

        every { userSession.loginMethod } returns "google"
        every { loginTokenUseCase.executeLoginSocialMedia(any(), any()) } answers {
            secondArg<LoginTokenSubscriber>().onGoToActivationPage(exception)
        }

        viewModel.registerGoogle("", "")

        /* Then */
        verify {
            goToActivationPageObserver.onChanged(exception)
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
    fun `ovo check errors`() {
        val phone = "082242454511"

        checkHasOvoUseCase.setParams(phone)
        coEvery { checkHasOvoUseCase.executeOnBackground() } throws throwable

        viewModel.checkHasOvoAccount(phone)

        verify { hasOvoObserver.onChanged(Fail(throwable)) }
    }

    @Test
    fun `clear task`() {
        viewModel.clearBackgroundTask()
        verify {
            registerRequestUseCase.cancelJobs()
            registerCheckUseCase.cancelJobs()
            tickerInfoUseCase.unsubscribe()
            loginTokenUseCase.unsubscribe()
            getProfileUseCase.unsubscribe()
        }
    }
}