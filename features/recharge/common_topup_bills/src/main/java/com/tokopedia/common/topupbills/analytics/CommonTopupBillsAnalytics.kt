package com.tokopedia.common.topupbills.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsEventTracking.*
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsEventTracking.EnhanceEccomerce.Companion.ECOMMERCE
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class CommonTopupBillsAnalytics {
    fun eventClickUsePromo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_NATIVE,
                Action.CLICK_USE_PROMO,
                ""
        )
    }

    fun eventClickRemovePromo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.DIGITAL_NATIVE,
                Action.CLICK_REMOVE_PROMO,
                ""
        )
    }

    fun eventExpressCheckout(categoryName: String,
                             operatorName: String,
                             productId: String,
                             productName: String,
                             productPrice: Int,
                             isInstantCheckout: Boolean = false,
                             isPromoUsed: Boolean = false) {
        val isInstantCheckoutValue = if (isInstantCheckout) Label.INSTANT else Label.NO_INSTANT
        val isPromoUsedValue = if (isPromoUsed) Label.PROMO else Label.NO_PROMO

        // Click Buy
        val clickBuyEnhanceEccomerce = with(EnhanceEccomerce) {
            DataLayer.mapOf(
                CURRENCY_CODE, DEFAULT_CURRENCY_CODE,
                    ADD, DataLayer.mapOf(
                    PRODUCTS, DataLayer.listOf(DataLayer.mapOf(
                        NAME, productName,
                        ID, productId,
                        PRICE, productPrice,
                        BRAND, operatorName,
                        CATEGORY, categoryName,
                        VARIANT, "",
                        QUANTITY, 1,
                        DIMENSION_79, "",
                        DIMENSION_81, "",
                        DIMENSION_80, "",
                        DIMENSION_82, "",
                        DIMENSION_45, ""
                    ))
                )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.ADD_TO_CART,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_HOMEPAGE,
                        TrackAppUtils.EVENT_ACTION, Action.CLICK_BUY,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName - $isInstantCheckoutValue",
                        ECOMMERCE, clickBuyEnhanceEccomerce
                )
        )

        // View Checkout
        val viewCheckoutEnhanceEccomerce = with(EnhanceEccomerce) {
            DataLayer.mapOf(
                CHECKOUT, DataLayer.mapOf(
                    ActionField.ACTION_FIELD, DataLayer.mapOf(
                        ActionField.ACTION_FIELD_STEP, 1,
                        ActionField.ACTION_FIELD_OPTION, ActionField.ACTION_FIELD_VIEW_CHECKOUT
                    ),
                    PRODUCTS, DataLayer.listOf(DataLayer.mapOf(
                        NAME, productName,
                        ID, productId,
                        PRICE, productPrice,
                        BRAND, operatorName,
                        CATEGORY, categoryName,
                        VARIANT, "",
                        QUANTITY, 1,
                        DIMENSION_79, "",
                        DIMENSION_81, "",
                        DIMENSION_80, "",
                        DIMENSION_82, "",
                        DIMENSION_45, ""
                    ))
                )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.CHECKOUT,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_CHECKOUT,
                        TrackAppUtils.EVENT_ACTION, Action.VIEW_CHECKOUT,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName",
                        ECOMMERCE, viewCheckoutEnhanceEccomerce
                )
        )

        // Click Proceed to Payment
        val clickPaymentEnhanceEccomerce = with(EnhanceEccomerce) {
            DataLayer.mapOf(
                CHECKOUT, DataLayer.mapOf(
                    ActionField.ACTION_FIELD, DataLayer.mapOf(
                        ActionField.ACTION_FIELD_STEP, 2,
                        ActionField.ACTION_FIELD_OPTION, ActionField.ACTION_FIELD_CLICK_CHECKOUT
                    ),
                    PRODUCTS, DataLayer.listOf(DataLayer.mapOf(
                        NAME, productName,
                        ID, productId,
                        PRICE, productPrice,
                        BRAND, operatorName,
                        CATEGORY, categoryName,
                        VARIANT, "",
                        QUANTITY, 1
                    ))
                )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, Event.CHECKOUT,
                        TrackAppUtils.EVENT_CATEGORY, Category.DIGITAL_CHECKOUT,
                        TrackAppUtils.EVENT_ACTION, Action.CLICK_PROCEED_TO_PAYMENT,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName - $isPromoUsedValue",
                        ECOMMERCE, clickPaymentEnhanceEccomerce
                )
        )
    }
}