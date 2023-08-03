package com.tokopedia.loginHelper

import android.util.Base64
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.usecase.GetUserDetailsRestUseCase
import com.tokopedia.loginHelper.presentation.viewmodel.LoginHelperViewModel
import com.tokopedia.loginHelper.presentation.viewmodel.state.LoginHelperAction
import com.tokopedia.loginHelper.presentation.viewmodel.state.LoginHelperEvent
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.Error
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetAdminTypeUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenV2UseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LoginHelperViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var testRule = CoroutineTestRule()

    private lateinit var getUserDetailsRestCase: GetUserDetailsRestUseCase
    private lateinit var loginTokenV2UseCase: LoginTokenV2UseCase
    private lateinit var generatePublicKeyUseCase: GeneratePublicKeyUseCase
    private lateinit var getProfileUseCase: GetProfileUseCase
    private lateinit var getAdminTypeUseCase: GetAdminTypeUseCase

    private lateinit var viewModel: LoginHelperViewModel
    private lateinit var userSession: UserSessionInterface

    private val throwable = Throwable("Error")
    private val email = "sourav.saikia@tokopedia.com"
    private val password = "abc123456"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(RsaUtils)
        mockkObject(EncoderDecoder())
        getUserDetailsRestCase = mockk(relaxed = true)
        loginTokenV2UseCase = mockk(relaxed = true)
        generatePublicKeyUseCase = mockk(relaxed = true)
        getProfileUseCase = mockk(relaxed = true)
        getAdminTypeUseCase = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        viewModel =
            LoginHelperViewModel(
                testRule.dispatchers,
                getUserDetailsRestCase,
                loginTokenV2UseCase,
                generatePublicKeyUseCase,
                userSession,
                getProfileUseCase,
                getAdminTypeUseCase
            )
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `processEvent with Change Env Type = STAGING`() {
        viewModel.processEvent(LoginHelperEvent.ChangeEnvType(LoginHelperEnvType.STAGING))
        val result = viewModel.uiState.value.envType
        assertEquals(result, LoginHelperEnvType.STAGING)
    }

    @Test
    fun `processEvent with Change Env Type = PRODUCTION`() {
        viewModel.processEvent(LoginHelperEvent.ChangeEnvType(LoginHelperEnvType.PRODUCTION))
        val result = viewModel.uiState.value.envType
        assertEquals(result, LoginHelperEnvType.PRODUCTION)
    }

    @Test
    fun `processEvent when Back Button is tapped`() {
        runBlockingTest {
            val emittedValues = arrayListOf<LoginHelperAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }
            viewModel.processEvent(LoginHelperEvent.TapBackButton)
            val actualEvent = emittedValues.last()
            val backButtonTapped = actualEvent is LoginHelperAction.TapBackAction
            assertEquals(true, backButtonTapped)
            job.cancel()
        }
    }

    @Test
    fun `processEvent when need to go to login Screen`() {
        runBlockingTest {
            val emittedValues = arrayListOf<LoginHelperAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }
            viewModel.processEvent(LoginHelperEvent.GoToLoginPage)
            val actualEvent = emittedValues.last()
            val goToLoginPage = actualEvent is LoginHelperAction.GoToLoginPage
            assertEquals(true, goToLoginPage)
            job.cancel()
        }
    }

    @Test
    fun `processEvent with QueryEmail`() {
        runBlockingTest {
            viewModel.processEvent(LoginHelperEvent.GetLoginData)

            viewModel.processEvent(LoginHelperEvent.QueryEmail("pbs"))
            val result = viewModel.uiState.value.searchText
            assertEquals(result, "pbs")
        }
    }

    @Test
    fun `processEvent with QueryEmail throws exception`() {
        runBlockingTest {
            coEvery { getUserDetailsRestCase.executeOnBackground() } throws throwable

            viewModel.processEvent(LoginHelperEvent.QueryEmail("pbs"))
            val result = viewModel.uiState.value.searchText
            assertEquals(result, "pbs")
        }
    }

    @Test
    fun `on Success get user info`() {
        val profileInfo = ProfileInfo(firstName = "Test")
        val response = ProfilePojo(profileInfo = profileInfo)

        every { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().onSuccessGetProfile(response)
        }

        viewModel.processEvent(LoginHelperEvent.GetUserInfo)
        val result = viewModel.uiState.value.profilePojo
        assertTrue(result is Success)
    }

    @Test
    fun `on Failed get user info`() {
        every { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().onErrorGetProfile(throwable)
        }

        viewModel.processEvent(LoginHelperEvent.GetUserInfo)

        val result = viewModel.uiState.value.profilePojo
        assertTrue(result is Fail)
    }

    @Test
    fun `on Show Location Admin Popup`() {
        every { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().showLocationAdminPopUp?.invoke()
        }

        viewModel.processEvent(LoginHelperEvent.GetUserInfo)

        val result = viewModel.uiState.value.profilePojo
        assertTrue(result is Fail)
    }

    @Test
    fun `on Admin Redirection`() {
        every { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().onLocationAdminRedirection?.invoke()
        }

        viewModel.processEvent(LoginHelperEvent.GetUserInfo)

        val result = viewModel.uiState.value.profilePojo
        assertTrue(result is Fail)
    }

    @Test
    fun `on Show Location Admin Popup Error`() {
        every { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().showErrorGetAdminType?.invoke(throwable)
        }

        viewModel.processEvent(LoginHelperEvent.GetUserInfo)

        val result = viewModel.uiState.value.profilePojo
        assertTrue(result is Fail)
    }

    @Test
    fun `on LoginEvent Success`() {
        val loginToken = LoginToken(accessToken = "abc123")
        val responseToken = LoginTokenPojoV2(loginToken = loginToken)

        val keyData = KeyData(key = "cGFkZGluZw==", hash = "zzzz")
        val generateKeyPojo = GenerateKeyPojo(keyData = keyData)

        mockkStatic("android.util.Base64")
        mockkStatic(EncoderDecoder::class)

        every { EncoderDecoder.Encrypt(any(), any()) } returns "ok"
        every { Base64.decode(keyData.key, any()) } returns ByteArray(10)

        coEvery { RsaUtils.encrypt(any(), any(), true) } returns "qwerty"
        coEvery { generatePublicKeyUseCase() } returns generateKeyPojo
        coEvery { loginTokenV2UseCase.executeOnBackground() } returns responseToken

        viewModel.processEvent(LoginHelperEvent.LoginUser(email, password, true))

        assert(
            viewModel.uiState.value.loginToken is Success
        )
    }

    @Test
    fun `on LoginEvent Success with useHash=false`() {
        val loginToken = LoginToken(accessToken = "abc123")
        val responseToken = LoginTokenPojoV2(loginToken = loginToken)

        val keyData = KeyData(key = "cGFkZGluZw==", hash = "zzzz")
        val generateKeyPojo = GenerateKeyPojo(keyData = keyData)

        mockkStatic("android.util.Base64")
        mockkStatic(EncoderDecoder::class)

        every { EncoderDecoder.Encrypt(any(), any()) } returns "ok"
        every { Base64.decode(keyData.key, any()) } returns ByteArray(10)

        coEvery { RsaUtils.encrypt(any(), any(), true) } returns "qwerty"
        coEvery { generatePublicKeyUseCase() } returns generateKeyPojo
        coEvery { loginTokenV2UseCase.executeOnBackground() } returns responseToken

        viewModel.processEvent(LoginHelperEvent.LoginUser(email, password, false))

        assert(
            viewModel.uiState.value.loginToken is Success
        )
    }

    @Test
    fun `on Login Email V2 Success - has errors`() {
        val loginToken = LoginToken(accessToken = "abc123", errors = arrayListOf(Error("msg", "error")))
        val responseToken = LoginTokenPojoV2(loginToken = loginToken)

        val keyData = KeyData(key = "cGFkZGluZw==", hash = "zzzz")
        val generateKeyPojo = GenerateKeyPojo(keyData = keyData)

        mockkStatic("android.util.Base64")
        mockkStatic(EncoderDecoder::class)

        every { EncoderDecoder.Encrypt(any(), any()) } returns "ok"
        every { Base64.decode(keyData.key, any()) } returns ByteArray(10)

        coEvery { RsaUtils.encrypt(any(), any(), true) } returns "qwerty"
        coEvery { generatePublicKeyUseCase() } returns generateKeyPojo
        coEvery { loginTokenV2UseCase.executeOnBackground() } returns responseToken

        viewModel.processEvent(LoginHelperEvent.LoginUser(email, password, true))

        assert(
            viewModel.uiState.value.loginToken is Fail
        )
    }

    @Test
    fun `on Login Email V2 Success - popup error`() {
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
        coEvery { generatePublicKeyUseCase() } returns generateKeyPojo
        coEvery { loginTokenV2UseCase.executeOnBackground() } returns responseToken

        viewModel.processEvent(LoginHelperEvent.LoginUser(email, password, true))

        assert(
            viewModel.uiState.value.loginToken is Fail
        )
    }

    @Test
    fun `on Login Email V2 Success - activation error`() {
        val loginToken = LoginToken(accessToken = "", errors = arrayListOf(Error(message = "belum diaktivasi")))
        val responseToken = LoginTokenPojoV2(loginToken = loginToken)

        val keyData = KeyData(key = "cGFkZGluZw==", hash = "zzzz")
        val generateKeyPojo = GenerateKeyPojo(keyData = keyData)

        mockkStatic("android.util.Base64")
        mockkStatic(EncoderDecoder::class)

        every { EncoderDecoder.Encrypt(any(), any()) } returns "ok"
        every { Base64.decode(keyData.key, any()) } returns ByteArray(10)

        coEvery { RsaUtils.encrypt(any(), any(), true) } returns "qwerty"
        coEvery { generatePublicKeyUseCase() } returns generateKeyPojo
        coEvery { loginTokenV2UseCase.executeOnBackground() } returns responseToken

        viewModel.processEvent(LoginHelperEvent.LoginUser(email, password, true))

        assert(
            viewModel.uiState.value.loginToken is Fail
        )
    }

    @Test
    fun `on Login Email V2 Success - go to security question`() {
        val loginToken = LoginToken(accessToken = "abc123", sqCheck = true)
        val responseToken = LoginTokenPojoV2(loginToken = loginToken)

        val keyData = KeyData(key = "cGFkZGluZw==", hash = "zzzz")
        val generateKeyPojo = GenerateKeyPojo(keyData = keyData)

        mockkStatic("android.util.Base64")
        mockkStatic(EncoderDecoder::class)

        every { EncoderDecoder.Encrypt(any(), any()) } returns "ok"
        every { Base64.decode(keyData.key, any()) } returns ByteArray(10)

        coEvery { RsaUtils.encrypt(any(), any(), true) } returns "qwerty"
        coEvery { generatePublicKeyUseCase() } returns generateKeyPojo
        coEvery { loginTokenV2UseCase.executeOnBackground() } returns responseToken

        viewModel.processEvent(LoginHelperEvent.LoginUser(email, password, true))

        assert(
            viewModel.uiState.value.loginToken is Fail
        )
    }

    @Test
    fun `on Login Email V2 Empty key`() {
        val keyData = KeyData(key = "", hash = "")
        val generateKeyPojo = GenerateKeyPojo(keyData = keyData)

        coEvery { generatePublicKeyUseCase() } returns generateKeyPojo

        viewModel.processEvent(LoginHelperEvent.LoginUser(email, password, true))

        assert(
            viewModel.uiState.value.loginToken is Fail
        )
    }

    @Test
    fun `on Login Email V2 Error`() {
        coEvery { generatePublicKeyUseCase() } throws throwable

        viewModel.processEvent(LoginHelperEvent.LoginUser(email, password, true))

        assert(
            viewModel.uiState.value.loginToken is Fail
        )
    }

    @Test
    fun `onCleared success`() {
        every {
            getProfileUseCase.unsubscribe()
        } just runs
        every {
            loginTokenV2UseCase.cancelJobs()
        } just runs
        every {
            getUserDetailsRestCase.cancelJobs()
        } just runs

        val method = viewModel::class.java.getDeclaredMethod("onCleared")
        method.isAccessible = true
        method.invoke(viewModel)

        verify { getProfileUseCase.unsubscribe() }
        verify { loginTokenV2UseCase.cancelJobs() }
        verify { getUserDetailsRestCase.cancelJobs() }
    }
}
