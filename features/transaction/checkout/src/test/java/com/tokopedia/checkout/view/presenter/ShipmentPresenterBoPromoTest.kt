package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV3UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
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
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterBoPromoTest {

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

    private var shipmentMapper = ShipmentMapper()
    private var shipmentDataConverter = ShipmentDataConverter()
    private var shippingDurationConverter = ShippingDurationConverter()

    private lateinit var presenter: ShipmentPresenter

    private var gson = Gson()

    @Before
    fun before() {
        MockKAnnotations.init(this)
        ratesStatesConverter = RatesResponseStateConverter()
        shippingCourierConverter = ShippingCourierConverter()
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

    // Test ShipmentPresenter.getCartDataForRates()

    @Test
    fun `WHEN initialize presenter THEN cart data should be filled`() {
        // Given
        val response = DataProvider.provideShipmentAddressFormResponse()
        val cartShipmentAddressFormData = shipmentMapper.convertToShipmentAddressFormData(response.shipmentAddressFormResponse.data)

        // When
        presenter.initializePresenterData(cartShipmentAddressFormData)

        // Then
        assert(presenter.cartDataForRates == cartShipmentAddressFormData.cartData)
    }

    // Test ShipmentPresenter.doApplyBo()

    @Test
    fun `WHEN apply BO promo cart item found with no promo code THEN do apply BO`() {
        // Given
        val voucherOrder = PromoCheckoutVoucherOrdersItemUiModel(
            uniqueId = "111-111-111"
        )
        val itemAdapterPosition = 0
        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns itemAdapterPosition
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartString = "111-111-111",
            shopShipmentList = listOf(),
            voucherLogisticItemUiModel = null
        )
        every { view.getShipmentCartItemModel(any()) } returns shipmentCartItemModel
        every { presenter.processBoPromoCourierRecommendation(any(), any(), any()) } just runs

        // When
        presenter.doApplyBo(voucherOrder)

        // Then
        verify {
            presenter.processBoPromoCourierRecommendation(itemAdapterPosition, voucherOrder, shipmentCartItemModel)
        }
    }

    @Test
    fun `WHEN apply BO promo cart item found with different promo code THEN do apply BO`() {
        // Given
        val voucherOrder = PromoCheckoutVoucherOrdersItemUiModel(
            uniqueId = "111-111-111",
            code = "BOCODE"
        )
        val itemAdapterPosition = 0
        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns itemAdapterPosition
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartString = "111-111-111",
            shopShipmentList = listOf(),
            voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "PROMOCODE")
        )
        every { view.getShipmentCartItemModel(any()) } returns shipmentCartItemModel
        every { presenter.processBoPromoCourierRecommendation(any(), any(), any()) } just runs

        // When
        presenter.doApplyBo(voucherOrder)

        // Then
        verify {
            presenter.processBoPromoCourierRecommendation(itemAdapterPosition, voucherOrder, shipmentCartItemModel)
        }
    }

    @Test
    fun `WHEN apply BO promo cart item found with same promo code THEN don't do apply BO`() {
        // Given
        val voucherOrder = PromoCheckoutVoucherOrdersItemUiModel(
            uniqueId = "111-111-111",
            code = "BOCODE"
        )
        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns 0
        every { view.getShipmentCartItemModel(any()) } returns ShipmentCartItemModel(
            cartString = "111-111-111",
            shopShipmentList = listOf(),
            voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "BOCODE")
        )
        every { presenter.processBoPromoCourierRecommendation(any(), any(), any()) } just runs

        // When
        presenter.doApplyBo(voucherOrder)

        // Then
        verify(inverse = true) {
            presenter.processBoPromoCourierRecommendation(any(), any(), any())
        }
    }

    @Test
    fun `WHEN apply BO promo item not found THEN don't apply BO`() {
        // Given
        val voucherOrder = PromoCheckoutVoucherOrdersItemUiModel()
        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns -1
        every { view.getShipmentCartItemModel(any()) } returns null
        every { presenter.processBoPromoCourierRecommendation(any(), any(), any()) } just runs

        // When
        presenter.doApplyBo(voucherOrder)

        // Then
        verify(inverse = true) {
            presenter.processBoPromoCourierRecommendation(any(), any(), any())
        }
    }

    @Test
    fun `WHEN apply BO cart item found with invalid position THEN don't do apply BO`() {
        // Given
        val voucherOrder = PromoCheckoutVoucherOrdersItemUiModel()
        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns -1
        every { view.getShipmentCartItemModel(any()) } returns ShipmentCartItemModel()
        every { presenter.processBoPromoCourierRecommendation(any(), any(), any()) } just runs

        // When
        presenter.doApplyBo(voucherOrder)

        // Then
        verify(inverse = true) {
            presenter.processBoPromoCourierRecommendation(any(), any(), any())
        }
    }

    // Test ShipmentPresenter.doUnapplyBo()

    @Test
    fun `WHEN unapply BO promo cart item found THEN do unapply BO promo`() {
        // Given
        val uniqueId = "111-111-111"
        val promoCode = "BOCODE"

        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns 0
        every { view.getShipmentCartItemModel(any()) } returns ShipmentCartItemModel(
            cartString = "111-111-111",
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
            view.resetCourier(any<Int>())
            presenter.clearOrderPromoCodeFromLastValidateUseRequest(any(), any())
            view.onNeedUpdateViewItem(any())
        }
    }

    @Test
    fun `WHEN unapply BO promo cart item not found THEN don't do unapply BO promo`() {
        // Given
        val uniqueId = "111-111-111"
        val promoCode = "BOCODE"

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
        verify(inverse = true) {
            view.resetCourier(any<Int>())
            presenter.clearOrderPromoCodeFromLastValidateUseRequest(any(), any())
            view.onNeedUpdateViewItem(any())
        }
    }

    @Test
    fun `WHEN unapply BO promo item found null THEN don't do unapply BO promo`() {
        // Given
        val uniqueId = "111-111-111"
        val promoCode = "BOCODE"

        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns 0
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
        verify(inverse = true) {
            view.resetCourier(any<Int>())
            presenter.clearOrderPromoCodeFromLastValidateUseRequest(any(), any())
            view.onNeedUpdateViewItem(any())
        }
    }

    @Test
    fun `WHEN unapply BO promo item found with invalid position THEN don't do unapply BO promo`() {
        // Given
        val uniqueId = "111-111-111"
        val promoCode = "BOCODE"

        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns -1
        every { view.getShipmentCartItemModel(any()) } returns ShipmentCartItemModel()
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
        verify(inverse = true) {
            view.resetCourier(any<Int>())
            presenter.clearOrderPromoCodeFromLastValidateUseRequest(any(), any())
            view.onNeedUpdateViewItem(any())
        }
    }

    // Test ShipmentPresenter.validateClearAllBoPromo()

    @Test
    fun `WHEN clear BO promo while user has active BO promo in checkout page and user success to clear used BO promo THEN call unapply BO promo`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartString = "111-111-111"
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(
                    code = "TESTBO1"
                )
            },
            ShipmentCartItemModel().apply {
                cartString = "222-222-222"
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(
                    code = "TESTBO2"
                )
            }
        )
        val lastValidateUseRequest = ValidateUsePromoRequest().apply {
            orders = listOf(
                OrdersItem().apply {
                    uniqueId = "111-111-111"
                    codes = mutableListOf()
                },
                OrdersItem().apply {
                    uniqueId = "222-222-222"
                    codes = mutableListOf()
                }
            )
        }
        presenter.setLatValidateUseRequest(lastValidateUseRequest)

        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateClearAllBoPromo()

        // Then
        verify(exactly = 2) {
            presenter.doUnapplyBo(
                or("111-111-111", "222-222-222"),
                or("TESTBO1", "TESTBO2")
            )
        }
    }

    @Test
    fun `WHEN clear BO promo while user has active BO promo in checkout page and user failed to clear used BO promo THEN don't call unapply BO promo`() {
        // Given
        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartString = "111-111-111"
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
                    uniqueId = "111-111-111"
                    codes = mutableListOf("TESTBO")
                }
            )
        }
        presenter.setLatValidateUseRequest(lastValidateUseRequest)

        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateClearAllBoPromo()

        // Then
        verify(inverse = true) {
            presenter.doUnapplyBo(any(), any())
        }
    }

    @Test
    fun `WHEN clear BO promo while user has no active BO promo in checkout page then clear all promo THEN don't call unapply BO promo`() {
        // Given
        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartString = "111-111-111"
            voucherLogisticItemUiModel = null
        }
        presenter.shipmentCartItemModelList = listOf(
            shipmentCartItemModel
        )
        val lastValidateUseRequest = ValidateUsePromoRequest().apply {
            orders = listOf(
                OrdersItem().apply {
                    uniqueId = "111-111-111"
                    codes = mutableListOf()
                }
            )
        }
        presenter.setLatValidateUseRequest(lastValidateUseRequest)

        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateClearAllBoPromo()

        // Then
        verify(inverse = true) {
            presenter.doUnapplyBo(any(), any())
        }
    }

    @Test
    fun `WHEN clear BO promo while user has no active BO promo in checkout page then apply new promo THEN don't call unapply BO promo`() {
        // Given
        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartString = "111-111-111"
            voucherLogisticItemUiModel = null
        }
        presenter.shipmentCartItemModelList = listOf(
            shipmentCartItemModel
        )
        val lastValidateUseRequest = ValidateUsePromoRequest().apply {
            orders = listOf(
                OrdersItem().apply {
                    uniqueId = "111-111-111"
                    codes = mutableListOf("TESTBO", "TESTNONBO")
                }
            )
        }
        presenter.setLatValidateUseRequest(lastValidateUseRequest)

        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateClearAllBoPromo()

        // Then
        verify(inverse = true) {
            presenter.doUnapplyBo(any(), any())
        }
    }

    @Test
    fun `WHEN clear BO promo with cart item unique_id different from validateUse unique_id THEN don't call unapply BO promo`() {
        // Given
        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartString = "111-111-111"
            voucherLogisticItemUiModel = null
        }
        presenter.shipmentCartItemModelList = listOf(
            shipmentCartItemModel
        )
        val lastValidateUseRequest = ValidateUsePromoRequest().apply {
            orders = listOf(
                OrdersItem().apply {
                    uniqueId = "999-999-999"
                    codes = mutableListOf()
                }
            )
        }
        presenter.setLatValidateUseRequest(lastValidateUseRequest)

        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateClearAllBoPromo()

        // Then
        verify(inverse = true) {
            presenter.doUnapplyBo(any(), any())
        }
    }

    @Test
    fun `WHEN clear all BO promo with cart list null THEN don't clear all BO promo`() {
        // Given
        presenter.shipmentCartItemModelList = listOf()
        presenter.setLatValidateUseRequest(null)

        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateClearAllBoPromo()

        // Then
        verify(inverse = true) {
            presenter.doUnapplyBo(any(), any())
        }
    }

    @Test
    fun `WHEN clear all BO promo with last validate use null THEN don't clear all BO promo`() {
        // Given
        presenter.shipmentCartItemModelList = null
        presenter.setLatValidateUseRequest(ValidateUsePromoRequest())

        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateClearAllBoPromo()

        // Then
        verify(inverse = true) {
            presenter.doUnapplyBo(any(), any())
        }
    }

    // Test ShipmentPresenter.clearOrderPromoCodeFromLastValidateUseRequest()

    @Test
    fun `WHEN clear order from last validate use with valid unique id with valid promo code THEN clear last validate use promo code`() {
        // Given
        val uniqueId = "111-111-111"
        val promoCode = "TESTBO"

        presenter.setLatValidateUseRequest(
            ValidateUsePromoRequest(
                orders = listOf(
                    OrdersItem(
                        uniqueId = "111-111-111",
                        codes = mutableListOf("TESTBO")
                    )
                )
            )
        )

        // When
        presenter.clearOrderPromoCodeFromLastValidateUseRequest(uniqueId, promoCode)

        // Then
        assert(presenter.lastValidateUseRequest.orders.size == 1)
        assert(presenter.lastValidateUseRequest.orders[0].uniqueId == "111-111-111")
        assert(presenter.lastValidateUseRequest.orders[0].codes.isEmpty())
    }

    @Test
    fun `WHEN clear order from last validate use with valid unique id and valid promo code THEN remove matching last apply order`() {
        // Given
        val uniqueId = "111-111-111"
        val promoCode = "TESTBO"

        presenter.lastApplyData = LastApplyUiModel(
            voucherOrders = mutableListOf(
                LastApplyVoucherOrdersItemUiModel(
                    code = "TESTBO",
                    uniqueId = "111-111-111"
                )
            )
        )

        // When
        presenter.clearOrderPromoCodeFromLastValidateUseRequest(uniqueId, promoCode)

        // Then
        assert(presenter.lastApplyData.voucherOrders.isEmpty())
    }

    @Test
    fun `WHEN clear order from last validate use with valid unique id and invalid promo code THEN don't clear last validate use promo code`() {
        // Given
        val uniqueId = "111-111-111"
        val promoCode = "TESTBO"

        presenter.setLatValidateUseRequest(
            ValidateUsePromoRequest(
                orders = listOf(
                    OrdersItem(
                        uniqueId = "111-111-111",
                        codes = mutableListOf("TESTNONBO")
                    )
                )
            )
        )

        // When
        presenter.clearOrderPromoCodeFromLastValidateUseRequest(uniqueId, promoCode)

        // Then
        assert(presenter.lastValidateUseRequest.orders.size == 1)
        assert(presenter.lastValidateUseRequest.orders[0].uniqueId == "111-111-111")
        assert(presenter.lastValidateUseRequest.orders[0].codes.size == 1)
        assert(presenter.lastValidateUseRequest.orders[0].codes.contains("TESTNONBO"))
    }

    @Test
    fun `WHEN clear order from last validate use with valid unique id with invalid promo code THEN don't clear last apply order`() {
        // Given
        val uniqueId = "111-111-111"
        val promoCode = "TESTBO"

        presenter.lastApplyData = LastApplyUiModel(
            voucherOrders = listOf(
                LastApplyVoucherOrdersItemUiModel(
                    uniqueId = "111-111-111",
                    code = "TESTNONBO"
                )
            )
        )

        // When
        presenter.clearOrderPromoCodeFromLastValidateUseRequest(uniqueId, promoCode)

        // Then
        assert(presenter.lastApplyData.voucherOrders.size == 1)
        assert(presenter.lastApplyData.voucherOrders[0].uniqueId == "111-111-111")
        assert(presenter.lastApplyData.voucherOrders[0].code == "TESTNONBO")
    }

    @Test
    fun `WHEN clear order from last validate use with invalid unique id THEN do nothing`() {
        // Given
        val uniqueId = "111-111-111"
        val promoCode = "TESTBO"

        presenter.setLatValidateUseRequest(
            ValidateUsePromoRequest(
                orders = listOf(
                    OrdersItem(
                        uniqueId = "222-222-222",
                        codes = mutableListOf("TESTNONBO")
                    )
                )
            )
        )

        // When
        presenter.clearOrderPromoCodeFromLastValidateUseRequest(uniqueId, promoCode)

        // Then
        assert(presenter.lastValidateUseRequest.orders.size == 1)
        assert(presenter.lastValidateUseRequest.orders[0].uniqueId == "222-222-222")
        assert(presenter.lastValidateUseRequest.orders[0].codes[0] == "TESTNONBO")
    }

    @Test
    fun `WHEN clear order from last apply with invalid unique id THEN do nothing`() {
        // Given
        val uniqueId = "111-111-111"
        val promoCode = "TESTBO"

        presenter.lastApplyData = LastApplyUiModel(
            voucherOrders = listOf(
                LastApplyVoucherOrdersItemUiModel(
                    uniqueId = "222-222-222",
                    code = "TESTNONBO"
                )
            )
        )

        // When
        presenter.clearOrderPromoCodeFromLastValidateUseRequest(uniqueId, promoCode)

        // Then
        assert(presenter.lastApplyData.voucherOrders.size == 1)
        assert(presenter.lastApplyData.voucherOrders[0].uniqueId == "222-222-222")
        assert(presenter.lastApplyData.voucherOrders[0].code == "TESTNONBO")
    }

    @Test
    fun `WHEN clear order from last validate use with last validate use and last apply null THEN do nothing`() {
        // Given
        val uniqueId = "111-111-111"
        val promoCode = "TESTBO"

        presenter.setLatValidateUseRequest(null)
        presenter.lastApplyData = null

        // When
        presenter.clearOrderPromoCodeFromLastValidateUseRequest(uniqueId, promoCode)

        // Then
        assert(presenter.lastValidateUseRequest == null)
        assert(presenter.lastApplyData == null)
    }

    @Test
    fun `WHEN clear order from last validate use with last validate use and last apply order empty THEN do nothing`() {
        // Given
        val uniqueId = "111-111-111"
        val promoCode = "TESTBO"

        presenter.setLatValidateUseRequest(ValidateUsePromoRequest(orders = emptyList()))
        presenter.lastApplyData = LastApplyUiModel(voucherOrders = emptyList())

        // When
        presenter.clearOrderPromoCodeFromLastValidateUseRequest(uniqueId, promoCode)

        // Then
        assert(presenter.lastValidateUseRequest.orders.isEmpty())
        assert(presenter.lastApplyData.voucherOrders.isEmpty())
    }

    // Test ShipmentPresenter.validateBoPromo()

    @Test
    fun `WHEN validate BO promo has multiple green state orders and cart item with voucher logistic empty THEN do apply BO promo`() {
        // Given
        val appliedVoucherOrder1 = PromoCheckoutVoucherOrdersItemUiModel(
            uniqueId = "111-111-111",
            code = "TEST1",
            shippingId = 2,
            spId = 2,
            type = "logistic",
            messageUiModel = MessageUiModel(state = "green")
        )
        val appliedVoucherOrder2 = PromoCheckoutVoucherOrdersItemUiModel(
            uniqueId = "222-222-222",
            code = "TEST2",
            shippingId = 1,
            spId = 1,
            type = "logistic",
            messageUiModel = MessageUiModel(state = "green")
        )
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(appliedVoucherOrder1, appliedVoucherOrder2)
            )
        )
        presenter.shipmentCartItemModelList = listOf<ShipmentCartItemModel>()

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(exactly = 2) {
            presenter.doApplyBo(or(appliedVoucherOrder1, appliedVoucherOrder2))
        }
        verify(inverse = true) {
            presenter.doUnapplyBo(any(), any())
        }
    }

    @Test
    fun `WHEN validate BO promo has multiple green state orders and cart item with voucher logistic not empty THEN do apply and unapply BO promo`() {
        // Given
        val appliedVoucherOrder1 = PromoCheckoutVoucherOrdersItemUiModel(
            uniqueId = "111-111-111",
            code = "TEST1",
            shippingId = 2,
            spId = 2,
            type = "logistic",
            messageUiModel = MessageUiModel(state = "green")
        )
        val appliedVoucherOrder2 = PromoCheckoutVoucherOrdersItemUiModel(
            uniqueId = "222-222-222",
            code = "TEST2",
            shippingId = 1,
            spId = 1,
            type = "logistic",
            messageUiModel = MessageUiModel(state = "green")
        )
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    appliedVoucherOrder1,
                    appliedVoucherOrder2,
                )
            )
        )
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartString = "333-333-333",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(
                    code = "TEST3",
                )
            ),
        )

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(exactly = 2) {
            presenter.doApplyBo(or(appliedVoucherOrder1, appliedVoucherOrder2))
        }
        verify(exactly = 1) {
            presenter.doUnapplyBo("333-333-333", "TEST3")
        }
    }

    @Test
    fun `WHEN validate BO promo has empty logistic voucher order and cart item with voucher logistic not empty THEN do unapply BO promo`() {
        // Given
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf()
            )
        )
        val shipmentCartItemModels = listOf(
            ShipmentCartItemModel(
                cartString = "333-333-333",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(
                    code = "TEST3",
                )
            ),
            ShipmentCartItemModel(
                cartString = "555-555-555",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(
                    code = "TEST5",
                )
            ),
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModels

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(inverse = true) {
            presenter.doApplyBo(any())
        }
        verify(exactly = 2) {
            presenter.doUnapplyBo(
                or("333-333-333", "555-555-555"),
                or("TEST3", "TEST5")
            )
        }
    }

    @Test
    fun `WHEN validate BO promo logistic voucher order with red state promo and cart item voucher logistic empty THEN don't apply and unapply BO promo`() {
        // Given
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        uniqueId = "111-111-111",
                        code = "TEST1",
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        uniqueId = "222-222-222",
                        code = "TEST2",
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    ),
                )
            )
        )
        presenter.shipmentCartItemModelList = listOf()

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(inverse = true) {
            presenter.doApplyBo(any())
            presenter.doUnapplyBo(any(), any())
        }
    }

    @Test
    fun `WHEN validate BO promo with voucher order promo shippingId 0 THEN don't apply and unapply BO promo`() {
        // Given
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        uniqueId = "111-111-111",
                        code = "TEST1",
                        shippingId = 0,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                )
            )
        )
        presenter.shipmentCartItemModelList = listOf()

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(inverse = true) {
            presenter.doApplyBo(any())
            presenter.doUnapplyBo(any(), any())
        }
    }

    @Test
    fun `WHEN validate BO promo with voucher order promo spId 0 THEN don't apply and unapply BO promo`() {
        // Given
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        uniqueId = "111-111-111",
                        code = "TEST1",
                        shippingId = 1,
                        spId = 0,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                )
            )
        )
        presenter.shipmentCartItemModelList = listOf()

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(inverse = true) {
            presenter.doApplyBo(any())
            presenter.doUnapplyBo(any(), any())
        }
    }

    @Test
    fun `WHEN validate BO promo with voucher order non logistic promo THEN don't apply and unapply BO promo`() {
        // Given
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        uniqueId = "111-111-111",
                        code = "TEST1",
                        shippingId = 1,
                        spId = 1,
                        type = "merchant",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                )
            )
        )
        presenter.shipmentCartItemModelList = listOf()

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(inverse = true) {
            presenter.doApplyBo(any())
            presenter.doUnapplyBo(any(), any())
        }
    }

    @Test
    fun `WHEN validate BO promo with empty voucher order and empty cart items THEN don't apply and unapply BO promo`() {
        // Given
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf()
            )
        )
        presenter.shipmentCartItemModelList = listOf()

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(inverse = true) {
            presenter.doApplyBo(any())
            presenter.doUnapplyBo(any(), any())
        }
    }

    @Test
    fun `WHEN validate BO with cart item empty voucher logistic THEN don't unapply BO promo`() {
        // Given
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf()
            )
        )
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartString = "111-111-111",
                voucherLogisticItemUiModel = null
            ),
        )

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(inverse = true) {
            presenter.doApplyBo(any())
            presenter.doUnapplyBo(any(), any())
        }
    }

    @Test
    fun `WHEN validate BO with voucher order and cart item has same promo THEN apply BO and don't unapply BO promo`() {
        // Given
        val appliedVoucherOrder = PromoCheckoutVoucherOrdersItemUiModel(
            uniqueId = "111-111-111",
            code = "TEST1",
            shippingId = 1,
            spId = 1,
            type = "logistic",
            messageUiModel = MessageUiModel(state = "green")
        )
        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(voucherOrderUiModels = listOf(appliedVoucherOrder))
        )
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartString = "111-111-111",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(
                    code = "TEST1"
                )
            ),
        )

        every { presenter.doApplyBo(any()) } just runs
        every { presenter.doUnapplyBo(any(), any()) } just runs

        // When
        presenter.validateBoPromo(validateUsePromoRevampUiModel)

        // Then
        verify(exactly = 1) {
            presenter.doApplyBo(appliedVoucherOrder)
        }
        verify(inverse = true) {
            presenter.doUnapplyBo(any(), any())
        }
    }

    // Test ShipmentPresenter.getProductForRatesRequest()

    @Test
    fun `WHEN create product for rates with cart item valid THEN products should not be empty`() {
        // Given
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartString = "111-111-111",
            cartItemModels = listOf(
                CartItemModel(
                    isError = false,
                    productId = 1,
                    isFreeShipping = true,
                    isFreeShippingExtra = true
                ),
                CartItemModel(
                    isError = false,
                    productId = 2,
                    isFreeShipping = true,
                    isFreeShippingExtra = true
                )
            )
        )

        // When
        val result = presenter.getProductForRatesRequest(shipmentCartItemModel)

        // Then
        assert(result.size == 2)
        assert(result.map { it.productId }.containsAll(listOf(1, 2)))
    }

    @Test
    fun `WHEN create product for rates with cart item partially error THEN products should not be empty`() {
        // Given
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartString = "111-111-111",
            cartItemModels = listOf(
                CartItemModel(
                    isError = true,
                    productId = 1,
                    isFreeShipping = true,
                    isFreeShippingExtra = true
                ),
                CartItemModel(
                    isError = false,
                    productId = 2,
                    isFreeShipping = true,
                    isFreeShippingExtra = true
                )
            )
        )

        // When
        val result = presenter.getProductForRatesRequest(shipmentCartItemModel)

        // Then
        assert(result.size == 1)
        assert(result.map { it.productId }.contains(2))
    }

    @Test
    fun `WHEN create product for rates with cart item all error THEN products should be empty`() {
        // Given
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartString = "111-111-111",
            cartItemModels = listOf(
                CartItemModel(
                    isError = true,
                    productId = 1,
                    isFreeShipping = true,
                    isFreeShippingExtra = true
                ),
                CartItemModel(
                    isError = true,
                    productId = 2,
                    isFreeShipping = true,
                    isFreeShippingExtra = true
                )
            )
        )

        // When
        val result = presenter.getProductForRatesRequest(shipmentCartItemModel)

        // Then
        assert(result.isEmpty())
    }

    @Test
    fun `WHEN create product for rates with cart items empty THEN products should be empty`() {
        // Given
        val shipmentCartItemModel = null

        // When
        val result = presenter.getProductForRatesRequest(shipmentCartItemModel)

        // Then
        assert(result.isEmpty())
    }

    // Test ShipmentPresenter.processBoPromoCourierRecommendation()

    @Test
    fun `WHEN get shipping rates success THEN should render success`() {
        // Given
        presenter.initializePresenterData(
            CartShipmentAddressFormData(
            cod = CodModel(counterCod = 1)
        )
        )
        val response = DataProvider.provideRatesV3EnabledBoPromoResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)
        every { getRatesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)

        every { presenter.getProductForRatesRequest(any()) } returns listOf()
        val isTradeInByDropOff = false
        every { view.isTradeInByDropOff } returns isTradeInByDropOff
        val itemPosition = 0
        val voucherOrdersItemUiModel = PromoCheckoutVoucherOrdersItemUiModel(
            code = "WGOIN",
            shippingId = 1,
            spId = 1,
        )
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartString = "1",
            isLeasingProduct = true,
            shopShipmentList = listOf(),
            selectedShipmentDetailData = ShipmentDetailData(
                shipmentCartData = ShipmentCartData(
                    originDistrictId = "1",
                    originPostalCode = "1",
                    originLatitude = "1",
                    originLongitude = "1",
                    weight = 1.0,
                    weightActual = 1.0,
                )
            )
        )
        presenter.recipientAddressModel = RecipientAddressModel()

        // When
        presenter.processBoPromoCourierRecommendation(itemPosition, voucherOrdersItemUiModel, shipmentCartItemModel)

        // Then
        verifyOrder {
            getRatesUseCase.execute(any())
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInByDropOff, any())
        }
    }

    @Test
    fun `WHEN get shipping rates with isTradeInByDropOff true THEN should call ratesV3Api`() {
        // Given
        val response = DataProvider.provideRatesV3ApiEnabledBoPromoResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)
        every { getRatesApiUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)

        every { presenter.getProductForRatesRequest(any()) } returns listOf()
        val isTradeInByDropOff = true
        every { view.isTradeInByDropOff } returns isTradeInByDropOff
        val itemPosition = 0
        val voucherOrdersItemUiModel = PromoCheckoutVoucherOrdersItemUiModel(
            code = "WGOIN",
            shippingId = 1,
            spId = 1,
        )
        val shipmentCartItemModel = ShipmentCartItemModel(
            isLeasingProduct = true,
            shopShipmentList = listOf(),
            selectedShipmentDetailData = ShipmentDetailData(
                shipmentCartData = ShipmentCartData(
                    originDistrictId = "1",
                    originPostalCode = "1",
                    originLatitude = "1",
                    originLongitude = "1",
                    weight = 1.0,
                    weightActual = 1.0,
                )
            )
        )
        presenter.recipientAddressModel = RecipientAddressModel()

        // When
        presenter.processBoPromoCourierRecommendation(itemPosition, voucherOrdersItemUiModel, shipmentCartItemModel)

        // Then
        verifyOrder {
            getRatesApiUseCase.execute(any())
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInByDropOff, any())
        }
    }

    @Test
    fun `WHEN get shipping rates with cart item shipmentDetailData null THEN should get shipment from view`() {
        // Given
        val response = DataProvider.provideRatesV3EnabledBoPromoResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)
        every { getRatesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)

        every { presenter.getProductForRatesRequest(any()) } returns listOf()
        val isTradeInByDropOff = false
        every { view.isTradeInByDropOff } returns isTradeInByDropOff
        val itemPosition = 0
        val voucherOrdersItemUiModel = PromoCheckoutVoucherOrdersItemUiModel(
            code = "WGOIN",
            shippingId = 1,
            spId = 1,
        )
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartString = "1",
            isLeasingProduct = true,
            shopShipmentList = listOf(),
            selectedShipmentDetailData = null
        )
        val recipientAddressModel = RecipientAddressModel()
        presenter.recipientAddressModel = recipientAddressModel
        every { view.getShipmentDetailData(shipmentCartItemModel, recipientAddressModel) } returns ShipmentDetailData(
            shipmentCartData = ShipmentCartData(
                originDistrictId = "1",
                originPostalCode = "1",
                originLatitude = "1",
                originLongitude = "1",
                weight = 1.0,
                weightActual = 1.0,
            )
        )

        // When
        presenter.processBoPromoCourierRecommendation(itemPosition, voucherOrdersItemUiModel, shipmentCartItemModel)

        // Then
        verifyOrder {
            view.getShipmentDetailData(shipmentCartItemModel, recipientAddressModel)
            getRatesUseCase.execute(any())
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInByDropOff, any())
        }
    }
}
