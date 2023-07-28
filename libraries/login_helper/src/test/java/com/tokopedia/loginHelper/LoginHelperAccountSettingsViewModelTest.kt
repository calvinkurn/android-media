package com.tokopedia.loginHelper

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.loginHelper.presentation.accountSettings.viewmodel.LoginHelperAccountSettingsViewModel
import com.tokopedia.loginHelper.presentation.accountSettings.viewmodel.state.LoginHelperAccountSettingsAction
import com.tokopedia.loginHelper.presentation.accountSettings.viewmodel.state.LoginHelperAccountSettingsEvent
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
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
class LoginHelperAccountSettingsViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var testRule = CoroutineTestRule()

    private lateinit var viewModel: LoginHelperAccountSettingsViewModel

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(RsaUtils)
        mockkObject(EncoderDecoder())
        viewModel =
            LoginHelperAccountSettingsViewModel(
                testRule.dispatchers
            )
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `processEvent when need to go to Login Helper Home Screen`() {
        runBlockingTest {
            val emittedValues = arrayListOf<LoginHelperAccountSettingsAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }
            viewModel.processEvent(LoginHelperAccountSettingsEvent.GoToLoginHelperHome)
            val actualEvent = emittedValues.last()
            val goToLoginHelperHomePage = actualEvent is LoginHelperAccountSettingsAction.RouteToPage
            Assert.assertEquals(true, goToLoginHelperHomePage)
            job.cancel()
        }
    }

    @Test
    fun `processEvent when go to Add Account`() {
        runBlockingTest {
            val emittedValues = arrayListOf<LoginHelperAccountSettingsAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }
            viewModel.processEvent(LoginHelperAccountSettingsEvent.GoToAddAccount)
            val actualEvent = emittedValues.last()
            val goToAddAccount = actualEvent is LoginHelperAccountSettingsAction.RouteToPage
            Assert.assertEquals(true, goToAddAccount)
            job.cancel()
        }
    }

    @Test
    fun `processEvent when go to Edit Account`() {
        runBlockingTest {
            val emittedValues = arrayListOf<LoginHelperAccountSettingsAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }
            viewModel.processEvent(LoginHelperAccountSettingsEvent.GoToEditAccount)
            val actualEvent = emittedValues.last()
            val goToEditPage = actualEvent is LoginHelperAccountSettingsAction.RouteToPage
            Assert.assertEquals(true, goToEditPage)
            job.cancel()
        }
    }

    @Test
    fun `processEvent when go to delete account `() {
        runBlockingTest {
            val emittedValues = arrayListOf<LoginHelperAccountSettingsAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedValues)
            }
            viewModel.processEvent(LoginHelperAccountSettingsEvent.GoToDeleteAccount)
            val actualEvent = emittedValues.last()
            val goToDeleteAccount = actualEvent is LoginHelperAccountSettingsAction.RouteToPage
            Assert.assertEquals(true, goToDeleteAccount)
            job.cancel()
        }
    }
}
