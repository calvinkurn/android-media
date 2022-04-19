package com.tokopedia.saldodetails.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.saldodetails.transactionDetailPages.withdrawal.GetWithdrawalInfoUseCase
import com.tokopedia.saldodetails.transactionDetailPages.withdrawal.WithdrawalDetailViewModel
import com.tokopedia.saldodetails.transactionDetailPages.withdrawal.WithdrawalInfoData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WithdrawalDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getWithdrawalInfoUseCase = mockk<GetWithdrawalInfoUseCase>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: WithdrawalDetailViewModel
    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setup() {
        viewModel = WithdrawalDetailViewModel(getWithdrawalInfoUseCase, dispatcher)
    }

    @Test
    fun `Execute getWithdrawalInfo (Gql Error)`() {
        coEvery { getWithdrawalInfoUseCase.getWithdrawalInfo(any(), any(), any()) } coAnswers {
        secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getWithdrawalInfo("1234")
        assert(viewModel.withdrawalInfoLiveData.value is Fail)
        Assertions.assertThat((viewModel.withdrawalInfoLiveData.value as Fail).throwable.message)
            .isEqualTo(fetchFailedErrorMessage)
    }

    @Test
    fun `Execute getWithdrawalInfo Success`() {
        val withdrawalId = "1234"
        val data = WithdrawalInfoData(withdrawalId, 1, "", 1, 1.0, 1.0, 1.0, "", "", "", "", arrayListOf(), arrayListOf())
        coEvery { getWithdrawalInfoUseCase.getWithdrawalInfo(any(), any(), any()) } coAnswers {
            firstArg<(WithdrawalInfoData) -> Unit>().invoke(data)
        }
        viewModel.getWithdrawalInfo(withdrawalId)
        assert(viewModel.withdrawalInfoLiveData.value is Success)
        Assertions.assertThat((viewModel.withdrawalInfoLiveData.value as Success).data.withdrawalId)
            .isEqualTo(withdrawalId)
    }

}