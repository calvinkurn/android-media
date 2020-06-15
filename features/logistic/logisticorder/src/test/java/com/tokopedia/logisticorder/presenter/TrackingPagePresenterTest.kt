package com.tokopedia.logisticorder.presenter

import com.tokopedia.logisticorder.usecase.GetRetryAvailability
import com.tokopedia.logisticorder.usecase.RetryPickup
import com.tokopedia.logisticorder.usecase.TrackCourierUseCase
import com.tokopedia.logisticorder.usecase.entity.RetryAvailability
import com.tokopedia.logisticorder.usecase.entity.RetryAvailabilityResponse
import com.tokopedia.logisticorder.usecase.entity.RetryBookingResponse
import com.tokopedia.logisticorder.view.ITrackingPageFragment
import com.tokopedia.user.session.UserSession
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

object TrackingPagePresenterTest : Spek({
    val useCase: TrackCourierUseCase = mockk(relaxed = true)
    val getRetryUseCase : GetRetryAvailability = mockk(relaxed = true)
    val retryPickUpUseCase : RetryPickup = mockk(relaxed = true)
    val userSession: UserSession = mockk(relaxed = true)
    val view: ITrackingPageFragment = mockk(relaxed = true)

    val presenter : TrackingPagePresenter by memoized{
        TrackingPagePresenter(useCase, getRetryUseCase, retryPickUpUseCase, userSession, view)
    }

    beforeEachTest {
        MockKAnnotations.init(this)
        presenter.attachView(view)
    }

    Feature("tracking"){

        Scenario("presenter onDetach"){
            When("presenter onDetach"){
                presenter.onDetach()
            }
            Then("useCase unsubscribed"){
                verify { useCase.unsubscribe() }
            }
            Then("retryAvailUseCase unsubscribed"){
                verify { getRetryUseCase.unsubscribe() }
            }
            Then("retryPickupUseCase unsubscribed"){
                verify { retryPickUpUseCase.unsubscribe() }
            }
        }

        Scenario("onSuccessRetryPickup"){
            Given("retryPickUpUseCase success"){
                every { retryPickUpUseCase.execute(any()) } returns Observable.just(RetryBookingResponse())
            }
            When("onRetryPickup"){
                presenter.onRetryPickup("")
            }
            Then("view should startSuccessCountdown"){
                verify { view.startSuccessCountdown() }

            }

        }

        Scenario("onFailedRetryPickup"){
            Given("retryPickUpUseCase failed"){
                every { retryPickUpUseCase.execute(any()) } returns Observable.error(Exception())
            }
            When("onRetryPickup"){
                presenter.onRetryPickup("")
            }
            Then("error view"){
                verify { view.showError(any()) }

            }

        }

        Scenario("onSuccessRetryAvailability all true"){
            Given("retryAvailUseCase success"){
                val retryAvailability = RetryAvailability()
                retryAvailability.showRetryButton = true
                retryAvailability.availabilityRetry = true
                retryAvailability.deadlineRetryUnixtime = "0"

                val retryAvailabilityResponse = RetryAvailabilityResponse(retryAvailability)

                every { getRetryUseCase.execute(any()) } returns Observable.just(retryAvailabilityResponse)
            }
            When("onRetryAvailability"){
                presenter.onGetRetryAvailability("")
            }
            Then("view should setRetryButton"){
                verify { view.setRetryButton(true, 0L) }

            }

        }

        Scenario("onSuccessRetryAvailability getAvailabilityRetry false"){
            Given("retryAvailUseCase success"){
                val retryAvailability = RetryAvailability()
                retryAvailability.availabilityRetry = false
                retryAvailability.deadlineRetryUnixtime = "100"

                val retryAvailabilityResponse = RetryAvailabilityResponse(retryAvailability)

                every { getRetryUseCase.execute(any()) } returns Observable.just(retryAvailabilityResponse)
            }
            When("onRetryAvailability"){
                presenter.onGetRetryAvailability("")
            }
            Then("view should setRetryButton"){
                verify { view.setRetryButton(false, 100L ) }

            }

        }

        Scenario("onSuccessRetryAvailability getAvailabilityRetry true, getShowRetryButton false"){
            Given("retryAvailUseCase success"){
                val retryAvailability = RetryAvailability()
                retryAvailability.availabilityRetry = false
                retryAvailability.availabilityRetry = true
                retryAvailability.deadlineRetryUnixtime = "0"

                val retryAvailabilityResponse = RetryAvailabilityResponse(retryAvailability)

                every { getRetryUseCase.execute(any()) } returns Observable.just(retryAvailabilityResponse)
            }
            When("onRetryAvailability"){
                presenter.onGetRetryAvailability("")
            }
            Then("view should setRetryButton"){
                verify { view.setRetryButton(false, 0L ) }

            }

        }

        Scenario("onFailedGetRetryAvailability"){
            Given("retryAvailability failed"){
                every { getRetryUseCase.execute(any()) } returns Observable.error(Exception())
            }
            When("onRetryAvailability"){
                presenter.onGetRetryAvailability("")
            }
            Then("error view"){
                verify { view.showSoftError(any()) }

            }

        }
    }
})