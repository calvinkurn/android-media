package com.tokopedia.purchase_platform.common.usecase

import org.spekframework.spek2.Spek

object SubmitHelpTicketUseCaseTest : Spek({

//    val graphqlUseCase: GraphqlUseCase = mockk(relaxed = true)
//    val param = RequestParams.create().apply {
//        putObject(SubmitHelpTicketUseCase.PARAM, SubmitHelpTicketRequest())
//    }
//
//    RxAndroidPlugins.getInstance().reset()
//    RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
//        override fun getMainThreadScheduler(): Scheduler {
//            return Schedulers.trampoline()
//        }
//    })
//    RxJavaHooks.setOnIOScheduler { Schedulers.trampoline() }
//
//    Feature("Submit Help Ticket Use Case") {
//
//        val useCase by memoized {
//            SubmitHelpTicketUseCase("", graphqlUseCase)
//        }
//
//        Scenario("Success Submit Ticket") {
//
//            lateinit var subscriber: AssertableSubscriber<SubmitTicketResult>
//            val successMessage = "success"
//
//            Given("mock response") {
//                val result = HashMap<Type, Any>()
//                val objectType = SubmitHelpTicketGqlResponse::class.java
//                result[objectType] = SubmitHelpTicketGqlResponse(SubmitHelpTicketResponse(status = SubmitHelpTicketUseCase.STATUS_OK, data = SubmitTicketDataResponse(message = arrayListOf(successMessage))))
//                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(
//                    result, null, false))
//            }
//
//            When("create observable") {
//                subscriber = useCase.createObservable(param).test()
//            }
//
//            Then("result status true and contains success message") {
//                subscriber.assertValue(SubmitTicketResult(status = true, message = successMessage))
//            }
//        }
//
//        Scenario("Fail Submit Ticket") {
//
//            lateinit var subscriber: AssertableSubscriber<SubmitTicketResult>
//            val errorMessage = "failure"
//            Given("mock response") {
//                val result = HashMap<Type, Any>()
//                val objectType = SubmitHelpTicketGqlResponse::class.java
//                result[objectType] = SubmitHelpTicketGqlResponse(SubmitHelpTicketResponse(status = "ERROR", errorMessages = arrayListOf(errorMessage)))
//                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(
//                        result, null, false))
//            }
//
//            When("create observable") {
//                subscriber = useCase.createObservable(param).test()
//            }
//
//            Then("result status false and contains error message") {
//                subscriber.assertValue(SubmitTicketResult(status = false, message = errorMessage))
//            }
//        }
//    }
})