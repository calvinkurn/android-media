package com.tokopedia.sellerorder.common.util

object BulkRequestPickupStatus {
    const val ALL_SUCCESS = 200
    const val PARTIAL_SUCCESS = 210
    const val SUCCESS_NOT_PROCESSED = 220
    const val FAIL_NOT_FOUND = 404
    const val FAIL_ALL_VALIDATION = 400
}