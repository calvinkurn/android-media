package com.tokopedia.logisticseller.ui.findingnewdriver

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
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
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FindingNewDriverViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private val newDriverAvailabilityUseCase = mockk<NewDriverAvailabilityUseCase>(relaxed = true)
    private val newDriverBookingUseCase = mockk<NewDriverBookingUseCase>(relaxed = true)
    private val findingNewDriverMapper = mockk<FindingNewDriverMapper>(relaxed = true)

    private val newDriverAvailabilityObserver =
        mockk<Observer<NewDriverAvailabilityState>>(relaxed = true)
    private val newDriverBookingObserver =
        mockk<Observer<NewDriverBookingState>>(relaxed = true)

    lateinit var viewModel: FindingNewDriverViewModel

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
        val mockResponse = spyk(NewDriverAvailabilityResponse())

        coEvery {
            newDriverAvailabilityUseCase.invoke(any())
        } returns mockResponse

        viewModel.getNewDriverAvailability(orderId)

        verify {
            newDriverAvailabilityObserver.onChanged(
                NewDriverAvailabilityState.Fail(null)
            )
        }
    }

    @Test
    fun `verify show error message when new driver availability detect exception`() {
        val orderId = "12345"
        val errorMessage = "error"
        coEvery {
            newDriverAvailabilityUseCase.invoke(any())
        } throws Exception(errorMessage)

        viewModel.getNewDriverAvailability(orderId)

        verify {
            newDriverAvailabilityObserver.onChanged(
                NewDriverAvailabilityState.Fail(errorMessage)
            )
        }
    }

    @Test
    fun `verify show empty error message when new driver availability detect exception`() {
        val orderId = "12345"
        coEvery {
            newDriverAvailabilityUseCase.invoke(any())
        } throws Exception()

        viewModel.getNewDriverAvailability(orderId)

        verify {
            newDriverAvailabilityObserver.onChanged(
                NewDriverAvailabilityState.Fail(null)
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
        val mockResponse = spyk(NewDriverBookingResponse())

        coEvery {
            newDriverBookingUseCase.invoke(any())
        } returns mockResponse

        viewModel.getNewDriverBooking(orderId)

        verify {
            newDriverBookingObserver.onChanged(
                NewDriverBookingState.Fail(null)
            )
        }
    }

    @Test
    fun `verify when new driver booking detect exception`() {
        val errorMessage = "error"
        val orderId = "12345"

        coEvery {
            newDriverBookingUseCase.invoke(any())
        } throws Exception(errorMessage)

        viewModel.getNewDriverBooking(orderId)

        verify {
            newDriverBookingObserver.onChanged(
                NewDriverBookingState.Fail(errorMessage)
            )
        }
    }
}
