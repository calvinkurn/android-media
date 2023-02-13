package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.data.model.request.checkout.old.DataCheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.old.ProductDataCheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.old.ShopProductCheckoutRequest
import com.tokopedia.checkout.domain.model.checkout.CheckoutData
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV3UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.AnalyticsProductCheckoutData
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData.Companion.DEFAULT_VALUE_NONE_OTHER
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData.Companion.VALUE_BEBAS_ONGKIR
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData.Companion.VALUE_BEBAS_ONGKIR_EXTRA
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.AdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterEnhancedEcommerceTest {

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
    private lateinit var prescriptionIdsUseCase: GetPrescriptionIdsUseCase

    @MockK
    private lateinit var epharmacyUseCase: EPharmacyPrepareProductsGroupUseCase

    @MockK
    private lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

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
            eligibleForAddressUseCase
        )
        presenter.attachView(view)
    }

    @Test
    fun `WHEN generate enhanced ecommerce data step 2 THEN enhanced ecommerce data should not be null`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "")

        // When
        val enhancedEcommerceData =
            presenter.generateCheckoutAnalyticsDataLayer(checkoutRequest, "2", "")

        // Then
        assert(enhancedEcommerceData != null)
    }

    @Test
    fun `WHEN generate enhanced ecommerce data step 3 THEN enhanced ecommerce data should not be null`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "")

        // When
        val enhancedEcommerceData =
            presenter.generateCheckoutAnalyticsDataLayer(checkoutRequest, "3", "")

        // Then
        assert(enhancedEcommerceData != null)
    }

    @Test
    fun `WHEN generate enhanced ecommerce data step 4 THEN enhanced ecommerce data should not be null`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "")

        // When
        val enhancedEcommerceData =
            presenter.generateCheckoutAnalyticsDataLayer(checkoutRequest, "4", "")

        // Then
        assert(enhancedEcommerceData != null)
    }

    @Test
    fun `WHEN generate enhanced ecommerce with fulfilment THEN enhanced ecommerce data should not be null`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        val shopId = 652660L
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
                this.shopId = shopId
            }
        )
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "")

        // When
        val enhancedEcommerceData =
            presenter.generateCheckoutAnalyticsDataLayer(checkoutRequest, "2", "")

        // Then
        assert(enhancedEcommerceData != null)
    }

    @Test
    fun `WHEN generate enhanced ecommerce data with no Bebas Ongkir THEN enhanced ecommerce product data dimension83 should be none other`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "")

        // When
        val enhancedEcommerceData =
            presenter.generateCheckoutAnalyticsDataLayer(checkoutRequest, "2", "")

        // Then
        val checkoutData =
            enhancedEcommerceData[EnhancedECommerceCheckout.KEY_CHECKOUT] as Map<*, *>
        val products = checkoutData[EnhancedECommerceCheckout.KEY_PRODUCT] as List<*>
        val product = products.firstOrNull() as MutableMap<*, *>
        assertEquals(DEFAULT_VALUE_NONE_OTHER, product["dimension83"])
    }

    @Test
    fun `WHEN generate enhanced ecommerce data with Bebas Ongkir THEN enhanced ecommerce product data dimension83 should be bebas ongkir`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        val productData = dataCheckoutRequest.shopProducts?.firstOrNull()?.productData
        productData?.apply {
            clear()
            add(
                ProductDataCheckoutRequest().apply {
                    freeShippingName = VALUE_BEBAS_ONGKIR
                }
            )
        }
        dataCheckoutRequest.shopProducts?.firstOrNull()?.productData = productData
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "")

        // When
        val enhancedEcommerceData =
            presenter.generateCheckoutAnalyticsDataLayer(checkoutRequest, "2", "")

        // Then
        val checkoutData =
            enhancedEcommerceData[EnhancedECommerceCheckout.KEY_CHECKOUT] as Map<*, *>
        val products = checkoutData[EnhancedECommerceCheckout.KEY_PRODUCT] as List<*>
        val product = products.firstOrNull() as MutableMap<*, *>
        assertEquals(VALUE_BEBAS_ONGKIR, product["dimension83"])
    }

    @Test
    fun `WHEN generate enhanced ecommerce data with Bebas Ongkir Extra THEN enhanced ecommerce product data dimension83 should be bebas ongkir ekstra`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        val productData = dataCheckoutRequest.shopProducts?.firstOrNull()?.productData
        productData?.apply {
            clear()
            add(
                ProductDataCheckoutRequest().apply {
                    freeShippingName = VALUE_BEBAS_ONGKIR_EXTRA
                }
            )
        }
        dataCheckoutRequest.shopProducts?.firstOrNull()?.productData = productData
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "")

        // When
        val enhancedEcommerceData =
            presenter.generateCheckoutAnalyticsDataLayer(checkoutRequest, "2", "")

        // Then
        val checkoutData =
            enhancedEcommerceData[EnhancedECommerceCheckout.KEY_CHECKOUT] as Map<*, *>
        val products = checkoutData[EnhancedECommerceCheckout.KEY_PRODUCT] as List<*>
        val product = products.firstOrNull() as MutableMap<*, *>
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
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setCheckoutData(CheckoutData(transactionId = transactionId))
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        // When
        presenter.triggerSendEnhancedEcommerceCheckoutAnalytics(
            listOf(dataCheckoutRequest),
            tradeInCustomDimension,
            step,
            eventCategory,
            eventAction,
            eventLabel,
            "",
            ""
        )

        // Then
        verify {
            shipmentAnalyticsActionListener.sendEnhancedEcommerceAnalyticsCheckout(
                any(),
                tradeInCustomDimension,
                transactionId,
                "",
                false,
                eventCategory,
                eventAction,
                eventLabel
            )
        }
    }

    @Test
    fun `WHEN send enhance ecommerce with step 2 THEN should trigger enhanced ecommerce analytic with promo flag from last apply`() {
        // Given
        val tradeInCustomDimension = emptyMap<String, String>()
        val transactionId = "1"
        val eventCategory = "eventCategory"
        val eventAction = "eventAction"
        val eventLabel = "eventLabel"
        val step = "2"
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setCheckoutData(CheckoutData(transactionId = transactionId))
        presenter.listShipmentCrossSellModel = arrayListOf()
        val pomlAutoApplied = true
        presenter.lastApplyData = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(pomlAutoApplied = pomlAutoApplied)
        )
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        // When
        presenter.triggerSendEnhancedEcommerceCheckoutAnalytics(
            listOf(dataCheckoutRequest),
            tradeInCustomDimension,
            step,
            eventCategory,
            eventAction,
            eventLabel,
            "",
            ""
        )

        // Then
        verify {
            shipmentAnalyticsActionListener.sendEnhancedEcommerceAnalyticsCheckout(
                any(),
                tradeInCustomDimension,
                transactionId,
                "",
                pomlAutoApplied,
                eventCategory,
                eventAction,
                eventLabel
            )
        }
    }

    @Test
    fun `WHEN send enhance ecommerce with step 2 without last apply data THEN should trigger enhanced ecommerce analytic with promo flag false`() {
        // Given
        val tradeInCustomDimension = emptyMap<String, String>()
        val transactionId = "1"
        val eventCategory = "eventCategory"
        val eventAction = "eventAction"
        val eventLabel = "eventLabel"
        val step = "2"
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setCheckoutData(CheckoutData(transactionId = transactionId))
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)
        // When
        presenter.triggerSendEnhancedEcommerceCheckoutAnalytics(
            listOf(dataCheckoutRequest),
            tradeInCustomDimension,
            step,
            eventCategory,
            eventAction,
            eventLabel,
            "",
            ""
        )

        // Then
        verify {
            shipmentAnalyticsActionListener.sendEnhancedEcommerceAnalyticsCheckout(
                any(),
                tradeInCustomDimension,
                transactionId,
                "",
                false,
                eventCategory,
                eventAction,
                eventLabel
            )
        }
    }

    @Test
    fun `WHEN send enhance ecommerce with step 4 THEN should trigger enhanced ecommerce analytic with promo flag from validate use`() {
        // Given
        val tradeInCustomDimension = emptyMap<String, String>()
        val transactionId = "1"
        val eventCategory = "eventCategory"
        val eventAction = "eventAction"
        val eventLabel = "eventLabel"
        val step = "4"
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setCheckoutData(CheckoutData(transactionId = transactionId))
        presenter.listShipmentCrossSellModel = arrayListOf()
        val pomlAutoApplied = true
        presenter.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            PromoUiModel(additionalInfoUiModel = AdditionalInfoUiModel(pomlAutoApplied = pomlAutoApplied))
        )
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        // When
        presenter.triggerSendEnhancedEcommerceCheckoutAnalytics(
            listOf(dataCheckoutRequest),
            tradeInCustomDimension,
            step,
            eventCategory,
            eventAction,
            eventLabel,
            "",
            ""
        )

        // Then
        verify {
            shipmentAnalyticsActionListener.sendEnhancedEcommerceAnalyticsCheckout(
                any(),
                tradeInCustomDimension,
                transactionId,
                "",
                pomlAutoApplied,
                eventCategory,
                eventAction,
                eventLabel
            )
        }
    }

    @Test
    fun `WHEN update enhanced ecommerce shipping data success THEN data checkout request should be updated`() {
        // Given
        val cartString = "1"

        val dataCheckoutRequestList = arrayListOf<DataCheckoutRequest>().apply {
            add(
                DataCheckoutRequest().apply {
                    shopProducts = arrayListOf<ShopProductCheckoutRequest>().apply {
                        add(
                            ShopProductCheckoutRequest().apply {
                                this.cartString = cartString
                                productData = arrayListOf<ProductDataCheckoutRequest>().apply {
                                    add(ProductDataCheckoutRequest())
                                }
                            }
                        )
                    }
                }
            )
        }
        val shippingDuration = "1 Day"
        val shippingPrice = "100"
        val courierName = "courierName"

        every { view.generateNewCheckoutRequest(any(), any()) } returns dataCheckoutRequestList

        // When
        val newDataCheckoutRequest =
            presenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(
                cartString,
                shippingDuration,
                shippingPrice,
                courierName
            )

        // Then
        assert(
            newDataCheckoutRequest.firstOrNull()
                ?.shopProducts?.firstOrNull()
                ?.productData?.firstOrNull()
                ?.shippingDuration == shippingDuration
        )
        assert(
            newDataCheckoutRequest.firstOrNull()
                ?.shopProducts?.firstOrNull()
                ?.productData?.firstOrNull()
                ?.shippingPrice == shippingPrice
        )
        assert(
            newDataCheckoutRequest.firstOrNull()
                ?.shopProducts?.firstOrNull()
                ?.productData?.firstOrNull()
                ?.courier == courierName
        )
    }

    @Test
    fun `WHEN update enhanced ecommerce promo data success THEN data checkout request should be updated`() {
        // Given
        val cartString = "1"
        val productId = 1L
        val promoCodes = "a"
        val promoDetails = "aaa"

        presenter.setDataCheckoutRequestList(
            arrayListOf<DataCheckoutRequest>().apply {
                add(
                    DataCheckoutRequest().apply {
                        shopProducts = arrayListOf<ShopProductCheckoutRequest>().apply {
                            add(
                                ShopProductCheckoutRequest().apply {
                                    this.cartString = cartString
                                    productData = arrayListOf<ProductDataCheckoutRequest>().apply {
                                        add(
                                            ProductDataCheckoutRequest().apply {
                                                this.productId = productId
                                            }
                                        )
                                    }
                                }
                            )
                        }
                    }
                )
            }
        )
        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>().apply {
            add(
                ShipmentCartItemModel().apply {
                    this.cartString = cartString
                    cartItemModels = arrayListOf<CartItemModel>().apply {
                        add(
                            CartItemModel().apply {
                                this.productId = productId
                                analyticsProductCheckoutData =
                                    AnalyticsProductCheckoutData().apply {
                                        this.promoCode = promoCodes
                                        this.promoDetails = promoDetails
                                    }
                            }
                        )
                    }
                }
            )
        }

        // When
        val newDataCheckoutRequest =
            presenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(
                shipmentCartItemModelList
            )

        // Then
        assert(
            newDataCheckoutRequest.firstOrNull()
                ?.shopProducts?.firstOrNull()
                ?.productData?.firstOrNull()
                ?.promoCode == promoCodes
        )
        assert(
            newDataCheckoutRequest.firstOrNull()
                ?.shopProducts?.firstOrNull()
                ?.productData?.firstOrNull()
                ?.promoDetails == promoDetails
        )
    }

    @Test
    fun `WHEN update enhanced ecommerce promo data with null data checkout request THEN data checkout request should be generated`() {
        // Given
        val cartString = "1"
        val productId = 1L
        val promoCodes = "a"
        val promoDetails = "aaa"

        val dataCheckoutRequests = arrayListOf<DataCheckoutRequest>().apply {
            add(
                DataCheckoutRequest().apply {
                    shopProducts = arrayListOf<ShopProductCheckoutRequest>().apply {
                        add(
                            ShopProductCheckoutRequest().apply {
                                this.cartString = cartString
                                productData = arrayListOf<ProductDataCheckoutRequest>().apply {
                                    add(
                                        ProductDataCheckoutRequest().apply {
                                            this.productId = productId
                                        }
                                    )
                                }
                            }
                        )
                    }
                }
            )
        }

        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>().apply {
            add(
                ShipmentCartItemModel().apply {
                    this.cartString = cartString
                    cartItemModels = arrayListOf<CartItemModel>().apply {
                        add(
                            CartItemModel().apply {
                                this.productId = productId
                                analyticsProductCheckoutData =
                                    AnalyticsProductCheckoutData().apply {
                                        this.promoCode = promoCodes
                                        this.promoDetails = promoDetails
                                    }
                            }
                        )
                    }
                }
            )
        }

        every { view.generateNewCheckoutRequest(any(), any()) } returns dataCheckoutRequests

        // When
        val newDataCheckoutRequest =
            presenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(
                shipmentCartItemModelList
            )

        // Then
        verify { view.generateNewCheckoutRequest(any(), any()) }
    }
}
