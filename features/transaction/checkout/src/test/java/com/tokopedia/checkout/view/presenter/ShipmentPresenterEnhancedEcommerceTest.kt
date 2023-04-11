package com.tokopedia.checkout.view.presenter

import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData.Companion.DEFAULT_VALUE_NONE_OTHER
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData.Companion.VALUE_BEBAS_ONGKIR
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData.Companion.VALUE_BEBAS_ONGKIR_EXTRA
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.AdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class ShipmentPresenterEnhancedEcommerceTest : BaseShipmentPresenterTest() {

    @Test
    fun `WHEN generate enhanced ecommerce data with no Bebas Ongkir THEN enhanced ecommerce product data dimension83 should be none other`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
            }
        )

        // When
        val enhancedEcommerceData =
            presenter.generateCheckoutAnalyticsDataLayer("2", "")

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
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = "", freeShippingName = VALUE_BEBAS_ONGKIR))
            }
        )

        // When
        val enhancedEcommerceData =
            presenter.generateCheckoutAnalyticsDataLayer("2", "")

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
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = "", freeShippingName = VALUE_BEBAS_ONGKIR_EXTRA))
            }
        )

        // When
        val enhancedEcommerceData =
            presenter.generateCheckoutAnalyticsDataLayer("2", "")

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
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
            }
        )
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        // When
        presenter.triggerSendEnhancedEcommerceCheckoutAnalytics(
            tradeInCustomDimension,
            step,
            eventCategory,
            eventAction,
            eventLabel,
            transactionId,
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
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
            }
        )
        presenter.listShipmentCrossSellModel = arrayListOf()
        val pomlAutoApplied = true
        presenter.lastApplyData.value = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(pomlAutoApplied = pomlAutoApplied)
        )
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        // When
        presenter.triggerSendEnhancedEcommerceCheckoutAnalytics(
            tradeInCustomDimension,
            step,
            eventCategory,
            eventAction,
            eventLabel,
            transactionId,
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
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
            }
        )
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        // When
        presenter.triggerSendEnhancedEcommerceCheckoutAnalytics(
            tradeInCustomDimension,
            step,
            eventCategory,
            eventAction,
            eventLabel,
            transactionId,
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
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
            }
        )
        presenter.listShipmentCrossSellModel = arrayListOf()
        val pomlAutoApplied = true
        presenter.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            PromoUiModel(additionalInfoUiModel = AdditionalInfoUiModel(pomlAutoApplied = pomlAutoApplied))
        )
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        // When
        presenter.triggerSendEnhancedEcommerceCheckoutAnalytics(
            tradeInCustomDimension,
            step,
            eventCategory,
            eventAction,
            eventLabel,
            transactionId,
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
}
