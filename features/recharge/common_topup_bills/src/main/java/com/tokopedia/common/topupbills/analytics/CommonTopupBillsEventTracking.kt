package com.tokopedia.common.topupbills.analytics

/**
 * @author by resakemal on 27/08/19.
 */

interface CommonTopupBillsEventTracking {

    interface Event {
        companion object {
            const val CLICK_HOMEPAGE = "clickHomepage"
            const val ADD_TO_CART = "addToCart"
            const val CHECKOUT = "checkout"
        }
    }

    interface Category {
        companion object {
            const val DIGITAL_NATIVE = "digital - native"
            const val DIGITAL_HOMEPAGE = "digital - homepage"
            const val DIGITAL_CHECKOUT = "digital - checkout"
        }
    }

    interface Action {
        companion object {
            const val CLICK_USE_PROMO = "click pakai promo"
            const val CLICK_BUY = "click beli"
            const val VIEW_CHECKOUT = "view checkout"
            const val CLICK_PROCEED_TO_PAYMENT = "click proceed to payment"
            const val CLICK_REMOVE_PROMO = "click 'x' on promo"
        }
    }

    interface Label {
        companion object {
            const val INSTANT = "instant"
            const val NO_INSTANT = "no instant"
            const val PROMO = "promo"
            const val NO_PROMO = "no promo"
        }
    }

    interface EnhanceEccomerce {
        companion object {
            const val ECOMMERCE = "ecommerce"
            const val CURRENCY_CODE = "currencyCode"
            const val DEFAULT_CURRENCY_CODE = "IDR"
            const val ADD = "add"
            const val PRODUCTS = "products"
            const val CHECKOUT = "checkout"

            const val NAME = "name"
            const val ID = "id"
            const val PRICE = "price"
            const val BRAND = "brand"
            const val CATEGORY = "category"
            const val VARIANT = "variant"
            const val QUANTITY = "quantity"
            const val DIMENSION_45 = "dimension45"
            const val DIMENSION_79 = "dimension79"
            const val DIMENSION_80 = "dimension80"
            const val DIMENSION_81 = "dimension81"
            const val DIMENSION_82 = "dimension82"
        }
    }

    interface ActionField {
        companion object {
            const val ACTION_FIELD = "actionField"
            const val ACTION_FIELD_STEP = "step"
            const val ACTION_FIELD_OPTION = "option"

            const val ACTION_FIELD_VIEW_CHECKOUT = "view checkout page"
            const val ACTION_FIELD_CLICK_CHECKOUT = "click checkout"
        }
    }
}
