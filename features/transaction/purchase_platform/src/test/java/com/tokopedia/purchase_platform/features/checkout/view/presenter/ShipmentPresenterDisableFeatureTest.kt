package com.tokopedia.purchase_platform.features.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.usecase.GetCourierRecommendationUseCase
import com.tokopedia.logisticdata.data.analytics.CodAnalytics
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeFinalUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.purchase_platform.*
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.features.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse
import com.tokopedia.purchase_platform.features.checkout.data.repository.ICheckoutRepository
import com.tokopedia.purchase_platform.features.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.*
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.saf.each
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentContract
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentPresenter
import com.tokopedia.purchase_platform.features.checkout.view.converter.ShipmentDataConverter
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

object ShipmentPresenterDisableFeatureTest : Spek({

    val checkPromoStackingCodeFinalUseCase: CheckPromoStackingCodeFinalUseCase = mockk()
    val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase = mockk()
    val checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper = mockk()
    val compositeSubscription: CompositeSubscription = mockk(relaxed = true)
    val checkoutUseCase: CheckoutUseCase = mockk()
    val checkoutRepository: ICheckoutRepository = mockk()
    val getShipmentAddressFormUseCase = GetShipmentAddressFormUseCase(checkoutRepository, ShipmentMapper())
    val getShipmentAddressFormOneClickShipementUseCase: GetShipmentAddressFormOneClickShipementUseCase = mockk()
    val editAddressUseCase: EditAddressUseCase = mockk()
    val changeShippingAddressUseCase: ChangeShippingAddressUseCase = mockk()
    val saveShipmentStateUseCase: SaveShipmentStateUseCase = mockk()
    val codCheckoutUseCase: CodCheckoutUseCase = mockk()
    val getCourierRecommendationUseCase: GetCourierRecommendationUseCase = mockk()
    val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase = mockk()
    val submitHelpTicketUseCase: SubmitHelpTicketUseCase = mockk()
    val shippingCourierConverter: ShippingCourierConverter = mockk()
    val userSessionInterface: UserSessionInterface = mockk(relaxed = true)
    val analyticsPurchaseProtection: CheckoutAnalyticsPurchaseProtection = mockk(relaxed = true)
    val codAnalytics: CodAnalytics = mockk()
    val checkoutAnalytics: CheckoutAnalyticsCourierSelection = mockk()
    val getInsuranceCartUseCase: GetInsuranceCartUseCase = mockk()
    val shipmentAnalyticsActionListener: ShipmentContract.AnalyticsActionListener = mockk()
    val shipmentDataConverter = ShipmentDataConverter()

    val gson = Gson()

    RxAndroidPlugins.getInstance().reset()
    RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
        override fun getMainThreadScheduler(): Scheduler {
            return Schedulers.trampoline()
        }
    })
    RxJavaHooks.setOnIOScheduler { Schedulers.trampoline() }


    Feature("Disable Features") {

        val presenter by memoized {
            ShipmentPresenter(checkPromoStackingCodeFinalUseCase,
                    checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, compositeSubscription,
                    checkoutUseCase, getShipmentAddressFormUseCase,
                    getShipmentAddressFormOneClickShipementUseCase,
                    editAddressUseCase, changeShippingAddressUseCase,
                    saveShipmentStateUseCase, getCourierRecommendationUseCase,
                    codCheckoutUseCase, clearCacheAutoApplyStackUseCase, submitHelpTicketUseCase, shippingCourierConverter,
                    shipmentAnalyticsActionListener, userSessionInterface,
                    analyticsPurchaseProtection, codAnalytics, checkoutAnalytics, getInsuranceCartUseCase)
        }

        val view by memoized { mockk<ShipmentContract.View>(relaxed = true) }

        beforeEachTest {
            every { view.shipmentDataConverter } returns shipmentDataConverter
            presenter.attachView(view)
        }

        Scenario("Disable Dropshipper") {

            Given("mock response") {
                every { checkoutRepository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(apiResponseSAFDisableFeatureDropshipper, ShipmentAddressFormDataResponse::class.java))
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
                every { checkoutRepository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(apiResponseSAFDisableFeatureMultipleAddress, ShipmentAddressFormDataResponse::class.java))
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
                every { checkoutRepository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(apiResponseSAFDisableFeatureOrderPrioritas, ShipmentAddressFormDataResponse::class.java))
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
                every { checkoutRepository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(apiResponseSAFDisableFeatureEGold, ShipmentAddressFormDataResponse::class.java))
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
                every { checkoutRepository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(apiResponseSAFDisableFeaturePPP, ShipmentAddressFormDataResponse::class.java))
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
                every { checkoutRepository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(apiResponseSAFDisableFeatureDonation, ShipmentAddressFormDataResponse::class.java))
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
                every { checkoutRepository.getShipmentAddressForm(any()) } returns Observable.just(gson.fromJson(apiResponseSAFDisableFeatureAllNewBuyer, ShipmentAddressFormDataResponse::class.java))
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