package com.tokopedia.purchase_platform.features.checkout.domain.usecase

import com.google.gson.Gson
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.purchase_platform.*
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse
import com.tokopedia.purchase_platform.features.checkout.data.repository.ICheckoutRepository
import com.tokopedia.purchase_platform.features.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.observers.AssertableSubscriber

object GetShipmentAddressFormUseCaseDisableFeatureTest : Spek({

    val repository = mockk<ICheckoutRepository>()
    val mapper = ShipmentMapper()
    val useCase by memoized { GetShipmentAddressFormUseCase(repository, mapper) }

    val gson = Gson()
    val param = RequestParams.create().apply {
        putObject(GetShipmentAddressFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS, TKPDMapParam<String, String>())
    }

    Feature("Disabled Features") {

        lateinit var subscriber: AssertableSubscriber<CartShipmentAddressFormData>

        Scenario("Disable Dropshipper") {

            Given("mock response") {
                every { repository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(apiResponseSAFDisableFeatureDropshipper, ShipmentAddressFormDataResponse::class.java))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should only have dropshipper disabled") {
                val result = subscriber.onNextEvents[0]
                assertEquals(true, result.isDropshipperDisable)
                assertEquals(false, result.isMultipleDisable)
                assertEquals(false, result.isOrderPrioritasDisable)
                assertNotNull(result.egoldAttributes)
                result.groupAddress.each { groupShop.each { products.each { assertNotNull(purchaseProtectionPlanData) } } }
                assertNotNull(result.donation)
            }
        }

        Scenario("Disable Multiple Address") {

            Given("mock response") {
                every { repository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(apiResponseSAFDisableFeatureMultipleAddress, ShipmentAddressFormDataResponse::class.java))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should only have multiple address disabled") {
                val result = subscriber.onNextEvents[0]
                assertEquals(false, result.isDropshipperDisable)
                assertEquals(true, result.isMultipleDisable)
                assertEquals(false, result.isOrderPrioritasDisable)
                assertNotNull(result.egoldAttributes)
                result.groupAddress.each { groupShop.each { products.each { assertNotNull(purchaseProtectionPlanData) } } }
                assertNotNull(result.donation)
            }
        }

        Scenario("Disable Order Prioritas") {

            Given("mock response") {
                every { repository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(apiResponseSAFDisableFeatureOrderPrioritas, ShipmentAddressFormDataResponse::class.java))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should only have order prioritas disabled") {
                val result = subscriber.onNextEvents[0]
                assertEquals(false, result.isDropshipperDisable)
                assertEquals(false, result.isMultipleDisable)
                assertEquals(true, result.isOrderPrioritasDisable)
                assertNotNull(result.egoldAttributes)
                result.groupAddress.each { groupShop.each { products.each { assertNotNull(purchaseProtectionPlanData) } } }
                assertNotNull(result.donation)
            }
        }

        Scenario("Disable Egold") {

            Given("mock response") {
                every { repository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(apiResponseSAFDisableFeatureEGold, ShipmentAddressFormDataResponse::class.java))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should only have egold disabled") {
                val result = subscriber.onNextEvents[0]
                assertEquals(false, result.isDropshipperDisable)
                assertEquals(false, result.isMultipleDisable)
                assertEquals(false, result.isOrderPrioritasDisable)
                assertNull(result.egoldAttributes)
                result.groupAddress.each { groupShop.each { products.each { assertNotNull(purchaseProtectionPlanData) } } }
                assertNotNull(result.donation)
            }
        }

        Scenario("Disable PPP") {

            Given("mock response") {
                every { repository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(apiResponseSAFDisableFeaturePPP, ShipmentAddressFormDataResponse::class.java))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should only have ppp disabled") {
                val result = subscriber.onNextEvents[0]
                assertEquals(false, result.isDropshipperDisable)
                assertEquals(false, result.isMultipleDisable)
                assertEquals(false, result.isOrderPrioritasDisable)
                assertNotNull(result.egoldAttributes)
                result.groupAddress.each { groupShop.each { products.each { assertNull(purchaseProtectionPlanData) } } }
                assertNotNull(result.donation)
            }
        }

        Scenario("Disable donation") {

            Given("mock response") {
                every { repository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(apiResponseSAFDisableFeatureDonation, ShipmentAddressFormDataResponse::class.java))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should only have donation disabled") {
                val result = subscriber.onNextEvents[0]
                assertEquals(false, result.isDropshipperDisable)
                assertEquals(false, result.isMultipleDisable)
                assertEquals(false, result.isOrderPrioritasDisable)
                assertNotNull(result.egoldAttributes)
                assertNull(result.donation)
                result.groupAddress.each { groupShop.each { products.each { assertNotNull(purchaseProtectionPlanData) } } }
            }
        }

        Scenario("Disable all but not new buyer") {

            Given("mock response") {
                every { repository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(apiResponseSAFDisableFeatureAllOldBuyer, ShipmentAddressFormDataResponse::class.java))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should have no disabled features") {
                val result = subscriber.onNextEvents[0]
                assertEquals(false, result.isDropshipperDisable)
                assertEquals(false, result.isMultipleDisable)
                assertEquals(false, result.isOrderPrioritasDisable)
                assertNotNull(result.egoldAttributes)
                assertNull(result.donation)
                result.groupAddress.each { groupShop.each { products.each { assertNotNull(purchaseProtectionPlanData) } } }
            }
        }

        Scenario("Disable all") {

            Given("mock response") {
                every { repository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(apiResponseSAFDisableFeatureAllNewBuyer, ShipmentAddressFormDataResponse::class.java))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should have disabled features") {
                val result = subscriber.onNextEvents[0]
                assertEquals(true, result.isDropshipperDisable)
                assertEquals(true, result.isMultipleDisable)
                assertEquals(true, result.isOrderPrioritasDisable)
                assertNull(result.egoldAttributes)
                assertNull(result.donation)
                result.groupAddress.each { groupShop.each { products.each { assertNull(purchaseProtectionPlanData) } } }
            }
        }
    }

})

fun <T : Any> List<T>.each(action: T.() -> Unit) {
    for (item in this) {
        item.action()
    }
}