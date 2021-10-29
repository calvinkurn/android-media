package com.tokopedia.cart.view.analytics

import java.util.HashMap

/**
 * Created by Irfan Khoirul on 2019-09-03.
 */

class EnhancedECommerceClickData {
    private val data = HashMap<String, Any>()

    fun setActionField(actionFieldData: Map<String, Any>) {
        this.data.put(KEY_ACTION_FIELD, actionFieldData)
    }

    fun setProducts(productsData: List<Map<String, Any>>) {
        this.data.put(KEY_PRODUCTS, productsData)
    }

    fun getData(): Map<String, Any> {
        return this.data
    }

    companion object {
        private val KEY_ACTION_FIELD = "actionField"
        private val KEY_PRODUCTS = "products"
    }
}
