package com.tokopedia.updateinactivephone.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.domain.usecase.GetAccountListUseCase
import com.tokopedia.updateinactivephone.features.accountlist.InactivePhoneAccountListViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InactivePhoneAccountListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider
    val getAccountListUseCase = mockk<GetAccountListUseCase>(relaxed = true)
    private var observer = mockk<Observer<Result<AccountListDataModel>>>(relaxed = true)
    lateinit var viewmodel: InactivePhoneAccountListViewModel

    @Before
    fun setup() {
        viewmodel = InactivePhoneAccountListViewModel(getAccountListUseCase, dispatcherProviderTest)
        viewmodel.accountList.observeForever(observer)
    }

    @Test
    fun `get account list - fail empty list`() {
        val phonenumber = "62800000000000"
        val mockResponse = AccountListDataModel(AccountListDataModel.AccountList())

        coEvery {
            getAccountListUseCase(any())
        } returns mockResponse

        viewmodel.getAccountList(phonenumber)

        verify {
            observer.onChanged(Success(mockResponse))
        }

        assert(viewmodel.accountList.value is Success)

        val result = viewmodel.accountList.value as Success
        assert(result.data.accountList.userDetailDataModels.isEmpty())
    }

    @Test
    fun `get account list - success`() {
        val phonenumber = "62800000000000"
        val mockResponse = AccountListDataModel(AccountListDataModel.AccountList(userDetailDataModels = mutableListOf(
            AccountListDataModel.UserDetailDataModel(index = 1),
            AccountListDataModel.UserDetailDataModel(index = 2)
        )))

        coEvery {
            getAccountListUseCase(any())
        } returns mockResponse

        viewmodel.getAccountList(phonenumber)

        verify {
            observer.onChanged(Success(mockResponse))
        }

        assert(viewmodel.accountList.value is Success)

        val result = viewmodel.accountList.value as Success
        assert(result.data.accountList.userDetailDataModels.isNotEmpty())
    }

    @Test
    fun `get account list - fail`() {
        val phonenumber = "62800000000000"
        val throwable = Throwable("Opps!")

        coEvery {
            getAccountListUseCase(any())
        }.throws(throwable)

        viewmodel.getAccountList(phonenumber)

        verify {
            observer.onChanged(Fail(throwable))
        }

        assert(viewmodel.accountList.value is Fail)
    }

    @After
    fun tearDown() {
        viewmodel.accountList.removeObserver(observer)
    }
}
