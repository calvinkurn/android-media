package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import com.tokopedia.logisticcart.datamock.DummyProvider
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable

object ShippingDurationPresenterTest : Spek({

    val ratesUseCase: GetRatesUseCase = mockk(relaxed = true)
    val ratesApiUseCase: GetRatesApiUseCase = mockk(relaxed = true)
    val responseConverter: RatesResponseStateConverter = mockk()
    val courierConverter: ShippingCourierConverter = mockk()
    val view: ShippingDurationContract.View = mockk(relaxed = true)

    Feature("load courier recommendation") {

        val presenter by memoized {
            ShippingDurationPresenter(ratesUseCase, ratesApiUseCase,
                    responseConverter, courierConverter)
        }

        beforeEachTest {
            presenter.attachView(view)
        }

        Scenario("get rates from express checkout") {

            val shippingParam = DummyProvider.getShippingParam()
            val shopShipments = DummyProvider.getShopShipments()
            val shippingData = DummyProvider.shippingRecommendationDataWithState

            Given("data") {
                every { ratesUseCase.execute(any()) } returns Observable.just(ShippingRecommendationData())
                every {
                    responseConverter.fillState(any(), shopShipments, 0, 0)
                } returns shippingData
            }

            When("executed") {
                presenter.loadCourierRecommendation(shippingParam, 0, shopShipments)
            }

            Then("view showing positive data") {
                verify {
                    view.showData(shippingData.shippingDurationViewModels, shippingData.logisticPromo)
                }
            }

        }

        Scenario("get rates") {

            val shipmentDetailData = DummyProvider.shipmentDetailData
            val shopShipments = DummyProvider.getShopShipments()
            val products = DummyProvider.products
            val address = DummyProvider.address
            val shippingData = DummyProvider.shippingRecommendationDataWithState

            Given("data") {
                every { ratesUseCase.execute(any()) } returns Observable.just(ShippingRecommendationData())
                every {
                    responseConverter.fillState(any(), shopShipments, any(), 0)
                } returns shippingData
            }

            When("executed") {
                presenter.loadCourierRecommendation(shipmentDetailData, 0, shopShipments, -1, false, false, "", products,
                        "1479278-30-740525-99367774", false, address)
            }

            Then("view showing positive data") {
                verify {
                    view.showData(any(), any())
                }
            }

        }

    }

    Feature("basic presenter") {
        val presenter by memoized {
            ShippingDurationPresenter(ratesUseCase, ratesApiUseCase,
                    responseConverter, courierConverter)
        }

        Scenario("detached") {

            When("presenter detached") {
                presenter.detachView()
            }

            Then("all usecases is unsubscribed") {
                verify {
                    ratesUseCase.unsubscribe()
                    ratesApiUseCase.unsubscribe()
                }
            }

        }
    }
})