package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.usecase.*
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
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.domain.UpdateDynamicDataPassingUseCase
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrder
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterClearPromoTest {

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
    private lateinit var updateDynamicDataPassingUseCase: UpdateDynamicDataPassingUseCase

    @MockK
    private lateinit var prescriptionIdsUseCase: GetPrescriptionIdsUseCase

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
            validateUsePromoRevampUseCase, gson, TestSchedulers,
            eligibleForAddressUseCase, updateDynamicDataPassingUseCase
        )
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
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs

        // When
        presenter.cancelAutoApplyPromoStackLogistic(
            0,
            "code",
            ShipmentCartItemModel(
                cartString = "",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel())
            )
        )

        // Then
        verify {
            view.onSuccessClearPromoLogistic(0, any())
        }
    }

    @Test
    fun `WHEN clear BBO promo success with ticker data THEN should render success and update ticker`() {
        // Given
        presenter.tickerAnnouncementHolderData = TickerAnnouncementHolderData("0", "", "message")

        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true,
                    tickerMessage = "message"
                )
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs

        // When
        presenter.cancelAutoApplyPromoStackLogistic(
            0,
            "code",
            ShipmentCartItemModel(
                cartString = "",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel())
            )
        )

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
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs

        // When
        presenter.cancelAutoApplyPromoStackLogistic(
            0,
            promoCode,
            ShipmentCartItemModel(
                cartString = "",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel())
            )
        )

        // Then
        verifySequence {
            view.onSuccessClearPromoLogistic(0, true)
        }
        assertNull(presenter.validateUsePromoRevampUiModel)
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
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs

        // When
        presenter.cancelAutoApplyPromoStackLogistic(
            0,
            promoCode,
            ShipmentCartItemModel(
                cartString = "",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel())
            )
        )

        // Then
        verifySequence {
            view.onSuccessClearPromoLogistic(0, false)
        }
    }

    @Test
    fun `WHEN clear BBO promo and it's last applied global promo THEN should render success and flag last applied promo is true`() {
        // Given
        val promoCode = "code"
        presenter.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                codes = ArrayList<String>().apply {
                    add(promoCode)
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
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs

        // When
        presenter.cancelAutoApplyPromoStackLogistic(
            0,
            promoCode,
            ShipmentCartItemModel(
                cartString = "",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel())
            )
        )

        // Then
        verifySequence {
            view.onSuccessClearPromoLogistic(0, true)
        }
        assertNull(presenter.validateUsePromoRevampUiModel)
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
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs

        // When
        presenter.cancelAutoApplyPromoStackLogistic(
            0,
            promoCode,
            ShipmentCartItemModel(
                cartString = "",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel())
            )
        )

        // Then
        verifySequence {
            view.onSuccessClearPromoLogistic(0, false)
        }
    }

    @Test
    fun `WHEN clear non eligible promo success THEN should render success`() {
        // Given
        val notEligilePromoList = ArrayList<NotEligiblePromoHolderdata>().apply {
            add(
                NotEligiblePromoHolderdata(
                    promoCode = "code",
                    iconType = NotEligiblePromoHolderdata.TYPE_ICON_GLOBAL
                )
            )
        }
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs

        // When
        presenter.cancelNotEligiblePromo(notEligilePromoList)

        // Then
        verify {
            view.removeIneligiblePromo(notEligilePromoList)
        }
    }

    @Test
    fun `WHEN clear non eligible promo by unique id success THEN should render success`() {
        // Given
        val notEligiblePromos = arrayListOf(
            NotEligiblePromoHolderdata(
                promoCode = "code",
                iconType = -1
            )
        )
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs

        val presenterSpy = spyk(presenter)
        every { presenterSpy.getClearPromoOrderByUniqueId(any(), any()) } returns ClearPromoOrder(uniqueId = "1")
        presenterSpy.shipmentCartItemModelList = null

        // When
        presenterSpy.cancelNotEligiblePromo(notEligiblePromos)

        // Then
        verify {
            view.removeIneligiblePromo(notEligiblePromos)
        }
    }

    @Test
    fun `WHEN clear non eligible promo from shipment cart list success THEN should render success`() {
        // Given
        val notEligiblePromos = arrayListOf(
            NotEligiblePromoHolderdata(
                uniqueId = "1",
                promoCode = "code",
                iconType = -1
            )
        )
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs

        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartString = "1",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(boType = 1)),
                shopId = 1,
                isProductIsPreorder = false,
                cartItemModels = listOf(CartItemModel(preOrderDurationDay = 10)),
                fulfillmentId = 1
            )
        )

        // When
        presenter.cancelNotEligiblePromo(notEligiblePromos)

        // Then
        verify {
            view.removeIneligiblePromo(notEligiblePromos)
        }
    }

    @Test
    fun `WHEN clear non eligible promo from shipment cart list unique id not match THEN should not update view`() {
        // Given
        val notEligiblePromos = arrayListOf(
            NotEligiblePromoHolderdata(
                uniqueId = "1",
                promoCode = "code",
                iconType = -1
            )
        )
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs

        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartString = "2"
            )
        )

        // When
        presenter.cancelNotEligiblePromo(notEligiblePromos)

        // Then
        verify(inverse = true) {
            view.updateTickerAnnouncementMessage()
            view.removeIneligiblePromo(notEligiblePromos)
        }
    }

    @Test
    fun `WHEN clear non eligible promo from shipment cart list empty THEN should not update view`() {
        // Given
        val notEligiblePromos = arrayListOf(
            NotEligiblePromoHolderdata(
                uniqueId = "1",
                promoCode = "code",
                iconType = -1
            )
        )
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs

        presenter.shipmentCartItemModelList = listOf()

        // When
        presenter.cancelNotEligiblePromo(notEligiblePromos)

        // Then
        verify(inverse = true) {
            view.updateTickerAnnouncementMessage()
            view.removeIneligiblePromo(notEligiblePromos)
        }
    }

    @Test
    fun `WHEN clear non eligible promo success with ticker data THEN should render success and update ticker`() {
        // Given
        presenter.tickerAnnouncementHolderData = TickerAnnouncementHolderData("0", "", "message")

        val notEligilePromoList = ArrayList<NotEligiblePromoHolderdata>().apply {
            add(
                NotEligiblePromoHolderdata(
                    promoCode = "code",
                    iconType = NotEligiblePromoHolderdata.TYPE_ICON_GLOBAL
                )
            )
        }

        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true,
                    tickerMessage = "message"
                )
            )
        )

        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs

        // When
        presenter.cancelNotEligiblePromo(notEligilePromoList)

        // Then
        verifySequence {
            view.updateTickerAnnouncementMessage()
            view.removeIneligiblePromo(notEligilePromoList)
        }
    }

    @Test
    fun `WHEN clear non eligible not found THEN should not update view`() {
        // Given
        val notEligiblePromos = arrayListOf(
            NotEligiblePromoHolderdata(
                promoCode = "code",
                iconType = -1
            )
        )
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs

        val presenterSpy = spyk(presenter)
        every { presenterSpy.getClearPromoOrderByUniqueId(any(), any()) } returns null
        presenterSpy.shipmentCartItemModelList = null

        // When
        presenterSpy.cancelNotEligiblePromo(notEligiblePromos)

        // Then
        verify(inverse = true) {
            view.updateTickerAnnouncementMessage()
            view.removeIneligiblePromo(notEligiblePromos)
        }
    }

    // Test ShipmentPresenter.hitClearAllBo()

    @Test
    fun `WHEN hit clear all BO with cart list with valid voucher logistic promo THEN call clear cache auto apply use case`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartString = "111-111-111",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(
                    code = "TEST1"
                ),
                shipmentCartData = ShipmentCartData(
                    boMetadata = BoMetadata(
                        boType = 1
                    )
                ),
                cartItemModels = listOf(
                    CartItemModel(
                        preOrderDurationDay = 10
                    )
                )
            ),
            ShipmentCartItemModel(
                cartString = "222-222-222",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(
                    code = "TEST2"
                ),
                shipmentCartData = ShipmentCartData(
                    boMetadata = BoMetadata(
                        boType = 1
                    )
                ),
                cartItemModels = listOf(
                    CartItemModel(
                        preOrderDurationDay = 10
                    )
                )
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
        presenter.hitClearAllBo()

        // Then
        verify {
            clearCacheAutoApplyStackUseCase.setParams(any())
            clearCacheAutoApplyStackUseCase.createObservable(any())
        }
    }

    @Test
    fun `WHEN hit clear all BO with invalid cart item THEN don't call clear cache auto apply use case`() {
        // Given
        // Test negative branch
        presenter.shipmentCartItemModelList = listOf(
            // Test shipmentCartItemModel == null
            null,
            // Test shipmentCartData == null
            ShipmentCartItemModel(
                cartString = "111-111-111",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "TEST1"),
                shipmentCartData = null,
                cartItemModels = listOf(
                    CartItemModel(
                        preOrderDurationDay = 10
                    )
                )
            ),
            // Test voucherLogisticItemUiModel.code == ""
            ShipmentCartItemModel(
                cartString = "111-111-111",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = ""),
                shipmentCartData = ShipmentCartData(
                    boMetadata = BoMetadata(
                        boType = 1
                    )
                ),
                cartItemModels = listOf(
                    CartItemModel(
                        preOrderDurationDay = 10
                    )
                )
            ),
            // Test voucherLogisticItemUiModel == null
            ShipmentCartItemModel(
                cartString = "111-111-111",
                voucherLogisticItemUiModel = null,
                shipmentCartData = ShipmentCartData(
                    boMetadata = BoMetadata(
                        boType = 1
                    )
                ),
                cartItemModels = listOf(
                    CartItemModel(
                        preOrderDurationDay = 10
                    )
                )
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
        presenter.hitClearAllBo()

        // Then
        verify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any())
            clearCacheAutoApplyStackUseCase.createObservable(any())
        }
    }

    // Test ShipmentPresenter.getClearPromoOrderByUniqueId(...)

    @Test
    fun `WHEN get clear promo order unique id with valid unique id THEN promo order should not be null`() {
        // Given
        val clearPromoOrders = arrayListOf(
            ClearPromoOrder(uniqueId = "111-111-111"),
            ClearPromoOrder(uniqueId = "222-222-222"),
            ClearPromoOrder(uniqueId = "333-333-333")
        )
        val uniqueId = "111-111-111"

        // When
        val result = presenter.getClearPromoOrderByUniqueId(clearPromoOrders, uniqueId)

        // Then
        assert(result != null)
        assert(result?.uniqueId == uniqueId)
    }

    @Test
    fun `WHEN get clear promo order unique id with invalid unique id THEN promo order should be null`() {
        // Given
        val clearPromoOrders = arrayListOf(
            ClearPromoOrder(uniqueId = "222-222-222"),
            ClearPromoOrder(uniqueId = "333-333-333")
        )
        val uniqueId = "111-111-111"

        // When
        val result = presenter.getClearPromoOrderByUniqueId(clearPromoOrders, uniqueId)

        // Then
        assert(result == null)
    }

    @Test
    fun `WHEN get clear promo order unique id with empty clear promo list THEN promo order should be null`() {
        // Given
        val clearPromoOrders = arrayListOf<ClearPromoOrder>()
        val uniqueId = "111-111-111"

        // When
        val result = presenter.getClearPromoOrderByUniqueId(clearPromoOrders, uniqueId)

        // Then
        assert(result == null)
    }

    @Test
    fun `WHEN get clear promo order unique id with null unique id THEN promo order should be null`() {
        // Given
        val clearPromoOrders = arrayListOf(
            ClearPromoOrder(uniqueId = "222-222-222"),
            ClearPromoOrder(uniqueId = "333-333-333")
        )
        val uniqueId = ""

        // When
        val result = presenter.getClearPromoOrderByUniqueId(clearPromoOrders, uniqueId)

        // Then
        assert(result == null)
    }
}
