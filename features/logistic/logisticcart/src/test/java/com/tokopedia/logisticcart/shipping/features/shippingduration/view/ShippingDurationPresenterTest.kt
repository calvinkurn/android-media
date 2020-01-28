package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import com.tokopedia.logisticcart.datamock.DummyProvider
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.model.*
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
    lateinit var presenter: ShippingDurationPresenter

    beforeEachTest {
        presenter = ShippingDurationPresenter(ratesUseCase, ratesApiUseCase,
                responseConverter, courierConverter)
    }

    Feature("load courier recommendation default") {

        lateinit var shipmentDetailData: ShipmentDetailData
        lateinit var shopShipments: List<ShopShipment>
        lateinit var products: List<Product>
        lateinit var address: RecipientAddressModel

        beforeEachTest {
            presenter.attachView(view)

            shipmentDetailData = DummyProvider.getShipmentDetailData()
            shopShipments = DummyProvider.getShopShipments()
            products = DummyProvider.products
            address = DummyProvider.getAddress()
        }

        Scenario("fetch data then show data") {

            val shippingData = DummyProvider.getShippingRecommendationDataWithState()

            Given("observable returning success data") {
                every { ratesUseCase.execute(any()) } returns Observable.just(ShippingRecommendationData())
                every {
                    responseConverter.fillState(any(), shopShipments, any(), 0)
                } returns shippingData
            }

            When("executed") {
                presenter.loadCourierRecommendation(shipmentDetailData, 0,
                        shopShipments, -1, false, false, "",
                        products, "1479278-30-740525-99367774", false, address)
            }

            Then("view shows positive data") {
                verify {
                    view.showData(any(), any())
                }
            }

        }

        Scenario("fetch data then show courier caused by 504") {

            val shippingData = DummyProvider.getShippingRecommendationDataWithState()
            shippingData.errorId = "504"
            shippingData.errorMessage = "Error test"

            Given("observable returning 504 error id") {
                every { ratesUseCase.execute(any()) } returns Observable.just(ShippingRecommendationData())
                every {
                    responseConverter.fillState(any(), shopShipments, any(), 0)
                } returns shippingData
            }

            When("executed") {
                presenter.loadCourierRecommendation(shipmentDetailData, 0,
                        shopShipments, -1, false, false, "",
                        products, "1479278-30-740525-99367774", false, address)
            }

            Then("view shows no courier page from errorMessage") {
                verify {
                    view.showNoCourierAvailable(shippingData.errorMessage)
                }
            }
        }
    }

    Feature("load courier recommendation express checkout") {


        beforeEachTest {
            presenter.attachView(view)
        }

        Scenario("fetch data and showing data") {

            val shippingParam = DummyProvider.getShippingParam()
            val shopShipments = DummyProvider.getShopShipments()
            val shippingData = DummyProvider.getShippingRecommendationDataWithState()

            Given("observable returning success data") {
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

    }

    Feature("basic presenter") {

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