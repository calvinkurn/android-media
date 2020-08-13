package com.tokopedia.logisticorder.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticorder.usecase.GetRetryAvailability
import com.tokopedia.logisticorder.usecase.RetryPickup
import com.tokopedia.logisticorder.usecase.TrackCourierUseCase
import com.tokopedia.logisticorder.usecase.entity.RetryAvailability
import com.tokopedia.logisticorder.usecase.entity.RetryAvailabilityResponse
import com.tokopedia.logisticorder.usecase.entity.RetryBookingResponse
import com.tokopedia.logisticorder.view.ITrackingPageFragment
import com.tokopedia.logisticorder.uimodel.TrackingUiModel
import com.tokopedia.user.session.UserSession
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.Subscriber

class TrackingPagePresenterTest {
    @get:Rule
    var  instantTaskExecutorRule = InstantTaskExecutorRule()

    private val useCase: TrackCourierUseCase = mockk(relaxed = true)
    private val getRetryUseCase : GetRetryAvailability = mockk(relaxed = true)
    private val retryPickUpUseCase : RetryPickup = mockk(relaxed = true)
    private val userSession: UserSession = mockk(relaxed = true)
    private val view: ITrackingPageFragment = mockk(relaxed = true)
    private val deadlineTime = 0L

    private val trackingPagePresenter by lazy {
        TrackingPagePresenter(
                useCase,
                getRetryUseCase,
                retryPickUpUseCase,
                userSession,
                view)
    }

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        trackingPagePresenter.attachView(view)
    }

    /**
     * Get Retry Availability
     * */
    @Test
    fun `get retry availability | on success | all true`() {
        val retryAvailability = RetryAvailability()
        retryAvailability.showRetryButton = true
        retryAvailability.availabilityRetry = true
        retryAvailability.deadlineRetryUnixtime = "0"

        val retryAvailabilityResponse = RetryAvailabilityResponse(retryAvailability)

        every {
            getRetryUseCase.execute(any() )
        } answers {
            Observable.just(retryAvailabilityResponse)
        }

        trackingPagePresenter.onGetRetryAvailability("")

        verify { view.setRetryButton(true,  deadlineTime) }
    }

    @Test
    fun  `get retry availability | on success | availabilityRetry false`() {
        val retryAvailability = RetryAvailability()
        retryAvailability.availabilityRetry = false
        retryAvailability.deadlineRetryUnixtime = "0"

        val retryAvailabilityResponse = RetryAvailabilityResponse(retryAvailability)

        every { getRetryUseCase.execute(any())
        } answers {
            Observable.just(retryAvailabilityResponse)
        }

        trackingPagePresenter.onGetRetryAvailability("")

        verify { view.setRetryButton(false, deadlineTime) }
    }

    @Test
    fun  `get retry availability | on success | showRetryButton false`() {
        val retryAvailability = RetryAvailability()
        retryAvailability.showRetryButton = false
        retryAvailability.deadlineRetryUnixtime = "0"

        val retryAvailabilityResponse = RetryAvailabilityResponse(retryAvailability)

        every { getRetryUseCase.execute(any())
        } answers {
            Observable.just(retryAvailabilityResponse)
        }

        trackingPagePresenter.onGetRetryAvailability("")

        verify { view.setRetryButton(false, deadlineTime) }
    }

    @Test
    fun `get retry availability | on fail`() {
        every {
            getRetryUseCase.execute(any())
        } answers {
            Observable.error(Exception())
        }

        trackingPagePresenter.onGetRetryAvailability("")

        verify { view.showSoftError(any()) }
    }

    /**
     * Retry Pickup
     * */
    @Test
    fun `retry pickup | on success`(){
        val retryBookingResponse = RetryBookingResponse()
        every {
            retryPickUpUseCase.execute(any())
        } answers {
            Observable.just(retryBookingResponse)
        }

        trackingPagePresenter.onRetryPickup("")

        verify { view.startSuccessCountdown() }
    }

    @Test
    fun `retry pickup | on fail`(){
        every {
            retryPickUpUseCase.execute(any())
        } answers {
            Observable.error(Exception())
        }

        trackingPagePresenter.onRetryPickup("")

        verify { view.showError(any()) }
    }

    /**
     * Get Tracking Data
     * */
    @Test
    fun `get tracking data | on success`() {
        val trackingUiModel = TrackingUiModel()
        every { useCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<TrackingUiModel>>().onNext(trackingUiModel)
        }

        trackingPagePresenter.onGetTrackingData("")

        verifyOrder {
            view.hideLoading()
            view.populateView(trackingUiModel)}
    }

    @Test
    fun `get tracking data | on fail`() {
        every { useCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<TrackingUiModel>>().onError(Throwable())
        }

        trackingPagePresenter.onGetTrackingData("")

        verifyOrder {
            view.hideLoading()
            view.showError(any())}
    }

    /**
     * OnDestroy
     * */
    @Test
    fun `on destroy`() {
        trackingPagePresenter.onDetach()

        verify {
            useCase.unsubscribe()
            getRetryUseCase.unsubscribe()
            retryPickUpUseCase.unsubscribe()
        }
    }
}