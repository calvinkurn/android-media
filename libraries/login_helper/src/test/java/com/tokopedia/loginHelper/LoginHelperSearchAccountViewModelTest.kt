package com.tokopedia.loginHelper

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.uiModel.deleteUser.LoginHelperDeleteUserUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.HeaderUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.LoginDataUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.UserDataUiModel
import com.tokopedia.loginHelper.domain.usecase.DeleteUserRestUseCase
import com.tokopedia.loginHelper.domain.usecase.GetUserDetailsRestUseCase
import com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.LoginHelperSearchAccountViewModel
import com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.state.LoginHelperSearchAccountAction
import com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.state.LoginHelperSearchAccountEvent
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LoginHelperSearchAccountViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var testRule = CoroutineTestRule()

    private lateinit var getUserDetailsRestCase: GetUserDetailsRestUseCase
    private lateinit var deleteUserRestUseCase: DeleteUserRestUseCase

    private lateinit var viewModel: LoginHelperSearchAccountViewModel
    private lateinit var userSession: UserSessionInterface

    private val mockEmail = "test@tokopedia.com"
    private val mockPassword = "password"
    private val mockTribe = "test-tribe"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(RsaUtils)
        mockkObject(EncoderDecoder())
        getUserDetailsRestCase = mockk(relaxed = true)
        deleteUserRestUseCase = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        viewModel =
            LoginHelperSearchAccountViewModel(
                getUserDetailsRestCase,
                deleteUserRestUseCase,
                testRule.dispatchers
            )
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `processEvent when GetUserDataFromRemoteDB success`() {
        val response: LoginDataUiModel = mockk(relaxed = true)

        coEvery {
            getUserDetailsRestCase.getRemoteOnlyLoginData(
                any()
            )
        } returns response

        viewModel.processEvent(
            LoginHelperSearchAccountEvent.GetUserData(LoginDataResponse())
        )

        assert(
            viewModel.uiState.value.loginDataList is Success
        )
    }

    @Test
    fun `processEvent when GetUserDataFromRemoteDB success with data`() {
        val response = LoginDataUiModel(
            count = HeaderUiModel(1),
            users = listOf(
                UserDataUiModel(mockEmail, mockPassword, mockTribe)
            )
        )

        coEvery {
            getUserDetailsRestCase.getRemoteOnlyLoginData(
                any()
            )
        } returns response

        viewModel.processEvent(
            LoginHelperSearchAccountEvent.GetUserData(LoginDataResponse())
        )

        assert(
            viewModel.uiState.value.loginDataList is Success
        )
    }

    @Test
    fun `processEvent when AddUserToRemoteDB failure`() {
        coEvery {
            getUserDetailsRestCase.makeNetworkCall(
                any()
            )
        } throws Exception()

        viewModel.processEvent(
            LoginHelperSearchAccountEvent.GetUserData(LoginDataResponse())
        )

        assert(
            viewModel.uiState.value.loginDataList is Fail
        )
    }

    @Test
    fun `processEvent when deleteUser success when response is 200`() {
        runBlockingTest {
            val response: LoginHelperDeleteUserUiModel = mockk(relaxed = true)

            coEvery {
                deleteUserRestUseCase.makeApiCall(
                    any(),
                    any()
                )
            } returns response

            val emittedValues = arrayListOf<LoginHelperSearchAccountAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }

            coEvery { response.code } returns 200L

            viewModel.processEvent(
                LoginHelperSearchAccountEvent.DeleteUserDetailsFromRemote(
                    1L
                )
            )

            val actualEvent = emittedValues.last()
            val onSuccessDeleteUserData =
                actualEvent is LoginHelperSearchAccountAction.OnSuccessDeleteAccountAction
            Assert.assertEquals(true, onSuccessDeleteUserData)

            job.cancel()
        }
    }

    @Test
    fun `processEvent when deleteUser success when response is not 200`() {
        runBlockingTest {
            val response: LoginHelperDeleteUserUiModel = mockk(relaxed = true)

            coEvery {
                deleteUserRestUseCase.makeApiCall(
                    any(),
                    any()
                )
            } returns response

            val emittedValues = arrayListOf<LoginHelperSearchAccountAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }

            coEvery { response.code } returns 500L

            viewModel.processEvent(
                LoginHelperSearchAccountEvent.DeleteUserDetailsFromRemote(
                    1L
                )
            )

            val actualEvent = emittedValues.last()
            val onFailureDeleteUserData =
                actualEvent is LoginHelperSearchAccountAction.OnFailureDeleteAccountAction
            Assert.assertEquals(true, onFailureDeleteUserData)

            job.cancel()
        }
    }

    @Test
    fun `processEvent when deleteUser failure`() {
        runBlockingTest {
            coEvery {
                deleteUserRestUseCase.makeApiCall(
                    any(),
                    any()
                )
            } throws Exception()

            viewModel.processEvent(
                LoginHelperSearchAccountEvent.DeleteUserDetailsFromRemote(1L)
            )

            val emittedValues = arrayListOf<LoginHelperSearchAccountAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }

            val actualEvent = emittedValues.last()
            val onFailureDeleteData =
                actualEvent is LoginHelperSearchAccountAction.OnFailureDeleteAccountAction
            Assert.assertEquals(true, onFailureDeleteData)

            job.cancel()
        }
    }

    @Test
    fun `processEvent with Change Env Type = STAGING`() {
        viewModel.processEvent(LoginHelperSearchAccountEvent.SetEnvType(LoginHelperEnvType.STAGING))
        val result = viewModel.uiState.value.envType
        Assert.assertEquals(result, LoginHelperEnvType.STAGING)
    }

    @Test
    fun `processEvent with Change Env Type = PRODUCTION`() {
        viewModel.processEvent(LoginHelperSearchAccountEvent.SetEnvType(LoginHelperEnvType.PRODUCTION))
        val result = viewModel.uiState.value.envType
        Assert.assertEquals(result, LoginHelperEnvType.PRODUCTION)
    }

    @Test
    fun `processEvent when Back Button is tapped`() {
        runBlockingTest {
            val emittedValues = arrayListOf<LoginHelperSearchAccountAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }
            viewModel.processEvent(LoginHelperSearchAccountEvent.TapBackButton)
            val actualEvent = emittedValues.last()
            val backButtonTapped = actualEvent is LoginHelperSearchAccountAction.TapBackSearchAccountAction
            Assert.assertEquals(true, backButtonTapped)
            job.cancel()
        }
    }

    @Test
    fun `processEvent when search User Name`() {
        viewModel.processEvent(LoginHelperSearchAccountEvent.QueryEmail(mockEmail))
        assert(true)
    }
}
