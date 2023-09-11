package com.tokopedia.loginHelper

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.encryption.security.AESEncryptorCBC
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.uiModel.addedit.LoginHelperAddUserUiModel
import com.tokopedia.loginHelper.domain.usecase.AddUserRestUseCase
import com.tokopedia.loginHelper.domain.usecase.EditUserRestUseCase
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.LoginHelperAddEditAccountViewModel
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state.LoginHelperAddEditAccountAction
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state.LoginHelperAddEditAccountEvent
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.unit.test.rule.CoroutineTestRule
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
class LoginHelperAddEditAccountViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var testRule = CoroutineTestRule()

    private lateinit var addUserRestUseCase: AddUserRestUseCase
    private lateinit var editUserRestUseCase: EditUserRestUseCase
    private lateinit var aesEncryptorCBC: AESEncryptorCBC
    private lateinit var gson: Gson

    private lateinit var viewModel: LoginHelperAddEditAccountViewModel

    private val mockEmail = "test@tokopedia.com"
    private val mockPassword = "password"
    private val mockTribe = "test-tribe"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(RsaUtils)
        mockkObject(EncoderDecoder())
        addUserRestUseCase = mockk(relaxed = true)
        editUserRestUseCase = mockk(relaxed = true)
        aesEncryptorCBC = mockk(relaxed = true)
        gson = mockk(relaxed = true)
        viewModel =
            LoginHelperAddEditAccountViewModel(
                addUserRestUseCase,
                editUserRestUseCase,
                aesEncryptorCBC,
                gson,
                testRule.dispatchers
            )
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `processEvent with Change Env Type = STAGING`() {
        viewModel.processEvent(
            LoginHelperAddEditAccountEvent.ChangeEnvType(LoginHelperEnvType.STAGING)
        )
        val result = viewModel.uiState.value.envType
        Assert.assertEquals(result, LoginHelperEnvType.STAGING)
    }

    @Test
    fun `processEvent with Change Env Type = PRODUCTION`() {
        viewModel.processEvent(
            LoginHelperAddEditAccountEvent.ChangeEnvType(LoginHelperEnvType.PRODUCTION)
        )
        val result = viewModel.uiState.value.envType
        Assert.assertEquals(result, LoginHelperEnvType.PRODUCTION)
    }

    @Test
    fun `processEvent when Back Button is tapped`() {
        runBlockingTest {
            val emittedValues = arrayListOf<LoginHelperAddEditAccountAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }
            viewModel.processEvent(LoginHelperAddEditAccountEvent.TapBackButton)
            val actualEvent = emittedValues.last()
            val backButtonTapped = actualEvent is LoginHelperAddEditAccountAction.TapBackAction
            Assert.assertEquals(true, backButtonTapped)
            job.cancel()
        }
    }

    @Test
    fun `processEvent when AddUserToRemoteDB success when response is 201`() {
        runBlockingTest {
            val response: LoginHelperAddUserUiModel = mockk(relaxed = true)

            coEvery {
                addUserRestUseCase.makeApiCall(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns response

            val emittedValues = arrayListOf<LoginHelperAddEditAccountAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }

            coEvery { response.code } returns 201L

            viewModel.processEvent(
                LoginHelperAddEditAccountEvent.AddUserToRemoteDB(
                    mockEmail,
                    mockPassword,
                    mockTribe
                )
            )

            val actualEvent = emittedValues.last()
            val onSuccessAddDataToRest =
                actualEvent is LoginHelperAddEditAccountAction.OnSuccessAddDataToRest
            Assert.assertEquals(true, onSuccessAddDataToRest)

            job.cancel()
        }
    }

    @Test
    fun `processEvent when AddUserToRemoteDB success when response is not 201`() {
        runBlockingTest {
            val response: LoginHelperAddUserUiModel = mockk(relaxed = true)

            coEvery {
                addUserRestUseCase.makeApiCall(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns response

            val emittedValues = arrayListOf<LoginHelperAddEditAccountAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }

            coEvery { response.code } returns 200L

            viewModel.processEvent(
                LoginHelperAddEditAccountEvent.AddUserToRemoteDB(
                    mockEmail,
                    mockPassword,
                    mockTribe
                )
            )

            val actualEvent = emittedValues.last()
            val onFailureAddDataToRest =
                actualEvent is LoginHelperAddEditAccountAction.OnFailureAddDataToRest
            Assert.assertEquals(true, onFailureAddDataToRest)

            job.cancel()
        }
    }

    @Test
    fun `processEvent when AddUserToRemoteDB failure`() {
        runBlockingTest {
            coEvery {
                addUserRestUseCase.makeApiCall(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } throws Exception()

            val emittedValues = arrayListOf<LoginHelperAddEditAccountAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }

            viewModel.processEvent(
                LoginHelperAddEditAccountEvent.AddUserToRemoteDB(
                    mockEmail,
                    mockPassword,
                    mockTribe
                )
            )

            val actualEvent = emittedValues.last()
            val onFailureAddDataToRest =
                actualEvent is LoginHelperAddEditAccountAction.OnFailureAddDataToRest
            Assert.assertEquals(true, onFailureAddDataToRest)

            job.cancel()
        }
    }

    @Test
    fun `processEvent when editUserFromRemote success when response is 200`() {
        runBlockingTest {
            val response: LoginHelperAddUserUiModel = mockk(relaxed = true)

            coEvery {
                editUserRestUseCase.makeApiCall(
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns response

            val emittedValues = arrayListOf<LoginHelperAddEditAccountAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }

            coEvery { response.code } returns 200L

            viewModel.processEvent(
                LoginHelperAddEditAccountEvent.EditUserDetailsFromRemote(
                    mockEmail,
                    mockPassword,
                    mockTribe
                )
            )

            val actualEvent = emittedValues.last()
            val onSuccessEditUserData =
                actualEvent is LoginHelperAddEditAccountAction.OnSuccessEditUserData
            Assert.assertEquals(true, onSuccessEditUserData)

            job.cancel()
        }
    }

    @Test
    fun `processEvent when editUserFromRemote success when response is not 200`() {
        runBlockingTest {
            val response: LoginHelperAddUserUiModel = mockk(relaxed = true)

            coEvery {
                editUserRestUseCase.makeApiCall(
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns response

            val emittedValues = arrayListOf<LoginHelperAddEditAccountAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }

            coEvery { response.code } returns 201L

            viewModel.processEvent(
                LoginHelperAddEditAccountEvent.EditUserDetailsFromRemote(
                    mockEmail,
                    mockPassword,
                    mockTribe
                )
            )

            val actualEvent = emittedValues.last()
            val onFailureEditDataToRest =
                actualEvent is LoginHelperAddEditAccountAction.OnFailureEditUserData
            Assert.assertEquals(true, onFailureEditDataToRest)

            job.cancel()
        }
    }

    @Test
    fun `processEvent when editUserFromRemote failure`() {
        runBlockingTest {
            coEvery {
                editUserRestUseCase.makeApiCall(
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } throws Exception()

            val emittedValues = arrayListOf<LoginHelperAddEditAccountAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }

            viewModel.processEvent(
                LoginHelperAddEditAccountEvent.EditUserDetailsFromRemote(
                    mockEmail,
                    mockPassword,
                    mockTribe
                )
            )

            val actualEvent = emittedValues.last()
            val onFailureEditData =
                actualEvent is LoginHelperAddEditAccountAction.OnFailureEditUserData
            Assert.assertEquals(true, onFailureEditData)

            job.cancel()
        }
    }
}
