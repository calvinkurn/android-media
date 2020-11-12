package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.model.changeaddress.SetShippingAddressData
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.data.analytics.CodAnalytics
import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticdata.domain.usecase.EditAddressUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterChangeShippingAddressTest {

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

    @MockK
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
                validateUsePromoRevampUseCase, TestSchedulers)
        presenter.attachView(view)
    }

    @Test
    fun changeShippingAddressSuccess_ShouldRenderSuccess() {
        // Given
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.just(SetShippingAddressData.Builder()
                .success(true)
                .build())

        // When
        presenter.changeShippingAddress(RecipientAddressModel(), false, false, false)

        // Then
        verifySequence {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.activityContext
            view.showToastNormal(any())
            view.renderChangeAddressSuccess()
        }
    }

    @Test
    fun changeShippingAddressFailed_ShouldShowError() {
        // Given
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.just(SetShippingAddressData.Builder()
                .success(false)
                .build())

        // When
        presenter.changeShippingAddress(RecipientAddressModel(), false, false, false)

        // Then
        verifySequence {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.activityContext
            view.showToastError(any())
        }
    }

    @Test
    fun changeShippingAddressError_ShouldShowError() {
        // Given
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.error(Throwable())

        // When
        presenter.changeShippingAddress(RecipientAddressModel(), false, false, false)

        // Then
        verifySequence {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.activityContext
            view.showToastError(any())
        }
    }
}