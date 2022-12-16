package com.tokopedia.logisticseller.ui.findingnewdriver

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.logisticseller.data.model.FindingNewDriverModel
import com.tokopedia.logisticseller.data.response.NewDriverAvailabilityResponse
import com.tokopedia.logisticseller.data.response.NewDriverBookingResponse
import com.tokopedia.logisticseller.domain.mapper.FindingNewDriverMapper
import com.tokopedia.logisticseller.domain.usecase.NewDriverAvailabilityUseCase
import com.tokopedia.logisticseller.domain.usecase.NewDriverBookingUseCase
import com.tokopedia.logisticseller.ui.findingnewdriver.uimodel.NewDriverAvailabilityState
import com.tokopedia.logisticseller.ui.findingnewdriver.uimodel.NewDriverBookingState
import com.tokopedia.logisticseller.ui.findingnewdriver.viewmodel.FindingNewDriverViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FindingNewDriverViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val newDriverAvailabilityUseCase = mockk<NewDriverAvailabilityUseCase>(relaxed = true)
    private val newDriverBookingUseCase = mockk<NewDriverBookingUseCase>(relaxed = true)
    private val findingNewDriverMapper = mockk<FindingNewDriverMapper>(relaxed = true)

    private val newDriverAvailabilityObserver =
        mockk<Observer<NewDriverAvailabilityState>>(relaxed = true)
    private val newDriverBookingObserver =
        mockk<Observer<NewDriverBookingState>>(relaxed = true)

    lateinit var viewModel: FindingNewDriverViewModel

    private val mockThrowable = mockk<Throwable>(relaxed = true)

    @Before
    fun setup() {
        viewModel = FindingNewDriverViewModel(
            CoroutineTestDispatchersProvider,
            newDriverAvailabilityUseCase,
            newDriverBookingUseCase,
            findingNewDriverMapper
        )
        viewModel.newDriverAvailability.observeForever(newDriverAvailabilityObserver)
        viewModel.newDriverBooking.observeForever(newDriverBookingObserver)
    }

    @Test
    fun `verify when get new driver availability success`() {
        val orderId = "12345"
        val newDriverModel = mockk<FindingNewDriverModel>()
        val newDriverAvailabilityData =
            spyk(NewDriverAvailabilityResponse.NewDriverAvailabilityData())
        val mockResponse = spyk(
            NewDriverAvailabilityResponse(
                data = newDriverAvailabilityData
            )
        )

        coEvery { findingNewDriverMapper.map(newDriverAvailabilityData) } returns newDriverModel
        coEvery {
            newDriverAvailabilityUseCase.invoke(any())
        } returns mockResponse

        viewModel.getNewDriverAvailability(orderId)

        verify {
            newDriverAvailabilityObserver.onChanged(
                NewDriverAvailabilityState.Success(
                    newDriverModel
                )
            )
        }
    }

    @Test
    fun `verify when get new driver availability error`() {
        val orderId = "12345"
        val errorMessage = "error"
        val graphqlError = mockk<GraphqlError>()
        val mockResponse = spyk(NewDriverAvailabilityResponse())

        coEvery { graphqlError.message } returns errorMessage
        coEvery {
            newDriverAvailabilityUseCase.invoke(any())
        } returns mockResponse

        viewModel.getNewDriverAvailability(orderId)

        verify {
            newDriverAvailabilityObserver.onChanged(
                NewDriverAvailabilityState.Fail
            )
        }
    }

    @Test
    fun `verify show error message when new driver availability detect exception`() {
        val orderId = "12345"
        val errorMessage = "error"
        coEvery {
            newDriverAvailabilityUseCase.invoke(any())
        } throws mockThrowable

        coEvery { mockThrowable.message } returns errorMessage
        viewModel.getNewDriverAvailability(orderId)

        verify {
            newDriverAvailabilityObserver.onChanged(
                NewDriverAvailabilityState.Fail
            )
        }
    }

    @Test
    fun `verify show empty error message when new driver availability detect exception`() {
        val orderId = "12345"
        coEvery {
            newDriverAvailabilityUseCase.invoke(any())
        } throws mockThrowable

        coEvery { mockThrowable.message } returns null
        viewModel.getNewDriverAvailability(orderId)

        verify {
            newDriverAvailabilityObserver.onChanged(
                NewDriverAvailabilityState.Fail
            )
        }
    }

    @Test
    fun `verify when get new driver booking success`() {
        val orderId = "12345"
        val message = "message"
        val newDriverAvailabilityData =
            spyk(NewDriverBookingResponse.NewDriverBookingData(message = message))
        val mockResponse = spyk(
            NewDriverBookingResponse(
                data = newDriverAvailabilityData
            )
        )

        coEvery {
            newDriverBookingUseCase.invoke(any())
        } returns mockResponse

        viewModel.getNewDriverBooking(orderId)

        verify {
            newDriverBookingObserver.onChanged(
                NewDriverBookingState.Success(
                    message
                )
            )
        }
    }

    @Test
    fun `verify when get new driver booking error`() {
        val orderId = "12345"
        val errorMessage = "error"
        val graphqlError = mockk<GraphqlError>()
        val mockResponse = spyk(NewDriverBookingResponse())

        coEvery { graphqlError.message } returns errorMessage
        coEvery {
            newDriverBookingUseCase.invoke(any())
        } returns mockResponse

        viewModel.getNewDriverBooking(orderId)

        verify {
            newDriverBookingObserver.onChanged(
                NewDriverBookingState.Fail
            )
        }
    }

    @Test
    fun `verify when new driver booking detect exception`() {
        val orderId = "12345"
        coEvery {
            newDriverBookingUseCase.invoke(any())
        } throws mockThrowable

        viewModel.getNewDriverBooking(orderId)

        verify {
            newDriverBookingObserver.onChanged(
                NewDriverBookingState.Fail
            )
        }
    }
}
