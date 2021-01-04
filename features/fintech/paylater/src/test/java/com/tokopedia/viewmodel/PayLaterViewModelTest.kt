package com.tokopedia.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.paylater.domain.model.PayLaterActivityResponse
import com.tokopedia.paylater.domain.model.PayLaterProductData
import com.tokopedia.paylater.domain.usecase.PayLaterApplicationStatusUseCase
import com.tokopedia.paylater.domain.usecase.PayLaterProductDetailUseCase
import com.tokopedia.paylater.domain.usecase.PayLaterSimulationUseCase
import com.tokopedia.paylater.helper.PayLaterException
import com.tokopedia.paylater.presentation.viewModel.PayLaterViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PayLaterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val payLaterProductDetailUseCase = mockk<PayLaterProductDetailUseCase>(relaxed = true)
    val payLaterApplicationStatusUseCase = mockk<PayLaterApplicationStatusUseCase>(relaxed = true)
    val payLaterSimulationDataUseCase = mockk<PayLaterSimulationUseCase>(relaxed = true)

    val dispatcher = TestCoroutineDispatcher()
    lateinit var viewModel: PayLaterViewModel

    val throwable = Fail(Throwable(message = "Error"))
    val payLaterDataEmpty = Fail(PayLaterException.PayLaterNullDataException(PayLaterViewModel.SIMULATION_DATA_FAILURE))
    private val payLaterActivityResponseResult = PayLaterActivityResponse()
    private var payLaterActivityObserver = mockk<Observer<Result<PayLaterProductData>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = PayLaterViewModel(
                payLaterProductDetailUseCase,
                payLaterApplicationStatusUseCase,
                payLaterSimulationDataUseCase,
                dispatcher,
                dispatcher
        )
        viewModel.payLaterActivityResultLiveData.observeForever(payLaterActivityObserver)
    }

    @Test
    fun `Execute getPayLaterProductData Fail`() {
        coEvery { payLaterProductDetailUseCase.executeOnBackground() } returns payLaterActivityResponseResult
        viewModel.getPayLaterProductData()

        Assertions.assertThat(viewModel.payLaterActivityResultLiveData.value).isEqualTo(Success(payLaterActivityResponseResult))
    }



}