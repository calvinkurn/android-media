package com.tokopedia.digital_checkout.utils.analytics

import android.text.TextUtils
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_WIDGET
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.util.BaseTrackerConst
import java.util.*

/**
 * @author by jessica on 22/01/21
 */

class DigitalAnalytics {

    fun sendCartScreen() {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(DigitalCheckoutTrackingConst.Screen.DIGITAL_CHECKOUT)
    }

    fun eventClickPromoButton(categoryName: String, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.CLICK_CHECKOUT,
                TrackAppUtils.EVENT_CATEGORY, DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT_PAGE,
                TrackAppUtils.EVENT_ACTION, DigitalCheckoutTrackingConst.Action.CLICK_PROMO,
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s", categoryName, operatorName),
                DigitalCheckoutTrackingConst.Label.BUSINESS_UNIT, DigitalCheckoutTrackingConst.Value.RECHARGE_BU,
                DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.SITE,
                DigitalCheckoutTrackingConst.Label.USER_ID, userId
        ))
    }

    fun eventClickUseVoucher(categoryName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalCheckoutTrackingConst.Event.CLICK_COUPON,
                DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT,
                DigitalCheckoutTrackingConst.Action.CLICK_USE_COUPON, categoryName.toLowerCase()))
    }

    fun eventClickCancelApplyCoupon(categoryName: String, promoCode: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalCheckoutTrackingConst.Event.CLICK_COUPON,
                DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT,
                DigitalCheckoutTrackingConst.Action.CLICK_CANCEL_APPLY_COUPON,
                categoryName.toLowerCase() + " - " + promoCode.toLowerCase()))
    }

    fun eventClickSubscription(tick: Boolean, categoryName: String?, operatorName: String?, userId: String?) {
        val actionValue: String = if (tick) {
            DigitalCheckoutTrackingConst.Action.TICK_AUTODEBIT
        } else {
            DigitalCheckoutTrackingConst.Action.UNTICK_AUTODEBIT
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.CLICK_CHECKOUT,
                TrackAppUtils.EVENT_CATEGORY, DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT_PAGE,
                TrackAppUtils.EVENT_ACTION, actionValue,
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s", categoryName, operatorName),
                DigitalCheckoutTrackingConst.Label.BUSINESS_UNIT, DigitalCheckoutTrackingConst.Value.RECHARGE_BU,
                DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.SITE,
                DigitalCheckoutTrackingConst.Label.USER_ID, userId
        ))
    }

    fun eventClickCrossSell(tick: Boolean, categoryName: String?, operatorName: String?, userId: String?) {
        val actionValue: String = if (tick) {
            DigitalCheckoutTrackingConst.Action.TICK_CROSSSELL
        } else {
            DigitalCheckoutTrackingConst.Action.UNTICK_CROSSSELL
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.CLICK_CHECKOUT,
                TrackAppUtils.EVENT_CATEGORY, DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT_PAGE,
                TrackAppUtils.EVENT_ACTION, actionValue,
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s", categoryName, operatorName),
                DigitalCheckoutTrackingConst.Label.BUSINESS_UNIT, DigitalCheckoutTrackingConst.Value.RECHARGE_BU,
                DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.SITE,
                DigitalCheckoutTrackingConst.Label.USER_ID, userId
        ))
    }

    fun eventClickProtection(tick: Boolean, categoryName: String?, operatorName: String?, userId: String?) {
        val actionValue: String = if (tick) {
            DigitalCheckoutTrackingConst.Action.TICK_PROTECTION
        } else {
            DigitalCheckoutTrackingConst.Action.UNTICK_PROTECTION
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.CLICK_CHECKOUT,
                TrackAppUtils.EVENT_CATEGORY, DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT_PAGE,
                TrackAppUtils.EVENT_ACTION, actionValue,
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s", categoryName, operatorName),
                DigitalCheckoutTrackingConst.Label.BUSINESS_UNIT, DigitalCheckoutTrackingConst.Value.RECHARGE_BU,
                DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.SITE,
                DigitalCheckoutTrackingConst.Label.USER_ID, userId
        ))
    }

    fun eventAddToCart(cartDigitalInfoData: CartDigitalInfoData, extraComeFrom: Int) {

        val productName: String = cartDigitalInfoData.attributes?.operatorName?.toLowerCase() + " " +
                cartDigitalInfoData.attributes?.price?.toLowerCase()

        val products: MutableList<Any> = ArrayList()

        val eventCategory = if (extraComeFrom == PARAM_WIDGET) {
            DigitalCheckoutTrackingConst.Category.HOMEPAGE_DIGITAL_WIDGET
        } else DigitalCheckoutTrackingConst.Category.DIGITAL_NATIVE

        val eventLabel = cartDigitalInfoData.attributes?.categoryName?.toLowerCase() + " - " +
                if (cartDigitalInfoData.isInstantCheckout) DigitalCheckoutTrackingConst.Value.INSTANT
                else DigitalCheckoutTrackingConst.Value.NON_INSTANT

        products.add(constructProductEnhanceEcommerce(cartDigitalInfoData, productName))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.ADD_TO_CART,
                        TrackAppUtils.EVENT_CATEGORY, eventCategory,
                        TrackAppUtils.EVENT_ACTION, DigitalCheckoutTrackingConst.Action.CLICK_BELI,
                        TrackAppUtils.EVENT_LABEL, eventLabel,
                        BaseTrackerConst.Ecommerce.KEY, DataLayer.mapOf(
                        DigitalCheckoutTrackingConst.CurrencyCode.KEY, DigitalCheckoutTrackingConst.CurrencyCode.IDR,
                        DigitalCheckoutTrackingConst.Label.ADD,
                        DataLayer.mapOf(DigitalCheckoutTrackingConst.Label.PRODUCTS, DataLayer.listOf(*products.toTypedArray()))),
                        DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.SITE
                )
        )
    }

    fun eventCheckout(cartDigitalInfoData: CartDigitalInfoData) {
        val productName: String = cartDigitalInfoData.attributes?.operatorName?.toLowerCase() + " " +
                cartDigitalInfoData.attributes?.price?.toLowerCase()

        val products: MutableList<Any> = ArrayList()
        products.add(constructProductEnhanceEcommerce(cartDigitalInfoData, productName))

        val label = String.format("%s - %s",
                cartDigitalInfoData.attributes?.categoryName?.toLowerCase(),
                cartDigitalInfoData.attributes?.operatorName?.toLowerCase()
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.CHECKOUT,
                        TrackAppUtils.EVENT_CATEGORY, DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT,
                        TrackAppUtils.EVENT_ACTION, DigitalCheckoutTrackingConst.Action.VIEW_CHECKOUT,
                        TrackAppUtils.EVENT_LABEL, label,
                        BaseTrackerConst.Ecommerce.KEY, DataLayer.mapOf(DigitalCheckoutTrackingConst.Event.CHECKOUT,
                        DataLayer.mapOf(DigitalCheckoutTrackingConst.Label.ACTION_FIELD,
                                DataLayer.mapOf(DigitalCheckoutTrackingConst.Label.STEP, "1",
                                        DigitalCheckoutTrackingConst.Label.OPTION, DigitalCheckoutTrackingConst.Misc.ACTION_FIELD_STEP1),
                                DigitalCheckoutTrackingConst.Label.PRODUCTS, DataLayer.listOf(*products.toTypedArray()))),
                        DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.SITE)
        )
    }

    fun eventProceedToPayment(cartDataInfo: CartDigitalInfoData, voucherCode: String) {
        val productName: String = cartDataInfo.attributes?.operatorName?.toLowerCase() + " " +
                cartDataInfo.attributes?.price?.toLowerCase()

        val products: MutableList<Any> = ArrayList()
        products.add(constructProductEnhanceEcommerce(cartDataInfo, productName))

        var label = String.format("%s - %s - ",
                cartDataInfo.attributes?.categoryName?.toLowerCase(),
                cartDataInfo.attributes?.operatorName?.toLowerCase())
        label += if (TextUtils.isEmpty(voucherCode)) {
            DigitalCheckoutTrackingConst.Value.NO_PROMO
        } else {
            DigitalCheckoutTrackingConst.Value.PROMO
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.CHECKOUT,
                        TrackAppUtils.EVENT_CATEGORY, DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT,
                        TrackAppUtils.EVENT_ACTION, DigitalCheckoutTrackingConst.Action.CLICK_PROCEED_PAYMENT,
                        TrackAppUtils.EVENT_LABEL, label,
                        BaseTrackerConst.Ecommerce.KEY, DataLayer.mapOf(
                        DigitalCheckoutTrackingConst.Event.CHECKOUT, DataLayer.mapOf(
                        DigitalCheckoutTrackingConst.Label.ACTION_FIELD, DataLayer.mapOf(
                        DigitalCheckoutTrackingConst.Label.STEP, "2",
                        DigitalCheckoutTrackingConst.Label.OPTION, DigitalCheckoutTrackingConst.Misc.ACTION_FIELD_STEP2),
                        DigitalCheckoutTrackingConst.Label.PRODUCTS, DataLayer.listOf(
                        *products.toTypedArray()))),
                        DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.SITE
                )
        )
    }

    private fun constructProductEnhanceEcommerce(cartDigitalInfoData: CartDigitalInfoData,
                                                 productName: String): Map<String?, Any?> {
        return DataLayer.mapOf(
                DigitalCheckoutTrackingConst.Product.KEY_NAME, productName,
                DigitalCheckoutTrackingConst.Product.KEY_ID, cartDigitalInfoData.relationProduct?.id,
                DigitalCheckoutTrackingConst.Product.KEY_PRICE, cartDigitalInfoData.attributes?.pricePlain.toString(),
                DigitalCheckoutTrackingConst.Product.KEY_BRAND, cartDigitalInfoData.attributes?.operatorName?.toLowerCase(),
                DigitalCheckoutTrackingConst.Product.KEY_CATEGORY, cartDigitalInfoData.attributes?.categoryName?.toLowerCase(),
                DigitalCheckoutTrackingConst.Product.KEY_VARIANT, "none",
                DigitalCheckoutTrackingConst.Product.KEY_QUANTITY, "1",
                DigitalCheckoutTrackingConst.Product.KEY_CATEGORY_ID, cartDigitalInfoData.relationCategory?.id,
                DigitalCheckoutTrackingConst.Product.KEY_CART_ID, cartDigitalInfoData.id
        )
    }

}