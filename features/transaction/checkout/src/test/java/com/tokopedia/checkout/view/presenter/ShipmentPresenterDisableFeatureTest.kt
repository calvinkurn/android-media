package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.UnitTestFileUtils
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse
import com.tokopedia.checkout.data.model.response.shipment_address_form.ShipmentAddressFormGqlResponse
import com.tokopedia.checkout.data.model.response.shipment_address_form.ShipmentAddressFormResponse
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.domain.usecase.saf.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.data.analytics.CodAnalytics
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.editaddress.domain.usecase.EditAddressUseCase
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.utils.each
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.lang.reflect.Type
import java.util.*

object ShipmentPresenterDisableFeatureTest : Spek({

    val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase = mockk()
    val compositeSubscription: CompositeSubscription = mockk(relaxed = true)
    val checkoutUseCase: CheckoutGqlUseCase = mockk()
    val graphqlUseCase: GraphqlUseCase = mockk(relaxUnitFun = true)
    val getShipmentAddressFormGqlUseCase = GetShipmentAddressFormGqlUseCase("", graphqlUseCase, ShipmentMapper(), TestSchedulers)
    val editAddressUseCase: EditAddressUseCase = mockk()
    val changeShippingAddressGqlUseCase: ChangeShippingAddressGqlUseCase = mockk()
    val saveShipmentStateGqlUseCase: SaveShipmentStateGqlUseCase = mockk()
    val codCheckoutUseCase: CodCheckoutUseCase = mockk()
    val getRatesUseCase: GetRatesUseCase = mockk()
    val getRatesApiUseCase: GetRatesApiUseCase = mockk()
    val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase = mockk()
    val submitHelpTicketUseCase: SubmitHelpTicketUseCase = mockk()
    val ratesStatesConverter: RatesResponseStateConverter = mockk()
    val shippingCourierConverter: ShippingCourierConverter = mockk()
    val userSessionInterface: UserSessionInterface = mockk(relaxed = true)
    val analyticsPurchaseProtection: CheckoutAnalyticsPurchaseProtection = mockk(relaxed = true)
    val codAnalytics: CodAnalytics = mockk()
    val checkoutAnalytics: CheckoutAnalyticsCourierSelection = mockk()
    val getInsuranceCartUseCase: GetInsuranceCartUseCase = mockk()
    val shipmentAnalyticsActionListener: ShipmentContract.AnalyticsActionListener = mockk()
    val shipmentDataConverter = ShipmentDataConverter()
    val releaseBookingUseCase: ReleaseBookingUseCase = mockk()

    val gson = Gson()
    val unitTestFileUtils = UnitTestFileUtils()

    RxAndroidPlugins.getInstance().reset()
    RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
        override fun getMainThreadScheduler(): Scheduler {
            return Schedulers.trampoline()
        }
    })
    RxJavaHooks.setOnIOScheduler { Schedulers.trampoline() }


    Feature("Disable Features") {

        val presenter by memoized {
            ShipmentPresenter(compositeSubscription,
                    checkoutUseCase, getShipmentAddressFormGqlUseCase,
                    editAddressUseCase, changeShippingAddressGqlUseCase,
                    saveShipmentStateGqlUseCase,
                    getRatesUseCase, getRatesApiUseCase,
                    codCheckoutUseCase, clearCacheAutoApplyStackUseCase, submitHelpTicketUseCase,
                    ratesStatesConverter, shippingCourierConverter, shipmentAnalyticsActionListener, userSessionInterface,
                    analyticsPurchaseProtection, codAnalytics, checkoutAnalytics,
                    getInsuranceCartUseCase, shipmentDataConverter, releaseBookingUseCase,
                    validateUsePromoRevampUseCase)
        }

        val view by memoized { mockk<ShipmentContract.View>(relaxed = true) }

        beforeEachTest {
            presenter.attachView(view)
        }

        Scenario("Disable Dropshipper") {

            Given("mock response") {
                val dataResponse = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_DROPSHIPPER), ShipmentAddressFormDataResponse::class.java)
                val result = hashMapOf<Type, Any>(
                        ShipmentAddressFormGqlResponse::class.java to ShipmentAddressFormGqlResponse(ShipmentAddressFormResponse(status = "OK", data = dataResponse))
                )
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false))
            }

            When("process initial load checkout page") {
                presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")
            }

            Then("should have dropshipper disable in all items") {
                presenter.shipmentCartItemModelList.each { assertEquals(true, isDropshipperDisable) }
            }

            Then("should not have multiple address disabled") {
                assertEquals(false, presenter.recipientAddressModel.isDisableMultipleAddress)
            }

            Then("should not have order prioritas disabled in all items") {
                presenter.shipmentCartItemModelList.each { assertEquals(false, isOrderPrioritasDisable) }
            }

            Then("should have egold attributes") {
                assertNotNull(presenter.egoldAttributeModel)
            }

            Then("should have purchase protection plan data in all items") {
                presenter.shipmentCartItemModelList.each { cartItemModels.each { assertEquals(true, isProtectionAvailable) } }
            }

            Then("should have donation") {
                assertNotNull(presenter.shipmentDonationModel)
            }
        }

        Scenario("Disable Multiple Address") {

            Given("mock response") {
                val dataResponse = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_MULTIPLE_ADDRESS), ShipmentAddressFormDataResponse::class.java)
                val result = hashMapOf<Type, Any>(
                        ShipmentAddressFormGqlResponse::class.java to ShipmentAddressFormGqlResponse(ShipmentAddressFormResponse(status = "OK", data = dataResponse))
                )
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false))
            }

            When("process initial load checkout page") {
                presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")
            }

            Then("should not have dropshipper disable in all items") {
                presenter.shipmentCartItemModelList.each { assertEquals(false, isDropshipperDisable) }
            }

            Then("should have multiple address disabled") {
                assertEquals(true, presenter.recipientAddressModel.isDisableMultipleAddress)
            }

            Then("should not have order prioritas disabled in all items") {
                presenter.shipmentCartItemModelList.each { assertEquals(false, isOrderPrioritasDisable) }
            }

            Then("should have egold attributes") {
                assertNotNull(presenter.egoldAttributeModel)
            }

            Then("should have purchase protection plan data in all items") {
                presenter.shipmentCartItemModelList.each { cartItemModels.each { assertEquals(true, isProtectionAvailable) } }
            }

            Then("should have donation") {
                assertNotNull(presenter.shipmentDonationModel)
            }
        }

        Scenario("Disable Order Prioritas") {

            Given("mock response") {
                val dataResponse = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_ORDER_PRIORITAS), ShipmentAddressFormDataResponse::class.java)
                val result = hashMapOf<Type, Any>(
                        ShipmentAddressFormGqlResponse::class.java to ShipmentAddressFormGqlResponse(ShipmentAddressFormResponse(status = "OK", data = dataResponse))
                )
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false))
            }

            When("process initial load checkout page") {
                presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")
            }

            Then("should not have dropshipper disable in all items") {
                presenter.shipmentCartItemModelList.each { assertEquals(false, isDropshipperDisable) }
            }

            Then("should not have multiple address disabled") {
                assertEquals(false, presenter.recipientAddressModel.isDisableMultipleAddress)
            }

            Then("should have order prioritas disabled in all items") {
                presenter.shipmentCartItemModelList.each { assertEquals(true, isOrderPrioritasDisable) }
            }

            Then("should have egold attributes") {
                assertNotNull(presenter.egoldAttributeModel)
            }

            Then("should have purchase protection plan data in all items") {
                presenter.shipmentCartItemModelList.each { cartItemModels.each { assertEquals(true, isProtectionAvailable) } }
            }

            Then("should have donation") {
                assertNotNull(presenter.shipmentDonationModel)
            }
        }

        Scenario("Disable Egold") {

            Given("mock response") {
                val dataResponse = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_EGOLD), ShipmentAddressFormDataResponse::class.java)
                val result = hashMapOf<Type, Any>(
                        ShipmentAddressFormGqlResponse::class.java to ShipmentAddressFormGqlResponse(ShipmentAddressFormResponse(status = "OK", data = dataResponse))
                )
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false))
            }

            When("process initial load checkout page") {
                presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")
            }

            Then("should not have dropshipper disable in all items") {
                presenter.shipmentCartItemModelList.each { assertEquals(false, isDropshipperDisable) }
            }

            Then("should not have multiple address disabled") {
                assertEquals(false, presenter.recipientAddressModel.isDisableMultipleAddress)
            }

            Then("should not have order prioritas disabled in all items") {
                presenter.shipmentCartItemModelList.each { assertEquals(false, isOrderPrioritasDisable) }
            }

            Then("should not have egold attributes") {
                assertNull(presenter.egoldAttributeModel)
            }

            Then("should have purchase protection plan data in all items") {
                presenter.shipmentCartItemModelList.each { cartItemModels.each { assertEquals(true, isProtectionAvailable) } }
            }

            Then("should have donation") {
                assertNotNull(presenter.shipmentDonationModel)
            }
        }

        Scenario("Disable PPP") {

            Given("mock response") {
                val dataResponse = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_PPP), ShipmentAddressFormDataResponse::class.java)
                val result = hashMapOf<Type, Any>(
                        ShipmentAddressFormGqlResponse::class.java to ShipmentAddressFormGqlResponse(ShipmentAddressFormResponse(status = "OK", data = dataResponse))
                )
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false))
            }

            When("process initial load checkout page") {
                presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")
            }

            Then("should not have dropshipper disable in all items") {
                presenter.shipmentCartItemModelList.each { assertEquals(false, isDropshipperDisable) }
            }

            Then("should not have multiple address disabled") {
                assertEquals(false, presenter.recipientAddressModel.isDisableMultipleAddress)
            }

            Then("should not have order prioritas disabled in all items") {
                presenter.shipmentCartItemModelList.each { assertEquals(false, isOrderPrioritasDisable) }
            }

            Then("should have egold attributes") {
                assertNotNull(presenter.egoldAttributeModel)
            }

            Then("should not have purchase protection plan data in all items") {
                presenter.shipmentCartItemModelList.each { cartItemModels.each { assertEquals(false, isProtectionAvailable) } }
            }

            Then("should have donation") {
                assertNotNull(presenter.shipmentDonationModel)
            }
        }

        Scenario("Disable Donation") {

            Given("mock response") {
                val dataResponse = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_DONATION), ShipmentAddressFormDataResponse::class.java)
                val result = hashMapOf<Type, Any>(
                        ShipmentAddressFormGqlResponse::class.java to ShipmentAddressFormGqlResponse(ShipmentAddressFormResponse(status = "OK", data = dataResponse))
                )
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false))
            }

            When("process initial load checkout page") {
                presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")
            }

            Then("should not have dropshipper disable in all items") {
                presenter.shipmentCartItemModelList.each { assertEquals(false, isDropshipperDisable) }
            }

            Then("should not have multiple address disabled") {
                assertEquals(false, presenter.recipientAddressModel.isDisableMultipleAddress)
            }

            Then("should not have order prioritas disabled in all items") {
                presenter.shipmentCartItemModelList.each { assertEquals(false, isOrderPrioritasDisable) }
            }

            Then("should have egold attributes") {
                assertNotNull(presenter.egoldAttributeModel)
            }

            Then("should have purchase protection plan data in all items") {
                presenter.shipmentCartItemModelList.each { cartItemModels.each { assertEquals(true, isProtectionAvailable) } }
            }

            Then("should not have donation") {
                assertNull(presenter.shipmentDonationModel)
            }
        }

        Scenario("Disable all") {

            Given("mock response") {
                val dataResponse = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_ALL), ShipmentAddressFormDataResponse::class.java)
                val result = hashMapOf<Type, Any>(
                        ShipmentAddressFormGqlResponse::class.java to ShipmentAddressFormGqlResponse(ShipmentAddressFormResponse(status = "OK", data = dataResponse))
                )
                every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false))
            }

            When("process initial load checkout page") {
                presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")
            }

            Then("should have dropshipper disable in all items") {
                presenter.shipmentCartItemModelList.each { assertEquals(true, isDropshipperDisable) }
            }

            Then("should have multiple address disabled") {
                assertEquals(true, presenter.recipientAddressModel.isDisableMultipleAddress)
            }

            Then("should have order prioritas disabled in all items") {
                presenter.shipmentCartItemModelList.each { assertEquals(true, isOrderPrioritasDisable) }
            }

            Then("should not have egold attributes") {
                assertNull(presenter.egoldAttributeModel)
            }

            Then("should not have purchase protection plan data in all items") {
                presenter.shipmentCartItemModelList.each { cartItemModels.each { assertEquals(false, isProtectionAvailable) } }
            }

            Then("should not have donation") {
                assertNull(presenter.shipmentDonationModel)
            }
        }
    }
})