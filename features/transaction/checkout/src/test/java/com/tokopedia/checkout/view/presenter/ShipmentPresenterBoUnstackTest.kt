package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase
import com.tokopedia.checkout.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV3UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterBoUnstackTest {

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
            compositeSubscription, checkoutUseCase, getShipmentAddressFormV3UseCase,
            editAddressUseCase, changeShippingAddressGqlUseCase, saveShipmentStateGqlUseCase,
            getRatesUseCase, getRatesApiUseCase, clearCacheAutoApplyStackUseCase,
            ratesStatesConverter, shippingCourierConverter,
            shipmentAnalyticsActionListener, userSessionInterface, analyticsPurchaseProtection,
            checkoutAnalytics, shipmentDataConverter, releaseBookingUseCase, prescriptionIdsUseCase,
            validateUsePromoRevampUseCase, gson, TestSchedulers, eligibleForAddressUseCase
        )
        presenter.attachView(view)
        presenter = spyk(presenter)
    }

    // Test doUnapplyBo()

    @Test
    fun `WHEN unapply BO promo item not found in list THEN don't do unapply BO promo`() {
        // Given
        val uniqueId = "111-222-333"
        val promoCode = "TESTBO"

        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns -1
        every { view.getShipmentCartItemModel(any()) } returns null
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true,
                    tickerMessage = ""
                )
            )
        )

        // When
        presenter.doUnapplyBo(uniqueId, promoCode)

        // Then
        verifyOrder {
            view.getShipmentCartItemModelAdapterPositionByUniqueId(any())
            view.getShipmentCartItemModel(any())
        }
        verifyOrder(inverse = true) {
            view.resetCourier(Mockito.anyInt())
            presenter.clearOrderPromoCodeFromLastValidateUseRequest(any(), any())
            view.onNeedUpdateViewItem(any())
        }
    }

    @Test
    fun `WHEN unapply BO promo item found in list THEN do unapply BO promo`() {
        // Given
        val uniqueId = "111-222-333"
        val promoCode = "TESTBO"

        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns 0
        every { view.getShipmentCartItemModel(any()) } returns ShipmentCartItemModel(
            cartString = "111-222-333",
            cartItemModels = listOf(CartItemModel()),
            shipmentCartData = ShipmentCartData(
                boMetadata = BoMetadata(boType = 1)
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true,
                    tickerMessage = ""
                )
            )
        )

        // When
        presenter.doUnapplyBo(uniqueId, promoCode)

        // Then
        verifyOrder {
            view.getShipmentCartItemModelAdapterPositionByUniqueId(any())
            view.getShipmentCartItemModel(any())
            view.resetCourier(Mockito.anyInt())
            presenter.clearOrderPromoCodeFromLastValidateUseRequest(any(), any())
            view.onNeedUpdateViewItem(any())
        }
    }

    // Test validateBoPromo()

    // Test validateBoPromo() => Test with BO orders (shippingId > 0 && spId > 0 && type == "logistic")

    @Test
    fun `WHEN validate BO promo has multiple red state orders THEN do unapply BO promo`() {
        // Given
        val shipmentCartItemModelList = listOf<ShipmentCartItemModel>()
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 2,
                        spId = 2,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                )
            )
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(exactly = 2) {
            presenter.doUnapplyBo(any(), any())
        }
        verify(inverse = true) {
            presenter.doApplyBo(any()) // validate doApplyBo() not called
        }
    }

    @Test
    fun `WHEN validate BO promo has multiple green state orders THEN do apply BO promo`() {
        // Given
        val shipmentCartItemModelList = listOf<ShipmentCartItemModel>()
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 2,
                        spId = 2,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                )
            )
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(exactly = 2) {
            presenter.doApplyBo(any())
        }
        verify(inverse = true) {
            presenter.doUnapplyBo(any(), any()) // validate doUnapplyBo() not called
        }
    }

    @Test
    fun `WHEN validate BO promo has multiple green and red state orders THEN do apply and unapply BO promo`() {
        // Given
        val shipmentCartItemModelList = listOf<ShipmentCartItemModel>()
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 2,
                        spId = 2,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 5,
                        spId = 5,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 6,
                        spId = 6,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 6,
                        spId = 6,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                )
            )
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(exactly = 2) {
            presenter.doApplyBo(any()) // validate doApplyBo() called 2 times
        }
        verify(exactly = 3) {
            presenter.doUnapplyBo(any(), any()) // validate doUnapplyBo() called 3 times
        }
    }

    // Test validateBoPromo() => Test with non BO orders (shippingId == 0 && spId == 0 && type != "logistic")

    @Test
    fun `WHEN validate BO promo has non BO orders THEN don't call apply and unapply BO promo`() {
        // Given
        val shipmentCartItemModelList = listOf<ShipmentCartItemModel>()
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 5,
                        spId = 5,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 1,
                        spId = 1,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                )
            )
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(inverse = true) {
            presenter.doApplyBo(any()) // validate doApplyBo() not called
            presenter.doUnapplyBo(any(), any()) // validate doUnapplyBo() not called
        }
    }

    @Test
    fun `WHEN validate BO promo has both BO orders and non BO orders THEN call apply and unapply BO promo`() {
        // Given
        val shipmentCartItemModelList = listOf<ShipmentCartItemModel>()
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    // valid green state
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 7,
                        spId = 7,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 5,
                        spId = 5,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 1,
                        spId = 1,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    // valid red state
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 6,
                        spId = 6,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    // valid green state
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 10,
                        spId = 10,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                )
            )
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(exactly = 2) {
            presenter.doApplyBo(any()) // validate doApplyBo() called twice
        }
        verify(exactly = 1) {
            presenter.doUnapplyBo(any(), any()) // validate doUnapplyBo() called once
        }
    }

    @Test
    fun `WHEN validate BO promo has empty orders THEN don't call apply and unapply BO promo`() {
        // Given
        val shipmentCartItemModelList = listOf<ShipmentCartItemModel>()
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf()
            )
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(inverse = true) {
            presenter.doApplyBo(any()) // validate doApplyBo() not called
            presenter.doUnapplyBo(any(), any()) // validate doUnapplyBo() not called
        }
    }

    // Test validateBoPromo() => Test reloadedUniqueIds

    @Test
    fun `WHEN validate BO promo has valid green or red orders THEN reloaded uniques ids should not be empty`() {
        // Given
        val shipmentCartItemModelList = listOf<ShipmentCartItemModel>()
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    // valid green state
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 7,
                        spId = 7,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 5,
                        spId = 5,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 1,
                        spId = 1,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    // valid red state
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 6,
                        spId = 6,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    // valid green state
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 10,
                        spId = 10,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                )
            )
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        val reloadedUniqueIds = presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        assert(reloadedUniqueIds.size == 3)
    }

    @Test
    fun `WHEN validate BO promo has invalid green or red orders THEN reloaded uniques ids should be empty`() {
        // Given
        val shipmentCartItemModelList = listOf<ShipmentCartItemModel>()
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 5,
                        spId = 5,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 1,
                        spId = 1,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                )
            )
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        val reloadedUniqueIds = presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        assert(reloadedUniqueIds.size == 0)
    }

    @Test
    fun `WHEN validate BO promo has empty orders THEN reloaded uniques ids should be empty`() {
        // Given
        val shipmentCartItemModelList = listOf<ShipmentCartItemModel>()
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf()
            )
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        val reloadedUniqueIds = presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        assert(reloadedUniqueIds.size == 0)
    }

    // Test validateBoPromo() => Test unprocessedUniqueIds

    @Test
    fun `WHEN validate BO promo with unprocessed shipment in cart THEN call do unapply BO promo()`() {
        // Given
        val shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartString = "111-111-111",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel()
            ),
            ShipmentCartItemModel(
                cartString = "222-222-222",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel()
            )
        )
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    // valid green state
                    PromoCheckoutVoucherOrdersItemUiModel(
                        uniqueId = "222-222-222",
                        shippingId = 7,
                        spId = 7,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        uniqueId = "111-111-111",
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        uniqueId = "444-444-444",
                        shippingId = 5,
                        spId = 5,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        uniqueId = "555-555-555",
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        uniqueId = "666-666-666",
                        shippingId = 1,
                        spId = 1,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    // valid red state
                    PromoCheckoutVoucherOrdersItemUiModel(
                        uniqueId = "777-777-777",
                        shippingId = 6,
                        spId = 6,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    // valid green state
                    PromoCheckoutVoucherOrdersItemUiModel(
                        uniqueId = "888-888-888",
                        shippingId = 10,
                        spId = 10,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                )
            )
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(exactly = 2) {
            presenter.doApplyBo(any())
        }
        verify(exactly = 2) {
            presenter.doUnapplyBo(any(), any())
        }
    }

    // Test validateBoPromo() => Test unappliedBoPromoUniqueIds (micro interaction)

    @Test
    fun `WHEN validate BO promo with valid green and red state THEN call renderUnapplyBoIncompleteShipment()`() {
        // Given
        val shipmentCartItemModelList = listOf<ShipmentCartItemModel>()
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    // valid green state
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 7,
                        spId = 7,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 5,
                        spId = 5,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 1,
                        spId = 1,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    // valid red state
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 6,
                        spId = 6,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    // valid green state
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 10,
                        spId = 10,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                )
            )
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs
        every { view.renderUnapplyBoIncompleteShipment(any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(exactly = 1) {
            view.renderUnapplyBoIncompleteShipment(any())
        }
    }

    @Test
    fun `WHEN validate BO promo with valid green state only THEN call renderUnapplyBoIncompleteShipment()`() {
        // Given
        val shipmentCartItemModelList = listOf<ShipmentCartItemModel>()
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    // valid green state
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 7,
                        spId = 7,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 5,
                        spId = 5,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 1,
                        spId = 1,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    // valid green state
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 10,
                        spId = 10,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                )
            )
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs
        every { view.renderUnapplyBoIncompleteShipment(any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(inverse = true) {
            view.renderUnapplyBoIncompleteShipment(any())
        }
    }

    @Test
    fun `WHEN validate BO promo with valid red state only THEN call renderUnapplyBoIncompleteShipment()`() {
        // Given
        val shipmentCartItemModelList = listOf<ShipmentCartItemModel>()
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 5,
                        spId = 5,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 0,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 1,
                        spId = 1,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    // valid red state
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 6,
                        spId = 6,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                )
            )
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs
        every { view.renderUnapplyBoIncompleteShipment(any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(exactly = 1) {
            view.renderUnapplyBoIncompleteShipment(any())
        }
    }

    @Test
    fun `WHEN validate BO promo with empty orders THEN don't call renderUnapplyBoIncompleteShipment()`() {
        // Given
        val shipmentCartItemModelList = listOf<ShipmentCartItemModel>()
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf()
            )
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs
        every { view.renderUnapplyBoIncompleteShipment(any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(inverse = true) {
            view.renderUnapplyBoIncompleteShipment(any())
        }
    }

    // Test validateClearAllBoPromo()

    @Test
    fun `WHEN clear BO promo while user has active BO promo in checkout page then user success to clear used BO promo THEN call unapply BO promo`() {
        // Given
        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartString = "111-222-333"
            voucherLogisticItemUiModel = VoucherLogisticItemUiModel(
                code = "TESTBO"
            )
        }
        presenter.shipmentCartItemModelList = listOf(
            shipmentCartItemModel
        )
        val lastValidateUseRequest = ValidateUsePromoRequest().apply {
            orders = listOf(
                OrdersItem().apply {
                    uniqueId = "111-222-333"
                    codes = mutableListOf()
                }
            )
        }
        presenter.setLatValidateUseRequest(lastValidateUseRequest)

        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns 0
        every { view.getShipmentCartItemModel(any()) } returns ShipmentCartItemModel(
            cartString = "111-222-333",
            cartItemModels = listOf(CartItemModel()),
            shipmentCartData = ShipmentCartData(
                boMetadata = BoMetadata(boType = 1)
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true,
                    tickerMessage = ""
                )
            )
        )

        // When
        presenter.validateClearAllBoPromo()

        // Then
        verify { presenter.doUnapplyBo(any(), any()) }
    }

    @Test
    fun `WHEN clear BO promo while user has active BO promo in checkout page then user failed to clear used BO promo THEN don't call unapply BO promo`() {
        // Given
        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartString = "111-222-333"
            voucherLogisticItemUiModel = VoucherLogisticItemUiModel(
                code = "TESTBO"
            )
        }
        presenter.shipmentCartItemModelList = listOf(
            shipmentCartItemModel
        )
        val lastValidateUseRequest = ValidateUsePromoRequest().apply {
            orders = listOf(
                OrdersItem().apply {
                    uniqueId = "111-222-333"
                    codes = mutableListOf(
                        "TESTBO"
                    )
                }
            )
        }
        presenter.setLatValidateUseRequest(lastValidateUseRequest)

        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true,
                    tickerMessage = ""
                )
            )
        )

        // When
        presenter.validateClearAllBoPromo()

        // Then
        verify(inverse = true) { presenter.doUnapplyBo(any(), any()) }
    }

    @Test
    fun `WHEN clear BO promo while user has no active BO promo in checkout page then clear all promo THEN don't call unapply BO promo`() {
        // Given
        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartString = "111-222-333"
            voucherLogisticItemUiModel = null
        }
        presenter.shipmentCartItemModelList = listOf(
            shipmentCartItemModel
        )
        val lastValidateUseRequest = ValidateUsePromoRequest().apply {
            orders = listOf(
                OrdersItem().apply {
                    uniqueId = "111-222-333"
                    codes = mutableListOf()
                }
            )
        }
        presenter.setLatValidateUseRequest(lastValidateUseRequest)

        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true,
                    tickerMessage = ""
                )
            )
        )

        // When
        presenter.validateClearAllBoPromo()

        // Then
        verify(inverse = true) { presenter.doUnapplyBo(any(), any()) }
    }

    @Test
    fun `WHEN clear BO promo while user has no active BO promo in checkout page then apply new promo THEN don't call unapply BO promo`() {
        // Given
        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartString = "111-222-333"
            voucherLogisticItemUiModel = null
        }
        presenter.shipmentCartItemModelList = listOf(
            shipmentCartItemModel
        )
        val lastValidateUseRequest = ValidateUsePromoRequest().apply {
            orders = listOf(
                OrdersItem().apply {
                    uniqueId = "111-222-333"
                    codes = mutableListOf(
                        "TESTBO",
                        "TESTNONBO"
                    )
                }
            )
        }
        presenter.setLatValidateUseRequest(lastValidateUseRequest)

        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true,
                    tickerMessage = ""
                )
            )
        )

        // When
        presenter.validateClearAllBoPromo()

        // Then
        verify(inverse = true) { presenter.doUnapplyBo(any(), any()) }
    }

    @Test
    fun `WHEN clear BO promo with cart item unique_id different from validateUse unique_id THEN don't call unapply BO promo`() {
        // Given
        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartString = "111-222-333"
            voucherLogisticItemUiModel = null
        }
        presenter.shipmentCartItemModelList = listOf(
            shipmentCartItemModel
        )
        val lastValidateUseRequest = ValidateUsePromoRequest().apply {
            orders = listOf(
                OrdersItem().apply {
                    uniqueId = "111-222-111"
                    codes = mutableListOf()
                }
            )
        }
        presenter.setLatValidateUseRequest(lastValidateUseRequest)

        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just runs
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true,
                    tickerMessage = ""
                )
            )
        )

        // When
        presenter.validateClearAllBoPromo()

        // Then
        verify(inverse = true) { presenter.doUnapplyBo(any(), any()) }
    }
}