package com.tokopedia.recommendation_widget_common

object RecommendationTrackingConstants {
    object Tracking {
        const val DEFAULT_VALUE = ""
        const val DEFAULT_QUANTITY = 0

        const val TRACKER_ID = "trackerId"

        const val ITEM_LIST = "item_list"
        const val ITEMS = "items"
        const val PROMOTIONS = "promotions"

        const val PRODUCT_ID = "productId"

        const val ECOMMERCE = "ecommerce"

        const val CURRENT_SITE_MP = "tokopediamarketplace"

        const val CATEGORY_PDP = "product detail page"
        const val BUSINESS_UNIT_HOME = "home & browse"
        const val BUSINESS_UNIT_PG = "Physical Goods"

        const val CATEGORY_ID = "category_id"
        const val DIMENSION_40 = "dimension40"
        const val DIMENSION_45 = "dimension45"
        const val DIMENSION_56 = "dimension56"
        const val DIMENSION_84 = "dimension84"
        const val DIMENSION_90 = "dimension90"

        const val ITEM_BRAND = "item_brand"
        const val ITEM_CATEGORY = "item_category"
        const val ITEM_ID = "item_id"
        const val ITEM_NAME = "item_name"
        const val ITEM_VARIANT = "item_variant"

        const val PRICE = "price"
        const val QUANTITY = "quantity"

        const val SHOP_ID = "shop_id"
        const val SHOP_NAME = "shop_name"
        const val SHOP_TYPE = "shop_type"

        const val KEY_INDEX = "index"

        const val IMPRESSIONS = "impressions"
        const val CURRENCY_CODE = "currencyCode"
        const val IDR = "IDR"

        const val VALUE_NONE_OTHER = "none / other"
        const val VALUE_IS_TOPADS = "- product topads"

        const val LIST = "list"

        fun String.convertToWidgetType(): String = replace("-", " ")

        const val CLICK_HOMEPAGE = "clickHomepage"
        const val CLICK_PG = "clickPG"
    }

    object Action {
        const val PROMO_VIEW = "promoView"
        const val PRODUCT_VIEW = "productView"
        const val VIEW_ITEM_LIST = "view_item_list"

        const val SELECT_CONTENT = "select_content"
        const val PRODUCT_CLICK = "productClick"

        const val EVENT_ADD_TO_CART = "addToCart"
        const val ADD_TO_CART = "add_to_cart"
    }
}
