package com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data

import java.util.*

/**
 * @author anggaprasetiyo on 05/06/18.
 */
class EnhancedECommerceCartMapData {
    private val cart: MutableMap<String, Any> = HashMap()
    private val act: MutableMap<String, Any> = HashMap()
    private val listProducts: MutableList<Any> = ArrayList()
    private val listImpressions: MutableList<Any> = ArrayList()

    fun setCurrencyCode(currencyCode: String) {
        cart[KEY_CURRENCY] = currencyCode
    }

    fun addProduct(Product: Map<String, Any>) {
        listProducts.add(Product)
    }

    fun setAction(action: String) {
        act[KEY_PRODUCTS] = listProducts
        cart[action] = act
    }

    fun addImpression(Impression: Map<String, Any>) {
        listImpressions.add(Impression)
        cart[KEY_IMPRESSIONS] = listImpressions
    }

    fun addClick(Click: Map<String?, Any?>) {
        cart[KEY_CLICK] = Click
    }

    val cartMap: Map<String, Any>
        get() = cart

    companion object {
        const val VALUE_CURRENCY_IDR = "IDR"
        const val ADD_ACTION = "add"
        const val REMOVE_ACTION = "remove"
        private const val KEY_CURRENCY = "currencyCode"
        const val KEY_PRODUCTS = "products"
        const val KEY_IMPRESSIONS = "impressions"
        const val KEY_CLICK = "click"
    }
}