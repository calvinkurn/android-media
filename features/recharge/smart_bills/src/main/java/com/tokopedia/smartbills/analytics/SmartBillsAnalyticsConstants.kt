package com.tokopedia.smartbills.analytics

/**
 * @author by resakemal on 17/05/20
 */

interface SmartBillsAnalyticConstants {

    interface Event {
        companion object {
            const val CLICK_SMART_BILLS = "clickSmartBill"
            const val PRODUCT_VIEW = "productView"
            const val PRODUCT_CLICK = "productClick"
            const val CHECKOUT = "checkout"
        }
    }

    interface Action {
        companion object {
            const val CLICK_LANGGANAN = "click langganan"
            const val CLICK_ALL_TAGIHAN = "click all tagihan"
            const val UNCLICK_ALL_TAGIHAN = "unclick all tagihan"
            const val CLICK_TICK_BILL = "click tick bill"
            const val CLICK_UNTICK_BILL = "click untick bill"
            const val IMPRESSION_ALL_PRODUCT = "impression all product"
            const val CLICK_BAYAR = "click bayar"
            const val CLICK_BAYAR_FULL = "click bayar full"
            const val CLICK_BAYAR_PARTIAL = "click bayar partial"
            const val CLICK_BAYAR_FAILED = "click bayar gagal"
            const val CLICK_DETAIL = "click detail"
            const val CLICK_TOOL_TIP = "click on tooltip icon"
            const val CLICK_MORE_LEARN = "click pelajari selengkapnya"
        }
    }

    interface Label {
        companion object {
            const val LANGGANAN = "langganan"
            const val ALL_TAGIHAN = "all tagihan"
            const val BAYAR = "bayar"
            const val GAGAL = "gagal"
        }
    }

    interface Key {
        companion object {
            const val USER_ID = "userId"
            const val SCREEN_NAME = "screenName"
            const val CURRENT_SITE = "currentSite"
            const val BUSINESS_UNIT = "businessUnit"
            const val IS_LOGIN_STATUS = "isLoggedInStatus"
        }
    }

    interface EnhanceEccomerce {
        companion object {
            const val ECOMMERCE = "ecommerce"
            const val CLICK = "click"
            const val CHECKOUT = "checkout"
            const val ACTION_FIELD = "actionField"
            const val LIST = "list"
            const val STEP = "step"
            const val OPTION = "option"
            const val PRODUCTS = "products"
            const val CURRENCY_CODE = "currencyCode"
            const val IMPRESSIONS = "impressions"
            const val NAME = "name"
            const val ID = "id"
            const val PRICE = "price"
            const val BRAND = "brand"
            const val CATEGORY = "category"
            const val VARIANT = "variant"
            const val POSITION = "position"
            const val QUANTITY = "quantity"
            const val NONE = "none/other"
        }
    }
}
