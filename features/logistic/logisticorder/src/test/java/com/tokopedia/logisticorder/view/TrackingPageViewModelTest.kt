package com.tokopedia.logisticorder.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticorder.domain.response.GetLogisticTrackingResponse
import com.tokopedia.logisticorder.mapper.TrackingPageMapperNew
import com.tokopedia.logisticorder.uimodel.TrackingDataModel
import com.tokopedia.logisticorder.usecase.GetTrackingUseCase
import com.tokopedia.logisticorder.usecase.SetRetryAvailabilityUseCase
import com.tokopedia.logisticorder.usecase.SetRetryBookingUseCase
import com.tokopedia.logisticorder.usecase.entity.RetryAvailabilityResponse
import com.tokopedia.logisticorder.usecase.entity.RetryBookingResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TrackingPageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val getTrackingUseCase: GetTrackingUseCase = mockk(relaxed = true)
    private val setRetryAvailabilityUseCase: SetRetryAvailabilityUseCase = mockk(relaxed = true)
    private val setRetryBookingUseCase: SetRetryBookingUseCase = mockk(relaxed = true)

    private val mapper = TrackingPageMapperNew()

    private lateinit var trackingPageViewModel: TrackingPageViewModel

    private val trackingDataObserver: Observer<Result<TrackingDataModel>> = mockk(relaxed = true)
    private val retryBookingObserver: Observer<Result<RetryBookingResponse>> = mockk(relaxed = true)
    private val retryAvailabilityObserver: Observer<Result<RetryAvailabilityResponse>> = mockk(relaxed = true)

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        trackingPageViewModel = TrackingPageViewModel(
            getTrackingUseCase,
            setRetryBookingUseCase,
            setRetryAvailabilityUseCase,
            mapper
        )
        trackingPageViewModel.trackingData.observeForever(trackingDataObserver)
        trackingPageViewModel.retryBooking.observeForever(retryBookingObserver)
        trackingPageViewModel.retryAvailability.observeForever(retryAvailabilityObserver)
    }

    @Test
    fun `Get Tracking Data Success`() {
        coEvery { getTrackingUseCase(any()) } returns GetLogisticTrackingResponse()
        trackingPageViewModel.getTrackingData("12234")
        verify { trackingDataObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Tracking Data Fail`() {
        coEvery { getTrackingUseCase(any()) } throws defaultThrowable
        trackingPageViewModel.getTrackingData("12234")
        verify { trackingDataObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Retry Booking Success`() {
        coEvery { setRetryBookingUseCase(any()) } returns RetryBookingResponse()
        trackingPageViewModel.retryBooking("12234")
        verify { retryBookingObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Retry Booking Fail`() {
        coEvery { setRetryBookingUseCase(any()) } throws defaultThrowable
        trackingPageViewModel.retryBooking("12234")
        verify { retryBookingObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Retry Availability Success`() {
        coEvery { setRetryAvailabilityUseCase(any()) } returns RetryAvailabilityResponse()
        trackingPageViewModel.retryAvailability("12234")
        verify { retryAvailabilityObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Retry Availability Fail`() {
        coEvery { setRetryAvailabilityUseCase(any()) } throws defaultThrowable
        trackingPageViewModel.retryAvailability("12234")
        verify { retryAvailabilityObserver.onChanged(match { it is Fail }) }
    }
}
