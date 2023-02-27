package com.tokopedia.shop.common.constant

import com.tokopedia.url.TokopediaUrl.Companion.getInstance

/**
 * Created by nathan on 10/24/17.
 */
object ShopUrl : ShopCommonUrl() {
    @JvmField
    var BASE_ACE_URL = getInstance().ACE
    var BASE_OFFICIAL_STORE_URL = getInstance().MOJITO
    const val SHOP_PRODUCT_PATH = "v1/web-service/shop/get_shop_product"
}
