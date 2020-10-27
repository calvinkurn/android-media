package com.tokopedia.updateinactivephone.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.updateinactivephone.revamp.domain.data.AccountList
import com.tokopedia.updateinactivephone.revamp.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.UserDetailDataModel
import com.tokopedia.updateinactivephone.revamp.domain.usecase.GetAccountListUseCase
import com.tokopedia.updateinactivephone.revamp.view.viewmodel.InactivePhoneAccountListViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InactivePhoneAccountListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val dispatcher = TestCoroutineDispatcher()
    val getAccountListUseCase = mockk<GetAccountListUseCase>(relaxed = true)
    private var observer = mockk<Observer<Result<AccountListDataModel>>>()
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
            observer.onChanged(Fail(mockThrowable))
        }
    }

    @Test
    fun `get account list - empty list`() {
        val phonenumber = "62800000000000"
        val mockThrowable = Throwable(InactivePhoneAccountListViewModel.ERROR_ACCOUNT_LIST_EMPTY)

        every {
            getAccountListUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewmodel.getAccountList(phonenumber)

        verify {
            observer.onChanged(Fail(mockThrowable))
            assertEquals((viewmodel.accountList.value as Fail).throwable, mockThrowable)
        }
    }

    @Test
    fun `get account list - success`() {
        val phonenumber = "62800000000000"
        val mockResponse = AccountListDataModel(AccountList(userDetailDataModels = mutableListOf(
                UserDetailDataModel(userId = "1"),
                UserDetailDataModel(userId = "2")
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
    }
}
