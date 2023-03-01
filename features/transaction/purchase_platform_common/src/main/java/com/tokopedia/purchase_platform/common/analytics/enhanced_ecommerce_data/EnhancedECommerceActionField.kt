package com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data

import java.util.*

/**
 * @author anggaprasetiyo on 31/07/18.
 */
class EnhancedECommerceActionField {
    private val actionFieldMap: MutableMap<String, String> = HashMap()

    fun setStep(step: String) {
        actionFieldMap[KEY_STEP] = step
    }

    fun setOption(option: String) {
        actionFieldMap[KEY_OPTION] = option
    }

    fun setList(list: String) {
        actionFieldMap[KEY_LIST] = list
    }

    fun getActionFieldMap(): Map<String, String> {
        return actionFieldMap
    }

    companion object {
        const val STEP_0 = "0"
        const val STEP_1 = "1"
        const val STEP_2 = "2"
        const val STEP_3 = "3"
        const val STEP_4 = "4"
        const val STEP_0_OPTION_VIEW_CART_PAGE = "view cart page"
        const val STEP_1_OPTION_CART_PAGE_LOADED = "cart page loaded"
        const val STEP_2_OPTION_CHECKOUT_PAGE_LOADED = "checkout page loaded"
        const val STEP_3_OPTION_DATA_VALIDATION = "data validation"
        const val STEP_4_OPTION_CLICK_PAYMENT_OPTION_BUTTON = "click_payment option button"
        const val OPTION_CLICK_CHECKOUT = "click checkout"
        const val LIST_WISHLIST = "/cart - wishlist"
        const val LIST_WISHLIST_ON_EMPTY_CART = "/cart empty - wishlist"
        const val LIST_RECENT_VIEW = "/cart - recent view"
        const val LIST_RECENT_VIEW_ON_EMPTY_CART = "/cart empty - recent view"
        const val LIST_RECOMMENDATION = "/recommendation - primary product"
        const val LIST_CART_RECOMMENDATION = "/cart - rekomendasi untuk anda - "
        const val LIST_CART_RECOMMENDATION_ON_EMPTY_CART = "/cart - rekomendasi untuk anda - empty_cart - "
        const val LIST_CART_RECOMMENDATION_TOPADS_TYPE = " - product topads"
        private const val KEY_STEP = "step"
        private const val KEY_OPTION = "option"
        const val KEY_LIST = "list"
    }
}
