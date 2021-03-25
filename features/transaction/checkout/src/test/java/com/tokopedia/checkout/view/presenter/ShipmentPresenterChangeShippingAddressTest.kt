package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.model.changeaddress.SetShippingAddressData
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.data.entity.address.LocationDataModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
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
    fun changeShippingAddressSuccess_ShouldRenderSuccess() {
        // Given
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        presenter.shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>().apply {
            add(
                    ShipmentCartItemModel().apply {
                        cartItemModels = ArrayList<CartItemModel>().apply {
                            add(
                                    CartItemModel().apply {
                                        quantity = 1
                                        productId = 1
                                        noteToSeller = "note"
                                        cartId = 123
                                    }
                            )
                        }
                    }
            )
        }
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.just(SetShippingAddressData(isSuccess = true))

        // When
        presenter.changeShippingAddress(recipientAddressModel, null, false, false, true, true)

        // Then
        verifySequence {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.activityContext
            view.showToastNormal(any())
            view.renderChangeAddressSuccess(true)
        }
    }

    @Test
    fun changeShippingAddressFailed_ShouldShowError() {
        // Given
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.just(SetShippingAddressData(isSuccess = false))

        // When
        presenter.changeShippingAddress(RecipientAddressModel(), null, false, false, true, true)

        // Then
        verifySequence {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.activityContext
            view.showToastError(any())
            view.renderChangeAddressFailed(any())
        }
    }

    @Test
    fun changeShippingAddressError_ShouldShowError() {
        // Given
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.error(Throwable())

        // When
        presenter.changeShippingAddress(RecipientAddressModel(), null, false, false, true, true)

        // Then
        verifySequence {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.activityContext
            view.showToastError(any())
            view.renderChangeAddressFailed(any())
        }
    }

    @Test
    fun `WHEN change address for trade in indopaket flow success THEN should render success`() {
        // Given
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
            locationDataModel = LocationDataModel().apply {
                addrId = "1"
            }
        }
        presenter.shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>().apply {
            add(
                    ShipmentCartItemModel().apply {
                        cartItemModels = ArrayList<CartItemModel>().apply {
                            add(
                                    CartItemModel().apply {
                                        quantity = 1
                                        productId = 1
                                        noteToSeller = "note"
                                        cartId = 123
                                    }
                            )
                        }
                    }
            )
        }
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.just(SetShippingAddressData(isSuccess = true))

        // When
        presenter.changeShippingAddress(recipientAddressModel, null, false, true, true, true)

        // Then
        verifySequence {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.activityContext
            view.showToastNormal(any())
            view.renderChangeAddressSuccess(true)
        }
    }

    @Test
    fun `WHEN change shipping address error with error message THEN should show error message`() {
        // Given
        val errorMessages = ArrayList<String>().apply {
            add("Error Message")
        }
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.just(SetShippingAddressData(isSuccess = false, messages = errorMessages))

        // When
        presenter.changeShippingAddress(RecipientAddressModel(), null, false, false, true, true)

        // Then
        verifySequence {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.showToastError(any())
            view.renderChangeAddressFailed(any())
        }
    }

}