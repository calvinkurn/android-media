package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.model.changeaddress.SetShippingAddressData
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.entity.address.LocationDataModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.domain.UpdateDynamicDataPassingUseCase
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
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
    private lateinit var validateUsePromoRevampUseCase: OldValidateUsePromoRevampUseCase

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
    private lateinit var getRatesWithScheduleUseCase: GetRatesWithScheduleUseCase

    @MockK(relaxed = true)
    private lateinit var clearCacheAutoApplyStackUseCase: OldClearCacheAutoApplyStackUseCase

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

    @MockK
    private lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

    @MockK
    private lateinit var prescriptionIdsUseCase: GetPrescriptionIdsUseCase

    @MockK
    private lateinit var epharmacyUseCase: EPharmacyPrepareProductsGroupUseCase

    @MockK
    private lateinit var updateDynamicDataPassingUseCase: UpdateDynamicDataPassingUseCase

    @MockK(relaxed = true)
    private lateinit var dynamicPaymentFeeCheckoutUseCase: GetPaymentFeeCheckoutUseCase

    @MockK(relaxed = true)
    private lateinit var view: ShipmentContract.View

    @MockK(relaxed = true)
    private lateinit var getShipmentAddressFormV3UseCase: GetShipmentAddressFormV3UseCase

    private var shipmentDataConverter = ShipmentDataConverter()

    private lateinit var presenter: ShipmentPresenter

    private var gson = Gson()

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = ShipmentPresenter(
            compositeSubscription,
            checkoutUseCase,
            getShipmentAddressFormV3UseCase,
            editAddressUseCase,
            changeShippingAddressGqlUseCase,
            saveShipmentStateGqlUseCase,
            getRatesUseCase,
            getRatesApiUseCase,
            clearCacheAutoApplyStackUseCase,
            ratesStatesConverter,
            shippingCourierConverter,
            shipmentAnalyticsActionListener,
            userSessionInterface,
            analyticsPurchaseProtection,
            checkoutAnalytics,
            shipmentDataConverter,
            releaseBookingUseCase,
            prescriptionIdsUseCase,
            epharmacyUseCase,
            validateUsePromoRevampUseCase,
            gson,
            TestSchedulers,
            eligibleForAddressUseCase,
            getRatesWithScheduleUseCase,
            updateDynamicDataPassingUseCase,
            dynamicPaymentFeeCheckoutUseCase
        )
        presenter.attachView(view)
    }

    @Test
    fun `WHEN change shipping address for normal checkout flow success THEN should render success`() {
        // Given
        val chosenAddress = ChosenAddressModel(addressId = 123)
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
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.just(
            SetShippingAddressData(isSuccess = true)
        )

        // When
        presenter.changeShippingAddress(
            recipientAddressModel,
            chosenAddress,
            false,
            false,
            true,
            true
        )

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
        val chosenAddress = ChosenAddressModel(addressId = 123)
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.just(
            SetShippingAddressData(isSuccess = false)
        )

        // When
        presenter.changeShippingAddress(
            RecipientAddressModel(),
            chosenAddress,
            false,
            false,
            true,
            true
        )

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
        val chosenAddress = ChosenAddressModel(addressId = 123)
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.error(
            Throwable()
        )

        // When
        presenter.changeShippingAddress(
            RecipientAddressModel(),
            chosenAddress,
            false,
            false,
            true,
            true
        )

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
        val chosenAddress = null
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
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.just(
            SetShippingAddressData(isSuccess = true)
        )

        // When
        presenter.changeShippingAddress(
            recipientAddressModel,
            chosenAddress,
            false,
            true,
            true,
            true
        )

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
        val chosenAddress = ChosenAddressModel(addressId = 123)
        val errorMessages = ArrayList<String>().apply {
            add("Error Message")
        }
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.just(
            SetShippingAddressData(isSuccess = false, messages = errorMessages)
        )

        // When
        presenter.changeShippingAddress(
            RecipientAddressModel(),
            chosenAddress,
            false,
            false,
            true,
            true
        )

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

    @Test
    fun `WHEN change shipping address get akamai error THEN should show error message`() {
        // Given
        val chosenAddress = ChosenAddressModel(addressId = 123)
        every { changeShippingAddressGqlUseCase.createObservable(any()) } returns Observable.error(
            AkamaiErrorException("error")
        )

        // When
        presenter.changeShippingAddress(
            RecipientAddressModel(),
            chosenAddress,
            false,
            false,
            true,
            true
        )

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
