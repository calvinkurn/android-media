package com.tokopedia.logisticcart.cod.view

import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticcart.cod.model.CodResponse
import com.tokopedia.logisticcart.cod.usecase.CodConfirmUseCase
import com.tokopedia.logisticcart.datamock.DummyProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Subscriber
import java.lang.reflect.Type

object CodPresenterTest : Spek({

    val view: CodContract.View = mockk(relaxed = true)
    val confirmUseCase: CodConfirmUseCase = mockk(relaxUnitFun = true)
    lateinit var presenter: CodPresenter

    beforeEachTest {
        presenter = CodPresenter(confirmUseCase)
    }

    Feature("detach view") {
        Scenario("on detach all usecase unsubscribed") {
            When("detached") {
                presenter.detachView()
            }
            Then("usecase is unsubscribed") {
                confirmUseCase.unsubscribe()
            }
        }
    }

    Feature("confirm payment") {
        beforeEachTest {
            presenter.attachView(view)
        }

        Scenario("success response") {
            val successResponse = DummyProvider.getCodSuccess()

            val result = HashMap<Type, Any>()
            result[CodResponse::class.java] = successResponse
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            Given("usecase invoke on next") {
                every { confirmUseCase.execute(any(), any())
                } answers { secondArg<Subscriber<GraphqlResponse>>().onNext(gqlResponse) }
            }

            When("executed") {
                presenter.confirmPayment()
            }

            Then("view sends analytics") {
                verify {
                    view.sendEventEECod()
                }
            }
            Then("view navigate thankyou page with link") {
                verify {
                    view.navigateToThankYouPage(successResponse.checkoutCod?.data?.data?.thanksApplink!!)
                }
            }
        }

        Scenario("success response with error graphql") {
            val errorGql = GraphqlError().apply { message = "error test" }
            val errors = HashMap<Type, List<GraphqlError>>()
            errors[CodResponse::class.java] = listOf(errorGql)
            val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)
            Given("usecase invoke on next") {
                every { confirmUseCase.execute(any(), any())
                } answers {
                    secondArg<Subscriber<GraphqlResponse>>().onNext(gqlResponse)
                }
            }
            When("executed") {
                presenter.confirmPayment()
            }
            Then("view shows error") {
                verify { view.showError(errorGql.message) }
            }
        }

        Scenario("error thrown") {
            val err = Throwable("error test")
            Given("usecase invoke onerror") {
                every { confirmUseCase.execute(any(), any()) } answers {
                    secondArg<Subscriber<GraphqlResponse>>().onError(err)
                }
            }

            When("executed") {
                presenter.confirmPayment()
            }

            Then("view shows error with message") {
                verify {
                    view.showError(err.message)
                }
            }
        }
    }
})