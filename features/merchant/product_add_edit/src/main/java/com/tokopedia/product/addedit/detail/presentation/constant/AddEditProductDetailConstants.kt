package com.tokopedia.product.addedit.detail.presentation.constant

class AddEditProductDetailConstants {
    companion object {
        const val MAX_PRODUCT_PHOTOS = 5
        const val MAX_WHOLESALE_PRICES = 5
        const val UNIVERSE_SEARCH_TYPE = "AUTOCOMPLETE"
        const val REQUEST_CODE_IMAGE = 0
        const val REQUEST_CODE_CATEGORY = 1
        const val REQUEST_CODE_DETAIL = 2
        const val REQUEST_CODE_DETAIL_EDIT = 3
        const val REQUEST_CODE_DESCRIPTION_EDIT = 4
        const val REQUEST_CODE_SHIPMENT_EDIT = 5
        const val REQUEST_CODE_VARIANT_DIALOG_EDIT = 6
        const val CATEGORY_RESULT_ID = "CATEGORY_RESULT_ID"
        const val CATEGORY_RESULT_FULL_NAME = "CATEGORY_RESULT_FULL_NAME"
        const val UNIT_DAY = 0
        const val UNIT_WEEK = 1
        const val NEW_PRODUCT_INDEX = 0
        const val CONDITION_NEW = "NEW"
        const val USED_PRODUCT_INDEX = 1
        const val CONDITION_USED = "USED"
        const val DEFAULT_STOCK_VALUE = 1
        const val DEFAULT_MIN_ORDER_VALUE = 1
        const val DEBOUNCE_DELAY_MILLIS = 500L
        const val MIN_PRODUCT_PRICE_LIMIT = 100
        const val MIN_PRODUCT_STOCK_LIMIT = 1
        const val MAX_PRODUCT_PRICE_LIMIT = 100000000
        const val MAX_PRODUCT_STOCK_LIMIT = 999999
        const val MIN_MIN_ORDER_QUANTITY = 1
        const val MIN_PREORDER_DURATION = 1
        const val MAX_PREORDER_DAYS = 90
        const val MAX_PREORDER_WEEKS = 13
        const val PARAM_SET_CASHBACK_VALUE = "cashback"
        const val PARAM_SET_CASHBACK_PRODUCT_PRICE = "price"
        const val PARAM_SET_CASHBACK_PRODUCT_NAME = "product_name"
        const val SET_CASHBACK_REQUEST_CODE = 3333
        const val SET_CASHBACK_CACHE_MANAGER_KEY = "set_cashback_cache_id"
        const val SET_CASHBACK_RESULT = "set_cashback_result"
        const val EXTRA_CASHBACK_SHOP_ID = "extra_shop_id"
        const val EXTRA_CASHBACK_IS_DRAFTING = "extra_is_drafting"
        // request key for set fragment result
        const val REQUEST_KEY_ADD_MODE = "request_key_add_mode"
        const val REQUEST_KEY_DESCRIPTION = "request_key_description"
        const val REQUEST_KEY_DETAIL = "request_key_detail"
        const val REQUEST_KEY_SHIPMENT = "request_key_shipment"
        const val BUNDLE_CACHE_MANAGER_ID = "bundle_cache_manager_id"
    }
}