package com.tokopedia.flight.orderdetail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailUseCase
import com.tokopedia.flight.shouldBe
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 22/10/2020
 */
class FlightOrderDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val useCase: FlightOrderDetailUseCase = mockk()
    private val testDispatcherProvider = TravelTestDispatcherProvider()

    private lateinit var viewModel: FlightOrderDetailViewModel

    @Before
    fun setUp() {
        viewModel = FlightOrderDetailViewModel(useCase, testDispatcherProvider)
    }

    @Test
    fun fetchOrderDetailData_errorFetching_shouldReturnFail() {
        // given
        coEvery { useCase.execute(any(), any()) } coAnswers { throw MessageErrorException("Error Fetch Data") }

        // when
        viewModel.fetchOrderDetailData("1234567890")

        // then
        assert(viewModel.orderDetailData.value is Fail)
        (viewModel.orderDetailData.value as Fail).throwable.message shouldBe "Error Fetch Data"
    }
}