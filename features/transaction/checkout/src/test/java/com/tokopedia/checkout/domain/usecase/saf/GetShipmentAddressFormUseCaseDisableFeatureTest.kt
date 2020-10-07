package com.tokopedia.checkout.domain.usecase.saf

import com.google.gson.Gson
import com.tokopedia.checkout.UnitTestFileUtils
import com.tokopedia.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormGqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.utils.each
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.observers.AssertableSubscriber
import java.lang.reflect.Type

const val PATH_JSON_SAF_DISABLE_DROPSHIPPER = "assets/saf_disable_dropshipper.json"
const val PATH_JSON_SAF_DISABLE_ORDER_PRIORITAS = "assets/saf_disable_order_prioritas.json"
const val PATH_JSON_SAF_DISABLE_EGOLD = "assets/saf_disable_egold.json"
const val PATH_JSON_SAF_DISABLE_PPP = "assets/saf_disable_ppp.json"
const val PATH_JSON_SAF_DISABLE_DONATION = "assets/saf_disable_donation.json"
const val PATH_JSON_SAF_DISABLE_ALL = "assets/saf_disable_all.json"

object GetShipmentAddressFormUseCaseDisableFeatureTest : Spek({

    val graphqlUseCase = mockk<GraphqlUseCase>(relaxed = true)
    val mapper = ShipmentMapper()
    val useCase by memoized { GetShipmentAddressFormGqlUseCase("mock_query", graphqlUseCase, mapper, TestSchedulers) }

    val gson = Gson()
    val unitTestFileUtils = UnitTestFileUtils()
    val param = RequestParams.EMPTY


    Feature("Disabled Features") {

        lateinit var subscriber: AssertableSubscriber<CartShipmentAddressFormData>

        Scenario("Disable Dropshipper") {

            val result = HashMap<Type, Any>()
            result[ShipmentAddressFormDataResponse::class.java] = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_DROPSHIPPER), ShipmentAddressFormDataResponse::class.java)
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            val resultData by lazy { mapper.convertToShipmentAddressFormData(result[ShipmentAddressFormDataResponse::class.java] as ShipmentAddressFormDataResponse) }

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should have dropshipper disabled") {
                assertEquals(true, resultData.isDropshipperDisable)
            }

            Then("should not have order prioritas disabled") {
                assertEquals(false, resultData.isOrderPrioritasDisable)
            }

            Then("should have egold attributes") {
                assertNotNull(resultData.egoldAttributes)
            }

            Then("should have purchase protection plan data") {
                resultData.groupAddress.each { groupShop.each { products.each { assertNotNull(purchaseProtectionPlanData) } } }
            }

            Then("should have donation") {
                assertNotNull(resultData.donation)
            }
        }

        Scenario("Disable Order Prioritas") {

            val result = HashMap<Type, Any>()
            result[ShipmentAddressFormDataResponse::class.java] = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_ORDER_PRIORITAS), ShipmentAddressFormDataResponse::class.java)
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            val resultData by lazy { mapper.convertToShipmentAddressFormData(result[ShipmentAddressFormDataResponse::class.java] as ShipmentAddressFormDataResponse) }

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should not have dropshipper disabled") {
                assertEquals(false, resultData.isDropshipperDisable)
            }

            Then("should have order prioritas disabled") {
                assertEquals(true, resultData.isOrderPrioritasDisable)
            }

            Then("should have egold attributes") {
                assertNotNull(resultData.egoldAttributes)
            }

            Then("should have purchase protection plan data") {
                resultData.groupAddress.each { groupShop.each { products.each { assertNotNull(purchaseProtectionPlanData) } } }
            }

            Then("should have donation") {
                assertNotNull(resultData.donation)
            }
        }

        Scenario("Disable Egold") {

            val result = HashMap<Type, Any>()
            result[ShipmentAddressFormDataResponse::class.java] = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_EGOLD), ShipmentAddressFormDataResponse::class.java)
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            val resultData by lazy { mapper.convertToShipmentAddressFormData(result[ShipmentAddressFormDataResponse::class.java] as ShipmentAddressFormDataResponse) }

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should not have dropshipper disabled") {
                assertEquals(false, resultData.isDropshipperDisable)
            }

            Then("should not have order prioritas disabled") {
                assertEquals(false, resultData.isOrderPrioritasDisable)
            }

            Then("should not have egold attributes") {
                assertNull(resultData.egoldAttributes)
            }

            Then("should have purchase protection plan data") {
                resultData.groupAddress.each { groupShop.each { products.each { assertNotNull(purchaseProtectionPlanData) } } }
            }

            Then("should have donation") {
                assertNotNull(resultData.donation)
            }
        }

        Scenario("Disable PPP") {

            val result = HashMap<Type, Any>()
            result[ShipmentAddressFormDataResponse::class.java] = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_PPP), ShipmentAddressFormDataResponse::class.java)
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            val resultData by lazy { mapper.convertToShipmentAddressFormData(result[ShipmentAddressFormDataResponse::class.java] as ShipmentAddressFormDataResponse) }

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should not have dropshipper disabled") {
                assertEquals(false, resultData.isDropshipperDisable)
            }

            Then("should not have order prioritas disabled") {
                assertEquals(false, resultData.isOrderPrioritasDisable)
            }

            Then("should have egold attributes") {
                assertNotNull(resultData.egoldAttributes)
            }

            Then("should not have purchase protection plan data") {
                resultData.groupAddress.each { groupShop.each { products.each { assertNull(purchaseProtectionPlanData) } } }
            }

            Then("should have donation") {
                assertNotNull(resultData.donation)
            }
        }

        Scenario("Disable Donation") {

            val result = HashMap<Type, Any>()
            result[ShipmentAddressFormDataResponse::class.java] = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_DONATION), ShipmentAddressFormDataResponse::class.java)
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            val resultData by lazy { mapper.convertToShipmentAddressFormData(result[ShipmentAddressFormDataResponse::class.java] as ShipmentAddressFormDataResponse) }

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should not have dropshipper disabled") {
                assertEquals(false, resultData.isDropshipperDisable)
            }

            Then("should not have order prioritas disabled") {
                assertEquals(false, resultData.isOrderPrioritasDisable)
            }

            Then("should have egold attributes") {
                assertNotNull(resultData.egoldAttributes)
            }

            Then("should have purchase protection plan data") {
                resultData.groupAddress.each { groupShop.each { products.each { assertNotNull(purchaseProtectionPlanData) } } }
            }

            Then("should not have donation") {
                assertNull(resultData.donation)
            }
        }

        Scenario("Disable all") {

            val result = HashMap<Type, Any>()
            result[ShipmentAddressFormDataResponse::class.java] = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_ALL), ShipmentAddressFormDataResponse::class.java)
            val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

            val resultData by lazy { mapper.convertToShipmentAddressFormData(result[ShipmentAddressFormDataResponse::class.java] as ShipmentAddressFormDataResponse) }

            Given("mock response") {
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
            }

            When("create observable") {
                subscriber = useCase.createObservable(param).test()
            }

            Then("should have dropshipper disabled") {
                assertEquals(true, resultData.isDropshipperDisable)
            }

            Then("should have order prioritas disabled") {
                assertEquals(true, resultData.isOrderPrioritasDisable)
            }

            Then("should not have egold attributes") {
                assertNull(resultData.egoldAttributes)
            }

            Then("should not have purchase protection plan data") {
                resultData.groupAddress.each { groupShop.each { products.each { assertNull(purchaseProtectionPlanData) } } }
            }

            Then("should not have donation") {
                assertNull(resultData.donation)
            }
        }
    }

})
