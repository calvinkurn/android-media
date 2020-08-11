package com.tokopedia.cart.view.analytics

import java.util.HashMap

/**
 * Created by Irfan Khoirul on 01/10/18.
 */

class EnhancedECommerceActionFieldData {

    private val data = HashMap<String, Any>()

    fun getData(): Map<String, Any> {
        return data
    }

    fun setList(value: String) {
        data[KEY_LIST] = value
    }

    companion object {
        private val KEY_LIST = "list"
        val VALUE_SECTION_NAME_WISHLIST = "/cart - wishlist"
        val VALUE_SECTION_NAME_RECENT_VIEW = "/cart - recent view"
        val VALUE_SECTION_NAME_RECOMMENDATION = "/cart - rekomendasi untuk anda"
        val VALUE_SECTION_NAME_RECENT_VIEW_EMPTY_CART = "/cart empty - recent view"
        val VALUE_SECTION_NAME_WISHLIST_EMPTY_CART = "/cart empty - wishlist"
    }
}