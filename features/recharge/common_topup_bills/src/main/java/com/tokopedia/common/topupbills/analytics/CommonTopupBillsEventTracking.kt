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
            const val DIGITAL_GENERAL_EVENT = "digitalGeneralEvent"
            const val DIGITAL_GENERAL_EVENT_IRIS = "digitalGeneralEventIris"
        }
    }

    interface Category {
        companion object {
            const val DIGITAL_NATIVE = "digital - native"
            const val DIGITAL_HOMEPAGE = "digital - homepage"
            const val DIGITAL_CHECKOUT = "digital - checkout"
            const val DIGITAL_PDP_FAVORITE_NUMBER = "digital - pdp favorite number"
        }
    }

    interface Action {
        companion object {
            const val CLICK_USE_PROMO = "click pakai promo"
            const val CLICK_BUY = "click beli"
            const val VIEW_CHECKOUT = "view checkout"
            const val CLICK_PROCEED_TO_PAYMENT = "click proceed to payment"
            const val CLICK_REMOVE_PROMO = "click 'x' on promo"
            const val VIEW_EMPTY_FAVORITE_NUMBER= "view empty favorite number"
            const val VIEW_COACHMARK = "view coachmark"
            const val CLICK_CONTINUE = "click lanjut transaksi"
            const val CLICK_KEBAB_MENU = "click kebab menu"
            const val VIEW_EDIT_BOTTOM_SHEET = "view edit bottom sheet"
            const val CLICK_SAVE_BOTTOM_SHEET = "click simpan on edit bottom sheet"
            const val VIEW_DELETION_POP_UP = "view deletion pop up"
            const val CLICK_CONFIRM_DELETE_POP_UP = "click hapus on deletion pop up"
            const val VIEW_DELETION_SUCCESS_TOASTER = "view deletion success toaster"
            const val VIEW_DELETION_FAILED_TOASTER = "view deletion failed toaster"
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

            const val EMPTY = "none/other"
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

    interface General {
        interface Key {
            companion object {
                const val BUSINESS_UNIT = "businessUnit"
                const val CURRENT_SITE = "currentSite"
                const val USER_ID ="userId"
            }
        }

        interface Value {
            companion object {
                const val BUSINESS_UNIT_RECHARGE = "recharge"
                const val CURRENT_SITE_RECHARGE = "tokopediadigitalRecharge"
            }
        }
    }
}
