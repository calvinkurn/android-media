package com.tokopedia.logisticaddaddress.features.district_recommendation

import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.domain.model.AddressResponse
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendation
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRequestUseCase
import com.tokopedia.logisticaddaddress.helper.DiscomDummyProvider
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.Subscriber

object DiscomPresenterTest: Spek({
    val first_page = 1
    val view: DiscomContract.View = mockk(relaxed = true)
    val rest: GetDistrictRequestUseCase = mockk(relaxUnitFun = true)
    val gql: GetDistrictRecommendation = mockk(relaxUnitFun = true)
    val revGeocodeUseCase: RevGeocodeUseCase = mockk(relaxed = true)
    val mapper = DistrictRecommendationMapper()
    lateinit var presenter: DiscomPresenter

    beforeEachTest {
        presenter = DiscomPresenter(rest, revGeocodeUseCase, gql, mapper)
        presenter.attach(view)
    }

    Feature("load data with data") {
        Scenario("load data with data return success") {
            val query = "jak"
            val expected = DiscomDummyProvider.getSuccessResponse()
            Given("data") {
                every { gql.execute(any(), any())
                } answers {
                    Observable.just(expected)
                }
            }

            When("load data") {
                presenter.loadData(query, first_page)
            }

            Then("return success") {
                verify {
                    view.renderData(withArg { org.assertj.core.api.Assertions.assertThat(it).isNotEmpty }, expected.keroDistrictRecommendation.nextAvailable)
                }

                verifyOrder {
                    view.setLoadingState(true)
                    view.setLoadingState(false)
                }
            }
        }

        Scenario("load data with data return error") {
            val query = "jak"
            val throwable = Throwable()
            Given("data") {
                every { gql.execute(any(), any())
                } answers {
                    Observable.error(throwable)
                }
            }

            When("load data") {
                presenter.loadData(query, first_page)
            }

            Then("return error"){
                verify {
                    view.showGetListError(throwable)
                }

                verifyOrder {
                    view.setLoadingState(true)
                    view.setLoadingState(false)
                }
            }
        }

        Scenario("load data with data return empty") {
            val query = "qwr"
            val datum = DiscomDummyProvider.getEmptyResponse()
            Given("data") {
                every { gql.execute(any(), any())
                } answers {
                    Observable.just(datum)
                }
            }

            When("load data") {
                presenter.loadData(query, first_page)
            }

            Then("return empty") {
                verify {
                    view.showEmpty()
                }

                verifyOrder {
                    view.setLoadingState(true)
                    view.setLoadingState(false)
                }
            }
        }
    }

    Feature("load data with token data") {
        Scenario("load data with token return success"){
            val query = "jak"
            val expected = AddressResponse().apply {
                addresses = arrayListOf(
                        Address().apply { cityName = "Jakarta" }
                )
                isNextAvailable = false
            }
            Given("token data") {
                every { rest.execute(any(), any())
                } answers {
                    secondArg<Subscriber<AddressResponse>>().onNext(expected)
                }
            }

            When("load data") {
                presenter.loadData(query, first_page, Token())
            }

            Then("return success") {
                verify {
                    view.renderData(expected.addresses, expected.isNextAvailable)
                }
            }
        }

        Scenario("load data with token return error") {
            val query = "jak"
            val throwable = Throwable()
            every { rest.execute(any(), any()) } answers {
                secondArg<Subscriber<AddressResponse>>().onError(throwable)
            }
            Given("token data") {
               every { rest.execute(any(), any())
               } answers {
                   secondArg<Subscriber<AddressResponse>>().onError(throwable)
               }
            }

            When("load data") {
                presenter.loadData(query, first_page, Token())
            }

            Then("return error") {
                verify {
                    view.showGetListError(throwable)
                }
            }
        }

        Scenario("load data with token return empty") {
            val query = "qwr"
            val expected = AddressResponse().apply {
                addresses = arrayListOf()
                isNextAvailable = false
            }
            Given("token data") {
                every { rest.execute(any(), any())
                } answers {
                    secondArg<Subscriber<AddressResponse>>().onNext(expected)
                }
            }

            When("load data") {
                presenter.loadData(query, first_page, Token())
            }

            Then("return empty") {
                verify {
                    view.showEmpty()
                }
            }
        }
    }

    Feature("onDetach") {
        Scenario("presenter onDetach") {
            When("presenter onDetach") {
                presenter.detach()
            }

            Then("gql unsubscribed") {
                gql.unsubscribe()
            }

            Then("rest unsubscrbed") {
                rest.unsubscribe()
            }
        }
    }
})