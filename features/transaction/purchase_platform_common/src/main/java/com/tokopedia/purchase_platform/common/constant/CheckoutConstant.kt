package com.tokopedia.purchase_platform.common.constant

import androidx.arch.core.executor.DefaultTaskExecutor

/**
 * Created by Irfan Khoirul on 2019-09-24.
 */

class CheckoutConstant {

    companion object {
        const val CHECKOUT = "CHECKOUT"
        const val REQUEST_CODE_CHECKOUT_ADDRESS = 981
        const val RESULT_CODE_ACTION_SELECT_ADDRESS = 100
        const val RESULT_CHECKOUT_CACHE_EXPIRED = 824
        const val TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST_FOR_MONEY_IN = 5
        const val EXTRA_SELECTED_ADDRESS_DATA = "EXTRA_SELECTED_ADDRESS_DATA"
        const val EXTRA_TYPE_REQUEST = "EXTRA_TYPE_REQUEST"
        const val EXTRA_IS_ONE_CLICK_SHIPMENT = "EXTRA_IS_ONE_CLICK_SHIPMENT"
        const val EXTRA_CACHE_EXPIRED_ERROR_MESSAGE = "EXTRA_CACHE_EXPIRED_ERROR_MESSAGE"
        const val STATE_RED = "red"
        const val TYPE_CASHBACK = "cashback"
        const val PARAM_DEFAULT = "default"
        const val PARAM_CHECKOUT = "checkout"
        const val PARAM_OCC = "occ"
    }

}