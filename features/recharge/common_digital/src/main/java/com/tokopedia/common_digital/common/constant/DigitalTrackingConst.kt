package com.tokopedia.common_digital.common.constant

/**
 * @author by jessica on 26/02/21
 */

object DigitalTrackingConst {

    object Event {
        const val ADD_TO_CART = "addToCart"
        const val VIEW_DIGITAL_IRIS = "viewDigitalIris"
    }

    object Category {
        const val DIGITAL_NATIVE = "digital - native"
        const val DIGITAL_HOMEPAGE = "digital - homepage"
        const val HOMEPAGE_DIGITAL_WIDGET = "homepage digital widget"
    }

    object Action {
        const val CLICK_BELI = "click beli"
        const val VIEW_PDP_PAGE = "view pdp page"
    }

    object Label {
        const val ADD = "add"
        const val PRODUCTS = "products"
        const val CURRENTSITE = "currentSite"
        const val BUSINESS_UNIT = "businessUnit"
        const val USER_ID = "userId"
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
        const val KEY_SHOP_ID = "shop_id"
        const val KEY_SHOP_NAME = "shop_name"
        const val KEY_SHOP_TYPE = "shop_type"
    }

    object Value {
        const val NONE = "none"
        const val RECHARGE_SITE = "tokopediadigitalRecharge"
        const val RECHARGE_BU = "recharge"
    }

    object CurrencyCode {
        const val KEY = "currencyCode"
        const val IDR = "IDR"
    }
}