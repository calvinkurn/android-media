package com.tokopedia.shop.common.util

import timber.log.Timber

object ShopUtil {
    fun isHasNextPage(page: Int, perPage: Int, totalData: Int): Boolean = page * perPage < totalData

    fun isMyShop(shopId: String, userSessionShopId: String)  = shopId == userSessionShopId

    fun logTimberWarning(errorTag: String, errorData: String) {
        Timber.w("P2#${errorTag}#${errorData}'")
    }
}