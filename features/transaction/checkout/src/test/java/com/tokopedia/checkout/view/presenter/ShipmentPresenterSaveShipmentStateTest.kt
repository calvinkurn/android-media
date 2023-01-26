package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateRequestData
import com.tokopedia.checkout.domain.model.saveshipmentstate.SaveShipmentStateData
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV3UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.model.DeliveryProduct
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.CapturingSlot
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterSaveShipmentStateTest {

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

    @MockK
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
                compositeSubscription, checkoutUseCase, getShipmentAddressFormV3UseCase,
                editAddressUseCase, changeShippingAddressGqlUseCase, saveShipmentStateGqlUseCase,
                getRatesUseCase, getRatesApiUseCase, clearCacheAutoApplyStackUseCase,
                ratesStatesConverter, shippingCourierConverter,
                shipmentAnalyticsActionListener, userSessionInterface, analyticsPurchaseProtection,
                checkoutAnalytics, shipmentDataConverter, releaseBookingUseCase, prescriptionIdsUseCase,
                validateUsePromoRevampUseCase, gson, TestSchedulers, eligibleForAddressUseCase,
                getRatesWithScheduleUseCase
        )
        presenter.attachView(view)
    }

    @Test
    fun checkParamsFromPresenter() {
        // Given
        every { view.isTradeInByDropOff } returns false

        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel().apply {
                productId = 1
            })
            selectedShipmentDetailData = ShipmentDetailData().apply {
                selectedCourier = CourierItemData().apply {
                    shipperId = 1
                    shipperProductId = 2
                }
            }
        })

        val addressId = "123"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = addressId
        }

        val capturedRequestParam = CapturingSlot<RequestParams>()
        every { saveShipmentStateGqlUseCase.createObservable(capture(capturedRequestParam)) } returns Observable.just(SaveShipmentStateData())

        // When
        presenter.processSaveShipmentState()

        // Then
        val params = capturedRequestParam.captured.getObject(SaveShipmentStateGqlUseCase.PARAM_CART_DATA_OBJECT) as HashMap<String, Any>
        val saveShipmentDataArray = params[SaveShipmentStateGqlUseCase.PARAM_CARTS] as List<ShipmentStateRequestData>

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList?.size ?: 0)

        verify(exactly = 1) { saveShipmentStateGqlUseCase.createObservable(any()) }
    }

    @Test
    fun checkParamsFromPresenter_PreOrderProduct() {
        // Given
        every { view.isTradeInByDropOff } returns false

        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel().apply {
                productId = 1
                isPreOrder = true
                preOrderDurationDay = 2
            })
            selectedShipmentDetailData = ShipmentDetailData().apply {
                selectedCourier = CourierItemData().apply {
                    shipperId = 1
                    shipperProductId = 2
                }
            }
        })

        val addressId = "123"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = addressId
        }

        val capturedRequestParam = CapturingSlot<RequestParams>()
        every { saveShipmentStateGqlUseCase.createObservable(capture(capturedRequestParam)) } returns Observable.just(SaveShipmentStateData())

        // When
        presenter.processSaveShipmentState()

        // Then
        val params = capturedRequestParam.captured.getObject(SaveShipmentStateGqlUseCase.PARAM_CART_DATA_OBJECT) as HashMap<String, Any>
        val saveShipmentDataArray = params[SaveShipmentStateGqlUseCase.PARAM_CARTS] as List<ShipmentStateRequestData>

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList?.size ?: 0)

        verify(exactly = 1) { saveShipmentStateGqlUseCase.createObservable(any()) }
    }

    @Test
    fun checkParamsFromPresenter_TradeInProduct() {
        // Given
        every { view.isTradeInByDropOff } returns true

        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel().apply {
                productId = 1
                isPreOrder = true
                preOrderDurationDay = 2
            })
            selectedShipmentDetailData = ShipmentDetailData().apply {
                selectedCourierTradeInDropOff = CourierItemData().apply {
                    shipperId = 1
                    shipperProductId = 2
                }
            }
        })

        val addressId = "123"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = addressId
        }

        val capturedRequestParam = CapturingSlot<RequestParams>()
        every { saveShipmentStateGqlUseCase.createObservable(capture(capturedRequestParam)) } returns Observable.just(SaveShipmentStateData())

        // When
        presenter.processSaveShipmentState()

        // Then
        val params = capturedRequestParam.captured.getObject(SaveShipmentStateGqlUseCase.PARAM_CART_DATA_OBJECT) as HashMap<String, Any>
        val saveShipmentDataArray = params[SaveShipmentStateGqlUseCase.PARAM_CARTS] as List<ShipmentStateRequestData>

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList?.size ?: 0)

        verify(exactly = 1) { saveShipmentStateGqlUseCase.createObservable(any()) }
    }

    @Test
    fun checkParamsFromInput_ShouldSendParam() {
        // Given
        every { view.isTradeInByDropOff } returns false

        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel().apply {
                productId = 1
            })
            selectedShipmentDetailData = ShipmentDetailData().apply {
                selectedCourier = CourierItemData().apply {
                    shipperId = 1
                    shipperProductId = 2
                }
            }
        }

        val addressId = "123"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = addressId
        }

        val capturedRequestParam = CapturingSlot<RequestParams>()
        every { saveShipmentStateGqlUseCase.createObservable(capture(capturedRequestParam)) } returns Observable.just(SaveShipmentStateData())

        // When
        presenter.processSaveShipmentState(shipmentCartItemModel)

        // Then
        val params = capturedRequestParam.captured.getObject(SaveShipmentStateGqlUseCase.PARAM_CART_DATA_OBJECT) as HashMap<String, Any>
        val saveShipmentDataArray = params[SaveShipmentStateGqlUseCase.PARAM_CARTS] as List<ShipmentStateRequestData>

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList?.size ?: 0)

        verify(exactly = 1) { saveShipmentStateGqlUseCase.createObservable(any()) }
    }

    @Test
    fun checkParamsFromInput_ShouldNotSendParam() {
        // Given
        every { view.isTradeInByDropOff } returns false

        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel().apply {
                productId = 1
            })
            selectedShipmentDetailData = ShipmentDetailData()
        }

        val addressId = "123"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = addressId
        }

        val capturedRequestParam = CapturingSlot<RequestParams>()
        every { saveShipmentStateGqlUseCase.createObservable(capture(capturedRequestParam)) } returns Observable.just(SaveShipmentStateData())

        // When
        presenter.processSaveShipmentState(shipmentCartItemModel)

        // Then
        assertEquals(false, capturedRequestParam.isCaptured)

        verify(inverse = true) { saveShipmentStateGqlUseCase.createObservable(any()) }
    }

    @Test
    fun checkParamsFromInput_WithScheduleDeliveryNotSelected_ShouldSendSelectedParam() {
        // Given
        every { view.isTradeInByDropOff } returns false

        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartItemModels = listOf(
                CartItemModel().apply {
                    productId = 1
                }
            )
            selectedShipmentDetailData = ShipmentDetailData().apply {
                selectedCourier = CourierItemData().apply {
                    shipperId = 1
                    shipperProductId = 2
                    scheduleDeliveryUiModel = ScheduleDeliveryUiModel(
                        isSelected = false
                    ).apply {
                        deliveryProduct = DeliveryProduct(
                            shipperId = 3,
                            shipperProductId = 4,
                            validationMetadata = "{\"timeslot_id\":2022092014123,\"schedule_date\":\"2022-09-20T00:00:00Z\",\"shipping_price\":10000}"
                        )
                    }
                }
            }
        }

        val addressId = "123"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = addressId
        }

        val capturedRequestParam = CapturingSlot<RequestParams>()
        every { saveShipmentStateGqlUseCase.createObservable(capture(capturedRequestParam)) } returns Observable.just(SaveShipmentStateData())

        // When
        presenter.processSaveShipmentState(shipmentCartItemModel)

        // Then
        val params = capturedRequestParam.captured.getObject(SaveShipmentStateGqlUseCase.PARAM_CART_DATA_OBJECT) as HashMap<String, Any>
        val saveShipmentDataArray = params[SaveShipmentStateGqlUseCase.PARAM_CARTS] as List<ShipmentStateRequestData>

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList?.size ?: 0)

        val firstShopProductDataList = data.shopProductDataList?.first()
        val shippingInfoData = firstShopProductDataList?.shippingInfoData

        assertEquals(1, shippingInfoData?.shippingId ?: 0)
        assertEquals(2, shippingInfoData?.spId ?: 0)
        assertEquals(
            "",
            firstShopProductDataList?.validationMetadata ?: ""
        )
    }

    @Test
    fun checkParamsFromInput_WithScheduleDeliverySelected_ShouldSendSelectedParam() {
        // Given
        every { view.isTradeInByDropOff } returns false

        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartItemModels = listOf(
                CartItemModel().apply {
                    productId = 1
                }
            )
            selectedShipmentDetailData = ShipmentDetailData().apply {
                selectedCourier = CourierItemData().apply {
                    shipperId = 1
                    shipperProductId = 2
                    scheduleDeliveryUiModel = ScheduleDeliveryUiModel(
                        isSelected = true
                    ).apply {
                        deliveryProduct = DeliveryProduct(
                            shipperId = 3,
                            shipperProductId = 4
                        )
                    }
                }
            }
            validationMetadata = "{\"timeslot_id\":2022092014123,\"schedule_date\":\"2022-09-20T00:00:00Z\",\"shipping_price\":10000}"
        }

        val addressId = "123"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = addressId
        }

        val capturedRequestParam = CapturingSlot<RequestParams>()
        every { saveShipmentStateGqlUseCase.createObservable(capture(capturedRequestParam)) } returns Observable.just(SaveShipmentStateData())

        // When
        presenter.processSaveShipmentState(shipmentCartItemModel)

        // Then
        val params = capturedRequestParam.captured.getObject(SaveShipmentStateGqlUseCase.PARAM_CART_DATA_OBJECT) as HashMap<String, Any>
        val saveShipmentDataArray = params[SaveShipmentStateGqlUseCase.PARAM_CARTS] as List<ShipmentStateRequestData>

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList?.size ?: 0)

        val firstShopProductDataList = data.shopProductDataList?.first()
        val shippingInfoData = firstShopProductDataList?.shippingInfoData
        assertEquals(3, shippingInfoData?.shippingId ?: 0)
        assertEquals(4, shippingInfoData?.spId ?: 0)
        assertEquals(
            "{\"timeslot_id\":2022092014123,\"schedule_date\":\"2022-09-20T00:00:00Z\",\"shipping_price\":10000}",
            firstShopProductDataList?.validationMetadata ?: ""
        )
    }
}
