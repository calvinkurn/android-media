package com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data

import java.util.*

/**
 * @author anggaprasetiyo on 31/07/18.
 */
class EnhancedECommerceCheckout {
    private val checkoutMap: MutableMap<String, Any> = HashMap()
    private val listProducts: MutableList<Any> = ArrayList()
    fun addProduct(Product: Map<String, Any>) {
        listProducts.add(Product)
    }

    fun setCurrencyCode(currencyCode: String) {
        checkoutMap[KEY_CURRENCY_CODE] = currencyCode
    }

    fun setActionField(actionFieldMap: Map<String, String>) {
        checkoutMap[KEY_ACTION_FIELD] = actionFieldMap
    }

    fun getCheckoutMap(): Map<String, Any> {
        checkoutMap[KEY_PRODUCT] = listProducts
        return checkoutMap
    }

    companion object {
        const val KEY_PRODUCT = "products"
        private const val KEY_CURRENCY_CODE = "currencyCode"
        const val KEY_ACTION_FIELD = "actionField"
        const val KEY_CHECKOUT = "checkout"
    }
}