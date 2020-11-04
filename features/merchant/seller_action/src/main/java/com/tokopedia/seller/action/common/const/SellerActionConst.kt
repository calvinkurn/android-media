package com.tokopedia.seller.action.common.const

object SellerActionConst {

    object Deeplink {
        internal const val ORDER = "/order"
    }

    object Params {
        // Actions params
        internal const val ORDER_DATE = "orderDate"

        internal const val FEATURE_NAME = "feature_name"
        internal const val ORDER_ID = "orderId"
    }

    internal const val DATE_DELIMITER = "T"
    internal const val DATE_RANGE_DELIMITER = "/"
    internal const val SLICE_DATE_FORMAT = "yyyy-MM-dd"
    internal const val SLICE_FULL_DATE_FORMAT = "dd MMM yyyy"
    internal const val REQUEST_DATE_FORMAT = "dd/MM/yyyy"

    internal const val MAIN_REQUEST_CODE = 9230

}