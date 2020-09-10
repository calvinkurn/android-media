package com.tokopedia.shop.common.util

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey

object ShopPageProductChangeGridRemoteConfig {

    fun isFeatureEnabled(remoteConfig: RemoteConfig?): Boolean = remoteConfig?.getBoolean(
            RemoteConfigKey.ENABLE_SHOP_PAGE_CHANGE_PRODUCT_GRID_LAYOUT_FEATURE,
            true
    ) == true

}