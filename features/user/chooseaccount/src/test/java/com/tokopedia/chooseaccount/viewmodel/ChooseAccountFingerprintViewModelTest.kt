package com.tokopedia.chooseaccount.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.chooseaccount.data.AccountListDataModel
import com.tokopedia.chooseaccount.data.AccountsDataModel
import com.tokopedia.chooseaccount.data.ErrorResponseDataModel
import com.tokopedia.chooseaccount.domain.usecase.GetFingerprintAccountListUseCase
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.RuntimeException

class ChooseAccountFingerprintViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: ChooseAccountFingerprintViewModel

    val getAccountListUseCase = mockk<GetFingerprintAccountListUseCase>(relaxed = true)
    val fingerprintPreferenceManager = mockk<FingerprintPreference>(relaxed = true)

    private var getAccountListObserver =
        mockk<Observer<Result<AccountListDataModel>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = ChooseAccountFingerprintViewModel(
            getAccountListUseCase,
            fingerprintPreferenceManager,
            CoroutineTestDispatchersProvider
        )
        viewModel.getAccountListDataModelResponse.observeForever(getAccountListObserver)
    }

    @Test
    fun `on Success Get Account List`() {
        val resp = AccountsDataModel()

        coEvery { getAccountListUseCase.invoke(any()) } returns resp

        viewModel.getAccountListFingerprint("abc123")

        verify { getAccountListObserver.onChanged(Success(resp.accountListDataModel)) }
    }

    @Test
    fun `on Success Get Account List has errors`() {
        val msg = "error message"
        val accountList = AccountListDataModel(
            errorResponseDataModels = listOf(
                ErrorResponseDataModel(
                    "error",
                    message = msg
                )
            )
        )
        val resp = AccountsDataModel(accountListDataModel = accountList)

        coEvery { getAccountListUseCase.invoke(any()) } returns resp

        viewModel.getAccountListFingerprint("abc123")

        /* Then */
        assertThat(
            viewModel.getAccountListDataModelResponse.value,
            instanceOf(Fail::class.java)
        )
        Assert.assertEquals(
            (viewModel.getAccountListDataModelResponse.value as Fail).throwable.message,
            msg
        )
    }

    @Test
    fun `on Success Get Account List has errors else condition`() {
        val accountList =
            AccountListDataModel(errorResponseDataModels = listOf(ErrorResponseDataModel()))
        val resp = AccountsDataModel(accountListDataModel = accountList)

        coEvery { getAccountListUseCase.invoke(any()) } returns resp

        viewModel.getAccountListFingerprint("abc123")

        /* Then */
        assertThat(
            viewModel.getAccountListDataModelResponse.getOrAwaitValue(),
            instanceOf(Fail::class.java)
        )
        assertThat(
            (viewModel.getAccountListDataModelResponse.getOrAwaitValue() as Fail).throwable,
            instanceOf(RuntimeException::class.java)
        )
    }

    @Test
    fun `on Error Get Account List`() {
        val error = Throwable()

        coEvery { getAccountListUseCase.invoke(any()) } throws error

        viewModel.getAccountListFingerprint("abc123")

        verify { getAccountListObserver.onChanged(Fail(error)) }
    }

}