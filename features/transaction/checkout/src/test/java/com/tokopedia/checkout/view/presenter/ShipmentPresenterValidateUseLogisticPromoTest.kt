package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticCommon.data.analytics.CodAnalytics
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterValidateUseLogisticPromoTest {

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
    private lateinit var codCheckoutUseCase: CodCheckoutUseCase

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

    @MockK
    private lateinit var codAnalytics: CodAnalytics

    @MockK(relaxed = true)
    private lateinit var checkoutAnalytics: CheckoutAnalyticsCourierSelection

    @MockK
    private lateinit var getInsuranceCartUseCase: GetInsuranceCartUseCase

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
        presenter = ShipmentPresenter(compositeSubscription,
                checkoutUseCase, getShipmentAddressFormGqlUseCase,
                editAddressUseCase, changeShippingAddressGqlUseCase,
                saveShipmentStateGqlUseCase,
                getRatesUseCase, getRatesApiUseCase,
                codCheckoutUseCase, clearCacheAutoApplyStackUseCase, submitHelpTicketUseCase,
                ratesStatesConverter, shippingCourierConverter, shipmentAnalyticsActionListener, userSessionInterface,
                analyticsPurchaseProtection, codAnalytics, checkoutAnalytics,
                getInsuranceCartUseCase, shipmentDataConverter, releaseBookingUseCase,
                validateUsePromoRevampUseCase, gson, TestSchedulers)
        presenter.attachView(view)
    }

    @Test
    fun validateUseSuccess_ShouldUpdateTickerAndButtonPromo() {
        // Given
        val promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                        PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", messageUiModel = MessageUiModel(state = "green"))
                )
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        promoUiModel = promoUiModel
                )
        )

        // When
        presenter.doValidateuseLogisticPromo(0, "", ValidateUsePromoRequest())

        // Then
        verify {
            view.updateButtonPromoCheckout(promoUiModel, true)
        }
    }

    @Test
    fun validateUseRedState_ShouldShowErrorAndResetCourier() {
        // Given
        val errorMessage = "error"
        val cartString = "cart123"
        val promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                        PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", uniqueId = cartString, messageUiModel = MessageUiModel(state = "red", text = errorMessage))
                )
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        promoUiModel = promoUiModel
                )
        )

        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            this.cartString = cartString
        }
        presenter.shipmentCartItemModelList = listOf(shipmentCartItemModel)

        // When
        val cartPosition = 0
        presenter.doValidateuseLogisticPromo(cartPosition, "", ValidateUsePromoRequest())

        // Then
        verifySequence {
            view.showToastError(errorMessage)
            view.resetCourier(shipmentCartItemModel)
            view.updateButtonPromoCheckout(promoUiModel, true)
        }
    }

    @Test
    fun validateUseError_ShouldShowErrorAndResetCourier() {
        // Given
        val errorMessage = "error"
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(Throwable(errorMessage))

        // When
        val cartPosition = 0
        presenter.doValidateuseLogisticPromo(cartPosition, "", ValidateUsePromoRequest())

        // Then
        verifySequence {
            checkoutAnalytics.eventClickLanjutkanTerapkanPromoError(errorMessage)
            view.showToastError(errorMessage)
            view.resetCourier(cartPosition)
        }
    }
}