package com.tokopedia.checkout.view.presenter

import android.app.Activity
import com.google.gson.Gson
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV3UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterValidateUseCourierPromoTest {

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

    @MockK
    private lateinit var clearCacheAutoApplyStackUseCase: OldClearCacheAutoApplyStackUseCase

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
    private lateinit var getShipmentAddressFormV3UseCase: GetShipmentAddressFormV3UseCase

    @MockK
    private lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

    @MockK
    private lateinit var prescriptionIdsUseCase: GetPrescriptionIdsUseCase

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
            validateUsePromoRevampUseCase,
            gson,
            TestSchedulers,
            eligibleForAddressUseCase,
            getRatesWithScheduleUseCase
        )
        presenter.attachView(view)
    }

    @Test
    fun `WHEN validate use success THEN should render promo from courier`() {
        // Given
        val validateUseModel = ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = PromoUiModel(
                        globalSuccess = true,
                        voucherOrderUiModels = listOf(
                                PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", messageUiModel = MessageUiModel(state = "green"))
                        )
                )
        )
        val position = 0
        val noToast = true
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(validateUseModel)

        // When
        presenter.processCheckPromoCheckoutCodeFromSelectedCourier("code", position, noToast)

        // Then
        verify {
            view.renderPromoCheckoutFromCourierSuccess(validateUseModel, position, noToast)
        }
    }

    @Test
    fun `WHEN validate use get red state THEN should  show error and reset courier`() {
        // Given
        val errorMessage = "error"
        val cartString = "cart123"
        val validateUseModel = ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = PromoUiModel(
                        globalSuccess = true,
                        voucherOrderUiModels = listOf(
                                PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", uniqueId = cartString, messageUiModel = MessageUiModel(state = "red", text = errorMessage))
                        )
                )
        )
        val position = 0
        val noToast = true

        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            this.cartString = cartString
        }
        presenter.shipmentCartItemModelList = listOf(shipmentCartItemModel)
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(validateUseModel)
        every { view.generateValidateUsePromoRequest() } returns ValidateUsePromoRequest()

        // When
        presenter.processCheckPromoCheckoutCodeFromSelectedCourier("code", position, noToast)

        // Then
        verifySequence {
            view.generateValidateUsePromoRequest()
            view.showToastError(errorMessage)
            view.resetCourier(shipmentCartItemModel)
            view.renderPromoCheckoutFromCourierSuccess(validateUseModel, position, noToast)
        }
    }

    @Test
    fun `WHEN validate use failed THEN should render error`() {
        // Given
        val errorMessage = "error"
        val validateUseModel = ValidateUsePromoRevampUiModel(
                status = "ERROR",
                message = listOf(errorMessage)
        )
        val position = 0
        val noToast = true
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(validateUseModel)

        // When
        presenter.processCheckPromoCheckoutCodeFromSelectedCourier("code", position, noToast)

        // Then
        verify {
            view.renderErrorCheckPromoShipmentData(errorMessage)
        }
    }

    @Test
    fun `WHEN validate use failed without error message THEN should render default error message`() {
        // Given
        val validateUseModel = ValidateUsePromoRevampUiModel(
                status = "ERROR",
                message = emptyList()
        )
        val position = 0
        val noToast = true
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(validateUseModel)

        // When
        presenter.processCheckPromoCheckoutCodeFromSelectedCourier("code", position, noToast)

        // Then
        verify {
            view.renderErrorCheckPromoShipmentData(DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO)
        }
    }

    @Test
    fun `WHEN validate use failed with exception THEN should show error`() {
        // Given
        val errorMessage = "error"
        val position = 0
        val noToast = true

        val exception = ResponseErrorException()
        val mockContext = mockk<Activity>()
        mockkObject(ErrorHandler.Companion)
        every { view.activityContext } returns mockContext
        every { ErrorHandler.Companion.getErrorMessage(any(), any(), any()) } returns errorMessage
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(exception)

        // When
        presenter.processCheckPromoCheckoutCodeFromSelectedCourier("code", position, noToast)

        // Then
        verify {
            view.showToastError(errorMessage)
        }
    }

    @Test
    fun `WHEN validate use failed with akamai exception THEN should show error`() {
        // Given
        val errorMessage = "error"
        val position = 0
        val noToast = true

        val exception = AkamaiErrorException(errorMessage)
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(exception)
        every { view.generateValidateUsePromoRequest() } returns ValidateUsePromoRequest()

        // When
        presenter.processCheckPromoCheckoutCodeFromSelectedCourier("code", position, noToast)

        // Then
        verifySequence {
            view.generateValidateUsePromoRequest()
            view.showToastError(errorMessage)
            view.resetAllCourier()
            view.cancelAllCourierPromo()
            view.doResetButtonPromoCheckout()
        }
    }

}
