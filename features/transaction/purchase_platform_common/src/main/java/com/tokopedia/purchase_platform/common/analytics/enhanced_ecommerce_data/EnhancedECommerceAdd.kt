package com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by Irfan Khoirul on 2019-07-01.
 */

class EnhancedECommerceAdd {

    companion object {
        @JvmStatic
        val KEY_ADD = "add"
        const val KEY_PRODUCT = "products"
        const val KEY_ACTION_FIELD = "actionField"
    }

    private val checkoutMap = HashMap<String, Any>()
    private val listProducts = ArrayList<Any>()

    fun addProduct(Product: Map<String, Any>) {
        listProducts.add(Product)
    }

    fun setActionField(actionFieldMap: Map<String, String>) {
        checkoutMap[KEY_ACTION_FIELD] = actionFieldMap
    }

    fun getAddMap(): Map<String, Any> {
        checkoutMap[KEY_PRODUCT] = listProducts
        return checkoutMap
    }
}
