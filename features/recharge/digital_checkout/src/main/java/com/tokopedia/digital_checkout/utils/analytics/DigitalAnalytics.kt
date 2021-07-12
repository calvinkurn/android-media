package com.tokopedia.digital_checkout.utils.analytics

import android.text.TextUtils
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common_digital.atc.data.response.FintechProduct
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.utils.analytics.DigitalCheckoutTrackingConst.Value.CROSSELL_CARD_TYPE
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

    fun eventTebusMurahImpression(fintechProduct: FintechProduct, categoryName: String, position: Int, userId: String) {

        val fintechProductList: MutableList<Any> = ArrayList()
        fintechProductList.add(constructTebusMurahFintechProduct(fintechProduct, categoryName, position))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.PRODUCT_VIEW,
                TrackAppUtils.EVENT_CATEGORY, DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT_PAGE,
                TrackAppUtils.EVENT_ACTION, DigitalCheckoutTrackingConst.Action.IMPRESSION_TEBUS_MURAH_ICON,
                TrackAppUtils.EVENT_LABEL, "",
                DigitalCheckoutTrackingConst.Label.BUSINESS_UNIT, DigitalCheckoutTrackingConst.Value.RECHARGE_BU,
                DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.RECHARGE_SITE,
                BaseTrackerConst.Ecommerce.KEY, DataLayer.mapOf(
                DigitalCheckoutTrackingConst.CurrencyCode.KEY, DigitalCheckoutTrackingConst.CurrencyCode.IDR,
                DigitalCheckoutTrackingConst.Label.IMPRESSIONS, DataLayer.listOf(*fintechProductList.toTypedArray())),
                DigitalCheckoutTrackingConst.Label.USER_ID, userId
        ))
    }

    fun eventTebusMurahChecked(fintechProduct: FintechProduct, categoryName: String, position: Int, userId: String) {
        val fintechProductList: MutableList<Any> = ArrayList()
        fintechProductList.add(constructTebusMurahFintechProduct(fintechProduct, categoryName, position))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.PRODUCT_CLICK,
                TrackAppUtils.EVENT_CATEGORY, DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT_PAGE,
                TrackAppUtils.EVENT_ACTION, DigitalCheckoutTrackingConst.Action.CLICK_TEBUS_MURAH_ICON,
                TrackAppUtils.EVENT_LABEL, "$categoryName - ${fintechProduct.info.title}",
                DigitalCheckoutTrackingConst.Label.BUSINESS_UNIT, DigitalCheckoutTrackingConst.Value.RECHARGE_BU,
                DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.RECHARGE_SITE,
                BaseTrackerConst.Ecommerce.KEY, DataLayer.mapOf(
                DigitalCheckoutTrackingConst.Label.CLICK,
                DataLayer.mapOf(DigitalCheckoutTrackingConst.Label.ACTION_FIELD,
                        DataLayer.mapOf(DigitalCheckoutTrackingConst.Product.KEY_LIST, "/checkout - ${fintechProduct.info.title} - tebus murah"),
                        DigitalCheckoutTrackingConst.Label.PRODUCTS, DataLayer.listOf(*fintechProductList.toTypedArray()))),
                DigitalCheckoutTrackingConst.Label.USER_ID, userId
        ))
    }

    fun eventTebusMurahUnchecked(fintechProduct: FintechProduct, categoryName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.DIGITAL_GENERAL_EVENT,
                TrackAppUtils.EVENT_CATEGORY, DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT_PAGE,
                TrackAppUtils.EVENT_ACTION, DigitalCheckoutTrackingConst.Action.UNCHECK_TEBUS_MURAH_ICON,
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s", categoryName, fintechProduct.info.title),
                DigitalCheckoutTrackingConst.Label.BUSINESS_UNIT, DigitalCheckoutTrackingConst.Value.RECHARGE_BU,
                DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.RECHARGE_SITE,
                DigitalCheckoutTrackingConst.Label.USER_ID, userId
        ))
    }

    fun eventImpressionCrossSell(fintechProduct: FintechProduct, categoryName: String, position: Int, userId: String) {
        val fintechProductList: MutableList<Any> = ArrayList()
        fintechProductList.add(constructFintechProduct(fintechProduct, categoryName, position))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.PRODUCT_VIEW,
                TrackAppUtils.EVENT_CATEGORY, DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT_PAGE,
                TrackAppUtils.EVENT_ACTION, DigitalCheckoutTrackingConst.Action.IMPRESSION_CROSSELL_ICON,
                TrackAppUtils.EVENT_LABEL, "",
                DigitalCheckoutTrackingConst.Label.BUSINESS_UNIT, DigitalCheckoutTrackingConst.Value.RECHARGE_BU,
                DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.RECHARGE_SITE,
                BaseTrackerConst.Ecommerce.KEY, DataLayer.mapOf(
                DigitalCheckoutTrackingConst.CurrencyCode.KEY, DigitalCheckoutTrackingConst.CurrencyCode.IDR,
                DigitalCheckoutTrackingConst.Label.IMPRESSIONS, DataLayer.listOf(*fintechProductList.toTypedArray())),
                DigitalCheckoutTrackingConst.Label.USER_ID, userId
        ))
    }

    fun eventClickCrossSell(fintechProduct: FintechProduct, categoryName: String, position: Int, userId: String?) {
        val fintechProductList: MutableList<Any> = ArrayList()
        fintechProductList.add(constructFintechProduct(fintechProduct, categoryName, position))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.PRODUCT_CLICK,
                TrackAppUtils.EVENT_CATEGORY, DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT_PAGE,
                TrackAppUtils.EVENT_ACTION, DigitalCheckoutTrackingConst.Action.TICK_CROSSSELL,
                TrackAppUtils.EVENT_LABEL, "${fintechProduct.transactionType} - ${fintechProduct.fintechPartnerAmount}",
                DigitalCheckoutTrackingConst.Label.BUSINESS_UNIT, DigitalCheckoutTrackingConst.Value.RECHARGE_BU,
                DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.RECHARGE_SITE,
                BaseTrackerConst.Ecommerce.KEY, DataLayer.mapOf(
                DigitalCheckoutTrackingConst.Label.CLICK,
                DataLayer.mapOf(DigitalCheckoutTrackingConst.Label.ACTION_FIELD,
                        DataLayer.mapOf(DigitalCheckoutTrackingConst.Product.KEY_LIST, "/checkout - ${fintechProduct.info.title} $position - $CROSSELL_CARD_TYPE"),
                        DigitalCheckoutTrackingConst.Label.PRODUCTS, DataLayer.listOf(*fintechProductList.toTypedArray()))),
                DigitalCheckoutTrackingConst.Label.USER_ID, userId
        ))
    }

    fun eventUnclickCrossSell(fintechProduct: FintechProduct, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.CLICK_CHECKOUT,
                TrackAppUtils.EVENT_CATEGORY, DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT_PAGE,
                TrackAppUtils.EVENT_ACTION, DigitalCheckoutTrackingConst.Action.UNTICK_CROSSSELL,
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s", fintechProduct.transactionType, fintechProduct.fintechPartnerAmount),
                DigitalCheckoutTrackingConst.Label.BUSINESS_UNIT, DigitalCheckoutTrackingConst.Value.RECHARGE_BU,
                DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.SITE,
                DigitalCheckoutTrackingConst.Label.USER_ID, userId
        ))
    }

    fun eventCheckout(cartDigitalInfoData: CartDigitalInfoData, userId: String, categoryId: String) {
        val productName: String = cartDigitalInfoData.attributes.operatorName.toLowerCase() + " " +
                cartDigitalInfoData.attributes.price.toLowerCase()

        val products: MutableList<Any> = ArrayList()
        products.add(constructProductEnhanceEcommerce(cartDigitalInfoData, productName, categoryId))

        val label = String.format("%s - %s",
                cartDigitalInfoData.attributes.categoryName.toLowerCase(),
                cartDigitalInfoData.attributes.operatorName.toLowerCase()
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.CHECKOUT,
                        TrackAppUtils.EVENT_CATEGORY, DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT,
                        TrackAppUtils.EVENT_ACTION, DigitalCheckoutTrackingConst.Action.VIEW_CHECKOUT,
                        TrackAppUtils.EVENT_LABEL, label,
                        DigitalCheckoutTrackingConst.Label.BUSINESS_UNIT, DigitalCheckoutTrackingConst.Value.RECHARGE_BU,
                        DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.RECHARGE_SITE,
                        DigitalCheckoutTrackingConst.Label.USER_ID, userId,
                        BaseTrackerConst.Ecommerce.KEY, DataLayer.mapOf(DigitalCheckoutTrackingConst.Event.CHECKOUT,
                        DataLayer.mapOf(DigitalCheckoutTrackingConst.Label.ACTION_FIELD,
                                DataLayer.mapOf(DigitalCheckoutTrackingConst.Label.STEP, "1",
                                        DigitalCheckoutTrackingConst.Label.OPTION, DigitalCheckoutTrackingConst.Misc.ACTION_FIELD_STEP1),
                                DigitalCheckoutTrackingConst.Label.PRODUCTS, DataLayer.listOf(*products.toTypedArray()))),
                        DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.RECHARGE_SITE)
        )
    }

    fun eventProceedToPayment(cartDataInfo: CartDigitalInfoData, voucherCode: String, userId: String, categoryId: String) {
        val productName: String = cartDataInfo.attributes.operatorName.toLowerCase() + " " +
                cartDataInfo.attributes.price.toLowerCase()

        val products: MutableList<Any> = ArrayList()
        products.add(constructProductEnhanceEcommerce(cartDataInfo, productName, categoryId))

        var label = String.format("%s - %s - ",
                cartDataInfo.attributes.categoryName.toLowerCase(),
                cartDataInfo.attributes.operatorName.toLowerCase())
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
                        DigitalCheckoutTrackingConst.Label.BUSINESS_UNIT, DigitalCheckoutTrackingConst.Value.RECHARGE_BU,
                        DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.RECHARGE_SITE,
                        DigitalCheckoutTrackingConst.Label.USER_ID, userId,
                        BaseTrackerConst.Ecommerce.KEY, DataLayer.mapOf(
                        DigitalCheckoutTrackingConst.Event.CHECKOUT, DataLayer.mapOf(
                        DigitalCheckoutTrackingConst.Label.ACTION_FIELD, DataLayer.mapOf(
                        DigitalCheckoutTrackingConst.Label.STEP, "2",
                        DigitalCheckoutTrackingConst.Label.OPTION, DigitalCheckoutTrackingConst.Misc.ACTION_FIELD_STEP2),
                        DigitalCheckoutTrackingConst.Label.PRODUCTS, DataLayer.listOf(
                        *products.toTypedArray()))),
                        DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.RECHARGE_SITE
                )
        )
    }

    fun eventProceedCheckoutCrossell(fintechProduct: FintechProduct, categoryName: String, userId: String) {
        val fintechProductList: MutableList<Any> = ArrayList()
        fintechProductList.add(constructFintechProductOnCheckout(fintechProduct, categoryName))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.CHECKOUT,
                        TrackAppUtils.EVENT_CATEGORY, DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT_PAGE,
                        TrackAppUtils.EVENT_ACTION, DigitalCheckoutTrackingConst.Action.CLICK_PROCEED_PAYMENT_CROSSELL,
                        TrackAppUtils.EVENT_LABEL, "${fintechProduct.transactionType} - ${fintechProduct.fintechPartnerAmount}",
                        DigitalCheckoutTrackingConst.Label.BUSINESS_UNIT, DigitalCheckoutTrackingConst.Value.RECHARGE_BU,
                        DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.RECHARGE_SITE,
                        DigitalCheckoutTrackingConst.Label.USER_ID, userId,
                        BaseTrackerConst.Ecommerce.KEY, DataLayer.mapOf(
                        DigitalCheckoutTrackingConst.Event.CHECKOUT, DataLayer.mapOf(
                        DigitalCheckoutTrackingConst.Label.ACTION_FIELD, DataLayer.mapOf(
                        DigitalCheckoutTrackingConst.Label.STEP, "2",
                        DigitalCheckoutTrackingConst.Label.OPTION, DigitalCheckoutTrackingConst.Misc.ACTION_FIELD_STEP2),
                        DigitalCheckoutTrackingConst.Label.PRODUCTS, DataLayer.listOf(
                        *fintechProductList.toTypedArray())))
                )
        )
    }

    fun eventProceedCheckoutTebusMurah(fintechProduct: FintechProduct, categoryName: String, userId: String) {
        val fintechProductList: MutableList<Any> = ArrayList()
        fintechProductList.add(constructFintechProductOnCheckout(fintechProduct, categoryName))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, DigitalCheckoutTrackingConst.Event.CHECKOUT,
                        TrackAppUtils.EVENT_CATEGORY, DigitalCheckoutTrackingConst.Category.DIGITAL_CHECKOUT_PAGE,
                        TrackAppUtils.EVENT_ACTION, DigitalCheckoutTrackingConst.Action.CLICK_PROCEED_PAYMENT_TEBUS_MURAH,
                        TrackAppUtils.EVENT_LABEL, "$categoryName - ${fintechProduct.info.title}",
                        DigitalCheckoutTrackingConst.Label.BUSINESS_UNIT, DigitalCheckoutTrackingConst.Value.RECHARGE_BU,
                        DigitalCheckoutTrackingConst.Label.CURRENTSITE, DigitalCheckoutTrackingConst.Value.RECHARGE_SITE,
                        DigitalCheckoutTrackingConst.Label.USER_ID, userId,
                        BaseTrackerConst.Ecommerce.KEY, DataLayer.mapOf(
                        DigitalCheckoutTrackingConst.Event.CHECKOUT, DataLayer.mapOf(
                        DigitalCheckoutTrackingConst.Label.ACTION_FIELD, DataLayer.mapOf(
                        DigitalCheckoutTrackingConst.Label.STEP, "2",
                        DigitalCheckoutTrackingConst.Label.OPTION, DigitalCheckoutTrackingConst.Misc.ACTION_FIELD_STEP2_TEBUS_MURAH),
                        DigitalCheckoutTrackingConst.Label.PRODUCTS, DataLayer.listOf(
                        *fintechProductList.toTypedArray())))
                )
        )
    }

    private fun constructProductEnhanceEcommerce(cartDigitalInfoData: CartDigitalInfoData,
                                                 productName: String, categoryId: String): Map<String?, Any?> {
        return DataLayer.mapOf(
                DigitalCheckoutTrackingConst.Product.KEY_NAME, productName,
                DigitalCheckoutTrackingConst.Product.KEY_ID, cartDigitalInfoData.productId,
                DigitalCheckoutTrackingConst.Product.KEY_PRICE, cartDigitalInfoData.attributes.pricePlain.toString(),
                DigitalCheckoutTrackingConst.Product.KEY_BRAND, cartDigitalInfoData.attributes.operatorName?.toLowerCase(),
                DigitalCheckoutTrackingConst.Product.KEY_CATEGORY, cartDigitalInfoData.attributes.categoryName?.toLowerCase(),
                DigitalCheckoutTrackingConst.Product.KEY_VARIANT, DigitalCheckoutTrackingConst.Value.NONE,
                DigitalCheckoutTrackingConst.Product.KEY_QUANTITY, "1",
                DigitalCheckoutTrackingConst.Product.KEY_CATEGORY_ID, categoryId,
                DigitalCheckoutTrackingConst.Product.KEY_CART_ID, cartDigitalInfoData.id,
                DigitalCheckoutTrackingConst.Product.KEY_SHOP_ID, DigitalCheckoutTrackingConst.Value.NONE,
                DigitalCheckoutTrackingConst.Product.KEY_SHOP_NAME, DigitalCheckoutTrackingConst.Value.NONE,
                DigitalCheckoutTrackingConst.Product.KEY_SHOP_TYPE, DigitalCheckoutTrackingConst.Value.NONE
        )
    }

    private fun constructFintechProduct(fintechProduct: FintechProduct, categoryName: String, position: Int): Map<String?, Any?> {
        return DataLayer.mapOf(
                //will be fill with operator_name
                DigitalCheckoutTrackingConst.Product.KEY_BRAND, fintechProduct.operatorName,
                DigitalCheckoutTrackingConst.Product.KEY_CATEGORY, categoryName,
                DigitalCheckoutTrackingConst.Product.KEY_ID, fintechProduct.tierId,
                DigitalCheckoutTrackingConst.Product.KEY_LIST, "/checkout - ${fintechProduct.info.title} $position - $CROSSELL_CARD_TYPE",
                DigitalCheckoutTrackingConst.Product.KEY_NAME, fintechProduct.info.title,
                DigitalCheckoutTrackingConst.Product.KEY_POSITION, position,
                DigitalCheckoutTrackingConst.Product.KEY_PRICE, fintechProduct.fintechAmount,
                DigitalCheckoutTrackingConst.Product.KEY_VARIANT, fintechProduct.fintechPartnerAmount.toString()
        )
    }

    private fun constructTebusMurahFintechProduct(fintechProduct: FintechProduct, categoryName: String, position: Int): Map<String?, Any?> {
        return DataLayer.mapOf(
                //will be fill with operator_name
                DigitalCheckoutTrackingConst.Product.KEY_BRAND, fintechProduct.operatorName,
                DigitalCheckoutTrackingConst.Product.KEY_CATEGORY, categoryName,
                DigitalCheckoutTrackingConst.Product.KEY_ID, fintechProduct.tierId,
                DigitalCheckoutTrackingConst.Product.KEY_LIST, "/checkout - ${fintechProduct.info.title} - tebus murah",
                DigitalCheckoutTrackingConst.Product.KEY_NAME, fintechProduct.info.title,
                DigitalCheckoutTrackingConst.Product.KEY_POSITION, position,
                DigitalCheckoutTrackingConst.Product.KEY_PRICE, fintechProduct.fintechAmount,
                DigitalCheckoutTrackingConst.Product.KEY_VARIANT, fintechProduct.fintechPartnerAmount.toString()
        )
    }

    private fun constructFintechProductOnCheckout(fintechProduct: FintechProduct, categoryName: String): Map<String?, Any?> {
        return DataLayer.mapOf(
                //will be fill with operator_name
                DigitalCheckoutTrackingConst.Product.KEY_BRAND, fintechProduct.operatorName,
                DigitalCheckoutTrackingConst.Product.KEY_CATEGORY, categoryName,
                DigitalCheckoutTrackingConst.Product.KEY_ID, fintechProduct.tierId,
                DigitalCheckoutTrackingConst.Product.KEY_NAME, fintechProduct.info.title,
                DigitalCheckoutTrackingConst.Product.KEY_PRICE, fintechProduct.fintechAmount,
                DigitalCheckoutTrackingConst.Product.KEY_QUANTITY, "1",
                DigitalCheckoutTrackingConst.Product.KEY_SHOP_ID, DigitalCheckoutTrackingConst.Value.NONE,
                DigitalCheckoutTrackingConst.Product.KEY_SHOP_NAME, DigitalCheckoutTrackingConst.Value.NONE,
                DigitalCheckoutTrackingConst.Product.KEY_SHOP_TYPE, DigitalCheckoutTrackingConst.Value.NONE,
                DigitalCheckoutTrackingConst.Product.KEY_VARIANT, fintechProduct.fintechPartnerAmount.toString(),
        )
    }

}