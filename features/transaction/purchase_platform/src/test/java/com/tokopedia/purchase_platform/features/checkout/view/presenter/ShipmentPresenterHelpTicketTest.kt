package com.tokopedia.purchase_platform.features.checkout.view.presenter

import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.data.analytics.CodAnalytics
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.data.api.CommonPurchaseApiUrl
import com.tokopedia.purchase_platform.common.data.model.request.checkout.DataCheckoutRequest
import com.tokopedia.purchase_platform.common.data.model.request.helpticket.SubmitHelpTicketRequest
import com.tokopedia.purchase_platform.common.domain.model.CheckoutData
import com.tokopedia.purchase_platform.common.domain.model.ErrorReporter
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.sharedata.helpticket.SubmitTicketResult
import com.tokopedia.purchase_platform.common.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.features.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.purchase_platform.features.checkout.data.repository.ICheckoutRepository
import com.tokopedia.purchase_platform.features.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.*
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentContract
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentPresenter
import com.tokopedia.purchase_platform.features.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.purchase_platform.features.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

object ShipmentPresenterHelpTicketTest : Spek({

    val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase = mockk()
    val compositeSubscription: CompositeSubscription = mockk(relaxed = true)
    val checkoutUseCase: CheckoutUseCase = mockk()
    val checkoutRepository: ICheckoutRepository = mockk()
    val getShipmentAddressFormUseCase = GetShipmentAddressFormUseCase(checkoutRepository, ShipmentMapper())
    val getShipmentAddressFormOneClickShipementUseCase: GetShipmentAddressFormOneClickShipementUseCase = mockk()
    val editAddressUseCase: EditAddressUseCase = mockk()
    val changeShippingAddressUseCase: ChangeShippingAddressUseCase = mockk()
    val saveShipmentStateUseCase: SaveShipmentStateUseCase = mockk()
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

    RxAndroidPlugins.getInstance().reset()
    RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
        override fun getMainThreadScheduler(): Scheduler {
            return Schedulers.trampoline()
        }
    })
    RxJavaHooks.setOnIOScheduler { Schedulers.trampoline() }

    Feature("Submit Help Ticket") {

        val presenter by memoized {
            ShipmentPresenter(compositeSubscription,
                    checkoutUseCase, getShipmentAddressFormUseCase,
                    getShipmentAddressFormOneClickShipementUseCase,
                    editAddressUseCase, changeShippingAddressUseCase,
                    saveShipmentStateUseCase, getRatesUseCase, getRatesApiUseCase,
                    codCheckoutUseCase, clearCacheAutoApplyStackUseCase, submitHelpTicketUseCase,
                    ratesStatesConverter, shippingCourierConverter, shipmentAnalyticsActionListener,
                    userSessionInterface, analyticsPurchaseProtection, codAnalytics,
                    checkoutAnalytics, getInsuranceCartUseCase, shipmentDataConverter,
                    releaseBookingUseCase, validateUsePromoRevampUseCase)
        }

        val view by memoized { mockk<ShipmentContract.View>(relaxed = true) }

        beforeEachTest {
            presenter.attachView(view)
        }

        Scenario("Show Error Reporter Dialog") {

            val data = CheckoutData().apply {
                isError = true
                errorReporter = ErrorReporter(eligible = true)
            }

            Given("mock response") {
                every { checkoutUseCase.createObservable(any()) } returns Observable.just(data)
            }

            Given("mock cart") {
                presenter.shipmentCartItemModelList = emptyList()
                presenter.setDataCheckoutRequestList(listOf(DataCheckoutRequest()))
            }

            When("process checkout") {
                presenter.processCheckout(false, false, false, false, "", "", "")
            }

            Then("should render error reporter dialog") {
                verify(exactly = 1) {
                    view.renderCheckoutCartErrorReporter(data)
                }
            }
        }

        Scenario("Submit Help Ticket Success") {

            val result = SubmitTicketResult(status = true)

            Given("mock response") {
                every {
                    submitHelpTicketUseCase.createObservable(match {
                        val request = it.getObject(SubmitHelpTicketUseCase.PARAM) as SubmitHelpTicketRequest
                        request.page == SubmitHelpTicketUseCase.PAGE_CHECKOUT && request.requestUrl == CommonPurchaseApiUrl.PATH_CHECKOUT
                    })
                } returns Observable.just(result)
            }

            When("process checkout") {
                presenter.processSubmitHelpTicket(CheckoutData().apply {
                    jsonResponse = ""
                    errorMessage = ""
                    errorReporter = ErrorReporter()
                })
            }

            Then("should render success submit ticket dialog") {
                verify(exactly = 1) {
                    view.renderSubmitHelpTicketSuccess(result)
                }
            }
        }

        Scenario("Submit Help Ticket Fail") {

            val responseErrorMessage = "something wrong"

            Given("mock response") {
                every {
                    submitHelpTicketUseCase.createObservable(match {
                        val request = it.getObject(SubmitHelpTicketUseCase.PARAM) as SubmitHelpTicketRequest
                        request.page == SubmitHelpTicketUseCase.PAGE_CHECKOUT && request.requestUrl == CommonPurchaseApiUrl.PATH_CHECKOUT
                    })
                } returns Observable.just(SubmitTicketResult(status = false, message = responseErrorMessage))
            }

            When("process checkout") {
                presenter.processSubmitHelpTicket(CheckoutData().apply {
                    jsonResponse = ""
                    errorMessage = ""
                    errorReporter = ErrorReporter()
                })
            }

            Then("should show toast error") {
                verify(exactly = 1) {
                    view.showToastError(responseErrorMessage)
                }
            }
        }

        Scenario("Submit Help Ticket Unexpected Fail") {

            Given("mock response") {
                every {
                    submitHelpTicketUseCase.createObservable(match {
                        val request = it.getObject(SubmitHelpTicketUseCase.PARAM) as SubmitHelpTicketRequest
                        request.page == SubmitHelpTicketUseCase.PAGE_CHECKOUT && request.requestUrl == CommonPurchaseApiUrl.PATH_CHECKOUT
                    })
                } returns Observable.error(Exception())
            }

            When("process checkout") {
                presenter.processSubmitHelpTicket(CheckoutData().apply {
                    jsonResponse = ""
                    errorMessage = ""
                    errorReporter = ErrorReporter()
                })
            }

            Then("should show toast error") {
                verify(exactly = 1) {
                    view.showToastError(any())
                }
            }
        }
    }
})