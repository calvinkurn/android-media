package com.tokopedia.logisticorder.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.tokopedia.logisticorder.domain.response.GetLogisticTrackingResponse
import com.tokopedia.logisticorder.mapper.TrackingPageMapperNew
import com.tokopedia.logisticorder.uimodel.TrackingDataModel
import com.tokopedia.logisticorder.usecase.TrackingPageRepository
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
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

class TrackingPageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repo: TrackingPageRepository = mockk(relaxed = true)
    private val mapper = TrackingPageMapperNew()

    private val trackingPageRepository: TrackingPageRepository = mockk(relaxed = true)

    private lateinit var trackingPageViewModel: TrackingPageViewModel

    private val trackingDataObserver: Observer<Result<TrackingDataModel>> = mockk(relaxed = true)
    private val retryBookingObserver: Observer<Result<RetryBookingResponse>> = mockk(relaxed = true)
    private val retryAvailabilityObserver: Observer<Result<RetryAvailabilityResponse>> = mockk(relaxed = true)

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        trackingPageViewModel = TrackingPageViewModel(repo, mapper)
        trackingPageViewModel.trackingData.observeForever(trackingDataObserver)
        trackingPageViewModel.retryBooking.observeForever(retryBookingObserver)
        trackingPageViewModel.retryAvailability.observeForever(retryAvailabilityObserver)
    }

    @Test
    fun `Get Tracking Data Success`() {
        coEvery { repo.getTrackingPage(any(), any()) } returns GetLogisticTrackingResponse()
        trackingPageViewModel.getTrackingData("12234")
        verify { trackingDataObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Tracking Data Fail`() {
        coEvery { repo.getTrackingPage(any(), any()) } throws  defaultThrowable
        trackingPageViewModel.getTrackingData("12234")
        verify { trackingDataObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Retry Booking Success`() {
        coEvery { repo.retryBooking(any()) } returns RetryBookingResponse()
        trackingPageViewModel.retryBooking("12234")
        verify { retryBookingObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Retry Booking Fail`() {
        coEvery { repo.retryBooking(any()) } throws defaultThrowable
        trackingPageViewModel.retryBooking("12234")
        verify { retryBookingObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Retry Availability Success`() {
        coEvery { repo.retryAvailability(any()) } returns RetryAvailabilityResponse()
        trackingPageViewModel.retryAvailability("12234")
        verify { retryAvailabilityObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Retry Availability Fail`() {
        coEvery { repo.retryAvailability(any()) } throws defaultThrowable
        trackingPageViewModel.retryAvailability("12234")
        verify { retryAvailabilityObserver.onChanged(match { it is Fail }) }
    }


}