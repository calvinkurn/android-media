package com.tokopedia.vouchercreation.product.voucherlist.view.constant

object CouponListConstant {
    const val PAGE_MODE_SEGMENT_INDEX = 1
    const val PAGE_MODE_ACTIVE = "active"
    const val PAGE_MODE_HISTORY = "history"

    const val LIST_COUPON_PER_PAGE = 10
    const val IS_SUCCESS_VOUCHER = "is_success"
    const val IS_UPDATE_VOUCHER = "is_update"
    const val VOUCHER_ID_KEY = "voucher_id"

    const val CANCEL_VOUCHER_ERROR = "Cancel voucher error - voucher list"
    const val STOP_VOUCHER_ERROR = "Stop voucher error - voucher list"
    const val GET_VOUCHER_DETAIL_ERROR = "Get voucher list error"
    const val GET_SHOP_BASIC_DATA_ERROR = "Get shop basic data error - voucher list"

    const val TAG_SCALYR_MVC_CANCEL_VOUCHER_ERROR = "MVC_CANCEL_VOUCHER_ERROR"
    const val TAG_SCALYR_MVC_STOP_VOUCHER_ERROR = "MVC_STOP_VOUCHER_ERROR"
    const val TAG_SCALYR_MVC_GET_VOUCHER_DETAIL_ERROR = "MVC_GET_VOUCHER_DETAIL_ERROR"
    const val TAG_SCALYR_MVC_GET_SHOP_BASIC_DATA_ERROR = "MVC_GET_SHOP_BASIC_DATA_ERROR"

    const val TYPE_MESSAGE_KEY = "type"
    const val SUCCESS_VOUCHER_DEFAULT_VALUE = 0
    const val IS_SUCCESS_VOUCHER_DEFAULT_VALUE = false
    const val IS_UPDATE_VOUCHER_DEFAULT_VALUE = false
}