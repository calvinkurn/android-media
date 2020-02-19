package com.tokopedia.purchase_platform.common.usecase

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.data.model.request.helpticket.SubmitHelpTicketRequest
import com.tokopedia.purchase_platform.common.data.model.response.helpticket.SubmitHelpTicketGqlResponse
import com.tokopedia.purchase_platform.common.data.model.response.helpticket.SubmitHelpTicketResponse
import com.tokopedia.purchase_platform.common.data.model.response.helpticket.SubmitTicketDataResponse
import com.tokopedia.purchase_platform.common.sharedata.helpticket.SubmitTicketResult
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.observers.AssertableSubscriber
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers

object SubmitHelpTicketUseCaseTest : Spek({

    val graphqlUseCase: GraphqlUseCase = mockk(relaxed = true)
    val param = RequestParams.create().apply {
        putObject(SubmitHelpTicketUseCase.PARAM, SubmitHelpTicketRequest())
    }

    RxAndroidPlugins.getInstance().reset()
    RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
        override fun getMainThreadScheduler(): Scheduler {
            return Schedulers.trampoline()
        }
    })
    RxJavaHooks.setOnIOScheduler { Schedulers.trampoline() }

    Feature("Submit Help Ticket Use Case") {

        val useCase by memoized {
            SubmitHelpTicketUseCase("", graphqlUseCase)
        }

        Scenario("Success Submit Ticket") {

            lateinit var subscriber: AssertableSubscriber<SubmitTicketResult>
            val successMessage = "success"

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(
                        mapOf(SubmitHelpTicketGqlResponse::class.java to SubmitHelpTicketGqlResponse(
                                SubmitHelpTicketResponse(status = SubmitHelpTicketUseCase.STATUS_OK, data = SubmitTicketDataResponse(message = arrayListOf(successMessage)))
                        )),
                        null, false))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("result status true and contains success message") {
                subscriber.assertValue(SubmitTicketResult(status = true, message = successMessage))
            }
        }

        Scenario("Fail Submit Ticket") {

            lateinit var subscriber: AssertableSubscriber<SubmitTicketResult>
            val errorMessage = "failure"

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(
                        mapOf(SubmitHelpTicketGqlResponse::class.java to SubmitHelpTicketGqlResponse(
                                SubmitHelpTicketResponse(status = "ERROR", errorMessages = arrayListOf(errorMessage))
                        )),
                        null, false))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("result status false and contains error message") {
                subscriber.assertValue(SubmitTicketResult(status = false, message = errorMessage))
            }
        }
    }
})