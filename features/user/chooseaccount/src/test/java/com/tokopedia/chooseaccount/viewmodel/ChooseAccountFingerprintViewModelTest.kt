package com.tokopedia.chooseaccount.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.chooseaccount.data.AccountListDataModel
import com.tokopedia.chooseaccount.data.AccountsDataModel
import com.tokopedia.chooseaccount.data.ErrorResponseDataModel
import com.tokopedia.chooseaccount.domain.usecase.GetAccountListUseCase
import com.tokopedia.chooseaccount.viewmodel.ChooseAccountFingerprintViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
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

    private var getAccountListObserver = mockk<Observer<Result<AccountListDataModel>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = ChooseAccountFingerprintViewModel(
            getAccountListUseCase,
            CoroutineTestDispatchersProvider
        )
        viewModel.getAccountListDataModelResponse.observeForever(getAccountListObserver)
    }

    @Test
    fun `on Success Get Account List`() {
        val resp = AccountsDataModel()

        every { getAccountListUseCase.getAccounts(any(), any(), any(), any()) } answers {
            arg<(AccountsDataModel) -> Unit>(2).invoke(resp)
        }

        viewModel.getAccountListFingerprint("abc123")

        verify { getAccountListObserver.onChanged(Success(resp.accountListDataModel)) }
    }

    @Test
    fun `on Success Get Account List has errors`() {
        val msg = "error message"
        val accountList = AccountListDataModel(errorResponseDataModels = listOf(ErrorResponseDataModel("error", message = msg)))
        val resp = AccountsDataModel(accountListDataModel = accountList)

        every { getAccountListUseCase.getAccounts(any(), any(), any(), any()) } answers {
            arg<(AccountsDataModel) -> Unit>(2).invoke(resp)
        }

        viewModel.getAccountListFingerprint("abc123")

        /* Then */
        MatcherAssert.assertThat(viewModel.getAccountListDataModelResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertEquals(
            (viewModel.getAccountListDataModelResponse.value as Fail).throwable.message,
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