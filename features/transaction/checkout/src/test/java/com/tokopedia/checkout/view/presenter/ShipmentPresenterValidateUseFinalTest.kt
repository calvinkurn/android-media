package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.*
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterValidateUseFinalTest {

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
    fun `WHEN validate use success THEN should update promo button`() {
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
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verify {
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success but has error message on additional info THEN should update promo button and show error message`() {
        // Given
        val message = "error"
        val promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                        PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", messageUiModel = MessageUiModel(state = "green"))
                ),
                additionalInfoUiModel = AdditionalInfoUiModel(
                        errorDetailUiModel = ErrorDetailUiModel(
                                message = message
                        )
                )
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        promoUiModel = promoUiModel
                )
        )

        // When
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifyOrder {
            view.showToastError(message)
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success but has error BBO THEN should update promo button and show error message`() {
        // Given
        val message = "error"
        val tmpCartString = "123-abc"
        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartString = tmpCartString
        }
        presenter.shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>().apply {
            add(shipmentCartItemModel)
        }
        val promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                        PromoCheckoutVoucherOrdersItemUiModel(
                                type = "logistic",
                                messageUiModel = MessageUiModel(state = "red", text = message),
                                uniqueId = tmpCartString
                        )
                )
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        promoUiModel = promoUiModel
                )
        )

        // When
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifyOrder {
            view.showToastError(message)
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success but has error multiple applied BBO THEN should update promo button and show error message`() {
        // Given
        val tmpCartString = "123-abc"
        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartString = tmpCartString
        }
        presenter.shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>().apply {
            add(shipmentCartItemModel)
        }
        val message = "error"
        val promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                        PromoCheckoutVoucherOrdersItemUiModel(
                                type = "logistic",
                                messageUiModel = MessageUiModel(state = "red", text = message),
                                uniqueId = tmpCartString)
                )
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        promoUiModel = promoUiModel
                )
        )

        // When
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifyOrder {
            view.showToastError(message)
            view.resetCourier(shipmentCartItemModel)
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success with ticker data and current ticker not exist THEN should update promo button and update ticker`() {
        // Given
        presenter.tickerAnnouncementHolderData = null
        val tickerMessage = "ticker message"
        val tickerStatusCode = "1"
        val promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                        PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", messageUiModel = MessageUiModel(state = "green"))
                ),
                tickerInfoUiModel = TickerInfoUiModel(statusCode = tickerStatusCode.toInt(), message = tickerMessage)
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        promoUiModel = promoUiModel
                )
        )

        // When
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verify {
            view.updateTickerAnnouncementMessage();
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(tickerStatusCode);
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success with ticker data and current ticker exist THEN should update promo button and update ticker`() {
        // Given
        presenter.tickerAnnouncementHolderData = TickerAnnouncementHolderData(id = "0", message = "")
        val tickerMessage = "ticker message"
        val tickerStatusCode = "1"
        val promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                        PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", messageUiModel = MessageUiModel(state = "green"))
                ),
                tickerInfoUiModel = TickerInfoUiModel(statusCode = tickerStatusCode.toInt(), message = tickerMessage)
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        promoUiModel = promoUiModel
                )
        )

        // When
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verify {
            view.updateTickerAnnouncementMessage();
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(tickerStatusCode);
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success and has mvc data THEN should update promo button and reload rates`() {
        // Given
        val lastSelectedCourierOrderIndex = 1
        val cartString = "123-abc"
        val promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                        PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", messageUiModel = MessageUiModel(state = "green"))
                ),
                additionalInfoUiModel = AdditionalInfoUiModel(
                        promoSpIds = ArrayList<PromoSpIdUiModel>().apply {
                            add(
                                    PromoSpIdUiModel(
                                            uniqueId = cartString
                                    )
                            )
                        }
                )
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        promoUiModel = promoUiModel
                )
        )

        // When
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), lastSelectedCourierOrderIndex, cartString)

        // Then
        verify {
            view.prepareReloadRates(lastSelectedCourierOrderIndex, false)
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success and has mvc data but uniqueId not matched THEN should not reload rates`() {
        // Given
        val lastSelectedCourierOrderIndex = 1
        val cartString = "123-abc"
        val promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                        PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", messageUiModel = MessageUiModel(state = "green"))
                ),
                additionalInfoUiModel = AdditionalInfoUiModel(
                        promoSpIds = ArrayList<PromoSpIdUiModel>().apply {
                            add(
                                    PromoSpIdUiModel(
                                            uniqueId = "other cartString"
                                    )
                            )
                        }
                )
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        promoUiModel = promoUiModel
                )
        )

        // When
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), lastSelectedCourierOrderIndex, cartString)

        // Then
        verify(inverse = true) {
            view.prepareReloadRates(lastSelectedCourierOrderIndex, false)
        }
    }

    @Test
    fun `WHEN validate use success and clashing THEN should update promo button and reload rates`() {
        // Given
        val promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                        PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", messageUiModel = MessageUiModel(state = "green"))
                ),
                clashingInfoDetailUiModel = ClashingInfoDetailUiModel(
                        clashMessage = "clash message",
                        clashReason = "clash reason",
                        options = ArrayList<PromoClashOptionUiModel>().apply {
                            add(
                                    PromoClashOptionUiModel(
                                            voucherOrders = ArrayList<PromoClashVoucherOrdersUiModel>().apply {
                                                add(PromoClashVoucherOrdersUiModel(code = "123"))
                                            }
                                    )
                            )
                        }
                )
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        promoUiModel = promoUiModel
                )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(ClearPromoUiModel())

        // When
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifySequence {
            view.updateButtonPromoCheckout(promoUiModel, false)
            view.showLoading();
            view.setHasRunningApiCall(true);
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.showToastNormal("Ada perubahan pada promo yang kamu pakai")
        }
    }

    @Test
    fun `WHEN validate use status get error THEN should render error and reset promo benefit`() {
        // Given
        val message = "error"
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "ERROR",
                        message = listOf(message),
                        promoUiModel = PromoUiModel()
                )
        )

        // When
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifySequence {
            view.renderErrorCheckPromoShipmentData(message)
            view.resetPromoBenefit()
            view.cancelAllCourierPromo()
        }
    }

    @Test
    fun `WHEN validate use status get exception THEN should render error`() {
        // Given
        val message = "error"
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(ResponseErrorException())

        // When
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifySequence {
            view.activityContext
            view.renderErrorCheckPromoShipmentData(any())
        }
    }

    @Test
    fun `WHEN validate use status get akamai exception THEN should show error and reset courier and clear promo`() {
        // Given
        val validateUsePromoRequest = ValidateUsePromoRequest().apply {
            codes = mutableListOf("a", "b")
            orders = mutableListOf(
                    OrdersItem().apply {
                        codes = mutableListOf("c")
                    }
            )
        }
        presenter.setLatValidateUseRequest(validateUsePromoRequest)
        val message = "error"
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(AkamaiErrorException(message))
        every { clearCacheAutoApplyStackUseCase.setParams(any(), any()) } just Runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(ClearPromoUiModel())

        // When
        presenter.checkPromoCheckoutFinalShipment(validateUsePromoRequest, 0, "")

        // Then
        verifySequence {
            view.showToastError(message)
            view.resetAllCourier()
            view.cancelAllCourierPromo()
            view.doResetButtonPromoCheckout()
        }
    }

}