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
    const val SHOP_DYNAMIC_FILTER = "v1/dynamic_attributes"
    const val SHOP_FAVOURITE_USER = "/v4/shop/get_people_who_favorite_myshop.pl"
    const val SHOP_PRODUCT_OS_DISCOUNT = "/os/v1/campaign/product/info"
    const val SHOP_HELP_CENTER = "https://www.tokopedia.com/bantuan/status-toko-tidak-aktif/"
}