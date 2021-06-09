package com.tokopedia.loginfingerprint.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.loginphone.chooseaccount.data.AccountList
import com.tokopedia.loginphone.chooseaccount.data.AccountListPojo
import com.tokopedia.loginphone.chooseaccount.data.Error
import com.tokopedia.loginphone.chooseaccount.domain.usecase.GetAccountListUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.domain.usecase.GetAdminTypeUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChooseAccountFingerprintViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: ChooseAccountFingerprintViewModel

    val loginFingerprintUseCase = mockk<LoginFingerprintUseCase>(relaxed = true)
    val getAccountListUseCase = mockk<GetAccountListUseCase>(relaxed = true)
    val getProfileUseCase = mockk<GetProfileUseCase>(relaxed = true)
    val getAdminTypeUseCase = mockk<GetAdminTypeUseCase>(relaxed = true)

    val userSession = mockk<UserSessionInterface>(relaxed = true)

    private var accountListObserver = mockk<Observer<Result<AccountList>>>(relaxed = true)
    private var loginBiometricObserver = mockk<Observer<Result<LoginToken>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = ChooseAccountFingerprintViewModel(
            loginFingerprintUseCase,
            getAccountListUseCase,
            userSession,
            getProfileUseCase,
            getAdminTypeUseCase,
            CoroutineTestDispatchersProvider
        )

        viewModel.getAccountListResponse.observeForever(accountListObserver)
        viewModel.loginBiometricResponse.observeForever(loginBiometricObserver)
    }

    private val throwable = Throwable("Error")

    @Test
    fun `on Success Get Accounts`() {
        /* When */
        val responseData = AccountList()
        val response = AccountListPojo(accountList = responseData)

        every { getAccountListUseCase.getAccounts(any(), any(), any(), any()) } answers {
            arg<(AccountListPojo) -> Unit>(2).invoke(response)
        }

        viewModel.getAccountListFingerprint("123")

        /* Then */
        verify { accountListObserver.onChanged(Success(responseData)) }
    }

    @Test
    fun `on Error Get Accounts has errors`() {
        val responseData = AccountList(errors = listOf(Error("error", "error")))
        val response = AccountListPojo(accountList = responseData)

        every { getAccountListUseCase.getAccounts(any(), any(), any(), any()) } answers  {
            arg<(AccountListPojo) -> Unit>(2).invoke(response)
        }

        viewModel.getAccountListFingerprint("123")

        /* Then */
        MatcherAssert.assertThat(viewModel.getAccountListResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertEquals(
            (viewModel.getAccountListResponse.value as Fail).throwable.message,
            responseData.errors[0].message
        )
        MatcherAssert.assertThat((viewModel.getAccountListResponse.value as Fail).throwable, CoreMatchers.instanceOf(
            MessageErrorException::class.java))
    }

    @Test
    fun `on Error Get Accounts`() {
        every { getAccountListUseCase.getAccounts(any(), any(), any(), any()) } answers {
            arg<(Throwable) -> Unit>(3).invoke(throwable)
        }

        viewModel.getAccountListFingerprint("123")

        /* Then */
        verify {
            accountListObserver.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `on Success Login Biometrics`() {
        /* When */
        val responseToken = LoginToken(accessToken = "abc", refreshToken = "bbb", sqCheck = false, tokenType = "123")

        every { loginFingerprintUseCase.loginBiometric(any(), any(), any(), any(), any(), any(), any()) } answers {
            thirdArg<(LoginToken) -> Unit>().invoke(responseToken)
        }

        viewModel.loginTokenBiometric("test", "123")

        /* Then */
        verify { loginBiometricObserver.onChanged(Success(responseToken)) }
    }

    @Test
    fun `on Error Login Biometrics`() {

        every { loginFingerprintUseCase.loginBiometric(any(), any(), any(), any(), any(), any(), any()) } answers {
            arg<(Throwable) -> Unit>(3).invoke(throwable)
        }

        viewModel.loginTokenBiometric("test", "123")

        /* Then */
        verify {
            userSession.clearToken()
        }
        MatcherAssert.assertThat(viewModel.loginBiometricResponse.value, CoreMatchers.instanceOf(Fail::class.java))
    }

}