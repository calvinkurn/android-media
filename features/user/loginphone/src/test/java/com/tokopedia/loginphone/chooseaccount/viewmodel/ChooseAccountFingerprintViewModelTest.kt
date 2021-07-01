package com.tokopedia.loginphone.chooseaccount.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.loginphone.chooseaccount.data.AccountList
import com.tokopedia.loginphone.chooseaccount.data.AccountListPojo
import com.tokopedia.loginphone.chooseaccount.data.Error
import com.tokopedia.loginphone.chooseaccount.domain.usecase.GetAccountListUseCase
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

    val getAccountListUseCase = mockk<GetAccountListUseCase>(relaxed = true)
    val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    val getProfileUseCase = mockk<GetProfileUseCase>(relaxed = true)
    val getAdminTypeUseCase = mockk<GetAdminTypeUseCase>(relaxed = true)

    private var getAccountListObserver = mockk<Observer<Result<AccountList>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = ChooseAccountFingerprintViewModel(
            getAccountListUseCase,
            userSessionInterface,
            getProfileUseCase,
            getAdminTypeUseCase,
            CoroutineTestDispatchersProvider
        )
        viewModel.getAccountListResponse.observeForever(getAccountListObserver)
    }

    @Test
    fun `on Success Get Account List`() {
        val resp = AccountListPojo()

        every { getAccountListUseCase.getAccounts(any(), any(), any(), any()) } answers {
            arg<(AccountListPojo) -> Unit>(2).invoke(resp)
        }

        viewModel.getAccountListFingerprint("abc123")

        verify { getAccountListObserver.onChanged(Success(resp.accountList)) }
    }

    @Test
    fun `on Success Get Account List has errors`() {
        val msg = "error message"
        val accountList = AccountList(errors = listOf(Error("error", message = msg)))
        val resp = AccountListPojo(accountList = accountList)

        every { getAccountListUseCase.getAccounts(any(), any(), any(), any()) } answers {
            arg<(AccountListPojo) -> Unit>(2).invoke(resp)
        }

        viewModel.getAccountListFingerprint("abc123")

        /* Then */
        MatcherAssert.assertThat(viewModel.getAccountListResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertEquals(
            (viewModel.getAccountListResponse.value as Fail).throwable.message,
            msg
        )
    }

    @Test
    fun `on Error Get Account List`() {
        val error = Throwable()

        every { getAccountListUseCase.getAccounts(any(), any(), any(), any()) } answers {
            arg<(Throwable) -> Unit>(3).invoke(error)
        }

        viewModel.getAccountListFingerprint("abc123")

        verify { getAccountListObserver.onChanged(Fail(error)) }
    }

}