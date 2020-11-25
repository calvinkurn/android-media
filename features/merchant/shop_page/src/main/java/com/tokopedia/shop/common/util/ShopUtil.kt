package com.tokopedia.shop.common.util

import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.shop.common.constant.IGNORED_FILTER_KONDISI
import com.tokopedia.shop.common.constant.IGNORED_FILTER_PENAWARAN
import com.tokopedia.shop.common.constant.IGNORED_FILTER_PENGIRIMAN
import com.tokopedia.shop.common.constant.ShopPageConstant
import timber.log.Timber

object ShopUtil {
    fun isHasNextPage(page: Int, perPage: Int, totalData: Int): Boolean = page * perPage < totalData

    fun isMyShop(shopId: String, userSessionShopId: String)  = shopId == userSessionShopId

    fun logTimberWarning(errorTag: String, errorData: String) {
        Timber.w("P2#${errorTag}#${errorData}'")
    }

    fun isFilterNotIgnored(title: String): Boolean {
        return when (title.toLowerCase()) {
            IGNORED_FILTER_PENGIRIMAN -> false
            IGNORED_FILTER_PENAWARAN -> false
            IGNORED_FILTER_KONDISI -> false
            else -> true
        }
    }

    fun isUsingNewNavigation(): Boolean {
        val navType = RemoteConfigInstance.getInstance().abTestPlatform?.getString(
                ShopPageConstant.AB_TEST_NAVIGATION_REVAMP_KEY,
                ShopPageConstant.AB_TEST_NAVIGATION_REVAMP_OLD_VALUE
        )
        return (navType == ShopPageConstant.AB_TEST_NAVIGATION_REVAMP_NEW_VALUE && !GlobalConfig.isSellerApp())
    }
}