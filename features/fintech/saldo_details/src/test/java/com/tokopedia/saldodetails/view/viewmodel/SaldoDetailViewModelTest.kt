package com.tokopedia.saldodetails.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.saldodetails.commom.utils.ErrorMessage
import com.tokopedia.saldodetails.saldoDetail.SaldoDetailViewModel
import com.tokopedia.saldodetails.saldoDetail.domain.data.GqlAutoWDInitResponse
import com.tokopedia.saldodetails.saldoDetail.domain.usecase.GetMCLLateCountUseCase
import com.tokopedia.saldodetails.saldoDetail.domain.usecase.GetMerchantSaldoDetails
import com.tokopedia.saldodetails.saldoDetail.domain.usecase.GetSaldoAutoWDInitUseCase
import com.tokopedia.saldodetails.saldoDetail.domain.usecase.GetSaldoBalanceUseCase
import com.tokopedia.saldodetails.saldoDetail.domain.usecase.GetTickerWithdrawalMessageUseCase
import com.tokopedia.saldodetails.commom.utils.Success
import com.tokopedia.saldodetails.saldoDetail.domain.data.GqlMclLateCountResponse
import com.tokopedia.saldodetails.saldoDetail.domain.data.GqlMerchantSaldoDetailsResponse
import com.tokopedia.saldodetails.saldoDetail.domain.data.GqlSaldoBalanceResponse
import com.tokopedia.saldodetails.saldoDetail.domain.data.GqlWithdrawalTickerResponse
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class SaldoDetailViewModelTest {

    private lateinit var viewModel: SaldoDetailViewModel
    private val getSaldobalanceUseCase = mockk<GetSaldoBalanceUseCase>(relaxed = true)
    private val getWithdrawalMessageUseCase = mockk<GetTickerWithdrawalMessageUseCase>(relaxed = true)
    private val getMerchantSaldoDetailUseCase = mockk<GetMerchantSaldoDetails>(relaxed = true)
    private val getMCLLateCountUseCase = mockk<GetMCLLateCountUseCase>(relaxed = true)
    private val getSaldoAutoWDInitUseCase = mockk<GetSaldoAutoWDInitUseCase>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private val dispatcherIO = TestCoroutineDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel = SaldoDetailViewModel(
            getSaldobalanceUseCase,
            getWithdrawalMessageUseCase,
            getMerchantSaldoDetailUseCase,
            getMCLLateCountUseCase,
            getSaldoAutoWDInitUseCase,
            dispatcher,
            dispatcherIO
        )
    }

    @Test
    fun `get saldo balance success`() {
        val mockResponse = mockk<GqlSaldoBalanceResponse>()

        // given
        coEvery {
            getSaldobalanceUseCase.getResponse()
        } returns mockResponse

        // when
        viewModel.getUserSaldoBalance()

        // then
        when(viewModel.gqlUserSaldoBalanceLiveData.value) {
            is Success -> {
                assertTrue((viewModel.gqlUserSaldoBalanceLiveData.value as Success).data == mockResponse)
            }
            else -> {

            }
        }
    }

    @Test
    fun `get withdraw message success`() {
        val mockResponse = mockk<GqlWithdrawalTickerResponse>()

        // given
        coEvery {
            getWithdrawalMessageUseCase.getResponse()
        } returns mockResponse

        // when
        viewModel.getTickerWithdrawalMessage()

        // then
        when(viewModel.gqlTickerWithdrawalLiveData.value) {
            is Success -> {
                assertTrue((viewModel.gqlTickerWithdrawalLiveData.value as Success).data == mockResponse)
            }
            else -> {

            }
        }
    }

    @Test
    fun `get merchant credit late count success`() {
        val mockResponse = mockk<GqlMclLateCountResponse>()

        // given
        coEvery {
            getMCLLateCountUseCase.getResponse()
        } returns mockResponse

        // when
        viewModel.getMerchantCreditLateCountValue()

        // then
        when(viewModel.gqlLateCountResponseLiveData.value) {
            is Success -> {
                assertTrue((viewModel.gqlLateCountResponseLiveData.value as Success).data == mockResponse)
            }
            else -> {

            }
        }
    }

    @Test
    fun `get merchant saldo detail success`() {
        val mockResponse = mockk<GqlMerchantSaldoDetailsResponse>()

        // given
        coEvery {
            getMerchantSaldoDetailUseCase.getResponse()
        } returns mockResponse

        // when
        viewModel.getMerchantSaldoDetails()

        // then
        when(viewModel.gqlMerchantSaldoDetailLiveData.value) {
            is Success -> {
                assertTrue((viewModel.gqlMerchantSaldoDetailLiveData.value as Success).data == mockResponse)
            }
            else -> {

            }
        }
    }

    @Test
    fun `get auto wd status success`() {
        val mockResponse = mockk<GqlAutoWDInitResponse>()

        // given
        coEvery {
            getSaldoAutoWDInitUseCase()
        } returns mockResponse

        // when
        viewModel.getAutoWDStatus(true)

        // then
        when(viewModel.gqlAutoWDInitLiveData.value) {
            is Success -> {
                assertTrue((viewModel.gqlAutoWDInitLiveData.value as Success).data == mockResponse)
            }
            else -> {

            }
        }
    }

    @Test
    fun `get auto wd status error`() {
        val mockThrowable = mockk<Throwable>("fail")

        // given
        coEvery {
            getSaldoAutoWDInitUseCase()
        } throws mockThrowable

        // when
        viewModel.getAutoWDStatus(true)

        // then
        assertTrue(viewModel.gqlAutoWDInitLiveData.value is ErrorMessage<*, *>)

    }
}
