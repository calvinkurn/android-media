package com.tokopedia.shop.common.util

import android.content.Intent

/**
 * Created By : Jonathan Darwin on July 27, 2023
 */
object ShopPageActivityResult {

    private const val EXTRA_IS_FOLLOW = "EXTRA_IS_FOLLOW"
    private const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"

    fun createResult(
        shopId: String,
        isFollow: Boolean
    ): Intent {
        return Intent().apply {
            putExtra(EXTRA_IS_FOLLOW, isFollow)
            putExtra(EXTRA_SHOP_ID, shopId)
        }
    }

    fun isFollow(intent: Intent): Boolean {
        return intent.getBooleanExtra(EXTRA_IS_FOLLOW, false)
    }

    fun getShopId(intent: Intent): String {
        return intent.getStringExtra(EXTRA_SHOP_ID).orEmpty()
    }
}
