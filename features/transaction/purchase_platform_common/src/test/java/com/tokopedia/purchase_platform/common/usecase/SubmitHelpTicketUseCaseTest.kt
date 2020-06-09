package com.tokopedia.purchase_platform.common.usecase

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.feature.helpticket.data.request.SubmitHelpTicketRequest
import com.tokopedia.purchase_platform.common.feature.helpticket.data.response.SubmitHelpTicketGqlResponse
import com.tokopedia.purchase_platform.common.feature.helpticket.data.response.SubmitHelpTicketResponse
import com.tokopedia.purchase_platform.common.feature.helpticket.data.response.SubmitTicketDataResponse
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.model.SubmitTicketResult
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
import java.lang.reflect.Type

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
                val result = HashMap<Type, Any>()
                val objectType = SubmitHelpTicketGqlResponse::class.java
                result[objectType] = SubmitHelpTicketGqlResponse(SubmitHelpTicketResponse(status = SubmitHelpTicketUseCase.STATUS_OK, data = SubmitTicketDataResponse(message = arrayListOf(successMessage))))
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(
                    result, null, false))
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
                val result = HashMap<Type, Any>()
                val objectType = SubmitHelpTicketGqlResponse::class.java
                result[objectType] = SubmitHelpTicketGqlResponse(SubmitHelpTicketResponse(status = "ERROR", errorMessages = arrayListOf(errorMessage)))
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(
                        result, null, false))
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