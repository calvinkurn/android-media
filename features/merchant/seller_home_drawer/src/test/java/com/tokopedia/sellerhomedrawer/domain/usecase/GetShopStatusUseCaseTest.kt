package com.tokopedia.sellerhomedrawer.domain.usecase

import com.tokopedia.sellerhomedrawer.data.GoldGetPmOsStatus
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.observers.AssertableSubscriber

class GetShopStatusUseCaseTest : Spek({

    Feature("GetShopStatusUseCase") {

        val getShopStatusUseCase: GetShopStatusUseCase = mockk(relaxed = true)
        val requestParams: RequestParams = mockk(relaxed = true)

        Scenario("GetShopStatusUseCase is success") {
            val getShopStatusUseCaseSuccess = mockk<GoldGetPmOsStatus>()
            lateinit var testSubscriber: AssertableSubscriber<GoldGetPmOsStatus>

            Given("GetShopStatusUseCase returns success") {
                every {
                    getShopStatusUseCase.createObservable(any())
                } returns Observable.just(getShopStatusUseCaseSuccess)
            }

            When("Execute GetShopStatusUseCase") {
                testSubscriber = getShopStatusUseCase.createObservable(requestParams).test()
            }

            Then("The Subscriber is completed and not throwing any errors") {
                testSubscriber.assertCompleted()
                testSubscriber.assertNoErrors()
            }
        }

        Scenario("GetShopStatusUseCase is failed") {
            val getShopStatusUseCaseError = mockk<Throwable>()
            lateinit var testSubscriber: AssertableSubscriber<GoldGetPmOsStatus>

            Given("GetShopStatusUseCase returns error") {
                every {
                    getShopStatusUseCase.createObservable(any())
                } returns Observable.error(getShopStatusUseCaseError)
            }

            When("GetShopStatusUseCase executed") {
                testSubscriber = getShopStatusUseCase.createObservable(requestParams).test()
            }

            Then("The subscriber will return error throwable") {
                testSubscriber.assertError(getShopStatusUseCaseError)
            }
        }

    }
})