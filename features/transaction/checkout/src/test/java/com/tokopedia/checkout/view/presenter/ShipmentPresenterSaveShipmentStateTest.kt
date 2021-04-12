package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateRequestData
import com.tokopedia.checkout.domain.model.saveshipmentstate.SaveShipmentStateData
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
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
        assertEquals(addressId.toInt(), data.addressId)

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
        assertEquals(addressId.toInt(), data.addressId)

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
        assertEquals(addressId.toInt(), data.addressId)

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
        assertEquals(addressId.toInt(), data.addressId)

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
}