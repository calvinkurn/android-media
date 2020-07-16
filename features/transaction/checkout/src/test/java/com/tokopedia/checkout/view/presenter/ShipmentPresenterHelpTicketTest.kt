package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.data.api.CommonPurchaseApiUrl
import com.tokopedia.checkout.domain.model.checkout.CheckoutData
import com.tokopedia.checkout.domain.model.checkout.ErrorReporter
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.data.analytics.CodAnalytics
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.checkout.request.DataCheckoutRequest
import com.tokopedia.purchase_platform.common.feature.editaddress.domain.usecase.EditAddressUseCase
import com.tokopedia.purchase_platform.common.feature.helpticket.data.request.SubmitHelpTicketRequest
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.model.SubmitTicketResult
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
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
    val checkoutUseCase: CheckoutGqlUseCase = mockk()
    val getShipmentAddressFormGqlUseCase: GetShipmentAddressFormGqlUseCase = mockk()
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
                    checkoutUseCase, getShipmentAddressFormGqlUseCase,
                    editAddressUseCase, changeShippingAddressGqlUseCase,
                    saveShipmentStateGqlUseCase, getRatesUseCase, getRatesApiUseCase,
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

            When("process submit help ticket") {
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

            When("process submit help ticket") {
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

            When("process submit help ticket") {
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