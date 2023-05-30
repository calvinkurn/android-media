package com.tokopedia.tokopedianow.common.util

import com.tokopedia.applink.ApplinkConst.TokopediaNow.TOKOPEDIA_NOW_PRODUCTION_SHOP_ID_1
import com.tokopedia.applink.ApplinkConst.TokopediaNow.TOKOPEDIA_NOW_PRODUCTION_SHOP_ID_2
import com.tokopedia.config.GlobalConfig

object ShopIdProvider {

    fun getShopId(): String {
        return if (GlobalConfig.DEBUG) {
            TOKOPEDIA_NOW_PRODUCTION_SHOP_ID_1 // QA shopId
        } else {
            TOKOPEDIA_NOW_PRODUCTION_SHOP_ID_2 // Prod shopId
        }
    }
}
