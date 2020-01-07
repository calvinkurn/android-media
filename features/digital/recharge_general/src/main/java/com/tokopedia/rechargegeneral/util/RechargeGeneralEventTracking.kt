package com.tokopedia.rechargegeneral.util

/**
 * @author by resakemal on 27/08/19.
 */

interface RechargeGeneralEventTracking {

    interface Event {
        companion object {
            val CLICK_HOMEPAGE = "clickHomepage"
            val CLICK_CATEGORY = "clickCategory"
            val PROMO_VIEW = "promoView"
            val PROMO_CLICK = "promoClick"
            val PRODUCT_VIEW = "productView"
            val PRODUCT_CLICK = "productClick"
            val ADD_TO_CART = "addToCart"
        }
    }

    interface Category {
        companion object {
            val DIGITAL_HOMEPAGE = "digital - homepage"
            val DIGITAL_CATEGORY = "digital - category page"
            val DIGITAL_NATIVE = "digital - native"
        }
    }

    interface Action {
        companion object {
            val CLICK_OPERATOR_CLUSTER_DROPDOWN = "click dropdown operator cluster list"
            val CHOOSE_OPERATOR_CLUSTER = "choose operator cluster"
            val CLICK_OPERATOR_LIST_DROPDOWN = "click dropdown operator list"
            val CHOOSE_OPERATOR = "choose operator"
            val CLICK_PRODUCT_LIST_DROPDOWN = "click dropdown product list"
            val CLICK_PRODUCT_CARD = "click on product card"
            val INPUT_MANUAL_NUMBER = "input manual number"
            val CLICK_CHECK_BILLS = "click check tagihan"
            val CHECKLIST_SUBSCRIPTION_BOX = "checklist subscription box"
            val CLICK_CLOSE_INQUIRY = "click close on inquiry"
            val CLICK_BUY = "click beli"
            val CLICK_RECENT_ICON = "click recent icon"
            val CLICK_PROMO_TAB = "click promo tab"
            val CLICK_COPY_PROMO = "click salin promo diigtal"
            val CLICK_BACK = "user click back button from PDP"
            val INPUT_FAVORITE_NUMBER = "input from favorite number"
        }
    }

    interface EnhanceEccomerce {
        companion object {
            val NAME = "name"
            val ID = "id"
            val PRICE = "price"
            val BRAND = "brand"
            val CATEGORY = "category"
            val LIST = "list"
            val POSITION = "position"
            val CREATIVE = "creative"
            val CREATIVE_URL = "creative_url"
            val PROMO_ID = "promo_id"
            val PROMO_CODE = "promo_code"
            val PROMOTIONS = "promotions"
            val QUANTITY = "quantity"
        }
    }
}
