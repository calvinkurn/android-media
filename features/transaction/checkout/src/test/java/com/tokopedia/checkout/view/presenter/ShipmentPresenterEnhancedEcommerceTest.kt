package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.data.model.request.checkout.DataCheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.ProductDataCheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.ShopProductCheckoutRequest
import com.tokopedia.checkout.domain.model.checkout.CheckoutData
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.AnalyticsProductCheckoutData
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout.KEY_PRODUCT
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData.Companion.DEFAULT_VALUE_NONE_OTHER
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData.Companion.VALUE_BEBAS_ONGKIR
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData.Companion.VALUE_BEBAS_ONGKIR_EXTRA
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterEnhancedEcommerceTest {

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
    fun `WHEN generate enhanced ecommerce data step 2 THEN enhanced ecommerce data should not be null`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)
        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel())
        })
        val checkoutRequest = presenter.generateCheckoutRequest(null, 0, "")

        // When
        val enhancedEcommerceData = presenter.generateCheckoutAnalyticsDataLayer(checkoutRequest, "2")

        // Then
        assert(enhancedEcommerceData != null)
    }

    @Test
    fun `WHEN generate enhanced ecommerce data step 3 THEN enhanced ecommerce data should not be null`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)
        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel())
        })
        val checkoutRequest = presenter.generateCheckoutRequest(null, 0, "")

        // When
        val enhancedEcommerceData = presenter.generateCheckoutAnalyticsDataLayer(checkoutRequest, "3")

        // Then
        assert(enhancedEcommerceData != null)
    }

    @Test
    fun `WHEN generate enhanced ecommerce data step 4 THEN enhanced ecommerce data should not be null`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)
        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel())
        })
        val checkoutRequest = presenter.generateCheckoutRequest(null, 0, "")

        // When
        val enhancedEcommerceData = presenter.generateCheckoutAnalyticsDataLayer(checkoutRequest, "4")

        // Then
        assert(enhancedEcommerceData != null)
    }

    @Test
    fun `WHEN generate enhanced ecommerce with fulfilment THEN enhanced ecommerce data should not be null`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        val shopId = 652660
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)
        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel())
            this.shopId = shopId
        })
        val checkoutRequest = presenter.generateCheckoutRequest(null, 0, "")

        // When
        val enhancedEcommerceData = presenter.generateCheckoutAnalyticsDataLayer(checkoutRequest, "2")

        // Then
        assert(enhancedEcommerceData != null)
    }

    @Test
    fun `WHEN generate enhanced ecommerce data with no Bebas Ongkir THEN enhanced ecommerce product data dimension83 should be none other`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)
        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel())
        })
        val checkoutRequest = presenter.generateCheckoutRequest(null, 0, "")

        // When
        val enhancedEcommerceData = presenter.generateCheckoutAnalyticsDataLayer(checkoutRequest, "2")

        // Then
        val checkoutData = enhancedEcommerceData[EnhancedECommerceCheckout.KEY_CHECKOUT] as Map<*, *>
        val products = checkoutData[KEY_PRODUCT] as List<*>
        val product = products.first() as MutableMap<*, *>
        assertEquals(DEFAULT_VALUE_NONE_OTHER, product["dimension83"])
    }

    @Test
    fun `WHEN generate enhanced ecommerce data with Bebas Ongkir THEN enhanced ecommerce product data dimension83 should be bebas ongkir`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        val productData = dataCheckoutRequest.shopProducts.first().productData
        productData.apply {
            clear()
            add(ProductDataCheckoutRequest.Builder()
                    .isFreeShipping(true)
                    .build())
        }
        dataCheckoutRequest.shopProducts.first().productData = productData
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)
        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel())
        })
        val checkoutRequest = presenter.generateCheckoutRequest(null, 0, "")

        // When
        val enhancedEcommerceData = presenter.generateCheckoutAnalyticsDataLayer(checkoutRequest, "2")

        // Then
        val checkoutData = enhancedEcommerceData[EnhancedECommerceCheckout.KEY_CHECKOUT] as Map<*, *>
        val products = checkoutData[KEY_PRODUCT] as List<*>
        val product = products.first() as MutableMap<*, *>
        assertEquals(VALUE_BEBAS_ONGKIR, product["dimension83"])
    }

    @Test
    fun `WHEN generate enhanced ecommerce data with Bebas Ongkir Extra THEN enhanced ecommerce product data dimension83 should be bebas ongkir ekstra`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        val productData = dataCheckoutRequest.shopProducts.first().productData
        productData.apply {
            clear()
            add(ProductDataCheckoutRequest.Builder()
                    .isFreeShippingExtra(true)
                    .build())
        }
        dataCheckoutRequest.shopProducts.first().productData = productData
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)
        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel())
        })
        val checkoutRequest = presenter.generateCheckoutRequest(null, 0, "")

        // When
        val enhancedEcommerceData = presenter.generateCheckoutAnalyticsDataLayer(checkoutRequest, "2")

        // Then
        val checkoutData = enhancedEcommerceData[EnhancedECommerceCheckout.KEY_CHECKOUT] as Map<*, *>
        val products = checkoutData[KEY_PRODUCT] as List<*>
        val product = products.first() as MutableMap<*, *>
        assertEquals(VALUE_BEBAS_ONGKIR_EXTRA, product["dimension83"])
    }

    @Test
    fun `WHEN send enhance ecommerce with valid data THEN should trigger enhanced ecommerce analytic`() {
        // Given
        val tradeInCustomDimension = emptyMap<String, String>()
        val transactionId = "1"
        val eventCategory = "eventCategory"
        val eventAction = "eventAction"
        val eventLabel = "eventLabel"
        val step = "4"
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)
        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel())
        })
        presenter.setCheckoutData(CheckoutData(transactionId = transactionId))

        // When
        presenter.triggerSendEnhancedEcommerceCheckoutAnalytics(listOf(dataCheckoutRequest), tradeInCustomDimension, step, eventCategory, eventAction, eventLabel, "")

        // Then
        verify {
            shipmentAnalyticsActionListener.sendEnhancedEcommerceAnalyticsCheckout(
                    any(), tradeInCustomDimension, transactionId, eventCategory, eventAction, eventLabel
            )
        }
    }

    @Test
    fun `WHEN update enhanced ecommerce shipping data success THEN data checkout request should be updated`() {
        // Given
        val cartString = "1"

        val dataCheckoutRequestList = arrayListOf<DataCheckoutRequest>().apply {
            add(DataCheckoutRequest().apply {
                shopProducts = arrayListOf<ShopProductCheckoutRequest>().apply {
                    add(ShopProductCheckoutRequest().apply {
                        this.cartString = cartString
                        productData = arrayListOf<ProductDataCheckoutRequest>().apply {
                            add(ProductDataCheckoutRequest())
                        }
                    })
                }
            })
        }
        val shippingDuration = "1 Day"
        val shippingPrice = "100"
        val courierName = "courierName"

        every { view.generateNewCheckoutRequest(any(), any()) } returns dataCheckoutRequestList

        // When
        val newDataCheckoutRequest = presenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(cartString, shippingDuration, shippingPrice, courierName)

        // Then
        assert(newDataCheckoutRequest.first().shopProducts.first().productData.first().shippingDuration == shippingDuration)
        assert(newDataCheckoutRequest.first().shopProducts.first().productData.first().shippingPrice == shippingPrice)
        assert(newDataCheckoutRequest.first().shopProducts.first().productData.first().courier == courierName)
    }

    @Test
    fun `WHEN update enhanced ecommerce promo data success THEN data checkout request should be updated`() {
        // Given
        val cartString = "1"
        val productId = 1L
        val promoCodes = "a"
        val promoDetails = "aaa"

        presenter.dataCheckoutRequestList = arrayListOf<DataCheckoutRequest>().apply {
            add(DataCheckoutRequest().apply {
                shopProducts = arrayListOf<ShopProductCheckoutRequest>().apply {
                    add(ShopProductCheckoutRequest().apply {
                        this.cartString = cartString
                        productData = arrayListOf<ProductDataCheckoutRequest>().apply {
                            add(ProductDataCheckoutRequest().apply {
                                this.productId = productId
                            })
                        }
                    })
                }
            })
        }
        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>().apply {
            add(ShipmentCartItemModel().apply {
                this.cartString = cartString
                cartItemModels = arrayListOf<CartItemModel>().apply {
                    add(CartItemModel().apply {
                        this.productId = productId
                        analyticsProductCheckoutData = AnalyticsProductCheckoutData().apply {
                            this.promoCode = promoCodes
                            this.promoDetails = promoDetails
                        }
                    })
                }
            })
        }

        // When
        val newDataCheckoutRequest = presenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(shipmentCartItemModelList)

        // Then
        assert(newDataCheckoutRequest.first().shopProducts.first().productData.first().promoCode == promoCodes)
        assert(newDataCheckoutRequest.first().shopProducts.first().productData.first().promoDetails == promoDetails)
    }
}