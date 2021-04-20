package com.tokopedia.updateinactivephone.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.domain.usecase.GetAccountListUseCase
import com.tokopedia.updateinactivephone.view.viewmodel.InactivePhoneAccountListViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InactivePhoneAccountListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val dispatcher = TestCoroutineDispatcher()
    val getAccountListUseCase = mockk<GetAccountListUseCase>(relaxed = true)
    private var observer = mockk<Observer<Result<AccountListDataModel>>>(relaxed = true)
    lateinit var viewmodel : InactivePhoneAccountListViewModel

    @Before
    fun setup() {
        viewmodel = InactivePhoneAccountListViewModel(getAccountListUseCase, dispatcher)
        viewmodel.accountList.observeForever(observer)
    }

    @Test
    fun `get account list - fail`() {
        val phonenumber = "62800000000000"
        val mockThrowable = Throwable("Opps!")

        every {
            getAccountListUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewmodel.getAccountList(phonenumber)

        verify {
            observer.onChanged(any())
        }

        assert(viewmodel.accountList.value is Fail)
    }

    @Test
    fun `get account list - fail empty list`() {
        val phonenumber = "62800000000000"
        val mockResponse = AccountListDataModel(AccountListDataModel.AccountList())
        val mockThrowable = Throwable(InactivePhoneAccountListViewModel.ERROR_ACCOUNT_LIST_EMPTY)

        every {
            getAccountListUseCase.execute(any(), any())
        } answers {
            firstArg<(AccountListDataModel) -> Unit>().invoke(mockResponse)
        }

        viewmodel.getAccountList(phonenumber)

        verify {
            observer.onChanged(any())
        }

        assert(viewmodel.accountList.value is Fail)

        val result = viewmodel.accountList.value as Fail
        assertEquals(result.throwable.message, mockThrowable.message)
    }

    @Test
    fun `get account list - success`() {
        val phonenumber = "62800000000000"
        val mockResponse = AccountListDataModel(AccountListDataModel.AccountList(userDetailDataModels = mutableListOf(
                AccountListDataModel.UserDetailDataModel(index = 1),
                AccountListDataModel.UserDetailDataModel(index = 2)
        )))

        every {
            getAccountListUseCase.execute(any(), any())
        } answers {
            firstArg<(AccountListDataModel) -> Unit>().invoke(mockResponse)
        }

        viewmodel.getAccountList(phonenumber)

        verify {
            observer.onChanged(Success(mockResponse))
        }

        assert(viewmodel.accountList.value is Success)

        val result = viewmodel.accountList.value as Success
        assert(result.data.accountList.userDetailDataModels.isNotEmpty())
    }

    @After
    fun tearDown() {
        viewmodel.accountList.removeObserver(observer)
        viewmodel.onCleared()
    }
}
