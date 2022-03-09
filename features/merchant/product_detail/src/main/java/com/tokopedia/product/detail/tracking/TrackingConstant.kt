package com.tokopedia.product.detail.tracking

/**
 * we provide constant for PDP tracking
 * to avoid miss-spelling while build the tracker.
 * please check the availability before add new constant.
 *
 * rules:
 * - insert the new constant in name ascending
 * - will update later.
 *
 * for shorter code you can do
 * import com.tokopedia.product.detail.tracking.TrackingConstant.Hit
 * import com.tokopedia.product.detail.tracking.TrackingConstant.Item
 * import com.tokopedia.product.detail.tracking.TrackingConstant.Value
 *
 * it will give you Hit.BUSINESS_UNIT instead of TrackingConstant.Hit.BUSINESS_UNIT
 *
 */
object TrackingConstant {
    /**
     * Hit constants provided for Key in Hit Level
     * which naming convention is camelCase
     *
     * please take a look at usage to understand Hit level.
     */
    object Hit {
        const val BUSINESS_UNIT = "businessUnit"
        const val COMPONENT = "component"
        const val CURRENT_SITE = "currentSite"
        const val ECOMMERCE = "ecommerce"
        const val EVENT = "event"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_LABEL = "eventLabel"
        const val LAYOUT = "layout"
        const val PRODUCT_ID = "productId"
        const val PROMOTIONS = "promotions"
        const val PROMO_VIEW = "promoView"
        const val SHOP_ID = "shopId"
        const val SHOP_TYPE = "shopType"
        const val USER_ID = "userId"

    }

    /**
     * Item constants provided for Key in Item Level
     * which naming convention is camelCase
     *
     * please take a look at usage to understand Item level.
     */
    object Item{
        const val CREATIVE_NAME = "creative_name"
        const val CREATIVE_SLOT = "creative_slot"
        const val ITEM_ID = "item_id"
        const val ITEM_NAME = "item_name"
    }

    /**
     * Value constants
     * only add if your value is common (reusable).
     *
     * example:
     * "promoView" -> PROMO_VIEW
     * value for event impression across pdp tracker
     *
     * "impression - shop component ticker"
     * this value only use in 1 tracking ImpressionShopTicker
     * you should create constant in the its own Tracking Object
     * instead of here.
     *
     * For formatted value like "shop_id:$shopId;"
     * still in progress for the rules.
     * you can write as is in your Tracking Object.
     * will update when find another idea.
     *
     */
    object Value{
        const val PROMO_VIEW = "promoView"
        const val PRODUCT_DETAIL_PAGE = "product detail page"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    }

}