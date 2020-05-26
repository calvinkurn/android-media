package com.tokopedia.checkout.domain.usecase.saf

import com.google.gson.Gson
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.checkout.UnitTestFileUtils
import com.tokopedia.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse
import com.tokopedia.checkout.data.repository.ICheckoutRepository
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.purchase_platform.common.utils.each
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.observers.AssertableSubscriber

const val PATH_JSON_SAF_DISABLE_DROPSHIPPER = "assets/saf_disable_dropshipper.json"
const val PATH_JSON_SAF_DISABLE_MULTIPLE_ADDRESS = "assets/saf_disable_multiple_address.json"
const val PATH_JSON_SAF_DISABLE_ORDER_PRIORITAS = "assets/saf_disable_order_prioritas.json"
const val PATH_JSON_SAF_DISABLE_EGOLD = "assets/saf_disable_egold.json"
const val PATH_JSON_SAF_DISABLE_PPP = "assets/saf_disable_ppp.json"
const val PATH_JSON_SAF_DISABLE_DONATION = "assets/saf_disable_donation.json"
const val PATH_JSON_SAF_DISABLE_ALL = "assets/saf_disable_all.json"

object GetShipmentAddressFormUseCaseDisableFeatureTest : Spek({

    val repository = mockk<ICheckoutRepository>()
    val mapper = ShipmentMapper()
    val useCase by memoized { GetShipmentAddressFormUseCase(repository, mapper) }

    val gson = Gson()
    val unitTestFileUtils = UnitTestFileUtils()
    val param = RequestParams.create().apply {
        putObject(GetShipmentAddressFormUseCase.PARAM_REQUEST_AUTH_MAP_STRING_GET_SHIPMENT_ADDRESS, TKPDMapParam<String, String>())
    }


    Feature("Disabled Features") {

        lateinit var subscriber: AssertableSubscriber<CartShipmentAddressFormData>

        Scenario("Disable Dropshipper") {

            val result by lazy { subscriber.onNextEvents[0] }

            Given("mock response") {
                every { repository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_DROPSHIPPER), ShipmentAddressFormDataResponse::class.java))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should have dropshipper disabled") {
                assertEquals(true, result.isDropshipperDisable)
            }

            Then("should not have multiple address disabled") {
                assertEquals(false, result.isMultipleDisable)
            }

            Then("should not have order prioritas disabled") {
                assertEquals(false, result.isOrderPrioritasDisable)
            }

            Then("should have egold attributes") {
                assertNotNull(result.egoldAttributes)
            }

            Then("should have purchase protection plan data") {
                result.groupAddress.each { groupShop.each { products.each { assertNotNull(purchaseProtectionPlanData) } } }
            }

            Then("should have donation") {
                assertNotNull(result.donation)
            }
        }

        Scenario("Disable Multiple Address") {

            val result by lazy { subscriber.onNextEvents[0] }

            Given("mock response") {
                every { repository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_MULTIPLE_ADDRESS), ShipmentAddressFormDataResponse::class.java))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should not have dropshipper disabled") {
                assertEquals(false, result.isDropshipperDisable)
            }

            Then("should have multiple address disabled") {
                assertEquals(true, result.isMultipleDisable)
            }

            Then("should not have order prioritas disabled") {
                assertEquals(false, result.isOrderPrioritasDisable)
            }

            Then("should have egold attributes") {
                assertNotNull(result.egoldAttributes)
            }

            Then("should have purchase protection plan data") {
                result.groupAddress.each { groupShop.each { products.each { assertNotNull(purchaseProtectionPlanData) } } }
            }

            Then("should have donation") {
                assertNotNull(result.donation)
            }
        }

        Scenario("Disable Order Prioritas") {

            val result by lazy { subscriber.onNextEvents[0] }

            Given("mock response") {
                every { repository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_ORDER_PRIORITAS), ShipmentAddressFormDataResponse::class.java))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should not have dropshipper disabled") {
                assertEquals(false, result.isDropshipperDisable)
            }

            Then("should not have multiple address disabled") {
                assertEquals(false, result.isMultipleDisable)
            }

            Then("should have order prioritas disabled") {
                assertEquals(true, result.isOrderPrioritasDisable)
            }

            Then("should have egold attributes") {
                assertNotNull(result.egoldAttributes)
            }

            Then("should have purchase protection plan data") {
                result.groupAddress.each { groupShop.each { products.each { assertNotNull(purchaseProtectionPlanData) } } }
            }

            Then("should have donation") {
                assertNotNull(result.donation)
            }
        }

        Scenario("Disable Egold") {

            val result by lazy { subscriber.onNextEvents[0] }

            Given("mock response") {
                every { repository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_EGOLD), ShipmentAddressFormDataResponse::class.java))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should not have dropshipper disabled") {
                assertEquals(false, result.isDropshipperDisable)
            }

            Then("should not have multiple address disabled") {
                assertEquals(false, result.isMultipleDisable)
            }

            Then("should not have order prioritas disabled") {
                assertEquals(false, result.isOrderPrioritasDisable)
            }

            Then("should not have egold attributes") {
                assertNull(result.egoldAttributes)
            }

            Then("should have purchase protection plan data") {
                result.groupAddress.each { groupShop.each { products.each { assertNotNull(purchaseProtectionPlanData) } } }
            }

            Then("should have donation") {
                assertNotNull(result.donation)
            }
        }

        Scenario("Disable PPP") {

            val result by lazy { subscriber.onNextEvents[0] }

            Given("mock response") {
                every { repository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_PPP), ShipmentAddressFormDataResponse::class.java))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should not have dropshipper disabled") {
                assertEquals(false, result.isDropshipperDisable)
            }

            Then("should not have multiple address disabled") {
                assertEquals(false, result.isMultipleDisable)
            }

            Then("should not have order prioritas disabled") {
                assertEquals(false, result.isOrderPrioritasDisable)
            }

            Then("should have egold attributes") {
                assertNotNull(result.egoldAttributes)
            }

            Then("should not have purchase protection plan data") {
                result.groupAddress.each { groupShop.each { products.each { assertNull(purchaseProtectionPlanData) } } }
            }

            Then("should have donation") {
                assertNotNull(result.donation)
            }
        }

        Scenario("Disable Donation") {

            val result by lazy { subscriber.onNextEvents[0] }

            Given("mock response") {
                every { repository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_DONATION), ShipmentAddressFormDataResponse::class.java))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should not have dropshipper disabled") {
                assertEquals(false, result.isDropshipperDisable)
            }

            Then("should not have multiple address disabled") {
                assertEquals(false, result.isMultipleDisable)
            }

            Then("should not have order prioritas disabled") {
                assertEquals(false, result.isOrderPrioritasDisable)
            }

            Then("should have egold attributes") {
                assertNotNull(result.egoldAttributes)
            }

            Then("should have purchase protection plan data") {
                result.groupAddress.each { groupShop.each { products.each { assertNotNull(purchaseProtectionPlanData) } } }
            }

            Then("should not have donation") {
                assertNull(result.donation)
            }
        }

        Scenario("Disable all") {

            val result by lazy { subscriber.onNextEvents[0] }

            Given("mock response") {
                every { repository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_ALL), ShipmentAddressFormDataResponse::class.java))
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should have dropshipper disabled") {
                assertEquals(true, result.isDropshipperDisable)
            }

            Then("should have multiple address disabled") {
                assertEquals(true, result.isMultipleDisable)
            }

            Then("should have order prioritas disabled") {
                assertEquals(true, result.isOrderPrioritasDisable)
            }

            Then("should not have egold attributes") {
                assertNull(result.egoldAttributes)
            }

            Then("should not have purchase protection plan data") {
                result.groupAddress.each { groupShop.each { products.each { assertNull(purchaseProtectionPlanData) } } }
            }

            Then("should not have donation") {
                assertNull(result.donation)
            }
        }
    }

})
