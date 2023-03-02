package com.example.login_helper

import android.util.Base64
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.login_helper.domain.LoginHelperEnvType
import com.tokopedia.login_helper.domain.uiModel.HeaderUiModel
import com.tokopedia.login_helper.domain.uiModel.LoginDataUiModel
import com.tokopedia.login_helper.domain.uiModel.UserDataUiModel
import com.tokopedia.login_helper.domain.usecase.GetUserDetailsRestUseCase
import com.tokopedia.login_helper.presentation.viewmodel.LoginHelperViewModel
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperAction
import com.tokopedia.login_helper.presentation.viewmodel.state.LoginHelperEvent
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
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

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val throwable = Throwable("Error")
    private val email = "yoris.prayogo@tokopedia.com"
    private val password = "abc123456"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
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

            val dummyUserList = Success(LoginDataUiModel(HeaderUiModel(1),listOf<UserDataUiModel>(
                UserDataUiModel(
                "abc@tokopedia.com","abc","abc"
            )
            )))


            viewModel.processEvent(LoginHelperEvent.QueryEmail("abc"))
            val result = viewModel.uiState.value.searchText
            assertEquals(result, "abc")
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
    fun `onCleared success`() {
        every {
            getProfileUseCase.unsubscribe()
        } just runs
        every {
            loginTokenV2UseCase.cancelJobs()
        } just runs
        every {
            generatePublicKeyUseCase.cancelJobs()
        } just runs
        every {
            getUserDetailsRestCase.cancelJobs()
        } just runs

        val method = viewModel::class.java.getDeclaredMethod("onCleared")
        method.isAccessible = true
        method.invoke(viewModel)

        verify { getProfileUseCase.unsubscribe() }
        verify { loginTokenV2UseCase.cancelJobs() }
        verify { generatePublicKeyUseCase.cancelJobs() }
        verify { getUserDetailsRestCase.cancelJobs() }

    }
}
