package com.tokopedia.digital_checkout.utils.analytics

/**
 * @author by jessica on 22/01/21
 */

object DigitalCheckoutTrackingConst {
    object Event {
        const val CLICK_COUPON = "clickCoupon"
        const val ADD_TO_CART = "addToCart"
        const val CHECKOUT = "checkout"
        const val CLICK_CHECKOUT = "clickCheckout"
    }

    object Category {
        const val DIGITAL_CHECKOUT = "digital - checkout"
        const val DIGITAL_CHECKOUT_PAGE = "digital - checkout page"
        const val DIGITAL_NATIVE = "digital - native"
        const val HOMEPAGE_DIGITAL_WIDGET = "homepage digital widget"
    }

    object Action {
        const val CLICK_CANCEL_APPLY_COUPON = "click x on ticker"
        const val CLICK_BELI = "click beli"
        const val VIEW_CHECKOUT = "view checkout"
        const val CLICK_PROCEED_PAYMENT = "click proceed to payment"
        const val CLICK_PROMO = "click promo button"
        const val CLICK_USE_COUPON = "click gunakan kode promo atau kupon"

        const val TICK_AUTODEBIT = "tick auto debit"
        const val TICK_CROSSSELL = "tick cross sell"
        const val TICK_PROTECTION = "tick protection"
        const val UNTICK_AUTODEBIT = "untick auto debit"
        const val UNTICK_CROSSSELL = "untick cross sell"
        const val UNTICK_PROTECTION = "untick protection"
    }

    object Label {
        const val ACTION_FIELD = "actionField"
        const val STEP = "step"
        const val OPTION = "option"
        const val PRODUCTS = "products"
        const val CURRENTSITE = "currentSite"
        const val BUSINESS_UNIT = "businessUnit"
        const val ADD = "add"

        const val USER_ID = "userId"
    }

    object Value {
        const val RECHARGE_BU = "recharge"
        const val SITE = "tokopediadigital"

        const val NO_PROMO = "no promo"
        const val PROMO = "no promo"

        const val INSTANT = "instant"
        const val NON_INSTANT = "non instant"
    }

    object Misc {
        const val ACTION_FIELD_STEP1 = "cart page loaded"
        const val ACTION_FIELD_STEP2 = "click payment option button"
    }

    object CurrencyCode {
        const val KEY = "currencyCode"
        const val IDR = "IDR"
    }

    object Product {
        const val KEY_NAME = "name"
        const val KEY_ID = "id"
        const val KEY_PRICE = "price"
        const val KEY_BRAND = "brand"
        const val KEY_CATEGORY = "category"
        const val KEY_VARIANT = "variant"
        const val KEY_QUANTITY = "quantity"
        const val KEY_CATEGORY_ID = "category_id"
        const val KEY_CART_ID = "cart_id"
    }

    object Screen {
        const val DIGITAL_CHECKOUT = "/digital/checkout"
    }
}
