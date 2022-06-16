package com.tokopedia.product.manage.common.util

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object ProductManageAdapterLogger {

    object MethodName {
        const val SHOW_LOADING = "showLoading"
        const val HIDE_LOADING = "hideLoading"
        const val REMOVE_ERROR_NETWORK = "removeErrorNetwork"
        const val ADD_ELEMENT = "addElement"
        const val CLEAR_ALL_ELEMENTS = "clearAllElements"
        const val UPDATE_PRODUCT = "updateProduct"
        const val REMOVE_AND_UPDATE_LAYOUT = "removeEmptyAndUpdateLayout"
        const val CHECK_ALL_PRODUCTS = "checkAllProducts"
        const val UNCHECK_MULTIPLE_PRODUCTS = "unCheckMultipleProducts"
        const val UPDATE_EMPTY_STATE = "updateEmptyState"
        const val UPDATE_PRICE = "updatePrice"
        const val UPDATE_PRICE_VARIANT = "updatePriceVariant"
        const val UPDATE_STOCK = "updateStock"
        const val UPDATE_CASHBACK = "updateCashBack"
        const val DELETE_PRODUCT = "deleteProduct"
        const val DELETE_PRODUCTS = "deleteProducts"
        const val UPDATE_FEATURED_PRODUCT = "updateFeaturedProduct"
        const val SET_PRODUCTS_STATUSES = "setProductsStatuses"
        const val SET_MULTI_SELECT_ENABLED = "setMultiSelectEnabled"
        const val FILTER_PRODUCT_LIST = "filterProductList"
    }

    private const val DEBUG_TAG = "DEBUG_PRODUCT_MANAGE_ADAPTER"

    private const val DEVICE_ID_KEY = "device_id"
    private const val TYPE_KEY = "type"

    fun logUpdate(deviceId: String,
                  methodName: String) {
        ServerLogger.log(
            Priority.P2,
            DEBUG_TAG,
            getUpdateMap(deviceId, methodName)
        )
    }

    private fun getUpdateMap(deviceId: String,
                             methodName: String): Map<String, String> {
        return mapOf(
            DEVICE_ID_KEY to deviceId,
            TYPE_KEY to methodName
        )
    }

}