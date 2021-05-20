package com.tokopedia.shop.common.constant

import com.tokopedia.url.TokopediaUrl.Companion.getInstance

/**
 * Created by nathan on 10/24/17.
 */
open class ShopCommonUrl {
    companion object {
        @JvmField
        var BASE_URL = getInstance().TOME
        @JvmField
        var BASE_WS_URL = getInstance().WS
        const val SHOP_INFO_PATH = "v1/web-service/shop/get_shop_info"
    }
}