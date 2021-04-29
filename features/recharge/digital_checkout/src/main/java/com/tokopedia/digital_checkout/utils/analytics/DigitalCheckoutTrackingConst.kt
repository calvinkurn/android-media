package com.tokopedia.digital_checkout.utils.analytics

/**
 * @author by jessica on 22/01/21
 */

object DigitalCheckoutTrackingConst {
    object Event {
        const val CLICK_COUPON = "clickCoupon"
        const val CHECKOUT = "checkout"
        const val CLICK_CHECKOUT = "clickCheckout"
        const val PRODUCT_VIEW = "productView"
        const val PRODUCT_CLICK = "productClick"
        const val DIGITAL_GENERAL_EVENT = "digitalGeneralEvent"
    }

    object Category {
        const val DIGITAL_CHECKOUT = "digital - checkout"
        const val DIGITAL_CHECKOUT_PAGE = "digital - checkout page"
    }

    object Action {
        const val CLICK_CANCEL_APPLY_COUPON = "click x on ticker"
        const val VIEW_CHECKOUT = "view checkout"
        const val CLICK_PROCEED_PAYMENT = "click proceed to payment"
        const val CLICK_PROCEED_PAYMENT_CROSSELL = "click proceed to payment crossell"
        const val CLICK_PROCEED_PAYMENT_TEBUS_MURAH = "click proceed to payment - tebus murah"
        const val CLICK_PROMO = "click promo button"
        const val CLICK_USE_COUPON = "click gunakan kode promo atau kupon"

        const val TICK_AUTODEBIT = "tick auto debit"
        const val TICK_CROSSSELL = "click crossell ticker"
        const val UNTICK_CROSSSELL = "uncheck crossell ticker"
        const val UNTICK_AUTODEBIT = "untick auto debit"

        const val IMPRESSION_TEBUS_MURAH_ICON = "impression tebus murah icon"
        const val CLICK_TEBUS_MURAH_ICON = "click tebus murah icon"
        const val UNCHECK_TEBUS_MURAH_ICON = "uncheck tebus murah icon"


        const val IMPRESSION_CROSSELL_ICON = "impression crossell icon"
    }

    object Label {
        const val ACTION_FIELD = "actionField"
        const val STEP = "step"
        const val OPTION = "option"
        const val PRODUCTS = "products"
        const val CURRENTSITE = "currentSite"
        const val BUSINESS_UNIT = "businessUnit"

        const val USER_ID = "userId"
        const val IMPRESSIONS = "impressions"
        const val CLICK = "click"
    }

    object Value {
        const val RECHARGE_BU = "recharge"
        const val SITE = "tokopediadigital"
        const val RECHARGE_SITE = "tokopediadigitalRecharge"

        const val NO_PROMO = "no promo"
        const val PROMO = "no promo"

        const val NONE = "none"
        const val CROSSELL_CART_TYPE = "crossell icon"
    }

    object Misc {
        const val ACTION_FIELD_STEP1 = "cart page loaded"
        const val ACTION_FIELD_STEP2 = "click payment option button"

        const val ACTION_FIELD_STEP2_TEBUS_MURAH = "click proceed to payment"
    }

    object CurrencyCode {
        const val KEY = "currencyCode"
        const val IDR = "IDR"
    }

    object Product {
        const val KEY_NAME = "name"
        const val KEY_ID = "id"
        const val KEY_LIST = "list"
        const val KEY_PRICE = "price"
        const val KEY_BRAND = "brand"
        const val KEY_POSITION = "position"
        const val KEY_CATEGORY = "category"
        const val KEY_VARIANT = "variant"
        const val KEY_QUANTITY = "quantity"
        const val KEY_CATEGORY_ID = "category_id"
        const val KEY_CART_ID = "cart_id"
        const val KEY_SHOP_ID = "shop_id"
        const val KEY_SHOP_NAME = "shop_name"
        const val KEY_SHOP_TYPE = "shop_type"
    }

    object Screen {
        const val DIGITAL_CHECKOUT = "/digital/checkout"
    }
}
