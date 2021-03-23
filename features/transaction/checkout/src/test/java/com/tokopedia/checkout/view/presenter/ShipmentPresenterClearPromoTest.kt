package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.promocheckout.common.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterClearPromoTest {

    @MockK
    private lateinit var validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase

    @MockK(relaxed = true)
    private lateinit var compositeSubscription: CompositeSubscription

    @MockK
    private lateinit var checkoutUseCase: CheckoutGqlUseCase

    @MockK
    private lateinit var editAddressUseCase: EditAddressUseCase

    @MockK
    private lateinit var changeShippingAddressGqlUseCase: ChangeShippingAddressGqlUseCase

    @MockK
    private lateinit var saveShipmentStateGqlUseCase: SaveShipmentStateGqlUseCase

    @MockK
    private lateinit var getRatesUseCase: GetRatesUseCase

    @MockK
    private lateinit var getRatesApiUseCase: GetRatesApiUseCase

    @MockK
    private lateinit var clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase

    @MockK
    private lateinit var submitHelpTicketUseCase: SubmitHelpTicketUseCase

    @MockK
    private lateinit var ratesStatesConverter: RatesResponseStateConverter

    @MockK
    private lateinit var shippingCourierConverter: ShippingCourierConverter

    @MockK(relaxed = true)
    private lateinit var userSessionInterface: UserSessionInterface

    @MockK(relaxed = true)
    private lateinit var analyticsPurchaseProtection: CheckoutAnalyticsPurchaseProtection

    @MockK(relaxed = true)
    private lateinit var checkoutAnalytics: CheckoutAnalyticsCourierSelection

    @MockK(relaxed = true)
    private lateinit var shipmentAnalyticsActionListener: ShipmentContract.AnalyticsActionListener

    @MockK
    private lateinit var releaseBookingUseCase: ReleaseBookingUseCase

    @MockK(relaxed = true)
    private lateinit var view: ShipmentContract.View

    @MockK(relaxed = true)
    private lateinit var getShipmentAddressFormGqlUseCase: GetShipmentAddressFormGqlUseCase

    private var shipmentDataConverter = ShipmentDataConverter()

    private lateinit var presenter: ShipmentPresenter

    private var gson = Gson()

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = ShipmentPresenter(
                compositeSubscription, checkoutUseCase, getShipmentAddressFormGqlUseCase,
                editAddressUseCase, changeShippingAddressGqlUseCase, saveShipmentStateGqlUseCase,
                getRatesUseCase, getRatesApiUseCase, clearCacheAutoApplyStackUseCase,
                submitHelpTicketUseCase, ratesStatesConverter, shippingCourierConverter,
                shipmentAnalyticsActionListener, userSessionInterface, analyticsPurchaseProtection,
                checkoutAnalytics, shipmentDataConverter, releaseBookingUseCase,
                validateUsePromoRevampUseCase, gson, TestSchedulers)
        presenter.attachView(view)
    }

    @Test
    fun `WHEN clear BBO promo success THEN should render success`() {
        // Given
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
                ClearPromoUiModel(
                        successDataModel = SuccessDataUiModel(
                                success = true
                        )
                )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just Runs

        // When
        presenter.cancelAutoApplyPromoStackLogistic(0, "code")

        // Then
        verify {
            view.onSuccessClearPromoLogistic(0, any())
        }
    }

    @Test
    fun `WHEN clear BBO promo success with ticker data THEN should render success and update ticker`() {
        // Given
        presenter.tickerAnnouncementHolderData = TickerAnnouncementHolderData("0", "message")

        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
                ClearPromoUiModel(
                        successDataModel = SuccessDataUiModel(
                                success = true,
                                tickerMessage = "message"
                        )
                )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just Runs

        // When
        presenter.cancelAutoApplyPromoStackLogistic(0, "code")

        // Then
        verifySequence {
            view.updateTickerAnnouncementMessage()
            view.onSuccessClearPromoLogistic(0, any())
        }
    }

    @Test
    fun `WHEN clear BBO promo and it's last applied promo THEN should render success and flag last applied promo is true`() {
        // Given
        val promoCode = "code"
        presenter.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
                promoUiModel = PromoUiModel(
                        voucherOrderUiModels = ArrayList<PromoCheckoutVoucherOrdersItemUiModel>().apply {
                            add(
                                    PromoCheckoutVoucherOrdersItemUiModel(promoCode)
                            )
                        }
                )
        )

        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
                ClearPromoUiModel(
                        successDataModel = SuccessDataUiModel(
                                success = true
                        )
                )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just Runs

        // When
        presenter.cancelAutoApplyPromoStackLogistic(0, promoCode)

        // Then
        verifySequence {
            view.onSuccessClearPromoLogistic(0, true)
        }
    }

    @Test
    fun `WHEN clear BBO promo and still has applied merchant promo THEN should render success and flag last applied promo is false`() {
        // Given
        val promoCode = "code"
        presenter.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
                promoUiModel = PromoUiModel(
                        voucherOrderUiModels = ArrayList<PromoCheckoutVoucherOrdersItemUiModel>().apply {
                            add(PromoCheckoutVoucherOrdersItemUiModel(promoCode))
                            add(PromoCheckoutVoucherOrdersItemUiModel("promoCode"))
                        }
                )
        )

        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
                ClearPromoUiModel(
                        successDataModel = SuccessDataUiModel(
                                success = true
                        )
                )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just Runs

        // When
        presenter.cancelAutoApplyPromoStackLogistic(0, promoCode)

        // Then
        verifySequence {
            view.onSuccessClearPromoLogistic(0, false)
        }
    }

    @Test
    fun `WHEN clear BBO promo and still has applied global promo THEN should render success and flag last applied promo is false`() {
        // Given
        val promoCode = "code"
        presenter.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
                promoUiModel = PromoUiModel(
                        codes = ArrayList<String>().apply {
                            add("promo code")
                        }
                )
        )

        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
                ClearPromoUiModel(
                        successDataModel = SuccessDataUiModel(
                                success = true
                        )
                )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just Runs

        // When
        presenter.cancelAutoApplyPromoStackLogistic(0, promoCode)

        // Then
        verifySequence {
            view.onSuccessClearPromoLogistic(0, false)
        }
    }

    @Test
    fun `WHEN clear non eligible promo success THEN should render success`() {
        // Given
        val notEligilePromoList = ArrayList<NotEligiblePromoHolderdata>().apply {
            add(NotEligiblePromoHolderdata(promoCode = "code"))
        }
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
                ClearPromoUiModel(
                        successDataModel = SuccessDataUiModel(
                                success = true
                        )
                )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just Runs

        // When
        presenter.cancelNotEligiblePromo(notEligilePromoList)

        // Then
        verify {
            view.removeIneligiblePromo(notEligilePromoList)
        }
    }

}