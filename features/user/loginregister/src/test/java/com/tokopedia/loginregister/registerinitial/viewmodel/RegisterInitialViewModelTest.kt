package com.tokopedia.loginregister.registerinitial.viewmodel

import android.util.Base64
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserPojo
import com.tokopedia.loginregister.common.domain.pojo.DiscoverData
import com.tokopedia.loginregister.common.domain.pojo.DiscoverPojo
import com.tokopedia.loginregister.common.domain.pojo.DynamicBannerDataModel
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.common.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.common.domain.usecase.DiscoverUseCase
import com.tokopedia.loginregister.common.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.common.domain.usecase.RegisterCheckUseCase
import com.tokopedia.loginregister.common.domain.usecase.TickerInfoUseCase
import com.tokopedia.loginregister.registerinitial.domain.RegisterRequestV2UseCase
import com.tokopedia.loginregister.registerinitial.domain.data.ProfileInfoData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestV2
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndSaveSessionUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegisterInitialViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val registerCheckUseCase = mockk<RegisterCheckUseCase>(relaxed = true)
    val activateUserUseCase = mockk<ActivateUserUseCase>(relaxed = true)
    val discoverUseCase = mockk<DiscoverUseCase>(relaxed = true)
    val loginTokenUseCase = mockk<LoginTokenUseCase>(relaxed = true)
    val getProfileUseCase = mockk<GetUserInfoAndSaveSessionUseCase>(relaxed = true)
    val tickerInfoUseCase = mockk<TickerInfoUseCase>(relaxed = true)
    val dynamicBannerUseCase = mockk<DynamicBannerUseCase>(relaxed = true)

    val userSession = mockk<UserSessionInterface>(relaxed = true)

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

    lateinit var viewModel: RegisterInitialViewModel

    val throwable = Throwable(message = "Error")
    val messageException = MessageErrorException("error bro")

    private var generatePublicKeyUseCase = mockk<GeneratePublicKeyUseCase>(relaxed = true)
    private var registerV2UseCase = mockk<RegisterRequestV2UseCase>(relaxed = true)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(RsaUtils)
        mockkObject(EncoderDecoder())

        viewModel = RegisterInitialViewModel(
            registerCheckUseCase,
            registerV2UseCase,
            activateUserUseCase,
            discoverUseCase,
            loginTokenUseCase,
            getProfileUseCase,
            tickerInfoUseCase,
            dynamicBannerUseCase,
            generatePublicKeyUseCase,
            userSession,
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
    }

    @Test
    fun `on Success Register Check`() {
        val testId = "123456"

        /* When */
        val responseData = RegisterCheckData()
        val response = RegisterCheckPojo(data = responseData)

        coEvery { registerCheckUseCase(any()) } returns response

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

        coEvery { registerCheckUseCase(any()) } returns response

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

        coEvery { registerCheckUseCase(any()) } returns response

        viewModel.registerCheck(testId)

        /* Then */
        verify { registerCheckObserver.onChanged(any<Fail>()) }
        assertThat((viewModel.registerCheckResponse.value as Fail).throwable, instanceOf(RuntimeException::class.java))
    }

    @Test
    fun `on Failed Register Check`() {
        val testId = "123456"

        coEvery { registerCheckUseCase(any()) } throws throwable

        viewModel.registerCheck(testId)

        /* Then */
        verify { registerCheckObserver.onChanged(Fail(throwable)) }
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

        coEvery { generatePublicKeyUseCase() } returns keyPojo
        coEvery { registerV2UseCase(any()) } returns response

        viewModel.registerRequestV2("yoris.prayogo@tokopedia.com", "123456", "Yoris", "asd")

        /* Then */
        coVerify {
            generatePublicKeyUseCase()
            RsaUtils.encrypt(any(), any(), true)
            userSession.setToken(any(), any())
            registerRequestObserver.onChanged(Success(responseData))
        }
    }

    @Test
    fun `on Failed Register Request v2`() {
        coEvery { generatePublicKeyUseCase() } throws throwable

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
    fun `on Success Activate User has other errors - 2`() {
        /* When */
        val responseData = ActivateUserData(isSuccess = 0, accessToken = "", refreshToken = "", tokenType = "", message = "")
        val response = ActivateUserPojo(data = responseData)

        coEvery { activateUserUseCase.executeOnBackground() } returns response

        viewModel.activateUser("", "")

        /* Then */
        verify { activateUserObserver.onChanged(any<Fail>()) }
        assertThat((viewModel.activateUserResponse.value as Fail).throwable, instanceOf(Throwable::class.java))
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
        val response = Success(ProfilePojo(profileInfo = profileInfo))

        coEvery { getProfileUseCase(Unit) } returns response

        viewModel.getUserInfo()

        /* Then */
        verify {
            getUserInfoObserver.onChanged(Success(ProfileInfoData(response.data.profileInfo)))
        }
    }

    @Test
    fun `on Failed get user info`() {
        /* When */
        val response = Fail(throwable)
        coEvery { getProfileUseCase(Unit) } returns response

        viewModel.getUserInfo()

        /* Then */
        verify {
            getUserInfoObserver.onChanged(response)
        }
    }

    @Test
    fun `on Success get user info after add pin`() {
        /* When */
        val profileInfo = ProfileInfo(firstName = "yoris")
        val response = Success(ProfilePojo(profileInfo = profileInfo))

        coEvery { getProfileUseCase(Unit) } returns response

        viewModel.getUserInfoAfterAddPin()

        /* Then */
        verify {
            getUserInfoAddPinObserver.onChanged(Success(ProfileInfoData(response.data.profileInfo)))
        }
    }

    @Test
    fun `on Failed get user info after add pin`() {
        /* When */
        val response = Fail(throwable)
        coEvery { getProfileUseCase(Unit) } returns response

        viewModel.getUserInfoAfterAddPin()

        /* Then */
        coVerify {
            getUserInfoAddPinObserver.onChanged(response)
        }
    }

    @Test
    fun `on Success get ticker`() {
        /* When */
        val tickerInfo = TickerInfoPojo("test", "test message", "")
        val tickerList = listOf(tickerInfo)

        coEvery { tickerInfoUseCase(any()) } returns tickerList

        viewModel.getTickerInfo()

        /* Then */
        verify {
            getTickerInfoObserver.onChanged(Success(tickerList))
        }
    }

    @Test
    fun `on Failed get ticker`() {
        /* When */
        val e = Exception()
        coEvery { tickerInfoUseCase(any()) } throws e

        viewModel.getTickerInfo()

        /* Then */
        verify {
            getTickerInfoObserver.onChanged(Fail(e))
        }
    }

    @Test
    fun `on Success get dynamic banner`() {
        /* When */
        val banner = DynamicBannerDataModel.GetBanner(message = "test")
        val response = DynamicBannerDataModel(banner = banner)

        coEvery { dynamicBannerUseCase(any()) } returns response

        viewModel.getDynamicBannerData()

        /* Then */
        verify {
            getDynamicBannerObserver.onChanged(Success(response))
        }
    }

    @Test
    fun `on Failed get dynamic banner`() {
        /* When */
        coEvery { dynamicBannerUseCase(any()) } throws throwable

        viewModel.getDynamicBannerData()

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
    fun `clear task`() {
        viewModel.clearBackgroundTask()
        verify {
            loginTokenUseCase.unsubscribe()
        }
    }
}
