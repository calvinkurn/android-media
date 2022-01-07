package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.model.response.AffiliateWithdrawalDetailData
import com.tokopedia.affiliate.model.response.WithdrawalInfoData
import com.tokopedia.affiliate.model.response.WithdrawalInfoResult
import com.tokopedia.affiliate.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateWithdrawalDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getWithdrawalInfoUseCase = mockk<AffiliateWithdrawalDetailsUseCase>(relaxed = true)
    private lateinit var viewModel: WithdrawalDetailViewModel
    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setup() {
        viewModel = WithdrawalDetailViewModel(getWithdrawalInfoUseCase)
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
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
        val data = WithdrawalInfoData(1, null, withdrawalId, null, null, null, null, null,
                "", null, "", null, null,null, arrayListOf(), arrayListOf())
        coEvery { getWithdrawalInfoUseCase.getWithdrawalInfo(any(), any(), any()) } coAnswers {
            firstArg<(WithdrawalInfoData) -> Unit>().invoke(data)
        }
        viewModel.getWithdrawalInfo(withdrawalId)
        assert(viewModel.withdrawalInfoLiveData.value is Success)
        Assertions.assertThat((viewModel.withdrawalInfoLiveData.value as Success).data.withdrawalId)
                .isEqualTo(withdrawalId)
    }

}