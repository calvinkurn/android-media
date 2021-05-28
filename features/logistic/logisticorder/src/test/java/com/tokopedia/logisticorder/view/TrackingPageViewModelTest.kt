package com.tokopedia.logisticorder.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.tokopedia.logisticorder.uimodel.TrackingDataModel
import com.tokopedia.logisticorder.usecase.TrackingPageRepository
import com.tokopedia.logisticorder.usecase.entity.RetryAvailabilityResponse
import com.tokopedia.logisticorder.usecase.entity.RetryBookingResponse
import com.tokopedia.usecase.coroutines.Result
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

class TrackingPageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val trackingPageRepository: TrackingPageRepository = mockk(relaxed = true)

    private lateinit var trackingPageViewModel: TrackingPageViewModel

    private val trackingDataObserver: Observer<Result<TrackingDataModel>> = mockk(relaxed = true)
    private val retryBookingObserver: Observer<Result<RetryBookingResponse>> = mockk(relaxed = true)
    private val retryAvailability: Observer<Result<RetryAvailabilityResponse>> = mockk(relaxed = true)

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMa
    }
}