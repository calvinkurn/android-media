package com.tokopedia.logisticorder.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticorder.domain.response.GetLogisticTrackingResponse
import com.tokopedia.logisticorder.domain.response.LogisticTrackingResponse
import com.tokopedia.logisticorder.domain.response.Page
import com.tokopedia.logisticorder.domain.response.TickerUnificationParams
import com.tokopedia.logisticorder.domain.response.TrackingData
import com.tokopedia.logisticorder.mapper.TrackingPageMapperNew
import com.tokopedia.logisticorder.uimodel.TrackingPageEvent
import com.tokopedia.logisticorder.usecase.GetTrackingUseCase
import com.tokopedia.logisticorder.usecase.SetRetryAvailabilityUseCase
import com.tokopedia.logisticorder.usecase.SetRetryBookingUseCase
import com.tokopedia.logisticorder.usecase.entity.RetryAvailabilityResponse
import com.tokopedia.logisticorder.usecase.entity.RetryBookingResponse
import com.tokopedia.targetedticker.domain.GetTargetedTickerResponse
import com.tokopedia.targetedticker.domain.GetTargetedTickerUseCase
import com.tokopedia.targetedticker.domain.TargetedTickerMapper
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TrackingPageComposeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = UnconfinedTestRule()

    private val getTrackingUseCase: GetTrackingUseCase = mockk(relaxed = true)
    private val setRetryAvailabilityUseCase: SetRetryAvailabilityUseCase = mockk(relaxed = true)
    private val setRetryBookingUseCase: SetRetryBookingUseCase = mockk(relaxed = true)
    private val targetedTickerUseCase: GetTargetedTickerUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)

    private val mapper = TrackingPageMapperNew()

    private lateinit var trackingPageViewModel: TrackingPageComposeViewModel

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        trackingPageViewModel = TrackingPageComposeViewModel(
            coroutineTestRule.dispatchers,
            getTrackingUseCase,
            setRetryBookingUseCase,
            setRetryAvailabilityUseCase,
            targetedTickerUseCase,
            userSession,
            mapper
        )
    }

    @Test
    fun `Get Tracking Data Success`() {
        coEvery { getTrackingUseCase(any()) } returns GetLogisticTrackingResponse()
        val event = TrackingPageEvent.LoadTrackingData("12234", "1", 2, "", "")
        trackingPageViewModel.onEvent(event)

        val result = trackingPageViewModel.uiState.value

        assert(!result.isLoading)
        assertNotNull(result.trackingData)
    }

    @Test
    fun `Get Tracking Data Fail`() {
        coEvery { getTrackingUseCase(any()) } throws defaultThrowable
        val event = TrackingPageEvent.LoadTrackingData("12234", "1", 2, "", "")
        trackingPageViewModel.onEvent(event)

        val result = trackingPageViewModel.uiState.value
        val error = trackingPageViewModel.error.replayCache.first()

        assert(!result.isLoading)
        assertNull(result.trackingData)
        assert(error == defaultThrowable)
    }

    @Test
    fun `Retry Booking Success`() {
        coEvery { setRetryBookingUseCase(any()) } returns RetryBookingResponse()
        coEvery { getTrackingUseCase(any()) } returns GetLogisticTrackingResponse()
        val initialEvent = TrackingPageEvent.LoadTrackingData("12234", "1", 2, "", "")
        trackingPageViewModel.onEvent(initialEvent)
        val retryEvent = TrackingPageEvent.FindNewDriver
        trackingPageViewModel.onEvent(retryEvent)

        coVerify {
            getTrackingUseCase(any())
            setRetryBookingUseCase("12234")
            delay(5000)
            getTrackingUseCase(any())
        }
    }

    @Test
    fun `Retry Booking Fail`() {
        coEvery { setRetryBookingUseCase(any()) } throws defaultThrowable
        coEvery { getTrackingUseCase(any()) } returns GetLogisticTrackingResponse()
        val initialEvent = TrackingPageEvent.LoadTrackingData("12234", "1", 2, "", "")
        trackingPageViewModel.onEvent(initialEvent)
        val retryEvent = TrackingPageEvent.FindNewDriver
        trackingPageViewModel.onEvent(retryEvent)
        val error = trackingPageViewModel.error.replayCache.first()
        assert(error == defaultThrowable)
    }

    @Test
    fun `Retry Availability Success`() {
        coEvery { setRetryAvailabilityUseCase(any()) } returns RetryAvailabilityResponse()
        coEvery { getTrackingUseCase(any()) } returns GetLogisticTrackingResponse()
        val initialEvent = TrackingPageEvent.LoadTrackingData("12234", "1", 2, "", "")
        trackingPageViewModel.onEvent(initialEvent)
        val retryAvailabilityEvent = TrackingPageEvent.CheckAvailabilityToFindNewDriver
        trackingPageViewModel.onEvent(retryAvailabilityEvent)
        val result = trackingPageViewModel.uiState.value

        assertNotNull(result.retryAvailability)
    }

    @Test
    fun `Retry Availability Fail`() {
        coEvery { setRetryAvailabilityUseCase(any()) } throws defaultThrowable
        coEvery { getTrackingUseCase(any()) } returns GetLogisticTrackingResponse()
        val initialEvent = TrackingPageEvent.LoadTrackingData("12234", "1", 2, "", "")
        trackingPageViewModel.onEvent(initialEvent)
        val retryAvailabilityEvent = TrackingPageEvent.CheckAvailabilityToFindNewDriver
        trackingPageViewModel.onEvent(retryAvailabilityEvent)
        val error = trackingPageViewModel.error.replayCache.first()
        val result = trackingPageViewModel.uiState.value
        assert(error == defaultThrowable)
        assertNull(result.retryAvailability)
    }

    @Test
    fun `Refresh Tracking Data`() {
        coEvery { getTrackingUseCase(any()) } returns GetLogisticTrackingResponse()
        val initialEvent = TrackingPageEvent.LoadTrackingData("12234", "1", 2, "", "")
        trackingPageViewModel.onEvent(initialEvent)
        val trackingParam = getTrackingUseCase.getParam("12234", "1", 2, "")

        val refreshEvent = TrackingPageEvent.Refresh
        trackingPageViewModel.onEvent(refreshEvent)

        val result = trackingPageViewModel.uiState.value

        coVerify(exactly = 2) { getTrackingUseCase(trackingParam) }

        assert(!result.isLoading)
        assertNotNull(result.trackingData)
    }

    @Test
    fun `Targeted Ticker Success`() {
        val targetedTickerParam = TickerUnificationParams(target = listOf(
            TickerUnificationParams.Target(
                type = "type",
                values = listOf("a", "b", "c")
            )
        ))
        coEvery { getTrackingUseCase(any()) } returns GetLogisticTrackingResponse(
            LogisticTrackingResponse(data = TrackingData(page = Page(tickerUnificationParams = targetedTickerParam)))
        )

        val response = GetTargetedTickerResponse()
        coEvery { targetedTickerUseCase(any()) } returns response
        val initialEvent = TrackingPageEvent.LoadTrackingData("12234", "1", 2, "", "")
        trackingPageViewModel.onEvent(initialEvent)

        val result = trackingPageViewModel.uiState.value
        val tickerResult = TargetedTickerMapper.convertTargetedTickerToUiModel(response.getTargetedTickerData)

        assertNotNull(result.tickerData)
        assert(tickerResult == result.tickerData)
    }

    @Test
    fun `When seller open lacak page for order with gojek or grab courier THEN hit retryAvailability to check availability to request new driver`() {
        coEvery { setRetryBookingUseCase(any()) } returns RetryBookingResponse()
        coEvery { getTrackingUseCase(any()) } returns GetLogisticTrackingResponse()
        val initialEvent = TrackingPageEvent.LoadTrackingData("12234", "1", 2, "https://track.gojek.com/?id=fd1015ee", "seller")
        trackingPageViewModel.onEvent(initialEvent)

        coVerify {
            setRetryAvailabilityUseCase(any())
        }
    }
}
